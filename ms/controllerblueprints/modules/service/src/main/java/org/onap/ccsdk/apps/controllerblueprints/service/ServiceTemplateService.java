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

package org.onap.ccsdk.apps.controllerblueprints.service;

import org.apache.commons.lang3.StringUtils;
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException;
import org.onap.ccsdk.apps.controllerblueprints.core.data.ServiceTemplate;
import org.onap.ccsdk.apps.controllerblueprints.resource.dict.ResourceAssignment;
import org.onap.ccsdk.apps.controllerblueprints.resource.dict.validator.ResourceAssignmentValidator;
import org.onap.ccsdk.apps.controllerblueprints.service.domain.ConfigModelContent;
import org.onap.ccsdk.apps.controllerblueprints.service.model.AutoMapResponse;
import org.onap.ccsdk.apps.controllerblueprints.service.repository.ResourceDictionaryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ServiceTemplateService.java Purpose: Provide Service Template Create Service processing ServiceTemplateService
 *
 * @author Brinda Santh
 * @version 1.0
 */

@Service
public class ServiceTemplateService {

    private ResourceDictionaryRepository dataDictionaryRepository;

    private ConfigModelCreateService configModelCreateService;
    private BluePrintEnhancerService bluePrintEnhancerService;

    /**
     * This is a SchemaGeneratorService constructor
     *
     * @param dataDictionaryRepository
     * @param configModelCreateService
     * @param bluePrintEnhancerService
     */
    public ServiceTemplateService(ResourceDictionaryRepository dataDictionaryRepository,
                                  ConfigModelCreateService configModelCreateService,
                                  BluePrintEnhancerService bluePrintEnhancerService) {
        this.dataDictionaryRepository = dataDictionaryRepository;
        this.configModelCreateService = configModelCreateService;
        this.bluePrintEnhancerService = bluePrintEnhancerService;

    }

    /**
     * This is a validateServiceTemplate method
     *
     * @param serviceTemplate
     * @return ServiceTemplate
     * @throws BluePrintException
     */
    public ServiceTemplate validateServiceTemplate(ServiceTemplate serviceTemplate) throws BluePrintException {
        return this.configModelCreateService.validateServiceTemplate(serviceTemplate);
    }

    /**
     * This is a enrichServiceTemplate method
     *
     * @param serviceTemplate
     * @return ServiceTemplate
     * @throws BluePrintException
     */
    public ServiceTemplate enrichServiceTemplate(ServiceTemplate serviceTemplate) throws BluePrintException {
        this.bluePrintEnhancerService.enhance(serviceTemplate);
        return serviceTemplate;
    }

    /**
     * This is a autoMap method to map the template keys
     *
     * @param resourceAssignments
     * @return AutoMapResponse
     * @throws BluePrintException
     */
    public AutoMapResponse autoMap(List<ResourceAssignment> resourceAssignments) throws BluePrintException {
        AutoResourceMappingService autoMappingService = new AutoResourceMappingService(dataDictionaryRepository);
        AutoMapResponse autoMapResponse = autoMappingService.autoMap(resourceAssignments);
        return autoMapResponse;
    }

    /**
     * This is a validateResourceAssignments method
     *
     * @param resourceAssignments
     * @return List<ResourceAssignment>
     * @throws BluePrintException
     */
    public List<ResourceAssignment> validateResourceAssignments(List<ResourceAssignment> resourceAssignments)
            throws BluePrintException {
        try {
            ResourceAssignmentValidator resourceAssignmentValidator =
                    new ResourceAssignmentValidator(resourceAssignments);
            resourceAssignmentValidator.validateResourceAssignment();
        } catch (BluePrintException e) {
            throw new BluePrintException(e.getMessage(), e);
        }
        return resourceAssignments;
    }

    /**
     * This is a generateResourceAssignments method
     *
     * @param templateContent
     * @return List<ResourceAssignment>
     */
    public List<ResourceAssignment> generateResourceAssignments(ConfigModelContent templateContent) {
        List<ResourceAssignment> resourceAssignments = new ArrayList<>();
        if (templateContent != null && StringUtils.isNotBlank(templateContent.getContent())) {
            Pattern p = Pattern.compile("(?<=\\$\\{)([^\\}]+)(?=\\})");
            Matcher m = p.matcher(templateContent.getContent());
            while (m.find()) {
                ResourceAssignment resourceAssignment = new ResourceAssignment();
                resourceAssignment.setName(m.group());
                resourceAssignments.add(resourceAssignment);
            }
        }
        return resourceAssignments;
    }

}
