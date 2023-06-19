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

package org.onap.ccsdk.apps.cadi.config.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.*;
import org.onap.ccsdk.apps.cadi.CadiException;
import org.onap.ccsdk.apps.cadi.PropAccess;
import org.onap.ccsdk.apps.cadi.SecuritySetter;
import org.onap.ccsdk.apps.cadi.config.SecurityInfoC;

public class JU_SecurityInfoC {

    ByteArrayOutputStream outStream;
    ByteArrayOutputStream errStream;

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

//    @Test
//    public void instanceTest() throws CadiException, MalformedURLException {
//        SecurityInfoC<HttpURLConnection> si = SecurityInfoC.instance(new PropAccess(), HttpURLConnection.class );
//        assertThat(si.defSS.getID(), is(SecurityInfoC.DEF_ID));
//        try {
//            si.defSS.setSecurity(new HttpURLConnectionStub());
//            fail("Should have thrown an exception");
//        } catch (CadiException e) {
//            assertTrue(e instanceof CadiException);
//            assertThat(e.getMessage(), is("No Client Credentials set."));
//        }
//        assertThat(si.defSS.setLastResponse(0), is(0));
//
//        // Try it again for coverage
//        SecurityInfoC<HttpURLConnection> siClone = SecurityInfoC.instance(new PropAccess(), HttpURLConnection.class);
//        assertThat(siClone, is(si));
//    }

    @Test
    public void setTest() throws MalformedURLException, CadiException {
        SecurityInfoC<HttpURLConnectionStub> si = SecurityInfoC.instance(new PropAccess(), HttpURLConnectionStub.class);
        SecuritySetter<HttpURLConnectionStub> ss = new SecuritySetterStub<HttpURLConnectionStub>();
        assertThat(si.set(ss), is(si));
        assertThat(si.defSS.getID(), is("Example ID"));
        try {
            si.defSS.setSecurity(new HttpURLConnectionStub());
            fail("Should have thrown an exception");
        } catch (CadiException e) {
            assertTrue(e instanceof CadiException);
            assertThat(e.getMessage(), is("Example exception"));
        }
        assertThat(si.defSS.setLastResponse(0), is(0));
        assertThat(si.defSS.setLastResponse(1), is(1));
        assertThat(si.defSS.setLastResponse(-1), is(-1));
    }

    public static class HttpURLConnectionStub extends HttpURLConnection {
        public HttpURLConnectionStub() throws MalformedURLException { super(new URL("http://www.example.com")); }
        @Override public void disconnect() { }
        @Override public boolean usingProxy() { return false; }
        @Override public void connect() throws IOException { }
    }

    private class SecuritySetterStub<CT> implements SecuritySetter<CT> {
        public String getID() { return "Example ID"; }
        public void setSecurity(CT client) throws CadiException { throw new CadiException("Example exception"); }
        public int setLastResponse(int respCode) { return respCode; }
    }

}
