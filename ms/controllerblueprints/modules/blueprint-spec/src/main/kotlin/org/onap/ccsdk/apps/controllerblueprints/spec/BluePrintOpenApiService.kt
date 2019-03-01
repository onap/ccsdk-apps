/*
 *  Copyright © 2019 IBM.
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

package org.onap.ccsdk.apps.controllerblueprints.spec

import io.swagger.v3.oas.models.*
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.links.Link
import io.swagger.v3.oas.models.media.*
import io.swagger.v3.oas.models.parameters.QueryParameter
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintTypes
import org.onap.ccsdk.apps.controllerblueprints.core.data.PropertyDefinition
import org.onap.ccsdk.apps.controllerblueprints.core.interfaces.BluePrintSpecService
import org.onap.ccsdk.apps.controllerblueprints.core.service.BluePrintContext
import org.onap.ccsdk.apps.controllerblueprints.core.utils.BluePrintMetadataUtils
import org.onap.ccsdk.apps.controllerblueprints.core.utils.JacksonUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
open class BluePrintOpenApiService : BluePrintSpecService {

    private val log = LoggerFactory.getLogger(BluePrintOpenApiService::class.java)!!

    private lateinit var bluePrintContext: BluePrintContext

    override fun generateSpec(bluePrintBasePath: String, properties: MutableMap<String, Any>) {
        val bluePrintContext = BluePrintMetadataUtils.getBluePrintContext(bluePrintBasePath)
        return generateSpec(bluePrintContext, properties)
    }

    override fun generateSpec(bluePrintContext: BluePrintContext, properties: MutableMap<String, Any>) {
        this.bluePrintContext = bluePrintContext

        val oai = OpenAPI().apply {
            info = info()
            paths = paths()
            components = components()
        }
        log.trace("Spec : ${JacksonUtils.getJson(oai, true)}")
    }

    private fun info(): Info {
        return Info().apply {
            title = bluePrintContext.serviceTemplate.metadata!!.get(BluePrintConstants.METADATA_TEMPLATE_NAME)
            description = bluePrintContext.serviceTemplate.description
            version = bluePrintContext.serviceTemplate.metadata!!.get(BluePrintConstants.METADATA_TEMPLATE_VERSION)
            contact = Contact().apply {
                email = "cds@onap.com"
                name = bluePrintContext.serviceTemplate.metadata!!.get(BluePrintConstants.METADATA_TEMPLATE_AUTHOR)
                url = "http://onap.com"
            }
        }
    }

    private fun paths(): Paths {
        val paths = Paths()
        //TODO("FIX Workflow Action as Paths")
        paths.addPathItem("/process", PathItem()
                .description("Self Service Processing API")
                .get(Operation()
                        .addParametersItem(QueryParameter()
                                .description("Records to skip")
                                .required(false)
                                .schema(IntegerSchema()
                                ))
                        .responses(ApiResponses()
                                .addApiResponse("200", ApiResponse()
                                        .description("it worked")
                                        .content(Content()
                                                .addMediaType("application/json",
                                                        MediaType().schema(Schema<Any>()
                                                                .`$ref`("#/components/schemas/WORKFLOWREQUEST")))
                                        )
                                        .link("link", Link()
                                                .operationId("process")))
                        )
                )
        )
        return paths
    }

    private fun components(): Components {
        val components = Components()
        components.schemas = schemas()
        return components
    }

    private fun schemas(): MutableMap<String, Schema<*>> {
        val schemas: MutableMap<String, Schema<*>> = hashMapOf()
        bluePrintContext.serviceTemplate.dataTypes?.forEach { dataTypeName, dataType ->
            val schema = Schema<Any>().apply {
                description = dataType.description
            }
            dataType.properties?.forEach { propertyName, property ->
                val propertySchema = getPropertySchema(propertyName, property)
                schema.addProperties(propertyName, propertySchema)
            }

            schemas[dataTypeName] = schema
        }
        return schemas
    }

    private fun prepareWorkflowPropertySchema() {
        TODO()
    }

    private fun getPropertySchema(name: String, propertyDefinition: PropertyDefinition): Schema<*> {
        var defProperty: Schema<*>? = null

        if (BluePrintTypes.validPrimitiveTypes().contains(propertyDefinition.type)) {
            defProperty = propertySchemaForPrimitiveType(name, propertyDefinition.type, propertyDefinition)
        } else if (BluePrintTypes.validCollectionTypes().contains(propertyDefinition.type)) {
            defProperty = ArraySchema()
            if (propertyDefinition.entrySchema != null) {
                val entrySchemaType = propertyDefinition.entrySchema!!.type
                if (!BluePrintTypes.validPrimitiveTypes().contains(entrySchemaType)) {
                    val innerPropertySchema = Schema<Any>()
                    innerPropertySchema.`$ref`("#/definitions/$entrySchemaType")
                    defProperty.setItems(innerPropertySchema)
                } else {
                    val innerPropertySchema = propertySchemaForPrimitiveType(name, entrySchemaType, propertyDefinition)
                    defProperty.setItems(innerPropertySchema)
                }
            } else {
                //TODO("JSON or Map Type")
            }
        } else {
            defProperty = Schema<Any>()
            defProperty.`$ref`("#/definitions/" + propertyDefinition.type)
        }
        defProperty.name = name
        defProperty.setDescription(propertyDefinition.description)
        // TODO ("Default Values")
        // TODO (" Valid Values")

        return defProperty
    }

    private fun propertySchemaForPrimitiveType(name: String, type: String, propertyDefinition: PropertyDefinition):
            Schema<*> {
        var defProperty: Schema<*>? = null
        when (type) {
            BluePrintConstants.DATA_TYPE_BOOLEAN -> {
                defProperty = BooleanSchema()
            }
            BluePrintConstants.DATA_TYPE_INTEGER -> {
                val integerSchema = IntegerSchema()
                defProperty = integerSchema
            }
            BluePrintConstants.DATA_TYPE_FLOAT -> {
                val numberSchema = NumberSchema()
                defProperty = numberSchema
            }
            BluePrintConstants.DATA_TYPE_TIMESTAMP -> {
                val dateTimeProperty = DateTimeSchema()
                dateTimeProperty.format = "date-time"
                defProperty = dateTimeProperty
            }
            else -> {
                defProperty = StringSchema()
            }
        }
        return defProperty
    }
}