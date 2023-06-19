/*******************************************************************************
 * ============LICENSE_START====================================================
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

import static org.junit.Assert.*;

import org.junit.Test;


import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.ccsdk.apps.cadi.Permission;
import org.onap.ccsdk.apps.cadi.User;
import org.onap.ccsdk.apps.cadi.lur.LocalPermission;

public class JU_User {

    private final Long SECOND = 1000L;
    private final String name = "Fakey McFake";
    private final String cred = "Fake credentials";

    private Field perms_field;
    private Field count_field;

    @Mock
    private Principal principal;

    @Mock
    private LocalPermission permission;
    @Mock
    private LocalPermission permission2;

    @Before
    public void setup() throws NoSuchFieldException, SecurityException {
        MockitoAnnotations.initMocks(this);

        when(principal.getName()).thenReturn("Principal");

        when(permission.getKey()).thenReturn("NewKey");
        when(permission.match(permission)).thenReturn(true);

        when(permission2.getKey()).thenReturn("NewKey2");
        when(permission2.match(permission)).thenReturn(false);

        perms_field = User.class.getDeclaredField("perms");
        perms_field.setAccessible(true);

        count_field = User.class.getDeclaredField("count");
        count_field.setAccessible(true);
    }

    @Test
    public void constructorPrincipalTest() throws IllegalArgumentException, IllegalAccessException {
        User<Permission> user = new User<Permission>(principal);
        assertThat(user.name, is(principal.getName()));
        assertThat(user.principal, is(principal));
        assertThat(user.permExpires(), is(Long.MAX_VALUE));
        assertThat((int)count_field.get(user), is(0));
    }

    @Test
    public void constructorNameCredTest() throws IllegalArgumentException, IllegalAccessException {
        User<Permission> user = new User<Permission>(name, cred.getBytes());
        assertThat(user.name, is(name));
        assertThat(user.principal, is(nullValue()));
        assertThat(user.permExpires(), is(Long.MAX_VALUE));
        assertThat((int)count_field.get(user), is(0));
        assertThat(user.getCred(), is(cred.getBytes()));
    }

    @Test
    public void constructorPrincipalIntervalTest() throws IllegalArgumentException, IllegalAccessException {
        User<Permission> user = new User<Permission>(principal, 61 * SECOND);
        Long approxExpiration = System.currentTimeMillis() + 61 * SECOND;
        assertThat(user.name, is(principal.getName()));
        assertThat(user.principal, is(principal));
        assertTrue(Math.abs(user.permExpires() - approxExpiration) < 10L);
        assertThat((int)count_field.get(user), is(0));
    }

    @Test
    public void constructorNameCredIntervalTest() throws IllegalArgumentException, IllegalAccessException {
        String name = "Fakey McFake";
        User<Permission> user = new User<Permission>(name, cred.getBytes(), 61 * SECOND);
        Long approxExpiration = System.currentTimeMillis() + 61 * SECOND;
        assertThat(user.name, is(name));
        assertThat(user.principal, is(nullValue()));
        assertTrue(Math.abs(user.permExpires() - approxExpiration) < 10L);
        assertThat((int)count_field.get(user), is(0));
        assertThat(user.getCred(), is(cred.getBytes()));
    }

    @Test
    public void countCheckTest() throws IllegalArgumentException, IllegalAccessException {
        User<Permission> user = new User<Permission>(principal);
        user.resetCount();
        assertThat((int)count_field.get(user), is(0));
        user.incCount();
        assertThat((int)count_field.get(user), is(1));
        user.incCount();
        assertThat((int)count_field.get(user), is(2));
        user.resetCount();
        assertThat((int)count_field.get(user), is(0));
    }

    @Test
    public void permTest() throws InterruptedException, IllegalArgumentException, IllegalAccessException {
        User<Permission> user = new User<Permission>(principal);
        assertThat(user.permExpires(), is(Long.MAX_VALUE));
        user.renewPerm();
        Thread.sleep(1);  // Let it expire
        assertThat(user.permExpired(), is(true));

        user = new User<Permission>(principal,100);
        assertTrue(user.noPerms());
        user.add(permission);
        assertFalse(user.permsUnloaded());
        assertFalse(user.noPerms());
        user.setNoPerms();
        assertThat(user.permExpired(), is(false));
        assertTrue(user.permsUnloaded());
        assertTrue(user.noPerms());
        perms_field.set(user, null);
        assertTrue(user.permsUnloaded());
        assertTrue(user.noPerms());
    }

    @Test
    public void addValuesToNewMapTest() {
        User<Permission> user = new User<Permission>(principal);
        Map<String, Permission> newMap = new HashMap<>();

        assertFalse(user.contains(permission));

        user.add(newMap, permission);
        user.setMap(newMap);

        assertTrue(user.contains(permission));

        List<Permission> sink = new ArrayList<>();
        user.copyPermsTo(sink);

        assertThat(sink.size(), is(1));
        assertTrue(sink.contains(permission));

        assertThat(user.toString(), is("Principal|:NewKey"));

        user.add(newMap, permission2);
        user.setMap(newMap);
        assertFalse(user.contains(permission2));

        assertThat(user.toString(), is("Principal|:NewKey2,NewKey"));
    }

}
