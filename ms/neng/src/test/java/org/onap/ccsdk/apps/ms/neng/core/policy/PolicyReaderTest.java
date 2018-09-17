/*-
 * ============LICENSE_START=======================================================
 * ONAP : CCSDK.apps
 * ================================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
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

package org.onap.ccsdk.apps.ms.neng.core.policy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.onap.ccsdk.apps.ms.neng.core.policy.PolicyReader.namingModels;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class PolicyReaderTest {
    @Test
    public void getPolicyFromFile() throws Exception {
        Map<String, Object> policy = new FilePolicyReader("sample_policy.json").getPolicy();
        assertEquals("VNF", namingModels(policy).get(0).get("naming-type"));
        assertEquals("COMPLEX|SEQUENCE|NF_NAMING_CODE", namingModels(policy).get(0).get("naming-recipe"));
    }

    @Test
    public void relaxedNamingType() throws Exception {
        assertEquals("VNF", PolicyReader.relaxedNamingType("VNF_NAME"));
        assertEquals("VNF", PolicyReader.relaxedNamingType("VNF-NAME"));
        assertEquals("VNF", PolicyReader.relaxedNamingType("vnf-name"));
        assertEquals("VNF", PolicyReader.relaxedNamingType("vnf_name"));
    }

    @Test
    public void testNamingProperty() throws Exception {
        List<Map<String, ?>> namingModels = PolicyReader.namingModels(buildPolicyResponse_withoutContent());
        assertNotNull(namingModels);
        assertNull(PolicyReader.namingProperty(namingModels.get(0), "TEST"));
        namingModels = PolicyReader.namingModels(buildPolicyResponse_withoutContent());
        assertNull(PolicyReader.namingProperty(namingModels.get(0), "TEST"));
    }

    @Test
    public void testNamingModelRelaxed() throws Exception {
        List<Map<String, ?>> namingModels = PolicyReader.namingModels(buildPolicyResponse_withoutContent());
        assertNotNull(PolicyReader.namingModelRelaxed(namingModels, "VNF"));
    }

    @Test
    public void testDependentNamingModel() throws Exception {
        List<Map<String, ?>> namingModels = PolicyReader.namingModels(buildPolicyResponse_withoutContent());
        assertNotNull(PolicyReader.dependentNamingModel(namingModels, "VNF"));
        assertNotNull(PolicyReader.dependentNamingModel(namingModels, "VNF_NAME"));
        assertNull(PolicyReader.dependentNamingModel(namingModels, "VNFC_NAME"));
    }

    @Test
    public void testNumber() throws Exception {
        assertEquals(100, PolicyReader.number("10G", 100L));
    }

    private Map<String, ?> buildPolicyResponse_withoutContent() {
        Map<String, Object> policyDataMap = new HashMap<>();
        policyDataMap.put("policy-instance-name", "SDNC_Policy.Config_MS_VNFCNamingPolicy");

        Map<String, Object> namingModelMap = new HashMap<>();
        namingModelMap.put("nf-role", "vPE");
        namingModelMap.put("naming-type", "VNF");
        namingModelMap.put("naming-recipe", "COMPLEX|NF-NAMING-CODE|Field2|Field3|Field4");

        Map<String, Object> namingPropertyMap = new HashMap<>();
        Map<String, Object> propertyMap1 = new HashMap<>();
        propertyMap1.put("property-name", "COMPLEX");
        Map<String, Object> propertyMap2 = new HashMap<>();
        propertyMap2.put("property-name", "NF-NAMING-CODE");
        namingPropertyMap.put("", Arrays.asList(new Object[] {propertyMap1, propertyMap2}));

        namingModelMap.put("naming-properties", Arrays.asList(new Object[] {propertyMap1, propertyMap2}));

        policyDataMap.put("naming-models", Arrays.asList(new Object[] {namingModelMap}));

        Map<String, Object> configMap = new HashMap<>();
        configMap.put("config", policyDataMap);
        return configMap;
    }
}
