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

package org.onap.ccsdk.apps.ms.neng.core.service;

import org.onap.ccsdk.apps.ms.neng.core.resource.model.HelloWorld;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.NameGenRequest;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.NameGenResponse;
import org.onap.ccsdk.apps.ms.neng.persistence.entity.PolicyDetails;

/**
 * Specification for the implementation of the APIs exposed by this micro-service.
 */
public interface SpringService {
    /**
     * Name generation API.
     */
    public NameGenResponse genNetworkElementName(NameGenRequest request) throws Exception;

    /**
     * Name removal API.
     */
    public NameGenResponse releaseNetworkElementName(NameGenRequest request) throws Exception;
    
    /**
     * API to return naming policy cached in this micro-service.
     * 
     * <p/>This is not used by clients -- it is here to help with diagnostics.
     */
    public PolicyDetails getPolicyDetails(String policyName);

    /**
     * API to add a naming policy to the database cache in this micro-service.
     * 
     * <p/>This is not used by clients -- it is here to help with diagnostics.
     */
    public void addPolicy(Object request) throws Exception;

    /**
     * Heart-beat/ping API.
     * 
     * <p/>This is not used by clients -- it is here to help with diagnostics.
     */
    public HelloWorld getQuickHello(String name);
}
