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

package org.onap.ccsdk.apps.controllerblueprints.core.validation

import com.att.eelf.configuration.EELFLogger
import com.att.eelf.configuration.EELFManager
import com.fasterxml.jackson.databind.JsonNode
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintTypes
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintValidationError
import org.onap.ccsdk.apps.controllerblueprints.core.data.*
import org.onap.ccsdk.apps.controllerblueprints.core.format
import org.onap.ccsdk.apps.controllerblueprints.core.interfaces.BluePrintNodeTemplateValidator
import org.onap.ccsdk.apps.controllerblueprints.core.interfaces.BluePrintTypeValidatorService
import org.onap.ccsdk.apps.controllerblueprints.core.service.BluePrintContext
import org.onap.ccsdk.apps.controllerblueprints.core.service.BluePrintExpressionService
import org.onap.ccsdk.apps.controllerblueprints.core.utils.JacksonUtils


open class BluePrintNodeTemplateValidatorImpl(private val bluePrintTypeValidatorService: BluePrintTypeValidatorService) : BluePrintNodeTemplateValidator {

    private val log: EELFLogger = EELFManager.getInstance().getLogger(BluePrintNodeTemplateValidatorImpl::class.toString())

    var bluePrintContext: BluePrintContext? = null
    var error: BluePrintValidationError? = null
    var paths: MutableList<String> = arrayListOf()

    override fun validate(bluePrintContext: BluePrintContext, error: BluePrintValidationError, nodeTemplateName: String, nodeTemplate: NodeTemplate) {

        paths.add(nodeTemplateName)

        val type: String = nodeTemplate.type

        val nodeType: NodeType = bluePrintContext.serviceTemplate.nodeTypes?.get(type)
                ?: throw BluePrintException("Failed to get NodeType($type) definition for NodeTemplate($nodeTemplateName)")

        nodeTemplate.artifacts?.let { validateArtifactDefinitions(nodeTemplate.artifacts!!) }
        nodeTemplate.properties?.let { validatePropertyAssignments(nodeType.properties!!, nodeTemplate.properties!!) }
        nodeTemplate.capabilities?.let { validateCapabilityAssignments(nodeType, nodeTemplateName, nodeTemplate) }
        nodeTemplate.requirements?.let { validateRequirementAssignments(nodeType, nodeTemplateName, nodeTemplate) }
        nodeTemplate.interfaces?.let { validateInterfaceAssignments(nodeType, nodeTemplateName, nodeTemplate) }

        paths.removeAt(paths.lastIndex)
    }

    @Throws(BluePrintException::class)
    open fun validateArtifactDefinitions(artifacts: MutableMap<String, ArtifactDefinition>) {
        paths.add("artifacts")
        artifacts.forEach { artifactDefinitionName, artifactDefinition ->
            paths.add(artifactDefinitionName)
            val type: String = artifactDefinition.type
                    ?: throw BluePrintException("type is missing for ArtifactDefinition$artifactDefinitionName)")
            // Check Artifact Type
            checkValidArtifactType(artifactDefinitionName, type)

            val file: String = artifactDefinition.file
                    ?: throw BluePrintException("file is missing for ArtifactDefinition($artifactDefinitionName)")

            paths.removeAt(paths.lastIndex)
        }
        paths.removeAt(paths.lastIndex)
    }


    @Throws(BluePrintException::class)
    open fun validatePropertyAssignments(nodeTypeProperties: MutableMap<String, PropertyDefinition>,
                                         properties: MutableMap<String, JsonNode>) {
        properties.forEach { propertyName, propertyAssignment ->
            val propertyDefinition: PropertyDefinition = nodeTypeProperties[propertyName]
                    ?: throw BluePrintException("failed to get definition for the property ($propertyName)")

            validatePropertyAssignment(propertyName, propertyDefinition, propertyAssignment)

        }
    }

    @Throws(BluePrintException::class)
    open fun validatePropertyAssignment(propertyName: String, propertyDefinition: PropertyDefinition,
                                        propertyAssignment: JsonNode) {
        // Check and Validate if Expression Node
        val expressionData = BluePrintExpressionService.getExpressionData(propertyAssignment)
        if (!expressionData.isExpression) {
            checkPropertyValue(propertyName, propertyDefinition, propertyAssignment)
        }
    }

