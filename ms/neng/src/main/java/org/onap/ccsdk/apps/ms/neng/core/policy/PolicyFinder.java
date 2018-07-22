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

import java.util.Map;

/**
 * Finds policy data, normally by calling policy-manager.
 */
public interface PolicyFinder {
    /**
     * Finds the policy with a given name.
     * 
     * @param policyName   the name of the policy the caller is looking for
     * @return             a map ( String -> Object ) representing the policy, as a general JSON structure, 
     *     where the map entries are String-s, or arrays of similar maps, or similar nested maps. 
     * @throws Exception   any exceptions caught are propagated
     */
    public Map<String, Object> findPolicy(String policyName) throws Exception;
}
