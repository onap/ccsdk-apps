/*
 * Copyright © 2017-2018 AT&T Intellectual Property.
 * Modifications Copyright © 2018 IBM.
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

package org.onap.ccsdk.apps.controllerblueprints.core.service


import com.att.eelf.configuration.EELFLogger
import com.att.eelf.configuration.EELFManager
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintError
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintProcessorException
import org.onap.ccsdk.apps.controllerblueprints.core.data.ArtifactDefinition
import org.onap.ccsdk.apps.controllerblueprints.core.data.NodeTemplate
import org.onap.ccsdk.apps.controllerblueprints.core.data.PropertyDefinition
import org.onap.ccsdk.apps.controllerblueprints.core.utils.JacksonUtils

interface BluePrintRuntimeService<T> {

    fun id(): String

    fun bluePrintContext(): BluePrintContext

    fun getExecutionContext(): T

    fun setExecutionContext(executionContext: T)

    fun put(key: String, value: JsonNode)

    fun get(key: String): JsonNode?

    fun check(key: String): Boolean

    fun cleanRuntime()

    fun getAsString(key: String): String?

    fun getAsBoolean(key: String): Boolean?

    fun getAsInt(key: String): Int?

    fun getAsDouble(key: String): Double?

    fun getBluePrintError(): BluePrintError

    fun setBluePrintError(bluePrintError: BluePrintError)

    /*
      Get the Node Type Definition for the Node Template, Then iterate Node Type Properties and resolve the expressing
   */
    fun resolveNodeTemplateProperties(nodeTemplateName: String): MutableMap<String, JsonNode>

    fun resolveNodeTemplateCapabilityProperties(nodeTemplateName: String, capability: String): MutableMap<String,
            JsonNode>

    fun resolveNodeTemplateRequirementProperties(nodeTemplateName: String, requirementName: String): MutableMap<String,
            JsonNode>

    fun resolveNodeTemplateInterfaceOperationInputs(nodeTemplateName: String, interfaceName: String, operationName: String): MutableMap<String, JsonNode>

    fun resolveNodeTemplateInterfaceOperationOutputs(nodeTemplateName: String, interfaceName: String, operationName: String): MutableMap<String, JsonNode>

    fun resolveNodeTemplateArtifact(nodeTemplateName: String, artifactName: String): String

    fun resolveNodeTemplateArtifactDefinition(nodeTemplateName: String, artifactName: String): ArtifactDefinition

    fun setInputValue(propertyName: String, propertyDefinition: PropertyDefinition, value: JsonNode)

    fun setWorkflowInputValue(workflowName: String, propertyName: String, propertyDefinition: PropertyDefinition, value: JsonNode)

    fun setNodeTemplatePropertyValue(nodeTemplateName: String, propertyName: String, value: JsonNode)

    fun setNodeTemplateAttributeValue(nodeTemplateName: String, attributeName: String, value: JsonNode)

    fun setNodeTemplateOperationPropertyValue(nodeTemplateName: String, interfaceName: String, operationName: String, propertyName: String, value: JsonNode)

    fun setNodeTemplateOperationInputValue(nodeTemplateName: String, interfaceName: String, operationName: String, propertyName: String, value: JsonNode)

    fun setNodeTemplateOperationOutputValue(nodeTemplateName: String, interfaceName: String, operationName: String, propertyName: String, value: JsonNode)

    fun getInputValue(propertyName: String): JsonNode

    fun getNodeTemplateOperationOutputValue(nodeTemplateName: String, interfaceName: String, operationName: String, propertyName: String): JsonNode

    fun getNodeTemplatePropertyValue(nodeTemplateName: String, propertyName: String): JsonNode?

    fun getNodeTemplateAttributeValue(nodeTemplateName: String, attributeName: String): JsonNode?

    fun getNodeTemplateRequirementPropertyValue(nodeTemplateName: String, requirementName: String, propertyName: String): JsonNode?

    fun getNodeTemplateCapabilityPropertyValue(nodeTemplateName: String, capabilityName: String, propertyName: String): JsonNode?

    fun assignInputs(jsonNode: JsonNode)

    fun assignWorkflowInputs(workflowName: String, jsonNode: JsonNode)

    fun getJsonForNodeTemplateAttributeProperties(nodeTemplateName: String, keys: List<String>): JsonNode
}

