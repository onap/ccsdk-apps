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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.onap.ccsdk.apps.cadi.AbsUserCache;
import org.onap.ccsdk.apps.cadi.PropAccess;
import org.onap.ccsdk.apps.cadi.config.UsersDump;
import org.onap.ccsdk.apps.cadi.lur.LocalLur;
import org.onap.ccsdk.apps.cadi.lur.LocalPermission;
import org.onap.ccsdk.apps.cadi.util.Split;

public class JU_UsersDump {

    private ByteArrayOutputStream outStream;
    private ByteArrayOutputStream stdoutSuppressor;

    private static final String expected = "<?xml version='1.0' encoding='utf-8'?>\n" +
        "<!--\n" +
        "    Code Generated Tomcat Users and Roles from AT&T LUR on ...\n" +
        "-->\n" +
        "<tomcat-users>\n" +
        "  <role rolename=\"suser\"/>\n" +
        "  <role rolename=\"admin\"/>\n" +
        "  <role rolename=\"groupB\"/>\n" +
        "  <role rolename=\"groupA\"/>\n" +
        "  \n" +
        "  <user username=\"hisname@people.osaaf.org\" roles=\"suser\"/>\n" +
        "  <user username=\"yourname@people.osaaf.org\" roles=\"admin\"/>\n" +
        "  <user username=\"myname@people.osaaf.org\" roles=\"admin\"/>\n" +
        "  <user username=\"m1234@people.osaaf.org\" roles=\"suser\"/>\n" +
        "  <user username=\"myname\" roles=\"groupB,groupA\"/>\n" +
        "  <user username=\"hername@people.osaaf.org\" roles=\"suser\"/>\n" +        
        "</tomcat-users>\n";

    private final static String groups = "myname:groupA,groupB";
    private final static String names = "admin:myname,yourname;suser:hisname,hername,m1234";

    private AbsUserCache<LocalPermission> lur;

    @Before
    public void setup() throws IOException {
        outStream = new ByteArrayOutputStream();
        stdoutSuppressor = new ByteArrayOutputStream();

        System.setOut(new PrintStream(stdoutSuppressor));

        lur = new LocalLur(new PropAccess(), groups, names);
    }

    @After
    public void tearDown() {
        System.setOut(System.out);
    }

    @Test
    public void writeTest() throws IOException {
        UsersDump.write(outStream, lur);
        String[] actualLines = Split.splitTrim('\n', outStream.toString());
        String[] expectedLines = Split.splitTrim('\n', expected);
        for (String s : actualLines) {
            System.out.println(s);
        }

        assertThat(actualLines.length, is(expectedLines.length));

        // Check that the output starts with an XML tag
        assertThat(actualLines[0], is(expectedLines[0]));
        // Check that lines 2-4 are a comment
        assertThat(actualLines[1], is(expectedLines[1]));
        assertThat(actualLines[3], is(expectedLines[3]));

        // Check that the rest of the output matches the expected output
        for (int i = 4; i < actualLines.length; i++) {
            assertThat(actualLines[i], is(expectedLines[i]));
        }

        // Run the test again with outStream as a PrintStream (for coverage)
        outStream.reset();
        UsersDump.write(new PrintStream(outStream), lur);
        actualLines = Split.splitTrim('\n', outStream.toString());

        assertThat(actualLines.length, is(expectedLines.length));

        // Check that the output starts with an XML tag
        assertThat(actualLines[0], is(expectedLines[0]));
        // Check that lines 2-4 are a comment
        assertThat(actualLines[1], is(expectedLines[1]));
        assertThat(actualLines[3], is(expectedLines[3]));

        // Check that the rest of the output matches the expected output
        for (int i = 4; i < actualLines.length; i++) {
            assertThat(actualLines[i], is(expectedLines[i]));
        }
    }

    @Test
    public void updateUsersTest() {
        String output;
        File outputFile = new File("src/test/resources/userdump.xml");
        assertThat(outputFile.exists(), is(false));

        output = UsersDump.updateUsers("src/test/resources/userdump.xml", (LocalLur) lur);
        assertThat(output, is(nullValue()));
        assertThat(outputFile.exists(), is(true));

        output = UsersDump.updateUsers("src/test/resources/userdump.xml", (LocalLur) lur);
        assertThat(output, is(nullValue()));
        assertThat(outputFile.exists(), is(true));

        outputFile.delete();
    }

}
