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
import org.onap.ccsdk.apps.cadi.LocatorException;

import static org.hamcrest.CoreMatchers.is;

public class JU_LocatorException {
    @Test
    public void stringTest() {
        LocatorException exception = new LocatorException("New Exception");
        assertNotNull(exception);
        assertThat(exception.getMessage(), is("New Exception"));
    }

    @Test
    public void throwableTest() {
        LocatorException exception = new LocatorException(new Throwable("New Exception"));
        assertNotNull(exception);
        assertThat(exception.getMessage(), is("java.lang.Throwable: New Exception"));
    }

    @Test
    public void stringThrowableTest() {
        LocatorException exception = new LocatorException("New Exception",new Throwable("New Exception"));
        assertNotNull(exception);
        assertThat(exception.getMessage(), is("New Exception"));
    }

    @Test
    public void characterSequenceTest() {
        CharSequence testCS = new String("New Exception");
        LocatorException exception = new LocatorException(testCS);
        assertNotNull(exception);
        assertThat(exception.getMessage(), is("New Exception"));
    }
}
