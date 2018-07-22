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

import org.onap.ccsdk.apps.ms.neng.persistence.entity.ExternalInterface;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Repository for the ExternalInterface entity.
 */
public interface ExternalInterfaceRespository extends CrudRepository<ExternalInterface, Integer> {

    /**
     * Gives the URL to be used for calling an external interface, for the given type of parameter.
     */
    @Query("select ie.urlSuffix from ExternalInterface ie where ie.param = :param")
    public String getUriByNameType(@Param("param") String param) throws Exception;
}
