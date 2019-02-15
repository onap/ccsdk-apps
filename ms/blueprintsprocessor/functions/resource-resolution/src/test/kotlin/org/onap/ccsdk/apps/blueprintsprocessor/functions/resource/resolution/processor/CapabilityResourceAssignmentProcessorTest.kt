/*
 * Copyright © 2017-2018 AT&T Intellectual Property.
 *
 * Modifications Copyright © 2019 IBM, Bell Canada.
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

package org.onap.ccsdk.apps.blueprintsprocessor.functions.resource.resolution.processor

import org.junit.Test
import org.junit.runner.RunWith
import org.onap.ccsdk.apps.blueprintsprocessor.functions.python.executor.BlueprintJythonService
import org.onap.ccsdk.apps.blueprintsprocessor.functions.python.executor.PythonExecutorProperty
import org.onap.ccsdk.apps.blueprintsprocessor.functions.resource.resolution.ResourceAssignmentRuntimeService
import org.onap.ccsdk.apps.controllerblueprints.core.data.PropertyDefinition
import org.onap.ccsdk.apps.controllerblueprints.core.utils.BluePrintMetadataUtils
import org.onap.ccsdk.apps.controllerblueprints.core.utils.JacksonUtils
import org.onap.ccsdk.apps.controllerblueprints.resource.dict.ResourceAssignment
import org.onap.ccsdk.apps.controllerblueprints.resource.dict.ResourceDefinition
import org.onap.ccsdk.apps.controllerblueprints.scripts.BluePrintScriptsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertNotNull

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [CapabilityResourceAssignmentProcessor::class, BluePrintScriptsServiceImpl::class,
    BlueprintJythonService::class, PythonExecutorProperty::class, MockCapabilityService::class])
@TestPropertySource(properties =
["blueprints.processor.functions.python.executor.modulePaths=./../../../../components/scripts/python/ccsdk_blueprints",
    "blueprints.processor.functions.python.executor.executionPath=./../../../../components/scripts/python/ccsdk_blueprints"])
class CapabilityResourceAssignmentProcessorTest {

    @Autowired
    lateinit var capabilityResourceAssignmentProcessor: CapabilityResourceAssignmentProcessor

    @Test
    fun `test kotlin capability`() {

        val bluePrintContext = BluePrintMetadataUtils.getBluePrintContext(
                "./../../../../components/model-catalog/blueprint-model/test-blueprint/baseconfiguration")

        val resourceAssignmentRuntimeService = ResourceAssignmentRuntimeService("1234", bluePrintContext)

        capabilityResourceAssignmentProcessor.raRuntimeService = resourceAssignmentRuntimeService
        capabilityResourceAssignmentProcessor.resourceDictionaries = hashMapOf()


        val scriptPropertyInstances: MutableMap<String, Any> = mutableMapOf()
        scriptPropertyInstances["mock-service1"] = MockCapabilityService()
        scriptPropertyInstances["mock-service2"] = MockCapabilityService()

        val resourceAssignmentProcessor = capabilityResourceAssignmentProcessor
                .getKotlinResourceAssignmentProcessorInstance(
                        "ResourceAssignmentProcessor_cba\$ScriptResourceAssignmentProcessor", scriptPropertyInstances)

        assertNotNull(resourceAssignmentProcessor, "couldn't get kotlin script resource assignment processor")

        val resourceAssignment = ResourceAssignment().apply {
            name = "ra-name"
            dictionaryName = "ra-dict-name"
            dictionarySource = "capability"
            property = PropertyDefinition().apply {
                type = "string"
            }
        }

        val processorName = resourceAssignmentProcessor.apply(resourceAssignment)
        assertNotNull(processorName, "couldn't get kotlin script resource assignment processor name")
        println(processorName)
    }

    @Test
    fun `test jython capability`() {

        val bluePrintContext = BluePrintMetadataUtils.getBluePrintContext(
                "./../../../../components/model-catalog/blueprint-model/test-blueprint/capability_python")

        val resourceAssignmentRuntimeService = ResourceAssignmentRuntimeService("1234", bluePrintContext)

        capabilityResourceAssignmentProcessor.raRuntimeService = resourceAssignmentRuntimeService

        val resourceDefinition = JacksonUtils
                .readValueFromClassPathFile("mapping/capability/jython-resource-definitions.json",
                        ResourceDefinition::class.java)!!
        val resourceDefinitions: MutableMap<String, ResourceDefinition> = mutableMapOf()
        resourceDefinitions[resourceDefinition.name] = resourceDefinition
        capabilityResourceAssignmentProcessor.resourceDictionaries = resourceDefinitions

        val resourceAssignment = ResourceAssignment().apply {
            name = "service-instance-id"
            dictionaryName = "service-instance-id"
            dictionarySource = "capability"
            property = PropertyDefinition().apply {
                type = "string"
            }
        }

        val processorName = capabilityResourceAssignmentProcessor.apply(resourceAssignment)
        assertNotNull(processorName, "couldn't get Jython script resource assignment processor name")

    }

}

open class MockCapabilityService {

}