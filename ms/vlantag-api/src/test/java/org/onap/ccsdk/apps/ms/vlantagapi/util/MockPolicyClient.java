/*******************************************************************************
 * Copyright © 2017-2018 AT&T Intellectual Property.
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

package org.onap.ccsdk.apps.ms.vlantagapi.util;

import org.onap.ccsdk.apps.ms.vlantagapi.core.exception.VlantagApiException;
import org.onap.ccsdk.apps.ms.vlantagapi.core.extinf.pm.model.PolicyEngineResponse;
import org.onap.ccsdk.apps.ms.vlantagapi.core.extinf.pm.model.RequestObject;
import org.onap.ccsdk.apps.ms.vlantagapi.extinf.pm.PolicyManagerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockPolicyClient extends PolicyManagerClient{
	protected static final Logger logger = LoggerFactory.getLogger(MockPolicyClient.class);

    
    
    public PolicyEngineResponse[] getConfigUsingPost(RequestObject requestObject) throws VlantagApiException {
        
        PolicyEngineResponse peResponse = new PolicyEngineResponse();
        peResponse.setConfig("{\"riskLevel\":\"4\",\"riskType\":\"test\",\"policyName\":\"Internet_VlanTag_1810_US_VPE\",\"service\":\"vlantagResourceModel\",\"guard\":\"False\",\"description\":\"Internet_VlanTag_1810_US_VPE\",\"templateVersion\":\"1607\",\"priority\":\"4\",\"version\":\"20180709\",\"content\":{\"policy-instance-name\":\"Internet_VlanTag_1810_US_VPE\",\"resource-models\":[{\"data-store\":\"FALSE\",\"elements\":[{\"allowed-range\":[{\"min\":\"3553\",\"max\":\"3562\"}],\"recycle-vlantag-range\":\"TRUE\",\"overwrite\":\"FALSE\",\"vlantag-name\":\"VPE-Cust\"}],\"scope\":\"SITE\",\"vlan-type\":\"vlan-id-outer\",\"resource-resolution-recipe\":\"#BSB# VPE-Cust #ESB#\",\"resource-vlan-role\":\"outer-tag\"},{\"data-store\":\"TRUE\",\"elements\":[{\"allowed-range\":[{\"min\":\"3503\",\"max\":\"3503\"}],\"shared-range\":\"TRUE\",\"element-vlan-role\":\"outer-tag\",\"recycle-vlantag-range\":\"TRUE\",\"overwrite\":\"FALSE\",\"vlantag-name\":\"VPE-Cust-Outer\"},{\"allowed-range\":[{\"min\":\"4001\",\"max\":\"4012\"}],\"element-vlan-role\":\"outer-tag\",\"recycle-vlantag-range\":\"TRUE\",\"overwrite\":\"FALSE\",\"vlantag-name\":\"VPE-Core1\"},{\"allowed-range\":[{\"min\":\"4001\",\"max\":\"4012\"}],\"element-vlan-role\":\"outer-tag\",\"recycle-vlantag-range\":\"TRUE\",\"overwrite\":\"FALSE\",\"vlantag-name\":\"VPE-Core2\"}],\"scope\":\"SITE\",\"vlan-type\":\"vlan-id-filter\",\"resource-resolution-recipe\":\"#BSB# VPE-Cust-Outer, VPE-Core1, VPE-Core2 #ESB#\"}]}}");
        PolicyEngineResponse[] peResponses = new PolicyEngineResponse[1];
        peResponses[0] = peResponse;
        
        return peResponses;
    }
}
