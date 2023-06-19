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
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.ccsdk.apps.cadi.filter.AUTHZServlet;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JU_AUTHZServlet {

    @Mock private Servlet servletMock;
    @Mock private ServletConfig servletConfigMock;
    @Mock private HttpServletRequest reqMock;
    @Mock private HttpServletResponse respMock;
    @Mock private ServletRequestWrapper servletWrapperMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test() throws ServletException, IOException {
        AUTHZServletStub servlet = new AUTHZServletStub(Servlet.class);

        try {
            servlet.init(servletConfigMock);
            fail("Should've thrown an exception");
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("Invalid Servlet Delegate"));
        }

        setPrivateField(AUTHZServlet.class, "delegate", servlet, servletMock);
        servlet.init(servletConfigMock);
        servlet.getServletConfig();
        servlet.getServletInfo();

        servlet.service(reqMock, respMock);

        String[] roles = new String[] {"role1", "role2"};
        setPrivateField(AUTHZServlet.class, "roles", servlet, roles);
        servlet.service(reqMock, respMock);

        when(reqMock.isUserInRole("role1")).thenReturn(true);
        servlet.service(reqMock, respMock);

        try {
            servlet.service(servletWrapperMock, respMock);
            fail("Should've thrown an exception");
        } catch (ServletException e) {
            assertThat(e.getMessage(), is("JASPIServlet only supports HTTPServletRequest/HttpServletResponse"));
        }
        servlet.destroy();
    }

    private class AUTHZServletStub extends AUTHZServlet<Servlet> {
        public AUTHZServletStub(Class<Servlet> cls) { super(cls); }
    }

    private void setPrivateField(Class<?> clazz, String fieldName, Object target, Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
            field.setAccessible(false);
        } catch (Exception e) {
            System.err.println("Could not set field [" + fieldName + "] to " + value);
        }
    }

}
