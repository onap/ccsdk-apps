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

package org.onap.ccsdk.apps.ms.neng.persistence.repository;

import org.onap.ccsdk.apps.ms.neng.persistence.entity.IdentifierMap;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for the IdentifierMap entity.
 */
public interface IdentifierMapRespository extends CrudRepository<IdentifierMap, Integer> {
    
    /**
     * Finds the entity by a given policy function/identifier name.
     */
    public IdentifierMap findByPolicyFnName(String policyFnName);
}
