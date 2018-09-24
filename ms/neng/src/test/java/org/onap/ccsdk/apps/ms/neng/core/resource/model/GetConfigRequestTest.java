/*-
 * ============LICENSE_START=======================================================
 * ONAP : CCSDK.apps
 * ================================================================================
 * Copyright (C) 2018 IBM.
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

package org.onap.ccsdk.apps.ms.neng.core.resource.model;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GetConfigRequestTest {
    private GetConfigRequest getConfigRequest;

    @Before
    public void setup() {
        getConfigRequest = new GetConfigRequest();
    }

    @Test
    public void TestGetSetConfigName() {
        getConfigRequest.setConfigName("configName");
        Assert.assertEquals("configName", getConfigRequest.getConfigName());
    }

    @Test
    public void TestGetSetEcompName() {
        getConfigRequest.setEcompName("ecompName");
        Assert.assertEquals("ecompName", getConfigRequest.getEcompName());
    }

    @Test
    public void TestGetSetPolicyName() {
        getConfigRequest.setPolicyName("policy");
        Assert.assertEquals("policy", getConfigRequest.getPolicyName());
    }

    @Test
    public void TestIsUniqueFunction() {
        getConfigRequest.setUnique(true);
        Assert.assertTrue(getConfigRequest.isUnique());
    }
}
