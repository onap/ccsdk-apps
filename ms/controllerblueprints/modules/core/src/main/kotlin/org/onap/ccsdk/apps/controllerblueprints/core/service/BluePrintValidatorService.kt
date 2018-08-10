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

package org.onap.ccsdk.apps.controllerblueprints.core.service

import com.fasterxml.jackson.databind.JsonNode
import com.google.common.base.Preconditions
import org.apache.commons.lang3.StringUtils
import org.onap.ccsdk.apps.controllerblueprints.core.*
import org.onap.ccsdk.apps.controllerblueprints.core.data.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.Serializable

/**
 *
 *
 * @author Brinda Santh
 */
interface BluePrintValidatorService : Serializable {

    @Throws(BluePrintException::class)
    fun validateBlueprint(bluePrintContext: BluePrintContext, properties: MutableMap<String, Any>)

    @Throws(BluePrintException::class)
    fun validateBlueprint(serviceTemplate: ServiceTemplate, properties: MutableMap<String, Any>)
}

open class BluePrintValidatorDefaultService : BluePrintValidatorService {

    val logger: Logger = LoggerFactory.getLogger(BluePrintValidatorDefaultService::class.toString())

    lateinit var bluePrintContext: BluePrintContext
    lateinit var serviceTemplate: ServiceTemplate
    lateinit var properties: MutableMap<String, Any>
    var message: StringBuilder = StringBuilder()
    val seperator: String = "/"
    var paths: MutableList<String> = arrayListOf()

    @Throws(BluePrintException::class)
    override fun validateBlueprint(bluePrintContext: BluePrintContext, properties: MutableMap<String, Any>) {
        validateBlueprint(bluePrintContext.serviceTemplate,properties)
    }

    @Throws(BluePrintException::class)
    override fun validateBlueprint(serviceTemplate: ServiceTemplate, properties: MutableMap<String, Any>) {
        this.bluePrintContext = BluePrintContext(serviceTemplate)
        this.serviceTemplate = serviceTemplate
        this.properties = properties
        try {
            message.appendln("-> Config Blueprint")
            serviceTemplate.metadata?.let { validateMetadata(serviceTemplate.metadata!!) }
            serviceTemplate.artifactTypes?.let { validateArtifactTypes(serviceTemplate.artifactTypes!!) }
            serviceTemplate.dataTypes?.let { validateDataTypes(serviceTemplate.dataTypes!!) }
            serviceTemplate.nodeTypes?.let { validateNodeTypes(serviceTemplate.nodeTypes!!) }
            serviceTemplate.topologyTemplate?.let { validateTopologyTemplate(serviceTemplate.topologyTemplate!!) }
        } catch (e: Exception) {
            logger.error("validation failed in the path : {}", paths.joinToString(seperator), e)
            logger.error("validation trace message :{} ", message)
            throw BluePrintException(e,
                    format("failed to validate blueprint on path ({}) with message {}"
                            , paths.joinToString(seperator), e.message))
        }
    }

    @Throws(BluePrintException::class)
    open fun validateMetadata(metaDataMap: MutableMap<String, String>) {
        paths.add("metadata")
        Preconditions.checkArgument(StringUtils.isNotBlank(metaDataMap[BluePrintConstants.METADATA_TEMPLATE_NAME]), "failed to get template name metadata")
        Preconditions.checkArgument(StringUtils.isNotBlank(metaDataMap[BluePrintConstants.METADATA_TEMPLATE_VERSION]), "failed to get template version metadata")
        Preconditions.checkArgument(StringUtils.isNotBlank(metaDataMap[BluePrintConstants.METADATA_TEMPLATE_TAGS]), "failed to get template tags metadata")
        Preconditions.checkArgument(StringUtils.isNotBlank(metaDataMap[BluePrintConstants.METADATA_TEMPLATE_AUTHOR]), "failed to get template author metadata")
        Preconditions.checkArgument(StringUtils.isNotBlank(metaDataMap[BluePrintConstants.METADATA_USER_GROUPS]), "failed to get user groups metadata")
        paths.removeAt(paths.lastIndex)
    }

