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

public class PolicyConfigTest {

    private PolicyConfig policyConfig;

    @Before
    public void setUp() {
        policyConfig = new PolicyConfig();
    }

    @Test
    public void testGetSetConfigName() {
        policyConfig.setConfigName("configName");
        assertEquals("configName", policyConfig.getConfigName());
    }

    @Test
    public void testGetSetRiskLevel() {
        policyConfig.setRiskLevel("RiskLevel");
        assertEquals("RiskLevel", policyConfig.getRiskLevel());
    }

    @Test
    public void testGetSetPolicyName() {
        policyConfig.setPolicyName("PolicyName");
        assertEquals("PolicyName", policyConfig.getPolicyName());
    }

    @Test
    public void testGetSetPolicyScope() {
        policyConfig.setPolicyScope("PolicyScope");
        assertEquals("PolicyScope", policyConfig.getPolicyScope());
    }

    @Test
    public void testGetSetGuard() {
        policyConfig.setGuard("Guard");
        assertEquals("Guard", policyConfig.getGuard());
    }
    
    @Test
    public void testGetSetDescription() {
        policyConfig.setDescription("Description");
        assertEquals("Description", policyConfig.getDescription());
    }
    
    @Test
    public void testGetSetPriority() {
        policyConfig.setPriority("Priority");
        assertEquals("Priority", policyConfig.getPriority());
    }
    
    @Test
    public void testGetSetUuid() {
        policyConfig.setUuid("Uuid");
        assertEquals("Uuid", policyConfig.getUuid());
    }
    
    @Test
    public void testGetSetVersion() {
        policyConfig.setVersion("Version");
        assertEquals("Version", policyConfig.getVersion());
    }
    
    @Test
    public void testGetSetService() {
        policyConfig.setService("Service");
        assertEquals("Service", policyConfig.getService());
    }
     
    @Test
    public void testGetSetLocation() {
        policyConfig.setLocation("Location");
        assertEquals("Location", policyConfig.getLocation());
    }
    
    @Test
    public void testGetSetTemplateVersion() {
        policyConfig.setTemplateVersion("TemplateVersion");
        assertEquals("TemplateVersion", policyConfig.getTemplateVersion());
    }
    
    @Test
    public void testToString()
    {
        assertTrue(policyConfig.toString() instanceof String);
    }

}