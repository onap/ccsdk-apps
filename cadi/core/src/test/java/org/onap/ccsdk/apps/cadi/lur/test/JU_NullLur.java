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
 * * distributed under the License is distributed on an "AS IS" BASIS,Z
 * * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * * See the License for the specific language governing permissions and
 * * limitations under the License.
 * * ============LICENSE_END====================================================
 * *
 * *
 ******************************************************************************/

package org.onap.ccsdk.apps.cadi.lur.test;

import java.security.Principal;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.lang.reflect.*;

import org.onap.ccsdk.apps.cadi.Permission;
import org.onap.ccsdk.apps.cadi.lur.NullLur;

public class JU_NullLur {

    @Mock
    Principal p;

    @Mock
    Permission perm;

    @Mock
    List<Permission> perms;

    private NullLur nullLur;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        nullLur = new NullLur();
    }

    @Test
    public void coverageTests() throws Exception {

        Field nullClass = NullLur.class.getDeclaredField("NULL");
        nullClass.setAccessible(true);
        assertThat(((Permission) nullClass.get(NullLur.class)).permType(), is(""));
        assertThat(((Permission) nullClass.get(NullLur.class)).getKey(), is(""));
        assertFalse(((Permission) nullClass.get(NullLur.class)).match(perm));

        nullLur.fishAll(p, perms);
        nullLur.destroy();

        assertFalse(nullLur.fish(p, perm));
        assertFalse(nullLur.handlesExclusively(perm));
        assertFalse(nullLur.handles(p));
        assertThat(nullLur.createPerm(""), is(nullClass.get(NullLur.class)));

        StringBuilder sb = new StringBuilder();
        nullLur.clear(p, sb);
        assertThat(sb.toString(), is("NullLur\n"));
        assertThat(nullLur.toString(), is("NullLur\n"));
    }

}
