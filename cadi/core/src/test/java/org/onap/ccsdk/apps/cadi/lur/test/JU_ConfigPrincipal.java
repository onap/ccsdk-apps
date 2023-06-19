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

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.lang.reflect.Field;

import java.io.IOException;

import org.onap.ccsdk.apps.cadi.lur.ConfigPrincipal;

public class JU_ConfigPrincipal {

    private final String name = "User";
    private final String pass = "pass";

    // Expected output of base64("User:pass")
    private final String b64encoded = "VXNlcjpwYXNz";

    private Field content_field;

    @Before
    public void setup() throws NoSuchFieldException {
        content_field = ConfigPrincipal.class.getDeclaredField("content");
        content_field.setAccessible(true);
    }

    @Test
    public void testConfigPrincipalStringString() throws IOException, IllegalArgumentException, IllegalAccessException {
        ConfigPrincipal p =  new ConfigPrincipal(name, pass);

        assertThat(p.getName(), is(name));
        assertThat(p.toString(), is(name));
        assertThat(p.getCred(), is(pass.getBytes()));
        assertThat(p.getAsBasicAuthHeader(), is("Basic " + b64encoded));
        content_field.set(p, "pass");
        assertThat(p.getAsBasicAuthHeader(), is("Basic " + b64encoded));

        // One more time for coverage purposes
        assertThat(p.getAsBasicAuthHeader(), is("Basic " + b64encoded));
    }

    @Test
    public void testConfigPrincipalStringByteArray() throws IOException, IllegalArgumentException, IllegalAccessException {
        ConfigPrincipal p =  new ConfigPrincipal(name, pass.getBytes());

        assertThat(p.getName(), is(name));
        assertThat(p.toString(), is(name));
        assertThat(p.getCred(), is(pass.getBytes()));
        assertThat(p.getAsBasicAuthHeader(), is("Basic " + b64encoded));
        content_field.set(p, "pass");
        assertThat(p.getAsBasicAuthHeader(), is("Basic " + b64encoded));

        // One more time for coverage purposes
        assertThat(p.getAsBasicAuthHeader(), is("Basic " + b64encoded));
    }

}
