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
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.onap.ccsdk.apps.cadi.PropAccess;
import org.onap.ccsdk.apps.cadi.config.Get;
import org.onap.ccsdk.apps.cadi.config.GetAccess;

public class JU_GetAccess {

    private String defaultVal = "some default value";

    private ByteArrayOutputStream outStream;

    private PropAccess access;
    private Get.AccessGet accessGet;
    private File file;
    private String filePath;

    @Before
    public void setup() throws IOException {
        outStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outStream));

        file = File.createTempFile("GetAccess_test", "");
        filePath = file.getAbsolutePath();

        access = new PropAccess();
        access.setProperty("cadi_prop_files", filePath);
        accessGet = new Get.AccessGet(access);

    }

    @After
    public void tearDown() {
        System.setOut(System.out);

        file.delete();
    }

    @Test
    public void constructorTest() {
        String output;

        @SuppressWarnings("unused")
        GetAccess getAccess = new GetAccess(accessGet);
        String[] lines = outStream.toString().split(System.lineSeparator());
        assertThat(lines.length, is(5));
        output = lines[0].split(" ", 2)[1];

    }

    @Test
    public void getPropertyTest1() {
        GetAccess getAccess = new GetAccess(accessGet);

        getAccess.setProperty("tag", "value");
        assertThat(getAccess.getProperty("tag", defaultVal), is("value"));
        assertThat(getAccess.getProperty("not_a_tag", defaultVal), is(defaultVal));
    }

    @Test
    public void getPropertyTest2() {
        GetAccess getAccess = new GetAccess(accessGet);

        getAccess.setProperty("tag", "value");
        assertThat(getAccess.getProperty("tag"), is("value"));
        assertThat(getAccess.getProperty("not_a_tag"), is(nullValue()));
    }

    @Test
    public void getTest() {
        GetAccess getAccess = new GetAccess(accessGet);
        assertThat((Get.AccessGet)getAccess.get(), is(accessGet));
    }

}
