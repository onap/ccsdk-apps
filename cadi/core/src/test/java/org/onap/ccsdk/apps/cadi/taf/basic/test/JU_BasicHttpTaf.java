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

package org.onap.ccsdk.apps.cadi.taf.basic.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConnection;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpUpgradeHandler;
import jakarta.servlet.http.Part;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.ccsdk.apps.cadi.BasicCred;
import org.onap.ccsdk.apps.cadi.CachedPrincipal;
import org.onap.ccsdk.apps.cadi.CachedPrincipal.Resp;
import org.onap.ccsdk.apps.cadi.CredVal;
import org.onap.ccsdk.apps.cadi.PropAccess;
import org.onap.ccsdk.apps.cadi.Symm;
import org.onap.ccsdk.apps.cadi.Taf.LifeForm;
import org.onap.ccsdk.apps.cadi.taf.basic.BasicHttpTaf;

public class JU_BasicHttpTaf {

    private final static String realm = "realm";
    private final static String id = "id";
    private final static String addr = "addr";

    private final static String name = "User";
    private final static String password = "password";
    private final static String content = name + ":" + password;
    private static String encrypted;

    private final static long timeToLive = 10000L;

    private PropAccess access;

    @Mock private HttpServletResponse respMock;
    @Mock private HttpServletRequest reqMock;
    @Mock private CredVal rbacMock;
    @Mock private CachedPrincipal princMock;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        access = new PropAccess(new PrintStream(new ByteArrayOutputStream()), new String[0]);
        encrypted = new String(Symm.base64.encode(content.getBytes()));
    }

    @Test
    public void test() {
        BasicHttpTaf taf = new BasicHttpTaf(access, rbacMock, realm, timeToLive, true);
        BasicCredStub bcstub = new BasicCredStub();
        assertThat(taf.validate(LifeForm.SBLF, bcstub, respMock), is(not(nullValue())));

        assertThat(taf.validate(LifeForm.SBLF, reqMock, respMock), is(not(nullValue())));

        when(reqMock.getHeader("Authorization")).thenReturn("test");
        assertThat(taf.validate(LifeForm.SBLF, reqMock, respMock), is(not(nullValue())));

        when(reqMock.getHeader("Authorization")).thenReturn("Basic " + encrypted);
        assertThat(taf.validate(LifeForm.SBLF, reqMock, respMock), is(not(nullValue())));

        assertThat(taf.revalidate(princMock, "state"), is(Resp.NOT_MINE));

        assertThat(taf.toString(), is("Basic Auth enabled on realm: " + realm));
    }

    private class BasicCredStub implements HttpServletRequest, BasicCred {
        @Override public String getUser() { return id; }
        @Override public String getRemoteAddr() { return addr; }

        @Override public AsyncContext getAsyncContext() { return null; }
        @Override public Object getAttribute(String arg0) { return null; }
        @Override public Enumeration<String> getAttributeNames() { return null; }
        @Override public String getCharacterEncoding() { return null; }
        @Override public int getContentLength() { return 0; }
        @Override public String getContentType() { return null; }
        @Override public DispatcherType getDispatcherType() { return null; }
        @Override public ServletInputStream getInputStream() throws IOException { return null; }
        @Override public String getLocalAddr() { return null; }
        @Override public String getLocalName() { return null; }
        @Override public int getLocalPort() { return 0; }
        @Override public Locale getLocale() { return null; }
        @Override public Enumeration<Locale> getLocales() { return null; }
        @Override public String getParameter(String arg0) { return null; }
        @Override public Map<String, String[]> getParameterMap() { return null; }
        @Override public Enumeration<String> getParameterNames() { return null; }
        @Override public String[] getParameterValues(String arg0) { return null; }
        @Override public String getProtocol() { return null; }
        @Override public BufferedReader getReader() throws IOException { return null; }

        @Override public String getRemoteHost() { return null; }
        @Override public int getRemotePort() { return 0; }
        @Override public RequestDispatcher getRequestDispatcher(String arg0) { return null; }
        @Override public String getScheme() { return null; }
        @Override public String getServerName() { return null; }
        @Override public int getServerPort() { return 0; }
        @Override public ServletContext getServletContext() { return null; }
        @Override public boolean isAsyncStarted() { return false; }
        @Override public boolean isAsyncSupported() { return false; }
        @Override public boolean isSecure() { return false; }
        @Override public void removeAttribute(String arg0) { }
        @Override public void setAttribute(String arg0, Object arg1) { }
        @Override public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException { }
        @Override public AsyncContext startAsync() throws IllegalStateException { return null; }
        @Override public AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1) throws IllegalStateException { return null; }
        @Override public byte[] getCred() { return null; }
        @Override public void setUser(String user) { }
        @Override public void setCred(byte[] passwd) { }
        @Override public boolean authenticate(HttpServletResponse arg0) throws IOException, ServletException { return false; }
        @Override public String getAuthType() { return null; }
        @Override public String getContextPath() { return null; }
        @Override public Cookie[] getCookies() { return null; }
        @Override public long getDateHeader(String arg0) { return 0; }
        @Override public String getHeader(String arg0) { return null; }
        @Override public Enumeration<String> getHeaderNames() { return null; }
        @Override public Enumeration<String> getHeaders(String arg0) { return null; }
        @Override public int getIntHeader(String arg0) { return 0; }
        @Override public String getMethod() { return null; }
        @Override public Part getPart(String arg0) throws IOException, ServletException { return null; }
        @Override public Collection<Part> getParts() throws IOException, ServletException { return null; }
        @Override public String getPathInfo() { return null; }
        @Override public String getPathTranslated() { return null; }
        @Override public String getQueryString() { return null; }
        @Override public String getRemoteUser() { return null; }
        @Override public String getRequestURI() { return null; }
        @Override public StringBuffer getRequestURL() { return null; }
        @Override public String getRequestedSessionId() { return null; }
        @Override public String getServletPath() { return null; }
        @Override public HttpSession getSession() { return null; }
        @Override public HttpSession getSession(boolean arg0) { return null; }
        @Override public Principal getUserPrincipal() { return null; }
        @Override public boolean isRequestedSessionIdFromCookie() { return false; }
        @Override public boolean isRequestedSessionIdFromURL() { return false; }
        @Override public boolean isRequestedSessionIdValid() { return false; }
        @Override public boolean isUserInRole(String arg0) { return false; }
        @Override public void login(String arg0, String arg1) throws ServletException { }
        @Override public void logout() throws ServletException { }
        @Override
        public long getContentLengthLong() {
          return 0L;
        }
        @Override
        public String getRequestId() {
            return null;
        }
        @Override
        public String getProtocolRequestId() {
            return null;
        }
        @Override
        public ServletConnection getServletConnection() {
            return null;
        }
        @Override
        public String changeSessionId() {
            return null;
        }
        @Override
        public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {

            throw new UnsupportedOperationException("Unimplemented method 'upgrade'");
        }
    }
}
