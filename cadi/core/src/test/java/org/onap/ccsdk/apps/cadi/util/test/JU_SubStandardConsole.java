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

package org.onap.ccsdk.apps.cadi.util.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;

import org.onap.ccsdk.apps.cadi.util.SubStandardConsole;

public class JU_SubStandardConsole {

    private String inputString = "An input string";
    private ByteArrayOutputStream outStream;
    private ByteArrayOutputStream errStream;
    private String lineSeparator = System.lineSeparator();

    @Before
    public void setup() {
        outStream = new ByteArrayOutputStream();
        errStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outStream));
        System.setErr(new PrintStream(errStream));
    }

    @After
    public void tearDown() {
        System.setOut(System.out);
        System.setErr(System.err);
    }

    @Test
    public void readLineTest() {
        byte[] input = inputString.getBytes();
        System.setIn(new ByteArrayInputStream(input));
        SubStandardConsole ssc = new SubStandardConsole();
        String output = ssc.readLine("%s" + lineSeparator, ">>> ");
        assertThat(output, is(inputString));
        assertThat(outStream.toString(), is(">>> " + lineSeparator));
    }

    @Test
    public void readLineTest2() {
        byte[] input = inputString.getBytes();
        System.setIn(new ByteArrayInputStream(input));
        SubStandardConsole ssc = new SubStandardConsole();
        String output = ssc.readLine("%s %s"  + lineSeparator, ">>> ", "Another argument for coverage");
        assertThat(output, is(inputString));
    }

    @Test
    public void readLineTest3() {
        byte[] input = "\n".getBytes();
        System.setIn(new ByteArrayInputStream(input));
        SubStandardConsole ssc = new SubStandardConsole();
        String output = ssc.readLine("%s" + lineSeparator, ">>> ");
        assertThat(output, is(">>> "));
        assertThat(outStream.toString(), is(">>> " + lineSeparator));
    }

    @Test
    public void readPasswordTest() {
        byte[] input = inputString.getBytes();
        System.setIn(new ByteArrayInputStream(input));
        SubStandardConsole ssc = new SubStandardConsole();
        char[] output = ssc.readPassword("%s" + lineSeparator, ">>> ");
        System.out.println(output);
        assertThat(output, is(inputString.toCharArray()));
        assertThat(outStream.toString(), is(">>> " + lineSeparator + "An input string"  + lineSeparator));
    }

    @Test
    public void printfTest() {
        byte[] input = inputString.getBytes();
        System.setIn(new ByteArrayInputStream(input));
        SubStandardConsole ssc = new SubStandardConsole();
        ssc.printf("%s", "A format specifier");
        assertThat(outStream.toString(), is("A format specifier"));
    }

    @Test
    public void throwsTest() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        BufferedReader brMock = mock(BufferedReader.class);
        when(brMock.readLine()).thenThrow(new IOException());

        SubStandardConsole ssc = new SubStandardConsole();

        Field brField = SubStandardConsole.class.getDeclaredField("br");
        brField.setAccessible(true);
        brField.set(ssc, brMock);

        assertThat(ssc.readLine(""), is(""));
        assertThat(errStream.toString(), is("uh oh..." + lineSeparator));
        errStream.reset();
        assertThat(ssc.readPassword("").length, is(0));
        assertThat(errStream.toString(), is("uh oh..." + lineSeparator));
    }

}
