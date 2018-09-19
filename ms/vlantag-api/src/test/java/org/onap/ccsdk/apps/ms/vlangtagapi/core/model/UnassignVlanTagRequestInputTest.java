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

import org.junit.Before;
import org.junit.Test;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.UnassignVlanTagRequestInput;

public class UnassignVlanTagRequestInputTest {

    private UnassignVlanTagRequestInput unassignVlanTagRequestInput;
    private UnassignVlanTagRequestInput unassignVlanTagRequestInput1;
    
    @Before
    public void setUp()
    {
        unassignVlanTagRequestInput= new UnassignVlanTagRequestInput();
        unassignVlanTagRequestInput1= new UnassignVlanTagRequestInput();
    }
    
    @Test
    public void testPolicyInstanceName()
    {
        unassignVlanTagRequestInput.policyInstanceName("policyInstanceName");
        assertEquals("policyInstanceName", unassignVlanTagRequestInput.getPolicyInstanceName());
    }
    
    @Test
    public void testVlanType()
    {
        unassignVlanTagRequestInput.vlanType("vlanType");
        assertEquals("vlanType", unassignVlanTagRequestInput.getVlanType());
    }
    
    @Test
    public void testEquals()
    {
        assertTrue(unassignVlanTagRequestInput.equals(unassignVlanTagRequestInput1));
        
    }
    
    @Test
    public void testToStringAndHashCode()
    {
        assertTrue(unassignVlanTagRequestInput.toString() instanceof String);
        assertTrue((Integer)unassignVlanTagRequestInput.hashCode() instanceof Integer);
        
    }
    
}
