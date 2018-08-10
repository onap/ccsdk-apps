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

package org.onap.ccsdk.apps.controllerblueprints.core.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintTypes
import org.onap.ccsdk.apps.controllerblueprints.core.format
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.charset.Charset

/**
 *
 *
 * @author Brinda Santh
 */
object JacksonUtils {
    private val log = LoggerFactory.getLogger(JacksonUtils::class.java)

    inline fun <reified T : Any> readValue(content: String): T =
            jacksonObjectMapper().readValue(content, T::class.java)

    @JvmStatic
    fun <T> readValue(content: String, valueType: Class<T>): T? {
        return jacksonObjectMapper().readValue(content, valueType)
    }

    @JvmStatic
    fun jsonNodeFromObject(from: kotlin.Any): JsonNode = jacksonObjectMapper().convertValue(from, JsonNode::class.java)

    @JvmStatic
    fun jsonNodeFromClassPathFile(fileName: String): JsonNode {
        val content: String = IOUtils.toString(JacksonUtils::class.java.classLoader.getResourceAsStream(fileName), Charset.defaultCharset())
                ?: throw BluePrintException(String.format("Failed to read json file : %s", fileName))
        return jsonNode(content)
    }

    @JvmStatic
    fun jsonNodeFromFile(fileName: String): JsonNode {
        val content: String = FileUtils.readFileToString(File(fileName), Charset.defaultCharset())
                ?: throw BluePrintException(format("Failed to read json file : {}", fileName))
        return jsonNode(content)
    }

    @JvmStatic
    fun jsonNode(content: String): JsonNode {
        return jacksonObjectMapper().readTree(content)
    }

    @JvmStatic
    fun getJson(any: kotlin.Any): String {
        return getJson(any, false)
    }

    @JvmStatic
    fun getJson(any: kotlin.Any, pretty: Boolean = false): String {
        val objectMapper = jacksonObjectMapper()
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        if (pretty) {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
        }
        return objectMapper.writeValueAsString(any)
    }

    @JvmStatic
    fun <T> getListFromJson(content: String, valueType: Class<T>): List<T>? {
        val objectMapper = jacksonObjectMapper()
        val javaType = objectMapper.typeFactory.constructCollectionType(List::class.java, valueType)
        return objectMapper.readValue<List<T>>(content, javaType)
    }

    @JvmStatic
    fun <T> getMapFromJson(content: String, valueType: Class<T>): MutableMap<String, T>? {
        val objectMapper = jacksonObjectMapper()
        val typeRef = object : TypeReference<MutableMap<String, T>>() {}
        return objectMapper.readValue(content, typeRef)
    }

    @JvmStatic
    fun populatePrimitiveValues(key: String, value: Any, primitiveType: String, objectNode: ObjectNode) {
        if (org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants.DATA_TYPE_BOOLEAN == primitiveType) {
            objectNode.put(key, value as Boolean)
        } else if (org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants.DATA_TYPE_INTEGER == primitiveType) {
            objectNode.put(key, value as Int)
        } else if (org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants.DATA_TYPE_FLOAT == primitiveType) {
            objectNode.put(key, value as Float)
        } else if (org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants.DATA_TYPE_TIMESTAMP == primitiveType) {
            objectNode.put(key, value as String)
        } else {
            objectNode.put(key, value as String)
        }
    }

    @JvmStatic
    fun populatePrimitiveValues(value: Any, primitiveType: String, objectNode: ArrayNode) {
        if (org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants.DATA_TYPE_BOOLEAN == primitiveType) {
            objectNode.add(value as Boolean)
        } else if (org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants.DATA_TYPE_INTEGER == primitiveType) {
            objectNode.add(value as Int)
        } else if (org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants.DATA_TYPE_FLOAT == primitiveType) {
            objectNode.add(value as Float)
        } else if (org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants.DATA_TYPE_TIMESTAMP == primitiveType) {
            objectNode.add(value as String)
        } else {
            objectNode.add(value as String)
        }
    }

    @JvmStatic
    fun populatePrimitiveDefaultValues(key: String, primitiveType: String, objectNode: ObjectNode) {
        if (org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants.DATA_TYPE_BOOLEAN == primitiveType) {
            objectNode.put(key, false)
        } else if (org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants.DATA_TYPE_INTEGER == primitiveType) {
            objectNode.put(key, 0)
        } else if (org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants.DATA_TYPE_FLOAT == primitiveType) {
            objectNode.put(key, 0.0)
        } else {
            objectNode.put(key, "")
        }
    }

    @JvmStatic
    fun populatePrimitiveDefaultValuesForArrayNode(primitiveType: String, arrayNode: ArrayNode) {
        if (org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants.DATA_TYPE_BOOLEAN == primitiveType) {
            arrayNode.add(false)
        } else if (org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants.DATA_TYPE_INTEGER == primitiveType) {
            arrayNode.add(0)
        } else if (org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants.DATA_TYPE_FLOAT == primitiveType) {
            arrayNode.add(0.0)
        } else {
            arrayNode.add("")
        }
    }

    @JvmStatic
    fun populateJsonNodeValues(key: String, nodeValue: JsonNode?, type: String, objectNode: ObjectNode) {
        if (nodeValue == null || nodeValue is NullNode) {
            objectNode.set(key, nodeValue)
        } else if (BluePrintTypes.validPrimitiveTypes().contains(type)) {
            if (org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants.DATA_TYPE_BOOLEAN == type) {
                objectNode.put(key, nodeValue.asBoolean())
            } else if (org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants.DATA_TYPE_INTEGER == type) {
                objectNode.put(key, nodeValue.asInt())
            } else if (org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants.DATA_TYPE_FLOAT == type) {
                objectNode.put(key, nodeValue.floatValue())
            } else if (org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants.DATA_TYPE_TIMESTAMP == type) {
                objectNode.put(key, nodeValue.asText())
            } else {
                objectNode.put(key, nodeValue.asText())
            }
        } else {
            objectNode.set(key, nodeValue)
        }
    }
}