/**
 *
 *
 * @author Brinda Santh
 */
open class DefaultBluePrintRuntimeService(private var id: String, private var bluePrintContext: BluePrintContext)
    : BluePrintRuntimeService<MutableMap<String, JsonNode>> {

    @Transient
    private val log: EELFLogger = EELFManager.getInstance().getLogger(BluePrintRuntimeService::class.toString())

    private var store: MutableMap<String, JsonNode> = hashMapOf()

    private var bluePrintError = BluePrintError()

    override fun id(): String {
        return id
    }

    override fun bluePrintContext(): BluePrintContext {
        return bluePrintContext
    }

    override fun getExecutionContext(): MutableMap<String, JsonNode> {
        return store
    }

    @Suppress("UNCHECKED_CAST")
    override fun setExecutionContext(executionContext: MutableMap<String, JsonNode>) {
        this.store = executionContext
    }

    override fun put(key: String, value: JsonNode) {
        store[key] = value
    }

    override fun get(key: String): JsonNode {
        return store[key] ?: throw BluePrintProcessorException("failed to get execution property($key)")
    }

    override fun check(key: String): Boolean {
        return store.containsKey(key)
    }

    override fun cleanRuntime() {
        store.clear()
    }

    private fun getJsonNode(key: String): JsonNode {
        return get(key)
    }

    override fun getAsString(key: String): String? {
        return get(key).asText()
    }

    override fun getAsBoolean(key: String): Boolean? {
        return get(key).asBoolean()
    }

    override fun getAsInt(key: String): Int? {
        return get(key).asInt()
    }

    override fun getAsDouble(key: String): Double? {
        return get(key).asDouble()
    }

    override fun getBluePrintError(): BluePrintError {
        return this.bluePrintError
    }

    override fun setBluePrintError(bluePrintError: BluePrintError) {
        this.bluePrintError = bluePrintError
    }

    /*
                Get the Node Type Definition for the Node Template, Then iterate Node Type Properties and resolve the expressing
             */
    override fun resolveNodeTemplateProperties(nodeTemplateName: String): MutableMap<String, JsonNode> {
        log.info("resolveNodeTemplatePropertyValues for node template ({})", nodeTemplateName)
        val propertyAssignmentValue: MutableMap<String, JsonNode> = hashMapOf()

        val nodeTemplate: NodeTemplate = bluePrintContext.nodeTemplateByName(nodeTemplateName)

        val propertyAssignments: MutableMap<String, JsonNode> = nodeTemplate.properties!!

        // Get the Node Type Definitions
        val nodeTypeProperties: MutableMap<String, PropertyDefinition> = bluePrintContext.nodeTypeChainedProperties(nodeTemplate.type)!!

        // Iterate Node Type Properties
        nodeTypeProperties.forEach { nodeTypePropertyName, nodeTypeProperty ->
            // Get the Express or Value for the Node Template
            val propertyAssignment: JsonNode? = propertyAssignments[nodeTypePropertyName]

            var resolvedValue: JsonNode = NullNode.getInstance()
            if (propertyAssignment != null) {
                // Resolve the Expressing
                val propertyAssignmentExpression = PropertyAssignmentService(this)
                resolvedValue = propertyAssignmentExpression.resolveAssignmentExpression(nodeTemplateName, nodeTypePropertyName, propertyAssignment)
            } else {
                // Assign default value to the Operation
                nodeTypeProperty.defaultValue?.let { defaultValue ->
                    resolvedValue = defaultValue
                }
            }
            // Set for Return of method
            propertyAssignmentValue[nodeTypePropertyName] = resolvedValue
        }
        log.info("resolved property definition for node template ({}), values ({})", nodeTemplateName, propertyAssignmentValue)
        return propertyAssignmentValue
    }

    override fun resolveNodeTemplateCapabilityProperties(nodeTemplateName: String, capabilityName: String):
            MutableMap<String, JsonNode> {
        log.info("resolveNodeTemplateCapabilityProperties for node template($nodeTemplateName) capability " +
                "($capabilityName)")
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resolveNodeTemplateRequirementProperties(nodeTemplateName: String, requirementName: String): MutableMap<String, JsonNode> {
        log.info("resolveNodeTemplateRequirementProperties for node template($nodeTemplateName) requirement ($requirementName)")
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resolveNodeTemplateInterfaceOperationInputs(nodeTemplateName: String,
                                                             interfaceName: String, operationName: String): MutableMap<String, JsonNode> {
        log.info("resolveNodeTemplateInterfaceOperationInputs for node template ({}),interface name ({}), " +
                "operationName({})", nodeTemplateName, interfaceName, operationName)

        val propertyAssignmentValue: MutableMap<String, JsonNode> = hashMapOf()

        val propertyAssignments: MutableMap<String, JsonNode> =
                bluePrintContext.nodeTemplateInterfaceOperationInputs(nodeTemplateName, interfaceName, operationName)
                        ?: hashMapOf()

        val nodeTypeName = bluePrintContext.nodeTemplateByName(nodeTemplateName).type

        val nodeTypeInterfaceOperationInputs: MutableMap<String, PropertyDefinition> =
                bluePrintContext.nodeTypeInterfaceOperationInputs(nodeTypeName, interfaceName, operationName)
                        ?: hashMapOf()

        log.info("input definition for node template ({}), values ({})", nodeTemplateName, propertyAssignments)

        // Iterate Node Type Properties
        nodeTypeInterfaceOperationInputs.forEach { nodeTypePropertyName, nodeTypeProperty ->
            // Get the Express or Value for the Node Template
            val propertyAssignment: JsonNode? = propertyAssignments[nodeTypePropertyName]

            var resolvedValue: JsonNode = NullNode.getInstance()
            if (propertyAssignment != null) {
                // Resolve the Expressing
                val propertyAssignmentExpression = PropertyAssignmentService(this)
                resolvedValue = propertyAssignmentExpression.resolveAssignmentExpression(nodeTemplateName, nodeTypePropertyName, propertyAssignment)
            } else {
                // Assign default value to the Operation
                nodeTypeProperty.defaultValue?.let {
                    resolvedValue = JacksonUtils.jsonNodeFromObject(nodeTypeProperty.defaultValue!!)
                }
            }
            // Set for Return of method
            propertyAssignmentValue[nodeTypePropertyName] = resolvedValue
        }
        log.trace("resolved input assignments for node template ({}), values ({})", nodeTemplateName,
                propertyAssignmentValue)

        return propertyAssignmentValue
    }


    override fun resolveNodeTemplateInterfaceOperationOutputs(nodeTemplateName: String,
                                                              interfaceName: String, operationName: String): MutableMap<String, JsonNode> {
        log.info("resolveNodeTemplateInterfaceOperationOutputs for node template ({}),interface name ({}), " +
                "operationName({})", nodeTemplateName, interfaceName, operationName)

        val propertyAssignmentValue: MutableMap<String, JsonNode> = hashMapOf()

        val propertyAssignments: MutableMap<String, JsonNode> =
                bluePrintContext.nodeTemplateInterfaceOperationOutputs(nodeTemplateName, interfaceName, operationName)
                        ?: hashMapOf()

        val nodeTypeName = bluePrintContext.nodeTemplateByName(nodeTemplateName).type

        val nodeTypeInterfaceOperationOutputs: MutableMap<String, PropertyDefinition> =
                bluePrintContext.nodeTypeInterfaceOperationOutputs(nodeTypeName, interfaceName, operationName)
                        ?: hashMapOf()

        // Iterate Node Type Properties
        nodeTypeInterfaceOperationOutputs.forEach { nodeTypePropertyName, nodeTypeProperty ->

            // Get the Express or Value for the Node Template
            val propertyAssignment: JsonNode? = propertyAssignments[nodeTypePropertyName]

            var resolvedValue: JsonNode = NullNode.getInstance()
            if (propertyAssignment != null) {
                // Resolve the Expressing
                val propertyAssignmentExpression = PropertyAssignmentService(this)
                resolvedValue = propertyAssignmentExpression.resolveAssignmentExpression(nodeTemplateName, nodeTypePropertyName, propertyAssignment)
            } else {
                // Assign default value to the Operation
                nodeTypeProperty.defaultValue?.let {
                    resolvedValue = JacksonUtils.jsonNodeFromObject(nodeTypeProperty.defaultValue!!)
                }
            }
            // Set for Return of method
            propertyAssignmentValue[nodeTypePropertyName] = resolvedValue

            // Store  operation output values into context
            setNodeTemplateOperationOutputValue(nodeTemplateName, interfaceName, operationName, nodeTypePropertyName, resolvedValue)
            log.trace("resolved output assignments for node template ({}), property name ({}), value ({})", nodeTemplateName, nodeTypePropertyName, resolvedValue)
        }
        return propertyAssignmentValue
    }

    override fun resolveNodeTemplateArtifact(nodeTemplateName: String, artifactName: String): String {
        val artifactDefinition: ArtifactDefinition = resolveNodeTemplateArtifactDefinition(nodeTemplateName, artifactName)
        val propertyAssignmentExpression = PropertyAssignmentService(this)
        return propertyAssignmentExpression.artifactContent(artifactDefinition)
    }

    override fun resolveNodeTemplateArtifactDefinition(nodeTemplateName: String, artifactName: String): ArtifactDefinition {
        val nodeTemplate = bluePrintContext.nodeTemplateByName(nodeTemplateName)

        return nodeTemplate.artifacts?.get(artifactName)
                ?: throw BluePrintProcessorException(String.format("failed to get artifat definition {} from the node template"
                        , artifactName))

    }


    override fun setInputValue(propertyName: String, propertyDefinition: PropertyDefinition, value: JsonNode) {
        val path = StringBuilder(BluePrintConstants.PATH_INPUTS)
                .append(BluePrintConstants.PATH_DIVIDER).append(propertyName).toString()
        log.trace("setting input path ({}), values ({})", path, value)
        put(path, value)
    }

    override fun setWorkflowInputValue(workflowName: String, propertyName: String, propertyDefinition: PropertyDefinition, value: JsonNode) {
        val path: String = StringBuilder(BluePrintConstants.PATH_NODE_WORKFLOWS).append(BluePrintConstants.PATH_DIVIDER).append(workflowName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_INPUTS)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_PROPERTIES)
                .append(BluePrintConstants.PATH_DIVIDER).append(propertyName).toString()
        put(path, value)
    }

    override fun setNodeTemplatePropertyValue(nodeTemplateName: String, propertyName: String, value: JsonNode) {

        val path: String = StringBuilder(BluePrintConstants.PATH_NODE_TEMPLATES).append(BluePrintConstants.PATH_DIVIDER).append(nodeTemplateName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_PROPERTIES)
                .append(BluePrintConstants.PATH_DIVIDER).append(propertyName).toString()
        put(path, value)
    }

    override fun setNodeTemplateAttributeValue(nodeTemplateName: String, attributeName: String, value: JsonNode) {

        val path: String = StringBuilder(BluePrintConstants.PATH_NODE_TEMPLATES).append(BluePrintConstants.PATH_DIVIDER).append(nodeTemplateName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_ATTRIBUTES)
                .append(BluePrintConstants.PATH_DIVIDER).append(attributeName).toString()
        put(path, value)
    }

    override fun setNodeTemplateOperationPropertyValue(nodeTemplateName: String, interfaceName: String, operationName: String, propertyName: String,
                                                       value: JsonNode) {
        val path: String = StringBuilder(BluePrintConstants.PATH_NODE_TEMPLATES).append(BluePrintConstants.PATH_DIVIDER).append(nodeTemplateName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_INTERFACES).append(BluePrintConstants.PATH_DIVIDER).append(interfaceName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_OPERATIONS).append(BluePrintConstants.PATH_DIVIDER).append(operationName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_PROPERTIES)
                .append(BluePrintConstants.PATH_DIVIDER).append(propertyName).toString()
        log.trace("setting operation property path ({}), values ({})", path, value)
        put(path, value)
    }

    override fun setNodeTemplateOperationInputValue(nodeTemplateName: String, interfaceName: String, operationName: String, propertyName: String,
                                                    value: JsonNode) {
        val path: String = StringBuilder(BluePrintConstants.PATH_NODE_TEMPLATES).append(BluePrintConstants.PATH_DIVIDER).append(nodeTemplateName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_INTERFACES).append(BluePrintConstants.PATH_DIVIDER).append(interfaceName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_OPERATIONS).append(BluePrintConstants.PATH_DIVIDER).append(operationName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_INPUTS)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_PROPERTIES)
                .append(BluePrintConstants.PATH_DIVIDER).append(propertyName).toString()
        put(path, value)
    }

    override fun setNodeTemplateOperationOutputValue(nodeTemplateName: String, interfaceName: String, operationName: String, propertyName: String,
                                                     value: JsonNode) {
        val path: String = StringBuilder(BluePrintConstants.PATH_NODE_TEMPLATES).append(BluePrintConstants.PATH_DIVIDER).append(nodeTemplateName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_INTERFACES).append(BluePrintConstants.PATH_DIVIDER).append(interfaceName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_OPERATIONS).append(BluePrintConstants.PATH_DIVIDER).append(operationName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_OUTPUTS)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_PROPERTIES)
                .append(BluePrintConstants.PATH_DIVIDER).append(propertyName).toString()
        put(path, value)
    }


    override fun getInputValue(propertyName: String): JsonNode {
        val path = StringBuilder(BluePrintConstants.PATH_INPUTS)
                .append(BluePrintConstants.PATH_DIVIDER).append(propertyName).toString()
        return getJsonNode(path)
    }

    override fun getNodeTemplateOperationOutputValue(nodeTemplateName: String, interfaceName: String, operationName: String, propertyName: String): JsonNode {
        val path: String = StringBuilder(BluePrintConstants.PATH_NODE_TEMPLATES).append(BluePrintConstants.PATH_DIVIDER).append(nodeTemplateName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_INTERFACES).append(BluePrintConstants.PATH_DIVIDER).append(interfaceName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_OPERATIONS).append(BluePrintConstants.PATH_DIVIDER).append(operationName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_OUTPUTS).append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_PROPERTIES)
                .append(BluePrintConstants.PATH_DIVIDER).append(propertyName).toString()
        return getJsonNode(path)
    }

    override fun getNodeTemplatePropertyValue(nodeTemplateName: String, propertyName: String): JsonNode {
        val path: String = StringBuilder(BluePrintConstants.PATH_NODE_TEMPLATES).append(BluePrintConstants.PATH_DIVIDER).append(nodeTemplateName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_PROPERTIES)
                .append(BluePrintConstants.PATH_DIVIDER).append(propertyName).toString()
        return getJsonNode(path)
    }

    override fun getNodeTemplateAttributeValue(nodeTemplateName: String, attributeName: String): JsonNode {
        val path: String = StringBuilder(BluePrintConstants.PATH_NODE_TEMPLATES).append(BluePrintConstants.PATH_DIVIDER).append(nodeTemplateName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_ATTRIBUTES)
                .append(BluePrintConstants.PATH_DIVIDER).append(attributeName).toString()
        return getJsonNode(path)
    }

    override fun getNodeTemplateRequirementPropertyValue(nodeTemplateName: String, requirementName: String, propertyName:
    String): JsonNode {
        val path: String = StringBuilder(BluePrintConstants.PATH_NODE_TEMPLATES).append(BluePrintConstants.PATH_DIVIDER).append(nodeTemplateName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_REQUIREMENTS).append(requirementName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_PROPERTIES)
                .append(BluePrintConstants.PATH_DIVIDER).append(propertyName).toString()
        return getJsonNode(path)
    }

    override fun getNodeTemplateCapabilityPropertyValue(nodeTemplateName: String, capabilityName: String, propertyName:
    String): JsonNode {
        val path: String = StringBuilder(BluePrintConstants.PATH_NODE_TEMPLATES).append(BluePrintConstants.PATH_DIVIDER).append(nodeTemplateName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_CAPABILITIES).append(capabilityName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_PROPERTIES)
                .append(BluePrintConstants.PATH_DIVIDER).append(propertyName).toString()
        return getJsonNode(path)
    }

    override fun assignInputs(jsonNode: JsonNode) {
        log.info("assignInputs from input JSON ({})", jsonNode.toString())
        bluePrintContext.inputs?.forEach { propertyName, property ->
            val valueNode: JsonNode = jsonNode.at(BluePrintConstants.PATH_DIVIDER + propertyName)
                    ?: NullNode.getInstance()
            setInputValue(propertyName, property, valueNode)
        }
    }

    override fun assignWorkflowInputs(workflowName: String, jsonNode: JsonNode) {
        log.info("assign workflow {} input value ({})", workflowName, jsonNode.toString())

        val dynamicInputPropertiesName = "$workflowName-properties"

        bluePrintContext.workflowByName(workflowName).inputs?.forEach { propertyName, property ->
            if (propertyName != dynamicInputPropertiesName) {
                val valueNode: JsonNode = jsonNode.at(BluePrintConstants.PATH_DIVIDER + propertyName)
                        ?: NullNode.getInstance()
                setInputValue(propertyName, property, valueNode)
            }
        }
        // Load Dynamic data Types
        val workflowDynamicInputs: JsonNode? = jsonNode.get(dynamicInputPropertiesName)

        workflowDynamicInputs?.let {
            bluePrintContext.dataTypeByName("dt-$dynamicInputPropertiesName")?.properties?.forEach { propertyName, property ->
                val valueNode: JsonNode = workflowDynamicInputs.at(BluePrintConstants.PATH_DIVIDER + propertyName)
                        ?: NullNode.getInstance()
                setInputValue(propertyName, property, valueNode)

            }
        }
    }

    override fun getJsonForNodeTemplateAttributeProperties(nodeTemplateName: String, keys: List<String>): JsonNode {

        val jsonNode: ObjectNode = jacksonObjectMapper().createObjectNode()
        val path: String = StringBuilder(BluePrintConstants.PATH_NODE_TEMPLATES).append(BluePrintConstants.PATH_DIVIDER).append(nodeTemplateName)
                .append(BluePrintConstants.PATH_DIVIDER).append(BluePrintConstants.PATH_ATTRIBUTES)
                .append(BluePrintConstants.PATH_DIVIDER).toString()
        store.keys.filter {
            it.startsWith(path)
        }.map {
            val key = it.replace(path, "")
            if (keys.contains(key)) {
                val value = store[it] as JsonNode
                jsonNode.set(key, value)
            }
        }
        return jsonNode
    }


}