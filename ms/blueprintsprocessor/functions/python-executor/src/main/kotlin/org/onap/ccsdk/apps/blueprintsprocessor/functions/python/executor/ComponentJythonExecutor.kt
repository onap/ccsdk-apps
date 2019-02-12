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

package org.onap.ccsdk.apps.blueprintsprocessor.functions.python.executor

import com.fasterxml.jackson.databind.node.ArrayNode
import org.apache.commons.io.FilenameUtils
import org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ExecutionServiceInput
import org.onap.ccsdk.apps.blueprintsprocessor.services.execution.AbstractComponentFunction
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintProcessorException
import org.onap.ccsdk.apps.controllerblueprints.core.checkNotEmptyOrThrow
import org.onap.ccsdk.apps.controllerblueprints.core.data.OperationAssignment
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component("component-jython-executor")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
open class ComponentJythonExecutor(private var applicationContext: ApplicationContext,
                                   private val blueprintJythonService: BlueprintJythonService) : AbstractComponentFunction() {

    private val log = LoggerFactory.getLogger(ComponentJythonExecutor::class.java)

    private var componentFunction: AbstractComponentFunction? = null

    override fun prepareRequest(executionRequest: ExecutionServiceInput): ExecutionServiceInput {
        val request = super.prepareRequest(executionRequest)
        // Populate Component Instance
        populateJythonComponentInstance()
        return request
    }

    override fun process(executionRequest: ExecutionServiceInput) {
        log.info("Processing : $operationInputs")
        // Invoke Jython Component Script
        componentFunction!!.process(executionServiceInput)

    }

    override fun recover(runtimeException: RuntimeException, executionRequest: ExecutionServiceInput) {
        componentFunction!!.recover(runtimeException, executionRequest)
    }

    private fun populateJythonComponentInstance() {
        val bluePrintContext = bluePrintRuntimeService.bluePrintContext()

        val operationAssignment: OperationAssignment = bluePrintContext
                .nodeTemplateInterfaceOperation(nodeTemplateName, interfaceName, operationName)

        val artifactName: String = operationAssignment.implementation?.primary
                ?: throw BluePrintProcessorException("missing primary field to get artifact name for node template ($nodeTemplateName)")

        val artifactDefinition = bluePrintRuntimeService.resolveNodeTemplateArtifactDefinition(nodeTemplateName, artifactName)

        val pythonFileName = artifactDefinition.file
                ?: throw BluePrintProcessorException("missing file name for node template ($nodeTemplateName)'s artifactName($artifactName)")

        val pythonClassName = FilenameUtils.getBaseName(pythonFileName)

        val content: String? = bluePrintRuntimeService.resolveNodeTemplateArtifact(nodeTemplateName, artifactName)

        checkNotEmptyOrThrow(content, "artifact ($artifactName) content is empty")

        val instanceDependenciesNode: ArrayNode = operationInputs[PythonExecutorConstants.INPUT_INSTANCE_DEPENDENCIES] as? ArrayNode
                ?: throw BluePrintProcessorException("Failed to get property(${PythonExecutorConstants.INPUT_INSTANCE_DEPENDENCIES})")

        val jythonInstance: MutableMap<String, Any> = hashMapOf()
        jythonInstance["log"] = LoggerFactory.getLogger(pythonClassName)
        jythonInstance["bluePrintRuntimeService"] = bluePrintRuntimeService

        instanceDependenciesNode.forEach { instanceName ->
            jythonInstance[instanceName.textValue()] = applicationContext.getBean(instanceName.textValue())
        }

        // Setup componentFunction
        componentFunction = blueprintJythonService.jythonInstance(bluePrintContext, pythonClassName,
                content!!, jythonInstance)
        componentFunction?.bluePrintRuntimeService = bluePrintRuntimeService
        componentFunction?.executionServiceInput = executionServiceInput
        componentFunction?.stepName = stepName
        componentFunction?.interfaceName = interfaceName
        componentFunction?.operationName = operationName
        componentFunction?.processId = processId
        componentFunction?.workflowName = workflowName
    }
}