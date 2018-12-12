/*
 *  Copyright © 2018 IBM.
 *  Modifications Copyright © 2017-2018 AT&T Intellectual Property.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.onap.ccsdk.apps.controllerblueprints.resource.dict.service

import com.att.eelf.configuration.EELFLogger
import com.att.eelf.configuration.EELFManager
import com.fasterxml.jackson.databind.JsonNode
import com.google.common.base.Preconditions
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintTypes
import org.onap.ccsdk.apps.controllerblueprints.core.data.NodeTemplate
import org.onap.ccsdk.apps.controllerblueprints.core.data.NodeType
import org.onap.ccsdk.apps.controllerblueprints.core.data.PropertyDefinition
import org.onap.ccsdk.apps.controllerblueprints.core.format
import org.onap.ccsdk.apps.controllerblueprints.core.service.BluePrintExpressionService
import org.onap.ccsdk.apps.controllerblueprints.core.service.BluePrintRepoService
import org.onap.ccsdk.apps.controllerblueprints.core.utils.JacksonUtils
import org.onap.ccsdk.apps.controllerblueprints.resource.dict.ResourceDefinition
import java.io.Serializable

/**
 * ResourceDefinitionValidationService.
 *
 * @author Brinda Santh
 */
interface ResourceDefinitionValidationService : Serializable {

    @Throws(BluePrintException::class)
    fun validate(resourceDefinition: ResourceDefinition)

}

/**
 * ResourceDefinitionValidationService.
 *
 * @author Brinda Santh
 */
open class ResourceDefinitionDefaultValidationService(private val bluePrintRepoService: BluePrintRepoService) : ResourceDefinitionValidationService {

    private val log: EELFLogger = EELFManager.getInstance().getLogger(ResourceDefinitionValidationService::class.java)

    override fun validate(resourceDefinition: ResourceDefinition) {
        Preconditions.checkNotNull(resourceDefinition, "Failed to get Resource Definition")
        log.trace("Validating Resource Dictionary Definition {}", resourceDefinition.name)

        resourceDefinition.sources.forEach { (name, nodeTemplate) ->
            val sourceType = nodeTemplate.type

            val sourceNodeType = bluePrintRepoService.getNodeType(sourceType)

            // Validate Property Name, expression, values and Data Type
            validateNodeTemplateProperties(nodeTemplate, sourceNodeType)
        }
    }


    open fun validateNodeTemplateProperties(nodeTemplate: NodeTemplate, nodeType: NodeType) {
        nodeTemplate.properties?.let { validatePropertyAssignments(nodeType.properties!!, nodeTemplate.properties!!) }
    }


    open fun validatePropertyAssignments(nodeTypeProperties: MutableMap<String, PropertyDefinition>,
                                         properties: MutableMap<String, JsonNode>) {
        properties.forEach { propertyName, propertyAssignment ->
            val propertyDefinition: PropertyDefinition = nodeTypeProperties[propertyName]
                    ?: throw BluePrintException(format("failed to get definition for the property ({})", propertyName))
            // Check and Validate if Expression Node
            val expressionData = BluePrintExpressionService.getExpressionData(propertyAssignment)
            if (!expressionData.isExpression) {
                checkPropertyValue(propertyDefinition, propertyName, propertyAssignment)
            } else {
                throw BluePrintException(format("property({}) of expression ({}) is not supported",
                        propertyName, propertyAssignment))
            }
        }
    }

    open fun checkPropertyValue(propertyDefinition: PropertyDefinition, propertyName: String, propertyAssignment: JsonNode) {
        val propertyType = propertyDefinition.type
        val isValid: Boolean

        if (BluePrintTypes.validPrimitiveTypes().contains(propertyType)) {
            isValid = JacksonUtils.checkJsonNodeValueOfPrimitiveType(propertyType, propertyAssignment)

        } else if (BluePrintTypes.validCollectionTypes().contains(propertyType)) {

            isValid = JacksonUtils.checkJsonNodeValueOfCollectionType(propertyType, propertyAssignment)
        } else {
            bluePrintRepoService.getDataType(propertyType)
            isValid = true
        }

        check(isValid) {
            throw BluePrintException(format("property({}) defined of type({}) is not compatable with the value ({})",
                    propertyName, propertyType, propertyAssignment))
        }
    }
}