    @Throws(BluePrintException::class)
    open fun validateCapabilityAssignments(nodeType: NodeType, nodeTemplateName: String, nodeTemplate: NodeTemplate) {
        val capabilities = nodeTemplate.capabilities
        paths.add("capabilities")
        capabilities?.forEach { capabilityName, capabilityAssignment ->
            paths.add(capabilityName)

            val capabilityDefinition = nodeType.capabilities?.get(capabilityName)
                    ?: throw BluePrintException("Failed to get NodeTemplate($nodeTemplateName) capability definition ($capabilityName) " +
                            "from NodeType(${nodeTemplate.type})")

            validateCapabilityAssignment(nodeTemplateName, capabilityName, capabilityDefinition, capabilityAssignment)

            paths.removeAt(paths.lastIndex)
        }
        paths.removeAt(paths.lastIndex)
    }

    @Throws(BluePrintException::class)
    open fun validateCapabilityAssignment(nodeTemplateName: String, capabilityName: String,
                                          capabilityDefinition: CapabilityDefinition, capabilityAssignment: CapabilityAssignment) {

        capabilityAssignment.properties?.let { validatePropertyAssignments(capabilityDefinition.properties!!, capabilityAssignment.properties!!) }

    }

    @Throws(BluePrintException::class)
    open fun validateRequirementAssignments(nodeType: NodeType, nodeTemplateName: String, nodeTemplate: NodeTemplate) {
        val requirements = nodeTemplate.requirements
        paths.add("requirements")
        requirements?.forEach { requirementName, requirementAssignment ->
            paths.add(requirementName)
            val requirementDefinition = nodeType.requirements?.get(requirementName)
                    ?: throw BluePrintException("Failed to get NodeTemplate($nodeTemplateName) requirement definition ($requirementName) from" +
                            " NodeType(${nodeTemplate.type})")
            // Validate Requirement Assignment
            validateRequirementAssignment(nodeTemplateName, requirementName, requirementDefinition, requirementAssignment)
            paths.removeAt(paths.lastIndex)
        }
        paths.removeAt(paths.lastIndex)

    }

    @Throws(BluePrintException::class)
    open fun validateRequirementAssignment(nodeTemplateName: String, requirementAssignmentName: String,
                                           requirementDefinition: RequirementDefinition, requirementAssignment: RequirementAssignment) {
        log.info("Validating NodeTemplate({}) requirement assignment ({}) ", nodeTemplateName, requirementAssignmentName)
        val requirementNodeTemplateName = requirementAssignment.node!!
        val capabilityName = requirementAssignment.capability
        val relationship = requirementAssignment.relationship!!

        check(BluePrintTypes.validRelationShipDerivedFroms.contains(relationship)) {
            throw BluePrintException("Failed to get relationship type ($relationship) for NodeTemplate($nodeTemplateName)'s requirement($requirementAssignmentName)")
        }

        val relationShipNodeTemplate = bluePrintContext!!.serviceTemplate.topologyTemplate?.nodeTemplates?.get(requirementNodeTemplateName)
                ?: throw BluePrintException("Failed to get requirement NodeTemplate($requirementNodeTemplateName)'s " +
                        "for NodeTemplate($nodeTemplateName) requirement($requirementAssignmentName)")

        relationShipNodeTemplate.capabilities?.get(capabilityName)
                ?: throw BluePrintException("Failed to get requirement NodeTemplate($requirementNodeTemplateName)'s " +
                        "capability($capabilityName) for NodeTemplate ($nodeTemplateName)'s requirement($requirementAssignmentName)")


    }

    @Throws(BluePrintException::class)
    open fun validateInterfaceAssignments(nodeType: NodeType, nodeTemplateName: String, nodeTemplate: NodeTemplate) {

        val interfaces = nodeTemplate.interfaces
        paths.add("interfaces")
        interfaces?.forEach { interfaceAssignmentName, interfaceAssignment ->
            paths.add(interfaceAssignmentName)
            val interfaceDefinition = nodeType.interfaces?.get(interfaceAssignmentName)
                    ?: throw BluePrintException("Failed to get NodeTemplate($nodeTemplateName) interface definition ($interfaceAssignmentName) from" +
                            " NodeType(${nodeTemplate.type})")

            validateInterfaceAssignment(nodeTemplateName, interfaceAssignmentName, interfaceDefinition,
                    interfaceAssignment)
            paths.removeAt(paths.lastIndex)
        }
        paths.removeAt(paths.lastIndex)


    }

    @Throws(BluePrintException::class)
    open fun validateInterfaceAssignment(nodeTemplateName: String, interfaceAssignmentName: String,
                                         interfaceDefinition: InterfaceDefinition,
                                         interfaceAssignment: InterfaceAssignment) {

        val operations = interfaceAssignment.operations
        operations?.let {
            validateInterfaceOperationsAssignment(nodeTemplateName, interfaceAssignmentName, interfaceDefinition,
                    interfaceAssignment)
        }

    }

