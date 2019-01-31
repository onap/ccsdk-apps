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

package org.onap.ccsdk.apps.blueprintsprocessor.functions.resource.resolution.processor

import org.onap.ccsdk.apps.blueprintsprocessor.functions.resource.resolution.CapabilityResourceSource
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintProcessorException
import org.onap.ccsdk.apps.controllerblueprints.core.interfaces.BluePrintScriptsService
import org.onap.ccsdk.apps.controllerblueprints.core.utils.JacksonUtils
import org.onap.ccsdk.apps.controllerblueprints.resource.dict.ResourceAssignment
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service

@Service("resource-assignment-processor-capability")
open class CapabilityResourceAssignmentProcessor(private var applicationContext: ApplicationContext,
                                                 private val bluePrintScriptsService: BluePrintScriptsService) :
        ResourceAssignmentProcessor() {

    companion object {
        const val CAPABILITY_TYPE_KOTLIN_COMPONENT = "KOTLIN-COMPONENT"
        const val CAPABILITY_TYPE_JAVA_COMPONENT = "JAVA-COMPONENT"
        const val CAPABILITY_TYPE_JYTHON_COMPONENT = "JYTHON-COMPONENT"
    }

    override fun getName(): String {
        return "resource-assignment-processor-capability"
    }

    override fun process(resourceAssignment: ResourceAssignment) {

        val resourceDefinition = resourceDictionaries[resourceAssignment.dictionaryName]
                ?: throw BluePrintProcessorException("couldn't get resource definition for ${resourceAssignment.dictionaryName}")

        val resourceSource = resourceDefinition.sources[resourceAssignment.dictionarySource]
                ?: throw BluePrintProcessorException("couldn't get resource definition " +
                        "${resourceAssignment.dictionaryName} source(${resourceAssignment.dictionarySource})")

        val resourceSourceProps = checkNotNull(resourceSource.properties) { "failed to get $resourceSource properties" }
        /**
         * Get the Capability Resource Source Info from Property Definitions.
         */
        val capabilityResourceSourceProperty = JacksonUtils
                .getInstanceFromMap(resourceSourceProps, CapabilityResourceSource::class.java)

        val instanceType = capabilityResourceSourceProperty.type
        val instanceName = capabilityResourceSourceProperty.instanceName

        var componentResourceAssignmentProcessor: ResourceAssignmentProcessor? = null

        when (instanceType) {
            CAPABILITY_TYPE_KOTLIN_COMPONENT -> {
                componentResourceAssignmentProcessor = getKotlinResourceAssignmentProcessorInstance(instanceName,
                        capabilityResourceSourceProperty.instanceDependencies)
            }
            CAPABILITY_TYPE_JAVA_COMPONENT -> {
                // Initialize Capability Resource Assignment Processor
                componentResourceAssignmentProcessor = applicationContext.getBean(instanceName, ResourceAssignmentProcessor::class.java)
            }
            CAPABILITY_TYPE_JYTHON_COMPONENT -> {
                TODO(" No implementation")
            }
        }

        checkNotNull(componentResourceAssignmentProcessor) { "failed to get capability resource assignment processor($instanceName)" }

        // Assign Current Blueprint runtime and ResourceDictionaries
        componentResourceAssignmentProcessor.raRuntimeService = raRuntimeService
        componentResourceAssignmentProcessor.resourceDictionaries = resourceDictionaries

        // Invoke componentResourceAssignmentProcessor
        componentResourceAssignmentProcessor.apply(resourceAssignment)
    }

    override fun recover(runtimeException: RuntimeException, resourceAssignment: ResourceAssignment) {

        TODO("To Implement")
    }

    private fun getKotlinResourceAssignmentProcessorInstance(scriptClassName: String,
                                                             instanceNames: List<String>? = null): ResourceAssignmentProcessor {
        var scriptPropertyInstances: MutableMap<String, Any>? = null

        if (instanceNames != null && instanceNames.isNotEmpty()) {
            scriptPropertyInstances = hashMapOf()
            instanceNames.forEach {
                scriptPropertyInstances[it] = applicationContext.getBean(it)
                        ?: throw BluePrintProcessorException("couldn't get the dependency instance($it)")
            }
        }

        return getKotlinResourceAssignmentProcessorInstance(scriptClassName, scriptPropertyInstances)

    }

    fun getKotlinResourceAssignmentProcessorInstance(scriptClassName: String,
                                                     scriptPropertyInstances: MutableMap<String, Any>? = null):
            ResourceAssignmentProcessor {

        val resourceAssignmentProcessor = bluePrintScriptsService
                .scriptInstance<ResourceAssignmentProcessor>(raRuntimeService.bluePrintContext(),
                        scriptClassName, false)

        // Add additional Instance
        if (scriptPropertyInstances != null) {
            resourceAssignmentProcessor.scriptPropertyInstances = scriptPropertyInstances
        }

        return resourceAssignmentProcessor
    }
}