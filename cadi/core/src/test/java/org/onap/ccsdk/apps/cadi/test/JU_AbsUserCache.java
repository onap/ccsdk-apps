/*******************************************************************************
 * * org.onap.ccsdk
 * * ===========================================================================
 * * Copyright © 2023 AT&T Intellectual Property. All rights reserved.
 * * ===========================================================================
 * * Licensed under the Apache License, Version 2.0 (the "License");
 * * you may not use this file except in compliance with the License.
 * * You may obtain a copy of the License at
 * *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 * *
 *  * Unless required by applicable law or agreed to in writing, software
 * * distributed under the License is distributed on an "AS IS" BASIS,
 * * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * * See the License for the specific language governing permissions and
 * * limitations under the License.
 * * ============LICENSE_END====================================================
 * *
 * *
 ******************************************************************************/

package org.onap.ccsdk.apps.cadi.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.ccsdk.apps.cadi.AbsUserCache;
import org.onap.ccsdk.apps.cadi.Access;
import org.onap.ccsdk.apps.cadi.CachedPrincipal.Resp;
import org.onap.ccsdk.apps.cadi.CachingLur;
import org.onap.ccsdk.apps.cadi.GetCred;
import org.onap.ccsdk.apps.cadi.Permission;
import org.onap.ccsdk.apps.cadi.PropAccess;
import org.onap.ccsdk.apps.cadi.User;
import org.onap.ccsdk.apps.cadi.lur.LocalPermission;
import org.onap.ccsdk.apps.cadi.principal.CachedBasicPrincipal;

public class JU_AbsUserCache {

    @Mock private CachingLur<Permission> cl;
    @Mock private Principal principal;
    @Mock private CachedBasicPrincipal cbp;
    @Mock private LocalPermission permission1;
    @Mock private LocalPermission permission2;

    private Access access;

    private ByteArrayOutputStream outStream;

    private String name1 = "name1";
    private String name2 = "name2";
    private byte[] password = "password".getBytes();

    private static Field timerField;

