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

import org.junit.Test;
import org.onap.ccsdk.apps.cadi.CadiException;

import static org.hamcrest.CoreMatchers.is;

public class JU_CadiException {
    @Test
    public void testCadiException() {
        CadiException exception = new CadiException();

        assertNotNull(exception);
    }

    @Test
    public void testCadiExceptionString() {
        CadiException exception = new CadiException("New Exception");
        assertNotNull(exception);
        assertThat(exception.getMessage(), is("New Exception"));
    }

    @Test
    public void testCadiExceptionThrowable() {
        CadiException exception = new CadiException(new Throwable("New Exception"));
        assertNotNull(exception);
        assertThat(exception.getMessage(), is("java.lang.Throwable: New Exception"));
    }

    @Test
    public void testCadiExceptionStringThrowable() {
        CadiException exception = new CadiException("New Exception",new Throwable("New Exception"));
        assertNotNull(exception);
        assertThat(exception.getMessage(), is("New Exception"));

    }

    @Test
    public void testCadiException1() {
        CadiException exception = new CadiException();

        assertNotNull(exception);
    }

    @Test
    public void testCadiExceptionString1() {
        CadiException exception = new CadiException("New Exception");
        assertNotNull(exception);
        assertThat(exception.getMessage(), is("New Exception"));
    }

    @Test
    public void testCadiExceptionThrowable1() {
        CadiException exception = new CadiException(new Throwable("New Exception"));
        assertNotNull(exception);
        assertThat(exception.getMessage(), is("java.lang.Throwable: New Exception"));
    }

    @Test
    public void testCadiExceptionStringThrowable1() {
        CadiException exception = new CadiException("New Exception",new Throwable("New Exception"));
        assertNotNull(exception);
        assertThat(exception.getMessage(), is("New Exception"));

    }

    @Test
    public void testCadiException2() {
        CadiException exception = new CadiException();

        assertNotNull(exception);
    }

    @Test
    public void testCadiExceptionString2() {
        CadiException exception = new CadiException("New Exception");
        assertNotNull(exception);
        assertThat(exception.getMessage(), is("New Exception"));
    }

    @Test
    public void testCadiExceptionThrowable2() {
        CadiException exception = new CadiException(new Throwable("New Exception"));
        assertNotNull(exception);
        assertThat(exception.getMessage(), is("java.lang.Throwable: New Exception"));
    }

    @Test
    public void testCadiExceptionStringThrowable2() {
        CadiException exception = new CadiException("New Exception",new Throwable("New Exception"));
        assertNotNull(exception);
        assertThat(exception.getMessage(), is("New Exception"));

    }



}