    @Throws(BluePrintException::class)
    open fun validateArtifactTypes(artifactTypes: MutableMap<String, ArtifactType>) {
        paths.add("artifact_types")
        artifactTypes.forEach { artifactName, artifactType ->
            paths.add(artifactName)
            message.appendln("--> Artifact Type :" + paths.joinToString(seperator))
            artifactType.properties?.let { validatePropertyDefinitions(artifactType.properties!!) }
            paths.removeAt(paths.lastIndex)
        }
        paths.removeAt(paths.lastIndex)
    }

    @Throws(BluePrintException::class)
    open fun validateDataTypes(dataTypes: MutableMap<String, DataType>) {
        paths.add("dataTypes")
        dataTypes.forEach { dataTypeName, dataType ->
            paths.add(dataTypeName)
            message.appendln("--> Data Type :" + paths.joinToString(seperator))
            dataType.properties?.let { validatePropertyDefinitions(dataType.properties!!) }
            paths.removeAt(paths.lastIndex)
        }
        paths.removeAt(paths.lastIndex)
    }

    @Throws(BluePrintException::class)
    open fun validateNodeTypes(nodeTypes: MutableMap<String, NodeType>) {
        paths.add("nodeTypes")
        nodeTypes.forEach { nodeTypeName, nodeType ->
            // Validate Single Node Type
            validateNodeType(nodeTypeName,nodeType)
        }
        paths.removeAt(paths.lastIndex)
    }

    @Throws(BluePrintException::class)
    open fun validateNodeType(nodeTypeName: String, nodeType: NodeType) {
        paths.add(nodeTypeName)
        message.appendln("--> Node Type :" + paths.joinToString(seperator))
        val derivedFrom: String = nodeType.derivedFrom
        //Check Derived From
        checkValidNodeTypesDerivedFrom(derivedFrom)

        nodeType.properties?.let { validatePropertyDefinitions(nodeType.properties!!) }
        nodeType.interfaces?.let { validateInterfaceDefinitions(nodeType.interfaces!!) }
        paths.removeAt(paths.lastIndex)
    }

    @Throws(BluePrintException::class)
    open fun validateTopologyTemplate(topologyTemplate: TopologyTemplate) {
        paths.add("topology")
        message.appendln("--> Topology Template")
        topologyTemplate.inputs?.let { validateInputs(topologyTemplate.inputs!!) }
        topologyTemplate.nodeTemplates?.let { validateNodeTemplates(topologyTemplate.nodeTemplates!!) }
        topologyTemplate.workflows?.let { validateWorkFlows(topologyTemplate.workflows!!) }
        paths.removeAt(paths.lastIndex)
    }

    @Throws(BluePrintException::class)
    open fun validateInputs(inputs: MutableMap<String, PropertyDefinition>) {
        paths.add("inputs")
        message.appendln("---> Input :" + paths.joinToString(seperator))
        validatePropertyDefinitions(inputs)
        paths.removeAt(paths.lastIndex)
    }

    @Throws(BluePrintException::class)
    open fun validateNodeTemplates(nodeTemplates: MutableMap<String, NodeTemplate>) {
        paths.add("nodeTemplates")
        nodeTemplates.forEach { nodeTemplateName, nodeTemplate ->
            validateNodeTemplate(nodeTemplateName, nodeTemplate)
        }
        paths.removeAt(paths.lastIndex)
    }

