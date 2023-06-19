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

package org.onap.ccsdk.apps.cadi.principal.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.security.Principal;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import org.onap.ccsdk.apps.cadi.principal.X509Principal;

public class JU_X509Principal {

    private final String name = "x509 name";
    private final byte[] cred = "super duper secret password".getBytes();

    @Mock
    X509Certificate cert;

    @Mock
    Principal subject;

    @Before
    public void setup() throws CertificateEncodingException {
        MockitoAnnotations.initMocks(this);
        when(cert.getEncoded()).thenReturn(cred);
    }

    @Test
    public void constructor1Test() throws IOException {
        X509Principal x509 = new X509Principal(name, cert);
        // Call twice to hit both branches
        assertThat(x509.getAsHeader(), is("X509 " + cred));
        assertThat(x509.getAsHeader(), is("X509 " + cred));
        assertThat(x509.toString(), is("X509 Authentication for " + name));
        assertTrue(x509.getCred().equals(cred));
        assertThat(x509.getName(), is(name));
        assertThat(x509.tag(), is("x509"));
    }

    @Test
    public void constructor2Test() throws IOException {
        X509Principal x509 = new X509Principal(name, cert, cred,null);
        // Call twice to hit both branches
        assertThat(x509.getAsHeader(), is("X509 " + cred));
        assertThat(x509.toString(), is("X509 Authentication for " + name));
        assertTrue(x509.getCred().equals(cred));
        assertThat(x509.getName(), is(name));
        assertThat(x509.tag(), is("x509"));
    }

    @Test
    public void constructor3Test() throws IOException {
        final String longName = "name@domain";
        when(subject.getName()).thenReturn("OU=" + longName + ",extra");
        when(cert.getSubjectDN()).thenReturn(subject);
        X509Principal x509 = new X509Principal(cert, cred,null);
        // Call twice to hit both branches
        assertThat(x509.getAsHeader(), is("X509 " + cred));
        assertThat(x509.toString(), is("X509 Authentication for " + longName));
        assertTrue(x509.getCred().equals(cred));
        assertThat(x509.getName(), is(longName));

        when(subject.getName()).thenReturn(longName + ",extra");
        when(cert.getSubjectDN()).thenReturn(subject);
        try {
            x509 = new X509Principal(cert, cred, null);
            fail("Should have thrown an Exception");
        } catch (IOException e) {
            assertThat(e.getMessage(), is("X509 does not have Identity as CN"));
        }

        when(subject.getName()).thenReturn("OU=" + longName);
        when(cert.getSubjectDN()).thenReturn(subject);
        try {
            x509 = new X509Principal(cert, cred, null);
            fail("Should have thrown an Exception");
        } catch (IOException e) {
            assertThat(e.getMessage(), is("X509 does not have Identity as CN"));
        }

        when(subject.getName()).thenReturn("OU=" + name + ",exta");
        when(cert.getSubjectDN()).thenReturn(subject);
        try {
            x509 = new X509Principal(cert, cred, null);
            fail("Should have thrown an Exception");
        } catch (IOException e) {
            assertThat(e.getMessage(), is("X509 does not have Identity as CN"));
        }

    }

    @Test
    public void throwsTest() throws CertificateEncodingException {
        when(cert.getEncoded()).thenThrow(new CertificateEncodingException());
        X509Principal x509 = new X509Principal(name, cert);
        assertThat(x509.getCred(), is(nullValue()));
        try {
            x509.getAsHeader();
            fail("Should have thrown an Exception");
        } catch (IOException e) {
        }
    }

    @Test
    public void getCredTest() {
        X509Principal x509 = new X509Principal(name, cert);
        // Call twice to hit both branches
        assertTrue(x509.getCred().equals(cred));
        assertTrue(x509.getCred().equals(cred));
    }

}
