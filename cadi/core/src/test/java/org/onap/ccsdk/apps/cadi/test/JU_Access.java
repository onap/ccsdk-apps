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
import static org.hamcrest.CoreMatchers.*;
import org.junit.*;

import java.io.IOException;

import org.onap.ccsdk.apps.cadi.Access;
import org.onap.ccsdk.apps.cadi.Access.Level;

public class JU_Access {

    @Test
    public void levelTests() {
        assertTrue(Level.DEBUG.inMask(0x1));
        for (int i = 2; i > 0; i <<= 1) {
            assertFalse(Level.DEBUG.inMask(i));
        }
        assertFalse(Level.DEBUG.inMask(0x80000000));

        assertThat(Level.DEBUG.addToMask(0x2), is(0x3));
        assertThat(Level.DEBUG.delFromMask(0x1), is(0x0));
        assertThat(Level.DEBUG.toggle(0x2), is(0x3));
        assertThat(Level.DEBUG.toggle(0x1), is(0x0));
        assertThat(Level.DEBUG.maskOf(), is(123153));
        assertThat(Level.NONE.maskOf(), is(0));
    }

    @Test
    public void nullTests() throws IOException {
        // These are entirely for coverage
        Access.NULL.log(Level.DEBUG);
        Access.NULL.printf(Level.DEBUG, "");
        Access.NULL.log(new Exception());
        Access.NULL.classLoader();
        assertThat(Access.NULL.getProperty("", ""), is(nullValue()));
        Access.NULL.load(System.in);
        Access.NULL.setLogLevel(Level.DEBUG);
        assertThat(Access.NULL.decrypt("test", true), is("test"));
        assertFalse(Access.NULL.willLog(Level.DEBUG));
        assertThat(Access.NULL.getProperties(), is(not(nullValue())));
    }

}
