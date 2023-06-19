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

package org.onap.ccsdk.apps.cadi.config.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.onap.ccsdk.apps.cadi.PropAccess;
import org.onap.ccsdk.apps.cadi.config.Get;
import org.onap.ccsdk.apps.cadi.config.MultiGet;

public class JU_MultiGet {

    private String defaultVal = "some default value";

    private ByteArrayOutputStream outStream;

    private MultiGet multiGet;
    private Get.AccessGet accessGet;
    private PropAccess access;

    @Before
    public void setup() throws IOException {
        outStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outStream));

        access = new PropAccess();
        access.setProperty("tag", "value");
        accessGet = new Get.AccessGet(access);
        multiGet = new MultiGet(accessGet, Get.NULL);
    }

    @After
    public void tearDown() {
        System.setOut(System.out);
    }

    @Test
    public void getTest() {
        assertThat(multiGet.get("tag", defaultVal, false), is("value"));
        assertThat(multiGet.get("not_a_tag", defaultVal, false), is(defaultVal));
    }

}