    @Throws(BluePrintException::class)
    open fun validateNodeTemplate(nodeTemplateName : String, nodeTemplate: NodeTemplate) {
        paths.add(nodeTemplateName)
        message.appendln("---> Node Template :" + paths.joinToString(seperator))
        val type: String = nodeTemplate.type

        val nodeType: NodeType = serviceTemplate.nodeTypes?.get(type)
                ?: throw BluePrintException(format("Failed to get node type definition  for node template : {}", nodeTemplateName))

        nodeTemplate.artifacts?.let { validateArtifactDefinitions(nodeTemplate.artifacts!!) }
        nodeTemplate.properties?.let { validatePropertyAssignments(nodeType.properties!!, nodeTemplate.properties!!) }
        nodeTemplate.capabilities?.let { validateCapabilityAssignments(nodeTemplate.capabilities!!) }
        nodeTemplate.requirements?.let { validateRequirementAssignments(nodeTemplate.requirements!!) }
        nodeTemplate.interfaces?.let { validateInterfaceAssignments(nodeTemplate.interfaces!!) }
        paths.removeAt(paths.lastIndex)
    }

    @Throws(BluePrintException::class)
    open fun validateWorkFlows(workflows: MutableMap<String, Workflow>) {
        paths.add("workflows")
        workflows.forEach { workflowName, workflow ->

            // Validate Single workflow
            validateWorkFlow(workflowName, workflow)
        }
        paths.removeAt(paths.lastIndex)
    }

    @Throws(BluePrintException::class)
    open fun validateWorkFlow(workflowName:String, workflow: Workflow) {
        paths.add(workflowName)
        message.appendln("---> Workflow :" + paths.joinToString(seperator))
        // Step Validation Start
        paths.add("steps")
        workflow.steps?.forEach { stepName, step ->
            paths.add(stepName)
            message.appendln("----> Steps :" + paths.joinToString(seperator))
            paths.removeAt(paths.lastIndex)
        }
        paths.removeAt(paths.lastIndex)
        // Step Validation Ends
        paths.removeAt(paths.lastIndex)
    }

    @Throws(BluePrintException::class)
    open fun validatePropertyDefinitions(properties: MutableMap<String, PropertyDefinition>) {
        paths.add("properties")
        properties.forEach { propertyName, propertyDefinition ->
            paths.add(propertyName)
            val dataType: String = propertyDefinition.type!!
            if (BluePrintTypes.validPrimitiveTypes().contains(dataType)) {
                // Do Nothing
            } else if (BluePrintTypes.validCollectionTypes().contains(dataType)) {
                val entrySchemaType: String = propertyDefinition.entrySchema?.type
                        ?: throw BluePrintException(format("Entry schema for data type ({}) for the property ({}) not found", dataType, propertyName))
                checkPrimitiveOrComplex(entrySchemaType, propertyName)
            } else {
                checkPropertyDataType(dataType, propertyName)
            }
            message.appendln("property " + paths.joinToString(seperator) + " of type " + dataType)
            paths.removeAt(paths.lastIndex)
        }
        paths.removeAt(paths.lastIndex)
    }

    @Throws(BluePrintException::class)
    open fun validatePropertyAssignments(nodeTypeProperties: MutableMap<String, PropertyDefinition>,
                                         properties: MutableMap<String, JsonNode>) {
        properties.forEach { propertyName, propertyAssignment ->
            val propertyDefinition: PropertyDefinition = nodeTypeProperties[propertyName]
                    ?: throw BluePrintException(format("failed to get definition for the property ({})", propertyName))
            // Check and Validate if Expression Node
            val expressionData = BluePrintExpressionService.getExpressionData(propertyAssignment)
            if (!expressionData.isExpression) {
                checkPropertyValue(propertyDefinition, propertyAssignment)
            }
        }
    }

    @Throws(BluePrintException::class)
    open fun validateArtifactDefinitions(artifacts: MutableMap<String, ArtifactDefinition>) {
        paths.add("artifacts")
        artifacts.forEach { artifactName, artifactDefinition ->
            paths.add(artifactName)
            message.appendln("Validating artifact " + paths.joinToString(seperator))
            val type: String = artifactDefinition.type
                    ?: throw BluePrintException("type is missing for artifact definition :" + artifactName)
            // Check Artifact Type
            checkValidArtifactType(type)

            val file: String = artifactDefinition.file
                    ?: throw BluePrintException(format("file is missing for artifact definition : {}", artifactName))

            paths.removeAt(paths.lastIndex)
        }
        paths.removeAt(paths.lastIndex)
    }

