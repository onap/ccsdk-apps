/**
 *
 * ============LICENSE_START====================================================
 * org.onap.ccsdk
 * ===========================================================================
 * Copyright (c) 2023 AT&T Intellectual Property. All rights reserved.
 * ===========================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END====================================================
 *
 */

package org.onap.ccsdk.apps.cadi.lur.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.ccsdk.apps.cadi.CachingLur;
import org.onap.ccsdk.apps.cadi.CadiException;
import org.onap.ccsdk.apps.cadi.CredVal;
import org.onap.ccsdk.apps.cadi.Lur;
import org.onap.ccsdk.apps.cadi.Permission;
import org.onap.ccsdk.apps.cadi.lur.EpiLur;

public class JU_EpiLur {

    private ArrayList<Permission> perms;
    private CredValStub lurMock3;

    @Mock private Lur lurMock1;
    @Mock private CachingLur<?> lurMock2;
    @Mock private Principal princMock;
    @Mock private Permission permMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        perms = new ArrayList<>();
        perms.add(permMock);

        lurMock3 = new CredValStub();
    }

    @Test
    public void test() throws CadiException {
        EpiLur lur;
        try {
            lur = new EpiLur();
        } catch (CadiException e) {
            assertThat(e.getMessage(), is("Need at least one Lur implementation in constructor"));
        }
        lur = new EpiLur(lurMock1, lurMock2, lurMock3);
        assertThat(lur.fish(null,  null), is(false));

        assertThat(lur.fish(princMock, permMock), is(false));

        when(lurMock2.handlesExclusively(permMock)).thenReturn(true);
        assertThat(lur.fish(princMock, permMock), is(false));

        when(lurMock2.fish(princMock, permMock)).thenReturn(true);
        assertThat(lur.fish(princMock, permMock), is(true));

        lur.fishAll(princMock, perms);

        assertThat(lur.handlesExclusively(permMock), is(false));

        assertThat(lur.get(-1), is(nullValue()));
        assertThat(lur.get(0), is(lurMock1));
        assertThat(lur.get(1), is((Lur)lurMock2));
        assertThat(lur.get(2), is((Lur)lurMock3));
        assertThat(lur.get(3), is(nullValue()));

        assertThat(lur.handles(princMock), is(false));
        when(lurMock2.handles(princMock)).thenReturn(true);
        assertThat(lur.handles(princMock), is(true));

        lur.remove("id");

        lur.clear(princMock, null);

        assertThat(lur.createPerm("perm"), is(not(nullValue())));

        lur.getUserPassImpl();
        assertThat(lur.getUserPassImpl(), is((CredVal)lurMock3));

        lur.toString();
        lur.destroy();

        lur = new EpiLur(lurMock1, lurMock2);
        assertThat(lur.getUserPassImpl(), is(nullValue()));

        assertThat(lur.subLur(Lur.class), is(nullValue()));
    }

    private class CredValStub implements Lur, CredVal {
        @Override public boolean validate(String user, Type type, byte[] cred, Object state) { return false; }
        @Override public Permission createPerm(String p) { return null; }
        @Override public boolean fish(Principal bait, Permission ... pond) { return false; }
        @Override public void fishAll(Principal bait, List<Permission> permissions) { }
        @Override public void destroy() { }
        @Override public boolean handlesExclusively(Permission ... pond) { return false; }
        @Override public boolean handles(Principal principal) { return false; }
        @Override public void clear(Principal p, StringBuilder report) { }
    }

}
