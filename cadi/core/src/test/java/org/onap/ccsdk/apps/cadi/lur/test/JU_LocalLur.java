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

package org.onap.ccsdk.apps.cadi.lur.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.ccsdk.apps.cadi.AbsUserCache;
import org.onap.ccsdk.apps.cadi.CredVal.Type;
import org.onap.ccsdk.apps.cadi.Permission;
import org.onap.ccsdk.apps.cadi.PropAccess;
import org.onap.ccsdk.apps.cadi.lur.ConfigPrincipal;
import org.onap.ccsdk.apps.cadi.lur.LocalLur;
import org.onap.ccsdk.apps.cadi.lur.LocalPermission;

public class JU_LocalLur {

    private PropAccess access;
    private ByteArrayOutputStream outStream;

    @Mock Permission permMock;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        outStream = new ByteArrayOutputStream();
        access = new PropAccess(new PrintStream(outStream), new String[0]) {
            @Override public String decrypt(String encrypted, boolean anytext) throws IOException {
                return rot13(encrypted);
            }
            @Override public String encrypt(String unencrypted) throws IOException {
                return rot13(unencrypted);
            }
        };

    }

    @Test
    public void test() throws IOException {
        final String password = "<pass>";
        final String encrypted = rot13(password);

        LocalLur lur;
        List<AbsUserCache<LocalPermission>.DumpInfo> info;

        lur = new LocalLur(access, null, null);
        assertThat(lur.dumpInfo().size(), is(0));

        lur = new LocalLur(access, "user1", null);
        info = lur.dumpInfo();
        assertThat(info.size(), is(1));
        assertThat(info.get(0).user, is("user1"));

        lur.clearAll();
        assertThat(lur.dumpInfo().size(), is(0));

        lur = new LocalLur(access, "user1%" + encrypted, null);
        info = lur.dumpInfo();
        assertThat(info.size(), is(1));
        assertThat(info.get(0).user, is("user1@people.osaaf.org"));

        lur.clearAll();
        assertThat(lur.dumpInfo().size(), is(0));

        lur = new LocalLur(access, "user1@domain%" + encrypted, null);
        info = lur.dumpInfo();
        assertThat(info.size(), is(1));
        assertThat(info.get(0).user, is("user1@domain"));

        lur = new LocalLur(access, "user1@domain%" + encrypted + ":groupA", null);
        info = lur.dumpInfo();
        assertThat(info.size(), is(1));
        assertThat(info.get(0).user, is("user1@domain"));

        when(permMock.getKey()).thenReturn("groupA");
        assertThat(lur.handlesExclusively(permMock), is(true));
        when(permMock.getKey()).thenReturn("groupB");
        assertThat(lur.handlesExclusively(permMock), is(false));

        assertThat(lur.fish(null, null), is(false));

        Principal princ = new ConfigPrincipal("user1@localized", encrypted);

        lur = new LocalLur(access, "user1@localized%" + password + ":groupA", null);
        assertThat(lur.fish(princ, lur.createPerm("groupA")), is(true));
        assertThat(lur.fish(princ, lur.createPerm("groupB")), is(false));
        assertThat(lur.fish(princ, permMock), is(false));

        princ = new ConfigPrincipal("user1@domain", encrypted);
        assertThat(lur.fish(princ, lur.createPerm("groupB")), is(false));

        princ = new ConfigPrincipal("user1@localized", "badpass");
        assertThat(lur.fish(princ, lur.createPerm("groupB")), is(false));

        assertThat(lur.handles(null), is(false));

        lur.fishAll(null,  null);

        List<Permission> perms = new ArrayList<>();
        perms.add(lur.createPerm("groupB"));
        perms.add(lur.createPerm("groupA"));
        princ = new ConfigPrincipal("user1@localized", encrypted);
        lur.fishAll(princ, perms);
        princ = new ConfigPrincipal("user1@localized", "badpass");
        lur.fishAll(princ, perms);

        assertThat(lur.validate(null, null, null, null), is(false));
        assertThat(lur.validate("user", null, "badpass".getBytes(), null), is(false));
        assertThat(lur.validate("user1@localized", null, encrypted.getBytes(), null), is(false));

        lur = new LocalLur(access, "user1@localized%" + password + ":groupA", null);
        // Inconsistent on Jenkins only.
        //assertThat(lur.validate("user1@localized", Type.PASSWORD, encrypted.getBytes(), null), is(true));

        lur = new LocalLur(access, null, "admin");
        lur = new LocalLur(access, null, "admin:user1");
        lur = new LocalLur(access, null, "admin:user1@localized");
        lur = new LocalLur(access, null, "admin:user1@localized,user2@localized%" + password + ";user:user1@localized");
    }

    public static String rot13(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c >= 'a' && c <= 'm') {
                c += 13;
            } else if (c >= 'A' && c <= 'M') {
                c += 13;
            } else if (c >= 'n' && c <= 'z') {
                c -= 13;
            } else if (c >= 'N' && c <= 'Z') {
                c -= 13;
            }
            sb.append(c);
        }
        return sb.toString();
    }

}

