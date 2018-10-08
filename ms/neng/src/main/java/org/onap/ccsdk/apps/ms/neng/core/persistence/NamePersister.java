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

package org.onap.ccsdk.apps.ms.neng.core.persistence;

import org.onap.ccsdk.apps.ms.neng.persistence.entity.GeneratedName;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.GeneratedNameRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Manages the persistence of generated names using a DB.
 */
@Component
public class NamePersister {
    @Autowired
    GeneratedNameRespository repository;

    /**
     * Persist the given name.
     */
    public void persist(GeneratedName name) throws Exception {
        repository.save(name);
    }

    /**
     * Finds a name stored in the DB of the given type, name, and the 'isReleased' flag. 
     * 
     * @param elementType    The type of the name
     * @param name           A name
     * @param isReleased     An Y/N flag indicating if the name is released or not
     */
    public GeneratedName findByElementTypeAndNameAndReleased(String elementType, String name, String isReleased) {
        return repository.findByElementTypeAndNameAndIsReleased(elementType, name, isReleased);
    }

    /**
     * Finds a name stored in the DB of the given external ID and type.
     * 
     * @param externalId     The external ID
     * @param elementType    The type of the name
     */
    public GeneratedName findByExternalIdAndElementType(String externalId, String elementType) {
        return repository.findByExternalIdAndRelaxedElementType(externalId, elementType);
    }
}
