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

package org.onap.ccsdk.apps.ms.neng.service.extinf.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.GetConfigResponse;
import org.onap.ccsdk.apps.ms.neng.persistence.entity.PolicyDetails;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.PolicyDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Finds policies from the database.
 */
@Component
@Qualifier("PolicyFinderDbImpl")
public class PolicyFinderServiceDbImpl extends PolicyFinderServiceImpl {
    @Autowired
    PolicyDetailsRepository policyDetailsRepo;

    /**
     * Finds the policy with the given name from the DB.
     */
    @Override
    public GetConfigResponse getConfig(String policyName) throws Exception {
        ObjectMapper objectmapper = new ObjectMapper();
        objectmapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        PolicyDetails policyDetails = policyDetailsRepo.findPolicyResponseByName(policyName);
        List<Map<Object, Object>> respObj = objectmapper.readValue(policyDetails.getPolicyResponse(),
                        new TypeReference<List<Map<Object, Object>>>() {});
        transformConfigObject(objectmapper, respObj);
        GetConfigResponse configResp = new GetConfigResponse();
        configResp.setResponse(respObj);
        return configResp;
    }
}
