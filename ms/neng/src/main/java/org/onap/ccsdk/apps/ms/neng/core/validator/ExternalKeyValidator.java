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

package org.onap.ccsdk.apps.ms.neng.core.validator;

import org.onap.ccsdk.apps.ms.neng.persistence.repository.GeneratedNameRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Checks if the external key in the request is already there in the DB or not.
 */
@Component
public class ExternalKeyValidator {
    @Autowired
    GeneratedNameRespository genNameRepo;

    /**
     * Tells if the given external ID is present in the DB.
     */
    public boolean isPresent(String externalId) {
        if (genNameRepo.findByExternalId(externalId).size() > 0) {
            return true;
        }
        return false;
    }
}