    @Throws(BluePrintException::class)
    open fun validateInterfaceOperationsAssignment(nodeTemplateName: String, interfaceAssignmentName: String,
                                                   interfaceDefinition: InterfaceDefinition,
                                                   interfaceAssignment: InterfaceAssignment) {

        val operations = interfaceAssignment.operations
        operations?.let {
            it.forEach { operationAssignmentName, operationAssignments ->

                val operationDefinition = interfaceDefinition.operations?.get(operationAssignmentName)
                        ?: throw BluePrintException("Failed to get NodeTemplate($nodeTemplateName) operation definition ($operationAssignmentName)")

                log.info("Validation NodeTemplate({}) Interface({}) Operation ({})", nodeTemplateName,
                        interfaceAssignmentName, operationAssignmentName)

                val inputs = operationAssignments.inputs
                val outputs = operationAssignments.outputs

                inputs?.forEach { propertyName, propertyAssignment ->
                    val propertyDefinition = operationDefinition.inputs?.get(propertyName)
                            ?: throw BluePrintException("Failed to get NodeTemplate(nodeTemplateName) operation " +
                                    "definition (operationAssignmentName) property definition(propertyName)")
                    // Check the property values with property definition
                    validatePropertyAssignment(propertyName, propertyDefinition, propertyAssignment)
                }

                outputs?.forEach { propertyName, propertyAssignment ->
                    val propertyDefinition = operationDefinition.outputs?.get(propertyName)
                            ?: throw BluePrintException("Failed to get NodeTemplate($nodeTemplateName) operation definition ($operationAssignmentName) " +
                                    "output property definition($propertyName)")
                    // Check the property values with property definition
                    validatePropertyAssignment(propertyName, propertyDefinition, propertyAssignment)
                }

            }
        }

    }

    open fun checkValidArtifactType(artifactDefinitionName: String, artifactTypeName: String) {

        val artifactType = bluePrintContext!!.serviceTemplate.artifactTypes?.get(artifactTypeName)
                ?: throw BluePrintException("failed to artifactType($artifactTypeName) for ArtifactDefinition($artifactDefinitionName)")

        checkValidArtifactTypeDerivedFrom(artifactTypeName, artifactType.derivedFrom)
    }

    @Throws(BluePrintException::class)
    open fun checkValidArtifactTypeDerivedFrom(artifactTypeName: String, derivedFrom: String) {
        check(BluePrintTypes.validArtifactTypeDerivedFroms.contains(derivedFrom)) {
            throw BluePrintException("failed to get artifactType($artifactTypeName)'s derivedFrom($derivedFrom) definition")
        }
    }

    open fun checkPropertyValue(propertyName: String, propertyDefinition: PropertyDefinition, propertyAssignment: JsonNode) {
        val propertyType = propertyDefinition.type
        val isValid: Boolean

        if (BluePrintTypes.validPrimitiveTypes().contains(propertyType)) {
            isValid = JacksonUtils.checkJsonNodeValueOfPrimitiveType(propertyType, propertyAssignment)

        } else if (BluePrintTypes.validCollectionTypes().contains(propertyType)) {

            val entrySchemaType = propertyDefinition.entrySchema?.type
                    ?: throw BluePrintException(format("Failed to get EntrySchema type for the collection property ({})", propertyName))

            if (!BluePrintTypes.validPropertyTypes().contains(entrySchemaType)) {
                checkPropertyDataType(entrySchemaType, propertyName)
            }
            isValid = JacksonUtils.checkJsonNodeValueOfCollectionType(propertyType, propertyAssignment)
        } else {
            checkPropertyDataType(propertyType, propertyName)
            isValid = true
        }

        check(isValid) {
            throw BluePrintException("property(propertyName) defined of type(propertyType) is not comptable with the value (propertyAssignment)")
        }
    }

    private fun checkPropertyDataType(dataTypeName: String, propertyName: String) {

        val dataType = bluePrintContext!!.serviceTemplate.dataTypes?.get(dataTypeName)
                ?: throw BluePrintException("DataType ($dataTypeName) for the property ($propertyName) not found")

        checkValidDataTypeDerivedFrom(propertyName, dataType.derivedFrom)

    }

    private fun checkValidDataTypeDerivedFrom(dataTypeName: String, derivedFrom: String) {
        check(BluePrintTypes.validDataTypeDerivedFroms.contains(derivedFrom)) {
            throw BluePrintException("Failed to get DataType($dataTypeName)'s  derivedFrom($derivedFrom) definition ")
        }
    }

}