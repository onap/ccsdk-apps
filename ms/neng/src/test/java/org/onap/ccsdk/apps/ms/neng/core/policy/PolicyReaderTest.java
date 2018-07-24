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
import static org.onap.ccsdk.apps.ms.neng.core.policy.PolicyReader.namingModels;

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
}
