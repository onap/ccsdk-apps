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
 ******************************************************************************/

package org.onap.ccsdk.apps.ms.vlangtagapi.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.UnassignVlanTagResponseOutput;


public class UnassignVlanTagResponseOutputTest {
    private UnassignVlanTagResponseOutput unassignVlanTagResponseOutput;
    private UnassignVlanTagResponseOutput unassignVlanTagResponseOutput1;
    
    @Before
    public void setUp()
    {
        unassignVlanTagResponseOutput= new UnassignVlanTagResponseOutput();
        unassignVlanTagResponseOutput1= new UnassignVlanTagResponseOutput();
    }
    
    @Test
    public void testVlanType()
    {
        unassignVlanTagResponseOutput.vlanType("vlanType");
        assertEquals("vlanType", unassignVlanTagResponseOutput.getVlanType());
    }
    
    @Test
    public void testKey()
    {
        unassignVlanTagResponseOutput.key("key");
        assertEquals("key", unassignVlanTagResponseOutput.getVlanTagKey());
    }
    
    @Test
    public void testVlantagName()
    {
        unassignVlanTagResponseOutput.vlantagName("vlantagName");
        assertEquals("vlantagName", unassignVlanTagResponseOutput.getVlantagName());
    }
    
    @Test
    public void testEqualsAndHashCode()
    {
        assertTrue(unassignVlanTagResponseOutput.equals(unassignVlanTagResponseOutput1));
        assertTrue((Integer)unassignVlanTagResponseOutput.hashCode() instanceof Integer);
    }
}
