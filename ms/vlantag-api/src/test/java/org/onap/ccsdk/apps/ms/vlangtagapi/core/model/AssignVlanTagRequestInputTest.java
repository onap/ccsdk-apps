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
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.AssignVlanTagRequestInput;

public class AssignVlanTagRequestInputTest {

    private AssignVlanTagRequestInput assignVlanTagRequestInput;
    
    private AssignVlanTagRequestInput assignVlanTagRequestInput1;
    
    @Before
    public void setUp()
    {
        assignVlanTagRequestInput = new AssignVlanTagRequestInput();
        assignVlanTagRequestInput1= new AssignVlanTagRequestInput();
        assignVlanTagRequestInput.setPolicyInstanceName("policyInstanceName");
        assignVlanTagRequestInput.setResourceName("resourceName");
        assignVlanTagRequestInput.setResourceValue("resourceValue");
        assignVlanTagRequestInput.setScopeId("scopeId");
        assignVlanTagRequestInput.setVlanTagKey("vlanTagKey");
        assignVlanTagRequestInput.setVlanType("vlanType");
        
        assignVlanTagRequestInput1.setPolicyInstanceName("policyInstanceName");
        assignVlanTagRequestInput1.setResourceName("resourceName");
        assignVlanTagRequestInput1.setResourceValue("resourceValue");
        assignVlanTagRequestInput1.setScopeId("scopeId");
        assignVlanTagRequestInput1.setVlanTagKey("vlanTagKey");
        assignVlanTagRequestInput1.setVlanType("vlanType");
    }
    
    @Test
    public void testPolicyInstanceName()
    {
        assignVlanTagRequestInput.policyInstanceName("testPolicyInstanceName");
        assertEquals("testPolicyInstanceName", assignVlanTagRequestInput.getPolicyInstanceName());
    }
    
    @Test
    public void testResourceName()
    {
        assignVlanTagRequestInput.resourceName("testResourceName");
        assertEquals("testResourceName", assignVlanTagRequestInput.getResourceName());
    }
    
    @Test
    public void testResourceValue()
    {
        assignVlanTagRequestInput.resourceValue("testResourceValue");
        assertEquals("testResourceValue", assignVlanTagRequestInput.getResourceValue());
    }
    
    @Test
    public void testScopeId()
    {
        assignVlanTagRequestInput.scopeId("testScopeId");
        assertEquals("testScopeId", assignVlanTagRequestInput.getScopeId());
    }
    
    @Test
    public void testVlanTagKey()
    {
        assignVlanTagRequestInput.vlanTagKey("testVlanTagKey");
        assertEquals("testVlanTagKey", assignVlanTagRequestInput.getVlanTagKey());
    }
    
    @Test
    public void testVlanType()
    {
        assignVlanTagRequestInput.vlanType("testVlanType");
        assertEquals("testVlanType", assignVlanTagRequestInput.getVlanType());
    }
    
    @Test
    public void testEquals()
    {
        assertEquals(true, assignVlanTagRequestInput.equals(assignVlanTagRequestInput1));
    }
    
    @Test
    public void testToStringAndHashcode()
    {
        assertTrue(assignVlanTagRequestInput.toString() instanceof String);
        assertTrue((Integer)assignVlanTagRequestInput.hashCode() instanceof Integer);
    }
}
