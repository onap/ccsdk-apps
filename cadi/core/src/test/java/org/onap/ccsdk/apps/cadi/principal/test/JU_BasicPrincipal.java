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

package org.onap.ccsdk.apps.cadi.principal.test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import org.junit.*;

import java.io.IOException;
import java.util.Date;

import org.onap.ccsdk.apps.cadi.BasicCred;
import org.onap.ccsdk.apps.cadi.Symm;
import org.onap.ccsdk.apps.cadi.principal.BasicPrincipal;

public class JU_BasicPrincipal {

    @Test
    public void Constructor1Test() throws Exception {
        // Test that everything works when the content doesn't contain "Basic"
        BasicPrincipal bp = new BasicPrincipal("content", "domain");
        assertThat(bp.getName(), is("content"));
        assertThat(bp.getCred(), is(nullValue()));

        // Test sending a user without an implicit domain
        String name = "User";
        String password = "password";
        String content = name + ":" + password;
        String domain = "exampledomain.com";
        String encrypted = new String(Symm.base64.encode(content.getBytes()));
        bp = new BasicPrincipal("Basic " + encrypted, domain);
        assertThat(bp.getShortName(), is(name));
        assertThat(bp.getName(), is(name + "@" + domain));
        assertThat(bp.getCred(), is(password.getBytes()));

        // Test sending a user with an implicit domain
        String longName = name + "@" + domain + ":" + password;
        encrypted = new String(Symm.base64.encode(longName.getBytes()));
        bp = new BasicPrincipal("Basic " + encrypted, domain);
        assertThat(bp.getShortName(), is(name));
        assertThat(bp.getName(), is(name + "@" + domain));
        assertThat(bp.getCred(), is(password.getBytes()));

        // Check that an exception is throw if no name is given in the content
        try {
            bp = new BasicPrincipal("Basic " + new String(Symm.base64.encode("no name".getBytes())), "");
            fail("Should have thrown an exception");
        } catch (IOException e) {
            assertThat(e.getMessage(), is("Invalid Coding"));
        }
    }

    @Test
    public void Constructor2Test() {
        String name = "User";
        String password = "password";
        BasicCred bc = mock(BasicCred.class);
        when(bc.getUser()).thenReturn(name);
        when(bc.getCred()).thenReturn(password.getBytes());

        BasicPrincipal bp = new BasicPrincipal(bc, "domain");
        assertThat(bp.getName(), is(name));
        assertThat(bp.getCred(), is(password.getBytes()));
    }

    @Test
    public void accessorsTest() throws IOException {
        String name = "User";
        String password = "password";
        String content = name + ":" + password;
        String domain = "exampledomain.com";
        String encrypted = new String(Symm.base64.encode(content.getBytes()));
        String bearer = "bearer";
        long created = System.currentTimeMillis();
        BasicPrincipal bp = new BasicPrincipal("Basic " + encrypted, domain);
        bp.setBearer(bearer);

        String expected = "Basic Authorization for " + name + "@" + domain + " evaluated on " + new Date(bp.created()).toString();
        assertTrue(Math.abs(bp.created() - created) < 10);
        assertThat(bp.toString(), is(expected));
        assertThat(bp.tag(), is("BAth"));
        assertThat(bp.personalName(), is(bp.getName()));

        // This test hits the abstract class BearerPrincipal
        assertThat(bp.getBearer(), is(bearer));
    }


    @Test
    public void coverageTest() throws IOException {
        String name = "User";
        String password = "password:with:colons";
        String content = name + ":" + password;
        String encrypted = new String(Symm.base64.encode(content.getBytes()));
        @SuppressWarnings("unused")
        BasicPrincipal bp = new BasicPrincipal("Basic " + encrypted, "domain");
    }

}
