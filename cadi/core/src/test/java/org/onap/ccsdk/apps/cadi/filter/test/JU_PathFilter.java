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

package org.onap.ccsdk.apps.cadi.filter.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.Principal;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.ccsdk.apps.cadi.PropAccess;
import org.onap.ccsdk.apps.cadi.config.Config;
import org.onap.ccsdk.apps.cadi.filter.PathFilter;

public class JU_PathFilter {

    private PropAccess access;

    @Mock private FilterConfig filterConfigMock;
    @Mock private ServletContext contextMock;
    @Mock private HttpServletRequest reqMock;
    @Mock private HttpServletResponse respMock;
    @Mock private FilterChain chainMock;
    @Mock private Principal princMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(filterConfigMock.getServletContext()).thenReturn(contextMock);
        when(reqMock.getUserPrincipal()).thenReturn(princMock);
        when(princMock.getName()).thenReturn("name");

        access = new PropAccess(new PrintStream(new ByteArrayOutputStream()), new String[0]);
    }

    @Test
    public void test() throws ServletException, IOException {
        PathFilter pathFilter = new PathFilter(access);
        try {
            pathFilter.init(filterConfigMock);
            fail("Should've thrown an exception");
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("PathFilter - pathfilter_ns is not set"));
        }

        when(contextMock.getAttribute(Config.PATHFILTER_NS)).thenReturn(5);
        when(contextMock.getAttribute(Config.PATHFILTER_STACK)).thenReturn(5);
        when(contextMock.getAttribute(Config.PATHFILTER_URLPATTERN)).thenReturn(5);
        when(contextMock.getAttribute(Config.PATHFILTER_NOT_AUTHORIZED_MSG)).thenReturn(5);
        pathFilter.init(filterConfigMock);

        pathFilter.doFilter(reqMock, respMock, chainMock);

        when(reqMock.isUserInRole(anyString())).thenReturn(true);
        pathFilter.doFilter(reqMock, respMock, chainMock);

        pathFilter.destroy();

        pathFilter = new PathFilter();
        pathFilter.init(filterConfigMock);

        pathFilter.doFilter(reqMock, respMock, chainMock);

        when(reqMock.isUserInRole(anyString())).thenReturn(false);
        pathFilter.doFilter(reqMock, respMock, chainMock);

        pathFilter.destroy();
    }

}