    @Throws(BluePrintException::class)
    open fun validateCapabilityAssignments(capabilities: MutableMap<String, CapabilityAssignment>) {

    }

    @Throws(BluePrintException::class)
    open fun validateRequirementAssignments(requirements: MutableMap<String, RequirementAssignment>) {

    }

    @Throws(BluePrintException::class)
    open fun validateInterfaceAssignments(interfaces: MutableMap<String, InterfaceAssignment>) {

    }

    @Throws(BluePrintException::class)
    open fun validateInterfaceDefinitions(interfaces: MutableMap<String, InterfaceDefinition>) {
        paths.add("interfaces")
        interfaces.forEach { interfaceName, interfaceDefinition ->
            paths.add(interfaceName)
            message.appendln("Validating : " + paths.joinToString(seperator))
            interfaceDefinition.operations?.let { validateOperationDefinitions(interfaceDefinition.operations!!) }
            paths.removeAt(paths.lastIndex)
        }
        paths.removeAt(paths.lastIndex)
    }

    @Throws(BluePrintException::class)
    open fun validateOperationDefinitions(operations: MutableMap<String, OperationDefinition>) {
        paths.add("operations")
        operations.forEach { opertaionName, operationDefinition ->
            paths.add(opertaionName)
            message.appendln("Validating : " + paths.joinToString(seperator))
            operationDefinition.implementation?.let { validateImplementation(operationDefinition.implementation!!) }
            operationDefinition.inputs?.let { validatePropertyDefinitions(operationDefinition.inputs!!) }
            operationDefinition.outputs?.let { validatePropertyDefinitions(operationDefinition.outputs!!) }
            paths.removeAt(paths.lastIndex)
        }
        paths.removeAt(paths.lastIndex)
    }

    @Throws(BluePrintException::class)
    open fun validateImplementation(implementation: Implementation) {
        checkNotEmptyNThrow(implementation.primary)
    }

    @Throws(BluePrintException::class)
    open fun checkValidNodeType(nodeType : String) {

    }

    @Throws(BluePrintException::class)
    open fun checkValidArtifactType(artifactType: String) {

        serviceTemplate.artifactTypes?.containsKey(artifactType)
                ?: throw BluePrintException(format("Failed to node type definition for artifact definition : {}", artifactType))
    }

    @Throws(BluePrintException::class)
    open fun checkValidNodeTypesDerivedFrom(derivedFrom: String) {

    }

    private fun checkPropertyValue(propertyDefinition: PropertyDefinition, jsonNode: JsonNode) {
        //logger.info("validating path ({}), Property {}, value ({})", paths, propertyDefinition, jsonNode)
    }

    private fun checkPropertyDataType(dataType: String, propertyName: String): Boolean {
        if (checkDataType(dataType)) {
            return true
        } else {
            throw BluePrintException(format("Data type ({}) for the property ({}) not found", dataType, propertyName))
        }
    }

    private fun checkPrimitiveOrComplex(dataType: String, propertyName: String): Boolean {
        if (BluePrintTypes.validPrimitiveTypes().contains(dataType) || checkDataType(dataType)) {
            return true
        } else {
            throw BluePrintException(format("Data type ({}) for the property ({}) is not valid", dataType))
        }
    }

    private fun checkDataType(key: String): Boolean {
        return serviceTemplate.dataTypes?.containsKey(key) ?: false
    }

    private fun checkNodeType(key: String): Boolean {
        return serviceTemplate.nodeTypes?.containsKey(key) ?: false
    }

}