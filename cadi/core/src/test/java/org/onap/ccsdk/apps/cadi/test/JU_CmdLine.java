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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.ccsdk.apps.cadi.CmdLine;
import org.onap.ccsdk.apps.cadi.Symm;

public class JU_CmdLine {

    @Mock
    private OutputStream thrower;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private String password;
    private String keyfile;
    private String quickBrownFoxPlain = "The quick brown fox jumps over the lazy dog";
    private String quickBrownFoxMD5 = "0x9e107d9d372bb6826bd81d3542a419d6";
    private String quickBrownFoxSHA256 = "0xd7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592";
    private Symm symm;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        System.setOut(new PrintStream(outContent));

        Properties p = new Properties();
        p.setProperty("force_exit", "false");

        CmdLine.setSystemExit(false);
        keyfile = "src/test/resources/keyfile";
        password = "password";

        File keyF = new File("src/test/resources", "keyfile");
        FileInputStream fis = new FileInputStream(keyF);
        try {
            symm = Symm.obtain(fis);
        } finally {
            fis.close();
        }
    }

    @After
    public void restoreStreams() throws IOException {
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void digestTest() throws Exception {
        CmdLine.main(new String[]{"digest", password, keyfile});
        String decrypted = symm.depass(outContent.toString());
        assertThat(decrypted, is(password));

        System.setIn(new ByteArrayInputStream(password.getBytes()));
        CmdLine.main(new String[]{"digest", "-i", keyfile});
        decrypted = symm.depass(outContent.toString());
        assertThat(decrypted, is(password));
    }

    @Test
    public void encode64Test() throws Exception {
        CmdLine.main(new String[]{"encode64", password});
        String decrypted = Symm.base64.decode(outContent.toString());
        assertThat(decrypted, is(password));
    }

    @Test
    public void decode64Test() throws Exception {
        String encrypted = Symm.base64.encode(password);
        CmdLine.main(new String[]{"decode64", encrypted});
        assertThat(outContent.toString(), is(password + System.lineSeparator()));
    }

    @Test
    public void encode64urlTest() throws Exception {
        CmdLine.main(new String[]{"encode64url", password});
        String decrypted = Symm.base64url.decode(outContent.toString());
        assertThat(decrypted, is(password));
    }

    @Test
    public void decode64urlTest() throws Exception {
        String encrypted = Symm.base64url.encode(password);
        CmdLine.main(new String[]{"decode64url", encrypted});
        assertThat(outContent.toString(), is(password  + System.lineSeparator()));
    }

    @Test
    public void md5Test() throws Exception {
        CmdLine.main(new String[]{"md5", quickBrownFoxPlain});
        assertThat(outContent.toString(), is(quickBrownFoxMD5  + System.lineSeparator()));
    }

    @Test
    public void sha256Test() throws Exception {
        CmdLine.main(new String[]{"sha256", quickBrownFoxPlain});
        assertThat(outContent.toString(), is(quickBrownFoxSHA256  + System.lineSeparator()));

        outContent.reset();
        CmdLine.main(new String[]{"sha256", quickBrownFoxPlain, "10"});
        String hash1 = outContent.toString();

        outContent.reset();
        CmdLine.main(new String[]{"sha256", quickBrownFoxPlain, "10"});
        String hash2 = outContent.toString();

        outContent.reset();
        CmdLine.main(new String[]{"sha256", quickBrownFoxPlain, "11"});
        String hash3 = outContent.toString();

        assertThat(hash1, is(hash2));
        assertThat(hash1, is(not(hash3)));
    }

    @Test
    public void keygenTest() throws Exception {
        CmdLine.main(new String[]{"keygen"});
        assertThat(outContent.toString().length(), is(2074));

        String filePath = "test/output_key";
        File testDir = new File("test");
        if (!testDir.exists()) {
            testDir.mkdirs();
        }
        CmdLine.main(new String[]{"keygen", filePath});
        File keyfile = new File(filePath);
        assertTrue(Files.isReadable(Paths.get(filePath)));
        assertFalse(Files.isWritable(Paths.get(filePath)));
        //assertFalse(Files.isExecutable(Paths.get(filePath)));
        keyfile.delete();
    }

    @Test
    public void passgenTest() throws Exception {
        CmdLine.main(new String[]{"passgen"});
        String output = outContent.toString().trim();
        assertThat(output.length(), is(24));
        assertTrue(containsAny(output, "+!@#$%^&*(){}[]?:;,."));
        assertTrue(containsAny(output, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertTrue(containsAny(output, "abcdefghijklmnopqrstuvwxyz"));
        assertTrue(containsAny(output, "0123456789"));

        int length = 10;
        outContent.reset();
        CmdLine.main(new String[]{"passgen", String.valueOf(length)});
        output = outContent.toString().trim();
        assertThat(output.length(), is(length));

        length = 5;
        outContent.reset();
        CmdLine.main(new String[]{"passgen", String.valueOf(length)});
        output = outContent.toString().trim();
        assertThat(output.length(), is(8));

        // Check that the custom hasRepeats method works
        assertTrue(hasRepeats("aa"));
        assertTrue(hasRepeats("baa"));
        assertTrue(hasRepeats("aab"));
        assertTrue(hasRepeats("baab"));
        assertFalse(hasRepeats("abc"));
        assertFalse(hasRepeats("aba"));

        // Run this a bunch of times for coverage
        for (int i = 0; i < 1000; i++) {
            outContent.reset();
            CmdLine.main(new String[]{"passgen"});
            output = outContent.toString().trim();
            assertFalse(hasRepeats(output));
        }
    }

    @Test
    public void urlgenTest() throws Exception {
        CmdLine.main(new String[]{"urlgen"});
        String output = outContent.toString().trim();
        assertThat(output.length(), is(24));

        int length = 5;
        outContent.reset();
        CmdLine.main(new String[]{"urlgen", String.valueOf(length)});
        output = outContent.toString().trim();
        assertThat(output.length(), is(5));
    }

    @Test
    public void showHelpTest() {
        String lineSeparator = System.lineSeparator();
        String expected =
            "Usage: java -jar <this jar> ..." + lineSeparator +
            "  keygen [<keyfile>]                     (Generates Key on file, or Std Out)" + lineSeparator +
            "  digest [<passwd>|-i|] <keyfile>        (Encrypts Password with \"keyfile\"" + lineSeparator +
            "                                          if passwd = -i, will read StdIn" + lineSeparator +
            "                                          if passwd is blank, will ask securely)" + lineSeparator +
            "  undigest <enc:...> <keyfile>           (Decrypts Encoded with \"keyfile\")" + lineSeparator +
            "  passgen <digits>                       (Generate Password of given size)" + lineSeparator +
            "  urlgen <digits>                        (Generate URL field of given size)" + lineSeparator +
            "  encode64 <your text>                   (Encodes to Base64)" + lineSeparator +
            "  decode64 <base64 encoded text>         (Decodes from Base64)" + lineSeparator +
            "  encode64url <your text>                (Encodes to Base64 URL charset)" + lineSeparator +
            "  decode64url <base64url encoded text>   (Decodes from Base64 URL charset)" + lineSeparator +
            "  sha256 <text> <salts(s)>               (Digest String into SHA256 Hash)" + lineSeparator +
            "  md5 <text>                             (Digest String into MD5 Hash)" + lineSeparator;

        CmdLine.main(new String[]{});

        assertThat(outContent.toString(), is(expected));
    }

    private boolean containsAny(String str, String searchChars) {
        for (char c : searchChars.toCharArray()) {
            if (str.indexOf(c) >= 0) {
                return true;
            }
        }
        return false;
    }

    private boolean hasRepeats(String str) {
        int c = -1;
        int last;
        for (int i = 0; i < str.length(); i++) {
            last = c;
            c = str.charAt(i);
            if (c == last) {
                return true;
            }
        }
        return false;
    }

}
