/*
 * ﻿============LICENSE_START=======================================================
 * org.onap.ccsdk
 * ================================================================================
 * Copyright © 2017-2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 *
 *
 */

package org.onap.ccsdk.apps.controllerblueprints.service.repository;

import org.onap.ccsdk.apps.controllerblueprints.service.domain.ResourceDictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ResourceMappingRepository.java Purpose: Provide Configuration Generator ResourceMappingRepository
 *
 * @author Brinda Santh
 * @version 1.0
 */
@Repository
public interface ResourceDictionaryRepository extends JpaRepository<ResourceDictionary, String> {


    /**
     * This is a findByName method
     * 
     * @param name
     * @return Optional<ResourceMapping>
     */
    Optional<ResourceDictionary> findByName(String name);

    /**
     * This is a findByNameIn method
     * 
     * @param names
     * @return Optional<ResourceMapping>
     */
    List<ResourceDictionary> findByNameIn(List<String> names);

    /**
     * This is a findByTagsContainingIgnoreCase method
     * 
     * @param tags
     * @return Optional<ModelType>
     */
    List<ResourceDictionary> findByTagsContainingIgnoreCase(String tags);

    /**
     * This is a deleteByName method
     * 
     * @param name
     */
    void deleteByName(String name);


}
