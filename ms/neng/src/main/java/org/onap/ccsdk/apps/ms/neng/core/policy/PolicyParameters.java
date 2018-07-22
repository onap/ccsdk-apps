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

/**
 * Specifies parameters that control the nature of policy data and the behavior of this micro-service.
 * 
 * <p/>These parameters are typically stored in DB or read from a configuration file.
 */
public interface PolicyParameters {
    /**
     * Gives the separator between the entries within the same recipe -- such as the pipe('|') character.
     */
    public String getRecipeSeparator() throws Exception;

    /**
     * Maps a given function, used in the policy, to the equivalent function in this micro-service. 
     */
    public String mapFunction(String name) throws Exception;

    /**
     * Maximum number of times the micro-service should attempt name generation in the same transaction
     * (if all previous attempts in the same transaction fail). 
     */
    public int getMaxGenAttempt() throws Exception;
}
