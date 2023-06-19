/**
 * ============LICENSE_START====================================================
 * org.onap.ccsdk
 * ===========================================================================
 * Copyright (c) 2023 AT&T Intellectual Property. All rights reserved.
 * ===========================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END====================================================
 *
 */

package org.onap.ccsdk.apps.cadi.config.test;


import static org.junit.Assert.assertNotNull;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.onap.ccsdk.apps.cadi.CadiException;
import org.onap.ccsdk.apps.cadi.PropAccess;
import org.onap.ccsdk.apps.cadi.config.Config;
import org.onap.ccsdk.apps.cadi.config.SecurityInfo;

public class JU_SecurityInfo {

    private static PropAccess access;

    private static final String keyStoreFileName = "src/test/resources/keystore.p12";
    private static final String keyStorePassword = "Password for the keystore";
    private static final String keyPassword = "Password for the key";

    private static final String trustStoreFileName = "src/test/resources/truststore.jks";
    private static final String trustStorePasswd = "Password for the truststore";

    @BeforeClass
    public static void setupOnce() throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, null);
        keyStore.store(new FileOutputStream(keyStoreFileName), keyStorePassword.toCharArray());

        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(null, null);
        trustStore.store(new FileOutputStream(trustStoreFileName), trustStorePasswd.toCharArray());
    }

    @Before
    public void setup() throws IOException {
        access = new PropAccess(new PrintStream(new ByteArrayOutputStream()), new String[0]);

        access.setProperty(Config.CADI_KEYSTORE, keyStoreFileName);
        access.setProperty(Config.CADI_KEYSTORE_PASSWORD, access.encrypt(keyStorePassword));
        access.setProperty(Config.CADI_KEY_PASSWORD, access.encrypt(keyPassword));

        access.setProperty(Config.CADI_TRUSTSTORE, trustStoreFileName);
        access.setProperty(Config.CADI_TRUSTSTORE_PASSWORD, access.encrypt(trustStorePasswd));
    }

    @AfterClass
    public static void tearDownOnce() {
        File keyStoreFile = new File(keyStoreFileName);
        if (keyStoreFile.exists()) {
            keyStoreFile.delete();
        }
        File trustStoreFile = new File(trustStoreFileName);
        if (trustStoreFile.exists()) {
            trustStoreFile.delete();
        }
    }

    @Test
    public void test() throws CadiException {
        SecurityInfo si = new SecurityInfo(access);

        assertNotNull(si.getSSLSocketFactory());
        assertNotNull(si.getSSLContext());
        assertNotNull(si.getKeyManagers());

        access.setProperty(Config.CADI_TRUST_MASKS, "123.123.123.123");
        si = new SecurityInfo(access);
    }

    @Test(expected = CadiException.class)
    public void nullkeyStoreTest() throws CadiException {
        access.setProperty(Config.CADI_KEYSTORE, "passwords.txt");
        @SuppressWarnings("unused")
        SecurityInfo si = new SecurityInfo(access);
    }

    @Test(expected = CadiException.class)
    public void nullTrustStoreTest() throws CadiException {
        access.setProperty(Config.CADI_TRUSTSTORE, "passwords.txt");
        @SuppressWarnings("unused")
        SecurityInfo si = new SecurityInfo(access);
    }


    @Test(expected = NumberFormatException.class)
    public void badTrustMaskTest() throws CadiException {
        access.setProperty(Config.CADI_TRUST_MASKS, "trustMask");
        @SuppressWarnings("unused")
        SecurityInfo si = new SecurityInfo(access);
    }

    @Test
    public void coverageTest() throws CadiException {
        PropAccess badAccess = new PropAccess(new PrintStream(new ByteArrayOutputStream()), new String[0]);
        @SuppressWarnings("unused")
        SecurityInfo si = new SecurityInfo(badAccess);
        badAccess.setProperty(Config.CADI_KEYSTORE, keyStoreFileName);
        si = new SecurityInfo(badAccess);
    }

}
