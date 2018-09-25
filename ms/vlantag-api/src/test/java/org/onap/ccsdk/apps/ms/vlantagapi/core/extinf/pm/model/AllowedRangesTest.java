/*******************************************************************************
 * Copyright © 2018 IBM.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************
*/
package org.onap.ccsdk.apps.ms.vlantagapi.core.extinf.pm.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class AllowedRangesTest {
    private AllowedRanges allowedRanges;

    @Before
    public void setUp() {
        allowedRanges = new AllowedRanges();
    }

    @Test
    public void testGetSEtMin() {
        allowedRanges.setMin("10");
        assertEquals("10", allowedRanges.getMin());
    }

    @Test
    public void testGetSEtMax() {
        allowedRanges.setMax("20");
        assertEquals("20", allowedRanges.getMax());
    }

    @Test
    public void testToStirng() {
        allowedRanges.setMax("20");
        allowedRanges.setMin("10");
        assertEquals("AllowedRanges [min=10, max=20]", allowedRanges.toString());
    }
}
