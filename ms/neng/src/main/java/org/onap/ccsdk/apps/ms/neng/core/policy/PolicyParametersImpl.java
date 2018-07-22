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

import org.onap.ccsdk.apps.ms.neng.persistence.repository.IdentifierMapRespository;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.ServiceParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Specifies parameters that control the nature of policy data and the behavior of this micro-service.
 * 
 * <p/>This implementation gets these parameters from DB.
 */
@Component
public class PolicyParametersImpl implements PolicyParameters {
    @Autowired
    IdentifierMapRespository identifierMapRepository;

    @Autowired
    ServiceParameterRepository serviceParameterRepository;

    static final String RECIPE_SEPERATOR_PARAM = "recipe_separator";
    static final String MAX_GEN_ATTEMPT_PARAM = "max_gen_attempt";

    /**
     * Gives the separator between the entries within the same recipe -- such as the pipe('|') character.
     */
    @Override
    public String getRecipeSeparator() throws Exception {
        return serviceParameterRepository.findByName(RECIPE_SEPERATOR_PARAM).getValue();
    }

    /**
     * Maps a given function, used in the policy, to the equivalent function in this micro-service. 
     */
    @Override
    public String mapFunction(String name) throws Exception {
        return identifierMapRepository.findByPolicyFnName(name).getJsFnName();
    }

    /**
     * Maximum number of times the micro-service should attempt name generation in the same transaction
     * (if all previous attempts in the same transaction fail). 
     */
    @Override
    public int getMaxGenAttempt() throws Exception {
        return Integer.parseInt(serviceParameterRepository.findByName(MAX_GEN_ATTEMPT_PARAM).getValue());
    }
}
