/*-
 * ============LICENSE_START=======================================================
 * ONAP : CCSDK.apps
 * ================================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Modifications Copyright (C) 2018 IBM.
 * ================================================================================
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
 * ============LICENSE_END=========================================================
 */

package org.onap.ccsdk.apps.ms.neng.core.gen;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.onap.ccsdk.apps.ms.neng.core.policy.PolicySequence;

public class SequenceFormatterTest {
    
    private PolicySequence poly;
    
    @Before
    public void setUp()
    {
        poly = new PolicySequence();
    }
    
    @Test
    public void formatSequence() throws Exception {
        poly.setLength(3);;
        assertEquals("001", SequenceFormatter.formatSequence(1, poly));
        poly.setLength(2);;
        assertEquals("01", SequenceFormatter.formatSequence(1, poly));
        poly.setLength(4);;
        assertEquals("0999", SequenceFormatter.formatSequence(999, poly));
    }

    @Test
    public void formatSequenceAlpha() throws Exception {
        poly.setLength(3);;
        poly.setType(PolicySequence.Type.ALPHA);
        assertEquals("001", SequenceFormatter.formatSequence(1, poly));
        poly.setLength(2);;
        assertEquals("01", SequenceFormatter.formatSequence(1, poly));
        poly.setLength(4);;
        assertEquals("000b", SequenceFormatter.formatSequence(11, poly));
    }
    
    @Test
    public void testGetSetIncrement()
    {
        poly.setIncrement(1L);
        assertEquals(1L, poly.getIncrement());
    }
    
    @Test
    public void testGetSetMaxValue()
    {
        poly.setMaxValue(1L);
        assertEquals(1L, poly.getMaxValue());
    }
    
    @Test
    public void testGetSetKey()
    {
        poly.setKey("testKey");
        assertEquals("testKey", poly.getKey());
    }
    
    @Test
    public void testGetSetLastReleaseSeqNumTried()
    {
        poly.setLastReleaseSeqNumTried(1L);
        Long expected=1L;
        assertEquals(expected, poly.getLastReleaseSeqNumTried());
    }
}
