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
import org.junit.*;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;

import org.onap.ccsdk.apps.cadi.AES;
import org.onap.ccsdk.apps.cadi.CadiException;
import org.onap.ccsdk.apps.cadi.Symm;

public class JU_AES {
    private AES aes;
    private ByteArrayInputStream baisEncrypt;
    private ByteArrayInputStream baisDecrypt;
    private ByteArrayOutputStream baosEncrypt;
    private ByteArrayOutputStream baosDecrypt;

    private ByteArrayOutputStream errStream;

    @Before
    public void setup() throws Exception {
         byte[] keyBytes = new byte[AES.AES_KEY_SIZE/8];
         char[] codeset = Symm.base64.codeset;
         int offset = (Math.abs(codeset[0]) + 47) % (codeset.length - keyBytes.length);
         for (int i = 0; i < keyBytes.length; ++i) {
             keyBytes[i] = (byte)codeset[i+offset];
         }
         aes = new AES(keyBytes, 0, keyBytes.length);

        errStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errStream));
    }

    @After
    public void tearDown() {
        System.setErr(System.err);
    }

    @Test
    public void newKeyTest() throws Exception {
        SecretKey secretKey = AES.newKey();
        assertThat(secretKey.getAlgorithm(), is(AES.class.getSimpleName()));
    }

    @Test
    public void encryptDecrpytFromBytes() throws Exception {
        String orig = "I'm a password, really";
        byte[] encrypted = aes.encrypt(orig.getBytes());
        byte[] decrypted = aes.decrypt(encrypted);
        assertThat(new String(decrypted), is(orig));

        Field aeskeySpec_field = AES.class.getDeclaredField("aeskeySpec");
        aeskeySpec_field.setAccessible(true);
        aeskeySpec_field.set(aes, null);

        try {
            aes.encrypt(orig.getBytes());
            fail("Should have thrown an exception");
        } catch (CadiException e) {
        }
        try {
            aes.decrypt(encrypted);
            fail("Should have thrown an exception");
        } catch (CadiException e) {
        }
    }

    @Test
    public void saveToFileTest() throws Exception {
        String filePath = "src/test/resources/output_key";
        File keyfile = new File(filePath);
        aes.save(keyfile);
        assertTrue(Files.isReadable(Paths.get(filePath)));
        assertFalse(Files.isWritable(Paths.get(filePath)));
        assertFalse(Files.isExecutable(Paths.get(filePath)));
        keyfile.delete();
    }

    @Test
    public void encryptDecryptFromInputStream() throws Exception {
        String orig = "I'm a password, really";
        byte[] b64encrypted;
        String output;

        CipherInputStream cisEncrypt;
        CipherInputStream cisDecrypt;

        // Test CipherInputStream
        baisEncrypt = new ByteArrayInputStream(orig.getBytes());
        cisEncrypt = aes.inputStream(baisEncrypt, true);
        baosEncrypt = new ByteArrayOutputStream();
        transferFromInputStreamToOutputStream(cisEncrypt, baosEncrypt);
        cisEncrypt.close();

        b64encrypted = baosEncrypt.toByteArray();

        baisDecrypt = new ByteArrayInputStream(b64encrypted);
        cisDecrypt = aes.inputStream(baisDecrypt, false);
        baosDecrypt = new ByteArrayOutputStream();
        transferFromInputStreamToOutputStream(cisDecrypt, baosDecrypt);
        cisDecrypt.close();

        output = new String(baosDecrypt.toByteArray());
        assertThat(output, is(orig));

        Field aeskeySpec_field = AES.class.getDeclaredField("aeskeySpec");
        aeskeySpec_field.setAccessible(true);
        aeskeySpec_field.set(aes, null);

        assertNull(aes.inputStream(baisEncrypt, true));
        assertThat(errStream.toString(), is("Error creating Aes CipherInputStream\n"));
    }

    @Test
    public void encryptDecryptFromOutputStream() throws Exception {
        String orig = "I'm a password, really";
        byte[] b64encrypted;
        String output;

        CipherOutputStream cosEncrypt;
        CipherOutputStream cosDecrypt;

        // Test CipherOutputStream
        baisEncrypt = new ByteArrayInputStream(orig.getBytes());
        baosEncrypt = new ByteArrayOutputStream();
        cosEncrypt = aes.outputStream(baosEncrypt, true);
        transferFromInputStreamToOutputStream(baisEncrypt, cosEncrypt);
        cosEncrypt.close();

        b64encrypted = baosEncrypt.toByteArray();

        baosDecrypt = new ByteArrayOutputStream();
        cosDecrypt = aes.outputStream(baosDecrypt, false);
        baisDecrypt = new ByteArrayInputStream(b64encrypted);
        transferFromInputStreamToOutputStream(baisDecrypt, cosDecrypt);
        cosDecrypt.close();

        output = new String(baosDecrypt.toByteArray());
        assertThat(output, is(orig));

        Field aeskeySpec_field = AES.class.getDeclaredField("aeskeySpec");
        aeskeySpec_field.setAccessible(true);
        aeskeySpec_field.set(aes, null);

        assertNull(aes.outputStream(baosEncrypt, true));
        assertThat(errStream.toString(), is("Error creating Aes CipherOutputStream\n"));
    }

    public void transferFromInputStreamToOutputStream(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[200];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
    }

}
