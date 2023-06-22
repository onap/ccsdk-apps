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

package org.onap.ccsdk.apps.cadi.util.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.ByteArrayOutputStream;

import org.junit.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.onap.ccsdk.apps.cadi.util.JsonOutputStream;

public class JU_JsonOutputStream {

    private JsonOutputStream jos;

    @Before
    public void setup() {
        jos = new JsonOutputStream(new ByteArrayOutputStream());
    }

    @Test
    public void constructorTest() {
        jos = new JsonOutputStream(System.out);
        jos = new JsonOutputStream(System.err);
    }

    @Test
    public void writeTest() throws IOException {
        byte[] json = ("{" +
                         "name: user," +
                         "password: pass," +
                         "contact: {" +
                           "email: user@att.com," +
                           "phone: 555-5555" +
                         "}," +
                         "list: [" +
                           "item1," +
                           "item2" +
                         "],[],{}," +
                         "list:" +
                         "[" +
                           "item1," +
                           "item2" +
                         "]" +
                       "}").getBytes();
        jos.write(json);
    }

    @Test
    public void resetIndentTest() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        Field indentField = JsonOutputStream.class.getDeclaredField("indent");
        indentField.setAccessible(true);

        assertThat((int)indentField.get(jos), is(0));
        jos.resetIndent();
        assertThat((int)indentField.get(jos), is(1));
    }

    @Test
    public void coverageTest() throws IOException {
        jos.flush();
        jos.close();

        jos = new JsonOutputStream(System.out);
        jos.close();
    }

}