    @BeforeClass
    public static void setupOnce() throws Exception {
        timerField = AbsUserCache.class.getDeclaredField("timer");
        timerField.setAccessible(true);
    }

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        outStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outStream));

        // This must happen after changing System.out
        access = new PropAccess();

        when(permission1.getKey()).thenReturn("NewKey1");
        when(permission2.getKey()).thenReturn("NewKey2");

        timerField.set(null, null);
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(System.out);
        timerField.set(null, null);
    }

    @SuppressWarnings("unused")
    @Test
    public void constructorTest() {
        int cleanInterval = 65000;
        int maxInterval = 70000;

        AbsUserCacheStub<Permission> aucs1 = new AbsUserCacheStub<Permission>(access, cleanInterval, maxInterval, Integer.MAX_VALUE);
        String output = outStream.toString().split(" ", 2)[1];

        outStream.reset();
        AbsUserCacheStub<Permission> aucs2 = new AbsUserCacheStub<Permission>(access, cleanInterval, maxInterval, Integer.MAX_VALUE);
        output = outStream.toString().split(" ", 2)[1];

        AbsUserCacheStub<Permission> aucs3 = new AbsUserCacheStub<Permission>(access, 0, 0, Integer.MAX_VALUE);
        AbsUserCacheStub<Permission> aucs4 = new AbsUserCacheStub<Permission>(aucs1);

        // For coverage
        AbsUserCacheCLStub<Permission> auccls1 = new AbsUserCacheCLStub<Permission>(aucs1);
        aucs1.setLur(cl);
        auccls1 = new AbsUserCacheCLStub<Permission>(aucs1);
        AbsUserCacheCLStub<Permission> auccls2 = new AbsUserCacheCLStub<Permission>(aucs3);
    }

    @Test
    public void setLurTest() {
        AbsUserCacheStub<Permission> aucs1 = new AbsUserCacheStub<Permission>(access, 65000, 70000, Integer.MAX_VALUE);
        AbsUserCacheStub<Permission> aucs2 = new AbsUserCacheStub<Permission>(access, 0, 0, Integer.MAX_VALUE);
        aucs1.setLur(cl);
        aucs2.setLur(cl);
    }

    @Test
    public void addUserGetUserTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        AbsUserCacheStub<Permission> aucs = new AbsUserCacheStub<Permission>(access, 0, 0, Integer.MAX_VALUE);
        User<Permission> user;

        // Test adding a user with a principal (non-GetCred). user does not have a cred
        // Then test getting that user
        when(principal.getName()).thenReturn(name1);
        user = new User<Permission>(principal, 0);
        aucs.addUser(user);
        assertThat(aucs.getUser(principal), is(user));

        // Test adding a user with a principal (GetCred). user does not have a cred
        // Then test getting that user
        GetCredStub gc = new GetCredStub();
        user = new User<Permission>(gc, 0);
        aucs.addUser(user);
        assertThat(aucs.getUser(gc), is(user));

        // Test adding a user with no principal
        // Then test getting that user via his name and cred
        user = new User<Permission>(name2, password);
        aucs.addUser(user);
        assertThat(aucs.getUser(name2, password), is(user));

        // Test getting a user by a CachedBasicPrincipal
        when(cbp.getName()).thenReturn(name2);
        when(cbp.getCred()).thenReturn(password);
        assertThat(aucs.getUser(cbp), is(user));

        // Force the user to expire, then test that he is no longer in the cache
        Field permExpiresField = User.class.getDeclaredField("permExpires");
        permExpiresField.setAccessible(true);
        permExpiresField.set(user, 0);
        assertThat(aucs.getUser(name2, password), is(nullValue()));

        // Test adding a user with a custom key
        // Then test gettin that user
        user = new User<Permission>(principal, 0);
        String key = principal.getName() + "NoCred";
        aucs.addUser(key, user);
        assertThat(aucs.getUser(principal), is(user));

        // Test that getUser returns null for principals that don't match any users
        when(principal.getName()).thenReturn("not in the cache");
        assertThat(aucs.getUser(principal), is(nullValue()));

        // That that getUser returns null for name/creds that are not in the cache
        assertThat(aucs.getUser("not a real user", "not in the cache".getBytes()), is(nullValue()));
    }

    @Test
    public void removeTest() {
        AbsUserCacheStub<Permission> aucs = new AbsUserCacheStub<Permission>(access, 0, 0, Integer.MAX_VALUE);
        User<Permission> user;

        when(principal.getName()).thenReturn(name1);
        user = new User<Permission>(principal);
        // Add a user with a principal
        aucs.addUser(user);
        // Check that the user is in the cache
        assertThat(aucs.getUser(principal), is(user));
        // Remove the user
        when(principal.getName()).thenReturn(name1 + "NoCred");
        aucs.remove(user);
        // Check that the user is no longer in the cache
        when(principal.getName()).thenReturn(name1);
        assertThat(aucs.getUser(principal), is(nullValue()));

        // Add the user again
        aucs.addUser(user);
        // Check that the user is in the cache
        assertThat(aucs.getUser(principal), is(user));
        // Remove the user by name
        aucs.remove(name1 + "NoCred");
        // Check that the user is no longer in the cache
        assertThat(aucs.getUser(principal), is(nullValue()));

        // Coverage test - attempt to remove a user that is not in the cache
        aucs.remove(name1 + "NoCred");
        assertThat(aucs.getUser(principal), is(nullValue()));
    }

    @Test
    public void clearAllTest() {
        AbsUserCacheStub<Permission> aucs = new AbsUserCacheStub<Permission>(access, 0, 0, Integer.MAX_VALUE);
        User<Permission> user1;
        User<Permission> user2;

        // Add some users to the cache
        when(principal.getName()).thenReturn(name1);
        user1 = new User<Permission>(principal);
        when(principal.getName()).thenReturn(name2);
        user2 = new User<Permission>(principal);
        aucs.addUser(user1);
        aucs.addUser(user2);

        // Check that the users are in the cache
        when(principal.getName()).thenReturn(name1);
        assertThat(aucs.getUser(principal), is(user1));
        when(principal.getName()).thenReturn(name2);
        assertThat(aucs.getUser(principal), is(user2));

        // Clear the cache
        aucs.clearAll();

        // Check that the users are no longer in the cache
        when(principal.getName()).thenReturn(name1);
        assertThat(aucs.getUser(principal), is(nullValue()));
        when(principal.getName()).thenReturn(name2);
        assertThat(aucs.getUser(principal), is(nullValue()));
    }

    @Test
    public void dumpInfoTest() {
        AbsUserCacheStub<Permission> aucs = new AbsUserCacheStub<Permission>(access, 0, 0, Integer.MAX_VALUE);
        User<Permission> user1;
        User<Permission> user2;

        Principal principal1 = mock(Principal.class);
        Principal principal2 = mock(Principal.class);
        when(principal1.getName()).thenReturn(name1);
        when(principal2.getName()).thenReturn(name2);

        // Add some users with permissions to the cache
        user1 = new User<Permission>(principal1);
        user1.add(permission1);
        user1.add(permission2);
        user2 = new User<Permission>(principal2);
        user2.add(permission1);
        user2.add(permission2);
        aucs.addUser(user1);
        aucs.addUser(user2);

        // Dump the info
        List<AbsUserCache<Permission>.DumpInfo> dumpInfo = aucs.dumpInfo();
        assertThat(dumpInfo.size(), is(2));

        // Utility lists
        List<String> names = new ArrayList<>();
        names.add(name1);
        names.add(name2);
        List<String> permissions = new ArrayList<>();
        permissions.add("NewKey1");
        permissions.add("NewKey2");

        // We need to use "contains" because the dumpInfo was created from a list, so we don't know it's order
        for (AbsUserCache<Permission>.DumpInfo di : dumpInfo) {
            assertTrue(names.contains(di.user));
            for (String perm : di.perms) {
                assertTrue(permissions.contains(perm));
            }
        }
    }

    @Test
    public void handlesExclusivelyTest() {
        AbsUserCacheStub<Permission> aucs = new AbsUserCacheStub<Permission>(access, 0, 0, Integer.MAX_VALUE);
        assertFalse(aucs.handlesExclusively(permission1));
        assertFalse(aucs.handlesExclusively(permission2));
    }

    @Test
    public void destroyTest() {
        AbsUserCacheStub<Permission> aucs = new AbsUserCacheStub<Permission>(access, 0, 0, Integer.MAX_VALUE);
        aucs.destroy();
        aucs = new AbsUserCacheStub<Permission>(access, 1, 1, Integer.MAX_VALUE);
        aucs.destroy();
    }

    @Test
    public void missTest() throws IOException {
        AbsUserCacheStub<Permission> aucs = new AbsUserCacheStub<Permission>(access, 0, 0, Integer.MAX_VALUE);
        // Add the Miss to the missmap
        assertTrue(aucs.addMiss("key", password));  // This one actually adds it
        assertTrue(aucs.addMiss("key", password));  // this one doesn't really do anything
        assertTrue(aucs.addMiss("key", password));  // neither does this one
        assertFalse(aucs.addMiss("key", password)); // By this time, the missMap is tired of this nonsense, and retaliates
        assertFalse(aucs.addMiss("key", password)); // Oh yea. He's angry

        // Can't really test this due to visibility
        aucs.missed("key", password);

        // Coverage
        AbsUserCacheStub<Permission> aucs1 = new AbsUserCacheStub<Permission>(access, 1, 1, Integer.MAX_VALUE);
        aucs1.addMiss("key", password);
    }

    class AbsUserCacheStub<PERM extends Permission> extends AbsUserCache<PERM> {
        public AbsUserCacheStub(Access access, long cleanInterval, int highCount, int usageCount) { super(access, cleanInterval, highCount, usageCount); }
        public AbsUserCacheStub(AbsUserCache<PERM> cache) { super(cache); }
        @Override public void setLur(CachingLur<PERM> lur) { super.setLur(lur); }
        @Override public void addUser(User<PERM> user) { super.addUser(user); }
        @Override public void addUser(String key, User<PERM> user) { super.addUser(key, user); }
        @Override public User<PERM> getUser(Principal p) { return super.getUser(p); }
        @Override public User<PERM> getUser(CachedBasicPrincipal p) { return super.getUser(p); }
        @Override public User<PERM> getUser(String user, byte[] cred) { return super.getUser(user, cred); }
        @Override public void remove(User<PERM> user) { super.remove(user); }
        @Override public boolean addMiss(String key, byte[] bs) { return super.addMiss(key, bs); }
        @Override public Miss missed(String key, byte[] bs) throws IOException { return super.missed(key, bs); }
    }

    class AbsUserCacheCLStub<PERM extends Permission> extends AbsUserCache<PERM> implements CachingLur<PERM> {
        public AbsUserCacheCLStub(AbsUserCache<PERM> cache) { super(cache); }
        @Override public Permission createPerm(String p) { return null; }
        @Override public boolean fish(Principal bait, Permission ... pond) { return false; }
        @Override public void fishAll(Principal bait, List<Permission> permissions) { }
        @Override public boolean handles(Principal principal) { return false; }
        @Override public Resp reload(User<PERM> user) { return null; }
        @Override public void setDebug(String commaDelimIDsOrNull) { }
    }

    class GetCredStub implements Principal, GetCred {
        @Override public byte[] getCred() { return password; }
        @Override public String getName() { return name1; }
    }

}
