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

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class PolicyEngineResponseTest {
    
    private PolicyEngineResponse policyEngineResponse;
    
    @Before
    public void setUp()
    {
        policyEngineResponse= new PolicyEngineResponse();
    }
    
    @Test
    public void testGetSetPolicyConfigMessage()
    {
        policyEngineResponse.setPolicyConfigMessage("policyConfigMessage");
        assertEquals("policyConfigMessage", policyEngineResponse.getPolicyConfigMessage());
    }

    @Test
    public void testGetSetPolicyConfigStatus()
    {
        policyEngineResponse.setPolicyConfigStatus("PolicyConfigStatus");
        assertEquals("PolicyConfigStatus", policyEngineResponse.getPolicyConfigStatus());
    }

    @Test
    public void testGetSetType()
    {
        policyEngineResponse.setType("Type");
        assertEquals("Type", policyEngineResponse.getType());
    }

    @Test
    public void testGetSetPolicyName()
    {
        policyEngineResponse.setPolicyName("PolicyName");
        assertEquals("PolicyName", policyEngineResponse.getPolicyName());
    }

    @Test
    public void testGetSetPolicyType()
    {
        policyEngineResponse.setPolicyType("PolicyType");
        assertEquals("PolicyType", policyEngineResponse.getPolicyType());
    }
    
    @Test
    public void testGetSetPolicyVersion()
    {
        policyEngineResponse.setPolicyVersion("PolicyVersion");
        assertEquals("PolicyVersion", policyEngineResponse.getPolicyVersion());
    }
    
    @Test
    public void testGetSetMatchingConditions()
    {
        HashMap<String, String> test= new HashMap<>();
        policyEngineResponse.setMatchingConditions(test);
        assertEquals(test, policyEngineResponse.getMatchingConditions());
    }
    
    
    @Test
    public void testGetSetResponseAttributes()
    {
        HashMap<String, String> test= new HashMap<>();
        policyEngineResponse.setResponseAttributes(test);
        assertEquals(test, policyEngineResponse.getResponseAttributes());
    }
    
    @Test
    public void testGetSetProperty()
    {
        policyEngineResponse.setProperty("Property");
        assertEquals("Property", policyEngineResponse.getProperty());
    }
    
    @Test
    public void testToString()
    {
        assertTrue(policyEngineResponse.toString() instanceof String);
    }
    
    
}
