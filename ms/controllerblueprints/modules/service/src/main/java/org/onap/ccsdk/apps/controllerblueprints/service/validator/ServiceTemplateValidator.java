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

package org.onap.ccsdk.apps.controllerblueprints.service.validator;

import com.google.common.base.Preconditions;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants;
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException;
import org.onap.ccsdk.apps.controllerblueprints.core.ConfigModelConstant;
import org.onap.ccsdk.apps.controllerblueprints.core.data.CapabilityAssignment;
import org.onap.ccsdk.apps.controllerblueprints.core.data.NodeTemplate;
import org.onap.ccsdk.apps.controllerblueprints.core.data.ServiceTemplate;
import org.onap.ccsdk.apps.controllerblueprints.core.service.BluePrintValidatorDefaultService;
import org.onap.ccsdk.apps.controllerblueprints.core.utils.JacksonUtils;
import org.onap.ccsdk.apps.controllerblueprints.resource.dict.ResourceAssignment;
import org.onap.ccsdk.apps.controllerblueprints.resource.dict.service.ResourceAssignmentValidationService;
import org.onap.ccsdk.apps.controllerblueprints.resource.dict.service.ResourceAssignmentValidationServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ServiceTemplateValidator.java Purpose: Provide Configuration Generator ServiceTemplateValidator
 *
 * @author Brinda Santh
 * @version 1.0
 */

public class ServiceTemplateValidator extends BluePrintValidatorDefaultService {

    StringBuilder message = new StringBuilder();
    private Map<String, String> metaData = new HashMap<>();

    /**
     * This is a validateServiceTemplate
     *
     * @param serviceTemplateContent serviceTemplateContent
     * @return boolean
     * @throws BluePrintException BluePrintException
     */
    public boolean validateServiceTemplate(String serviceTemplateContent) throws BluePrintException {
        if (StringUtils.isNotBlank(serviceTemplateContent)) {
            ServiceTemplate serviceTemplate =
                    JacksonUtils.Companion.readValue(serviceTemplateContent, ServiceTemplate.class);
            return validateServiceTemplate(serviceTemplate);
        } else {
            throw new BluePrintException(
                    "Service Template Content is  (" + serviceTemplateContent + ") not Defined.");
        }
    }

    /**
     * This is a validateServiceTemplate
     *
     * @param serviceTemplate serviceTemplate
     * @return boolean
     * @throws BluePrintException BluePrintException
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
     * @return Map<String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               String>
     */
    public Map<String, String> getMetaData() {
        return metaData;
    }

    @Override
    public void validateMetadata(@NotNull Map<String, String> metaDataMap) throws BluePrintException {

        Preconditions.checkNotNull(serviceTemplate.getMetadata(), "Service Template Metadata Information is missing.");
        super.validateMetadata(metaDataMap);

        this.metaData.putAll(serviceTemplate.getMetadata());
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

        if (BluePrintConstants.MODEL_TYPE_NODE_ARTIFACT.equals(derivedFrom)) {
            List<ResourceAssignment> resourceAssignment = getResourceAssignments(nodeTemplate);
            ResourceAssignmentValidationService resourceAssignmentValidationService = new ResourceAssignmentValidationServiceImpl();
            resourceAssignmentValidationService.validate(resourceAssignment);
        }
    }

    private List<ResourceAssignment> getResourceAssignments(@NotNull NodeTemplate nodeTemplate) {

        List<ResourceAssignment> resourceAssignment = null;

        if (MapUtils.isNotEmpty(nodeTemplate.getCapabilities())) {

            CapabilityAssignment capabilityAssignment =
                    nodeTemplate.getCapabilities().get(ConfigModelConstant.CAPABILITY_PROPERTY_MAPPING);
            if (capabilityAssignment != null && capabilityAssignment.getProperties() != null) {
                Object mappingObject =
                        capabilityAssignment.getProperties().get(ConfigModelConstant.CAPABILITY_PROPERTY_MAPPING);
                if (mappingObject != null) {
                    String mappingContent = JacksonUtils.Companion.getJson(mappingObject);
                    Preconditions.checkArgument(StringUtils.isNotBlank(mappingContent),
                            String.format("Failed to get capability mapping property (%s) ", ConfigModelConstant.CAPABILITY_PROPERTY_MAPPING));

                    resourceAssignment = JacksonUtils.Companion.getListFromJson(mappingContent, ResourceAssignment.class);

                    Preconditions.checkNotNull(resourceAssignment,
                            String.format("Failed to get resource assignment info from the content (%s) ", mappingContent));
                }
            }
        }
        return resourceAssignment;
    }
}
