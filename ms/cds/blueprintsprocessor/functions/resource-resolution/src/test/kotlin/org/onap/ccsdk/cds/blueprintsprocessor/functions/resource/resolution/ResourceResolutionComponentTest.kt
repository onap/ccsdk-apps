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

package org.onap.ccsdk.cds.blueprintsprocessor.functions.resource.resolution

import com.fasterxml.jackson.databind.JsonNode
import org.junit.Test
import org.junit.runner.RunWith
import org.onap.ccsdk.cds.blueprintsprocessor.core.api.data.ExecutionServiceInput
import org.onap.ccsdk.cds.blueprintsprocessor.core.utils.PayloadUtils
import org.onap.ccsdk.cds.blueprintsprocessor.functions.resource.resolution.processor.CapabilityResourceAssignmentProcessor
import org.onap.ccsdk.cds.blueprintsprocessor.functions.resource.resolution.processor.DataBaseResourceAssignmentProcessor
import org.onap.ccsdk.cds.blueprintsprocessor.functions.resource.resolution.processor.DefaultResourceAssignmentProcessor
import org.onap.ccsdk.cds.blueprintsprocessor.functions.resource.resolution.processor.InputResourceAssignmentProcessor
import org.onap.ccsdk.cds.blueprintsprocessor.functions.resource.resolution.processor.SimpleRestResourceAssignmentProcessor
import org.onap.ccsdk.cds.controllerblueprints.core.asJsonNode
import org.onap.ccsdk.cds.controllerblueprints.core.putJsonElement
import org.onap.ccsdk.cds.controllerblueprints.core.utils.BluePrintMetadataUtils
import org.onap.ccsdk.cds.controllerblueprints.core.utils.JacksonUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [ResourceResolutionComponent::class, ResourceResolutionService::class,
    InputResourceAssignmentProcessor::class, DefaultResourceAssignmentProcessor::class,
    DataBaseResourceAssignmentProcessor::class, SimpleRestResourceAssignmentProcessor::class,
    CapabilityResourceAssignmentProcessor::class])
class ResourceResolutionComponentTest {

    @Autowired
    lateinit var resourceResolutionComponent: ResourceResolutionComponent

    @Test
    fun testProcess() {

        val bluePrintRuntimeService = BluePrintMetadataUtils.getBluePrintRuntime("1234",
                "./../../../controllerblueprints/modules/model-catalog/blueprint-model/starter-blueprint/baseconfiguration")

        val executionServiceInput = JacksonUtils.readValueFromClassPathFile("payload/requests/sample-resourceresolution-request.json",
                ExecutionServiceInput::class.java)!!

        // Prepare Inputs
        PayloadUtils.prepareInputsFromWorkflowPayload(bluePrintRuntimeService, executionServiceInput.payload, "resource-assignment")

        val stepMetaData: MutableMap<String, JsonNode> = hashMapOf()
        stepMetaData.putJsonElement(org.onap.ccsdk.cds.controllerblueprints.core.BluePrintConstants.PROPERTY_CURRENT_NODE_TEMPLATE, "resource-assignment")
        stepMetaData.putJsonElement(org.onap.ccsdk.cds.controllerblueprints.core.BluePrintConstants.PROPERTY_CURRENT_INTERFACE, "ResourceAssignmentComponent")
        stepMetaData.putJsonElement(org.onap.ccsdk.cds.controllerblueprints.core.BluePrintConstants.PROPERTY_CURRENT_OPERATION, "process")
        bluePrintRuntimeService.put("resource-assignment-step-inputs", stepMetaData.asJsonNode())

        resourceResolutionComponent.bluePrintRuntimeService = bluePrintRuntimeService
        resourceResolutionComponent.stepName = "resource-assignment"
        resourceResolutionComponent.apply(executionServiceInput)
    }
}