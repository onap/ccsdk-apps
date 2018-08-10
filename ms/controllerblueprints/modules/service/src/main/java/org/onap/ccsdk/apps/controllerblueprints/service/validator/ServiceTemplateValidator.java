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

package org.onap.ccsdk.apps.controllerblueprints.service.validator;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants;
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException;
import org.onap.ccsdk.apps.controllerblueprints.core.data.NodeTemplate;
import org.onap.ccsdk.apps.controllerblueprints.core.data.ServiceTemplate;
import org.onap.ccsdk.apps.controllerblueprints.core.service.BluePrintValidatorDefaultService;
import org.onap.ccsdk.apps.controllerblueprints.core.utils.JacksonUtils;
import org.onap.ccsdk.apps.controllerblueprints.resource.dict.validator.ResourceAssignmentValidator;

import java.util.HashMap;
import java.util.Map;

/**
 * ServiceTemplateValidator.java Purpose: Provide Configuration Generator ServiceTemplateValidator
 *
 * @author Brinda Santh
 * @version 1.0
 */

public class ServiceTemplateValidator extends BluePrintValidatorDefaultService {

    StringBuilder message = new StringBuilder();
    private Map<String, String> metaData = new HashMap();

    /**
     * This is a validateServiceTemplate
     *
     * @param serviceTemplateContent
     * @return boolean
     * @throws BluePrintException
     */
    public boolean validateServiceTemplate(String serviceTemplateContent) throws BluePrintException {
        if (StringUtils.isNotBlank(serviceTemplateContent)) {
            ServiceTemplate serviceTemplate =
                    JacksonUtils.readValue(serviceTemplateContent, ServiceTemplate.class);
            return validateServiceTemplate(serviceTemplate);
        } else {
            throw new BluePrintException(
                    "Service Template Content is  (" + serviceTemplateContent + ") not Defined.");
        }
    }

    /**
     * This is a validateServiceTemplate
     *
     * @param serviceTemplate
     * @return boolean
     * @throws BluePrintException
     */
    @SuppressWarnings("squid:S00112")
    public boolean validateServiceTemplate(ServiceTemplate serviceTemplate) throws BluePrintException {
        Map<String, Object> properties = new HashMap<>();
        super.validateBlueprint(serviceTemplate, properties);
        return true;
    }

    /**
     * This is a getMetaData to get the key information during the
     *
     * @return Map<String                                                                                                                                                                                                                                                               ,
                       *       String>
     */
    public Map<String, String> getMetaData() {
        return metaData;
    }

    @Override
    public void validateMetadata(@NotNull Map<String, String> metaDataMap) throws BluePrintException {

        Preconditions.checkNotNull(serviceTemplate.getMetadata(), "Service Template Metadata Information is missing.");

        this.metaData.putAll(serviceTemplate.getMetadata());

        String author = serviceTemplate.getMetadata().get(BluePrintConstants.METADATA_TEMPLATE_AUTHOR);
        String serviceTemplateName =
                serviceTemplate.getMetadata().get(BluePrintConstants.METADATA_TEMPLATE_NAME);
        String serviceTemplateVersion =
                serviceTemplate.getMetadata().get(BluePrintConstants.METADATA_TEMPLATE_VERSION);

        Preconditions.checkArgument(StringUtils.isNotBlank(author), "Template Metadata (author) Information is missing.");
        Preconditions.checkArgument(StringUtils.isNotBlank(serviceTemplateName), "Template Metadata (service-template-name) Information is missing.");
        Preconditions.checkArgument(StringUtils.isNotBlank(serviceTemplateVersion), "Template Metadata (service-template-version) Information is missing.");
    }


    @Override
    public void validateNodeTemplate(@NotNull String nodeTemplateName, @NotNull NodeTemplate nodeTemplate)
            throws BluePrintException {
        super.validateNodeTemplate(nodeTemplateName, nodeTemplate);
        validateNodeTemplateCustom(nodeTemplateName, nodeTemplate);

    }

    @Deprecated()
    private void validateNodeTemplateCustom(@NotNull String nodeTemplateName, @NotNull NodeTemplate nodeTemplate)
            throws BluePrintException {
        String derivedFrom = getBluePrintContext().nodeTemplateNodeType(nodeTemplateName).getDerivedFrom();
        if ("tosca.nodes.Artifact".equals(derivedFrom)) {
            ResourceAssignmentValidator resourceAssignmentValidator = new ResourceAssignmentValidator(nodeTemplate);
            resourceAssignmentValidator.validateResourceAssignment();
        }
    }
}
