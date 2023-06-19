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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.*;
import org.junit.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import org.onap.ccsdk.apps.cadi.CadiException;
import org.onap.ccsdk.apps.cadi.PropAccess;
import org.onap.ccsdk.apps.cadi.Symm;

public class JU_Symm {
    private Symm defaultSymm;

    private ByteArrayOutputStream outStream;

    @Before
    public void setup() throws Exception {
        defaultSymm = new Symm(
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray()
                ,76, "Use default!" ,true, "Junit 1");
        outStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outStream));
    }

    @After
    public void tearDown() {
        System.setOut(System.out);
    }

    @Test
    public void constructorTest() throws Exception {
        Symm myCustomSymm = new Symm(
            "ACEGIKMOQSUWYacegikmoqsuwy02468+/".toCharArray(), 76, "Default", true, "Junit 2");
        Field convert_field = Symm.class.getDeclaredField("convert");
        convert_field.setAccessible(true);

        Class<?> Unordered_class = Class.forName("org.onap.ccsdk.apps.cadi.Symm$Unordered");
        assertThat(convert_field.get(myCustomSymm), instanceOf(Unordered_class));
    }

    @SuppressWarnings("unused")
    @Test
    public void copyTest() throws Exception {
        Symm copy = Symm.base64.copy(76);
    }

    @SuppressWarnings("deprecation")
    @Test
    public void deprecatedTest() {
        assertEquals(Symm.base64(), Symm.base64);
        assertEquals(Symm.base64noSplit(), Symm.base64noSplit);
        assertEquals(Symm.base64url(), Symm.base64url);
        assertEquals(Symm.baseCrypt(), Symm.encrypt);
    }

    @Test
    public void encodeDecodeStringTest() throws Exception {
        String orig = "hello";
        String b64encrypted = Symm.base64.encode(orig);
        assertEquals(Symm.base64.decode(b64encrypted), orig);

        String defaultEnrypted = defaultSymm.encode(orig);
        assertEquals(defaultSymm.decode(defaultEnrypted), orig);
    }

    @Test
    public void encodeDecodeByteArrayTest() throws Exception {
        String orig = "hello";
        byte[] b64encrypted = Symm.base64.encode(orig.getBytes());
        assertEquals(new String(Symm.base64.decode(b64encrypted)), orig);

        byte[] empty = null;
        assertTrue(Arrays.equals(Symm.base64.encode(empty), new byte[0]));
    }

    @Test
    public void encodeDecodeStringToStreamTest() throws Exception {
        String orig = "I'm a password, really";
        String b64encrypted;
        String output;

        ByteArrayOutputStream baosEncrypt = new ByteArrayOutputStream();
        Symm.base64.encode(orig, baosEncrypt);
        b64encrypted = new String(baosEncrypt.toByteArray());

        ByteArrayOutputStream baosDecrypt = new ByteArrayOutputStream();
        Symm.base64.decode(b64encrypted, baosDecrypt);
        output = new String(baosDecrypt.toByteArray());

        assertEquals(orig, output);
    }

    @Test
    public void encryptDecryptStreamWithPrefixTest() throws Exception {
        String orig = "I'm a password, really";
        byte[] b64encrypted;
        String output;

        byte[] prefix = "enc:".getBytes();

        ByteArrayInputStream baisEncrypt = new ByteArrayInputStream(orig.getBytes());
        ByteArrayOutputStream baosEncrypt = new ByteArrayOutputStream();
        Symm.base64.encode(baisEncrypt, baosEncrypt, prefix);

        b64encrypted = baosEncrypt.toByteArray();

        ByteArrayInputStream baisDecrypt = new ByteArrayInputStream(b64encrypted);
        ByteArrayOutputStream baosDecrypt = new ByteArrayOutputStream();
        Symm.base64.decode(baisDecrypt, baosDecrypt, prefix.length);

        output = new String(baosDecrypt.toByteArray());
        assertEquals(orig, output);
    }

    @Test
    public void randomGenTest() {
        // Ian - There really isn't a great way to test for randomness...
        String prev = null;
        for (int i = 0; i < 10; i++) {
            String current = Symm.randomGen(100);
            if (current.equals(prev)) {
                fail("I don't know how, but you generated the exact same random string twice in a row");
            }
            prev = current;
        }
        assertTrue(true);
    }

    @Test
    public void obtainTest() throws Exception {
        Symm symm = Symm.base64.obtain();

        String orig ="Another Password, please";
        String encrypted = symm.enpass(orig);
        String decrypted = symm.depass(encrypted);
        assertEquals(orig, decrypted);
    }

    @Test
    public void InputStreamObtainTest() throws Exception {
        byte[] keygen = Symm.keygen();

        Symm symm = Symm.obtain(new ByteArrayInputStream(keygen));

        String orig ="Another Password, please";
        String encrypted = symm.enpass(orig);
        String decrypted = symm.depass(encrypted);
        assertEquals(orig, decrypted);
    }

    @Test
    public void StringObtainTest() throws Exception {
        byte[] keygen = Symm.keygen();

        Symm symm = Symm.obtain(new String(keygen));

        String orig ="Another Password, please";
        String encrypted = symm.enpass(orig);
        String decrypted = symm.depass(encrypted);
        assertEquals(orig, decrypted);
    }

    @Test
    public void AccessObtainTest() throws Exception {
        PropAccess pa = new PropAccess("cadi_keyfile=src/test/resources/keyfile");
        Symm symm = Symm.obtain(pa);
        String orig ="Another Password, please";
        String encrypted = symm.enpass(orig);
        String decrypted = symm.depass(encrypted);
        assertEquals(orig, decrypted);

        try {
            PropAccess badPa = mock(PropAccess.class);
            when(badPa.getProperty("cadi_keyfile", null)).thenReturn("not_a_real_file.txt");
            symm = Symm.obtain(badPa);
            fail("Should have thrown an exception");
        } catch (CadiException e) {
            assertTrue(e.getMessage().contains("ERROR: "));
            assertTrue(e.getMessage().contains("not_a_real_file.txt"));
            assertTrue(e.getMessage().contains(" does not exist!"));
        }
    }

}
