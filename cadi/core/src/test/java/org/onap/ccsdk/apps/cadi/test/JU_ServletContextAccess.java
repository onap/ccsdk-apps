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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.onap.ccsdk.apps.cadi.PropAccess;
import org.onap.ccsdk.apps.cadi.ServletContextAccess;
import org.onap.ccsdk.apps.cadi.Access.Level;
import org.onap.ccsdk.apps.cadi.PropAccess.LogIt;

import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

import java.lang.reflect.Field;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;

@SuppressWarnings("unused")
public class JU_ServletContextAccess {

    private FilterConfig filter_mock;
    Enumeration<String> enumeration;

    private class CustomEnumeration implements Enumeration<String> {
        private int idx = 0;
        private final String[] elements = {"This", "is", "a", "test"};
        @Override
        public String nextElement() {
            return idx >= elements.length ? null : elements[idx++];
        }
        @Override
        public boolean hasMoreElements() {
            return idx < elements.length;
        }
    }

    @Before
    public void setup() {
        enumeration = new CustomEnumeration();
        filter_mock = mock(FilterConfig.class);
        when(filter_mock.getInitParameterNames()).thenReturn(enumeration);
    }


    @Test
    public void logTest() throws Exception {
        ServletContext sc_mock = mock(ServletContext.class);
        when(filter_mock.getServletContext()).thenReturn(sc_mock);
        ServletContextAccess sca = new ServletContextAccess(filter_mock);

        sca.log(Level.DEBUG);

        sca.setLogLevel(Level.DEBUG);
        sca.log(Level.DEBUG);

        try {
            sca.log(new Exception("This exception was thrown intentionally, please ignore it"));
        } catch (Exception e) {
            fail("Should have thrown an exception");
        }
    }

    @Test
    public void contextTest() {
        ServletContext sc_mock = mock(ServletContext.class);
        when(filter_mock.getServletContext()).thenReturn(sc_mock);
        ServletContextAccess sca = new ServletContextAccess(filter_mock);
        assertThat(sca.context(), instanceOf(ServletContext.class));
    }

}
