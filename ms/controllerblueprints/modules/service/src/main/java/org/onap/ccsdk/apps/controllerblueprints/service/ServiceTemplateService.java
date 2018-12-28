/*
 * Copyright © 2017-2018 AT&T Intellectual Property.
 *
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
 */

package org.onap.ccsdk.apps.controllerblueprints.service;

import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException;
import org.onap.ccsdk.apps.controllerblueprints.core.data.ServiceTemplate;
import org.onap.ccsdk.apps.controllerblueprints.resource.dict.ResourceAssignment;
import org.onap.ccsdk.apps.controllerblueprints.resource.dict.service.ResourceAssignmentValidationService;
import org.onap.ccsdk.apps.controllerblueprints.service.model.AutoMapResponse;
import org.onap.ccsdk.apps.controllerblueprints.service.repository.ResourceDictionaryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ServiceTemplateService.java Purpose: Provide Service Template Create Service processing ServiceTemplateService
 *
 * @author Brinda Santh
 * @version 1.0
 */

@Deprecated
@Service
public class ServiceTemplateService {

    private ResourceDictionaryRepository dataDictionaryRepository;

    private ResourceAssignmentValidationService resourceAssignmentValidationService;

    /**
     * This is a SchemaGeneratorService constructor
     *
     * @param dataDictionaryRepository            dataDictionaryRepository
     * @param resourceAssignmentValidationService resourceAssignmentValidationService
     */
    public ServiceTemplateService(ResourceDictionaryRepository dataDictionaryRepository,
                                  ResourceAssignmentValidationService resourceAssignmentValidationService) {
        this.dataDictionaryRepository = dataDictionaryRepository;
        this.resourceAssignmentValidationService = resourceAssignmentValidationService;
    }


    /**
     * This is a enrichServiceTemplate method
     *
     * @param serviceTemplate serviceTemplate
     * @return ServiceTemplate
     */
    public ServiceTemplate enrichServiceTemplate(ServiceTemplate serviceTemplate) throws BluePrintException {
        //FIXME("Connect New Enrichment service")
        return serviceTemplate;
    }

    /**
     * This is a autoMap method to map the template keys
     *
     * @param resourceAssignments resourceAssignments
     * @return AutoMapResponse
     * @throws BluePrintException BluePrintException
     */
    public AutoMapResponse autoMap(List<ResourceAssignment> resourceAssignments) throws BluePrintException {
        AutoResourceMappingService autoMappingService = new AutoResourceMappingService(dataDictionaryRepository);
        return autoMappingService.autoMap(resourceAssignments);
    }

    /**
     * This is a validateResourceAssignments method
     *
     * @param resourceAssignments resourceAssignments
     * @return List<ResourceAssignment>
     * @throws BluePrintException BluePrintException
     */
    public List<ResourceAssignment> validateResourceAssignments(List<ResourceAssignment> resourceAssignments)
            throws BluePrintException {
        resourceAssignmentValidationService.validate(resourceAssignments);
        return resourceAssignments;
    }

}
