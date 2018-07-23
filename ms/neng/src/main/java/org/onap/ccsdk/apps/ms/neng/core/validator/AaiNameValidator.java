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

import java.util.logging.Logger;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.ExternalInterfaceRespository;
import org.onap.ccsdk.apps.ms.neng.service.extinf.impl.AaiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validates that a generated name is valid, by comparing against A&AI.
 */
@Component
public class AaiNameValidator {
    @Autowired
    AaiServiceImpl aaiImpl;
    @Autowired
    ExternalInterfaceRespository dbStuff;

    private static Logger log = Logger.getLogger(AaiNameValidator.class.getName());

    /**
     * Returns true if the given name, of the given type, is valid.
     */
    public boolean validate(String namingType, String name) throws Exception {
        log.info("AAI Validation called for " + namingType + " - " + name);
        String url = dbStuff.getUriByNameType(namingType);
        if (url == null) {
            return true;
        }
        log.info("AAI URL " + url);
        return aaiImpl.validate(url, name);
    }
}
