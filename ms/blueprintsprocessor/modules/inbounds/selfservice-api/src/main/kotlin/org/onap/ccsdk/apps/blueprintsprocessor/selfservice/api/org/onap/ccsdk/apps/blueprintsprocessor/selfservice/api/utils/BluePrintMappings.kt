package org.onap.ccsdk.apps.blueprintsprocessor.selfservice.api.org.onap.ccsdk.apps.blueprintprocessor.selfservice.api.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import org.onap.ccsdk.apps.controllerblueprints.management.api.BluePrintRemoveOutput
import org.onap.ccsdk.apps.controllerblueprints.management.api.BluePrintRemoveInput
import org.onap.ccsdk.apps.controllerblueprints.management.api.BluePrintUploadOutput
import org.onap.ccsdk.apps.controllerblueprints.management.api.BluePrintUploadInput
import org.onap.ccsdk.apps.controllerblueprints.management.api.FileChunk
import org.onap.ccsdk.apps.controllerblueprints.processing.api.*
import java.text.SimpleDateFormat


fun org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ExecutionServiceInput.translateProcessingExecutionServiceInputFromGRPC(input: ExecutionServiceInput): org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ExecutionServiceInput {
    var executionServiceInput = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ExecutionServiceInput()

    val fieldsMapPayload: Map<String, com.google.protobuf.Value> = input.payload.fieldsMap
    var mapper = ObjectMapper()
    val template = "\"%s\":\"%s\","
    val sb = StringBuilder()
    sb.append("{")
    for (e in fieldsMapPayload.entries) {
        val args = e.value
        var value = when (args.kindCase.name) {
            "BOOL_VALUE" ->  args.boolValue
            "KIND_NOT_SET" -> args.unknownFields
            "LIST_VALUE" -> args.listValue
            "NULL_VALUE" -> args.nullValue
            "NUMBER_VALUE" -> args.numberValue
            "STRING_VALUE" -> args.stringValue
            "STRUCT_VALUE" -> args.structValue
            else -> args.nullValueValue
        }
        sb.append(String.format(template, e.key, value))
    }
    if (sb.length > 0) {
        sb.deleteCharAt(sb.length - 1)
    }
    sb.append("}")

    val fieldMapPayLoadConverted = sb.toString()
    val jsonNode: JsonNode = mapper.readTree(fieldMapPayLoadConverted)

    var fieldMap: MutableMap<String, JsonNode> = mutableMapOf<String, JsonNode>()

    for ((key) in fieldsMapPayload) {
        fieldMap.put(key, jsonNode.get(key))
    }

    executionServiceInput.actionIdentifiers = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ActionIdentifiers().translateProcessingActionIdentifiersFromGRPC(input.actionIdentifiers)
    executionServiceInput.commonHeader = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.CommonHeader().translateProcessingCommonHeaderFromGRPC(input.commonHeader)
    executionServiceInput.payload = com.fasterxml.jackson.databind.node.ObjectNode(JsonNodeFactory(false), fieldMap)
    return executionServiceInput
}

fun org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ExecutionServiceOutput.translateProcessingExecutionServiceOutputFromGRPC(input: ExecutionServiceOutput): org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ExecutionServiceOutput {
    var executionServiceOutput = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ExecutionServiceOutput()

    val fieldsMapPayload: Map<String, com.google.protobuf.Value> = input.payload.fieldsMap
    val mapper = ObjectMapper()

    val template = "\"%s\":\"%s\","
    val sb = StringBuilder()
    sb.append("{")
    for (e in fieldsMapPayload.entries) {
        val args = e.value
        var value = when (args.kindCase.name) {
            "BOOL_VALUE" ->  args.boolValue
            "KIND_NOT_SET" -> args.unknownFields
            "LIST_VALUE" -> args.listValue
            "NULL_VALUE" -> args.nullValue
            "NUMBER_VALUE" -> args.numberValue
            "STRING_VALUE" -> args.stringValue
            "STRUCT_VALUE" -> args.structValue
            else -> args.nullValueValue
        }
        sb.append(String.format(template, e.key, value))
    }
    if (sb.length > 0) {
        sb.deleteCharAt(sb.length - 1)
    }
    sb.append("}")

    val fieldMapPayLoadConverted = sb.toString()
    val jsonNode: JsonNode = mapper.readTree(fieldMapPayLoadConverted)
    var fieldMap: MutableMap<String, JsonNode> = mutableMapOf<String, JsonNode>()


    for ((key) in fieldsMapPayload) {
        fieldMap.put(key, jsonNode.get(key))
    }

    executionServiceOutput.actionIdentifiers = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ActionIdentifiers().translateProcessingActionIdentifiersFromGRPC(input.actionIdentifiers)
    executionServiceOutput.commonHeader = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.CommonHeader().translateProcessingCommonHeaderFromGRPC(input.commonHeader)
    executionServiceOutput.status = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.Status().translateProcessingStatusFromGRPC(input.status)
    executionServiceOutput.payload = com.fasterxml.jackson.databind.node.ObjectNode(JsonNodeFactory(false), fieldMap)

    return executionServiceOutput
}

fun org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ActionIdentifiers.translateProcessingActionIdentifiersFromGRPC(input: ActionIdentifiers): org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ActionIdentifiers {
    var actionIdentifier = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ActionIdentifiers()
    actionIdentifier.actionName = input.actionName
    actionIdentifier.blueprintName = input.blueprintName
    actionIdentifier.blueprintVersion = input.blueprintVersion
    actionIdentifier.mode = input.mode
    return actionIdentifier
}

fun org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ActionIdentifiers.translateManagementActionIdentifiersFromGRPC(input: org.onap.ccsdk.apps.controllerblueprints.management.api.ActionIdentifiers): org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ActionIdentifiers {
    var actionIdentifier = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ActionIdentifiers()
    actionIdentifier.actionName = input.actionName
    actionIdentifier.blueprintName = input.blueprintName
    actionIdentifier.blueprintVersion = input.blueprintVersion
    actionIdentifier.mode = input.mode
    return actionIdentifier
}


fun org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.CommonHeader.translateProcessingCommonHeaderFromGRPC(input: CommonHeader): org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.CommonHeader {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    var commonHeader = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.CommonHeader()
    commonHeader.originatorId = input.originatorId
    commonHeader.requestId = input.requestId
    commonHeader.subRequestId = input.subRequestId
    commonHeader.timestamp = formatter.parse(input.timestamp)
    commonHeader.flags = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.Flags().translateProcessingFlagsFromGRPC(input.flag)
    return commonHeader
}

fun org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.CommonHeader.translateManagementCommonHeaderFromGRPC(input: org.onap.ccsdk.apps.controllerblueprints.management.api.CommonHeader): org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.CommonHeader {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    var commonHeader = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.CommonHeader()
    commonHeader.originatorId = input.originatorId
    commonHeader.requestId = input.requestId
    commonHeader.subRequestId = input.subRequestId
    commonHeader.timestamp = formatter.parse(input.timestamp)
    return commonHeader
}

fun org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.Flags.translateProcessingFlagsFromGRPC(input: Flag): org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.Flags {
    var flag = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.Flags()
    flag.isForce = input.isForce
    flag.ttl = input.ttl
    return flag
}


fun org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.Status.translateProcessingStatusFromGRPC(input: Status): org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.Status {
    var status = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.Status()
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    status.code = input.code
    status.errorMessage = input.errorMessage
    status.eventType = input.eventType
    status.message = input.message
    status.timestamp = formatter.parse(input.timestamp)
    return status
}

fun org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.Status.translateManagementStatusFromGRPC(input: org.onap.ccsdk.apps.controllerblueprints.management.api.Status): org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.Status {
    var status = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.Status()
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    status.code = input.code
    status.errorMessage = input.errorMessage
    status.message = input.message
    status.timestamp = formatter.parse(input.timestamp)
    return status
}

fun org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.BluePrintRemoveOutput.translateManagementBluePrintRemoveOutputFromGRPC(input: BluePrintRemoveOutput): org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.BluePrintRemoveOutput {
    var bluePrintRemoveOutput = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.BluePrintRemoveOutput()
    bluePrintRemoveOutput.commonHeader = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.CommonHeader().translateManagementCommonHeaderFromGRPC(input.commonHeader)
    bluePrintRemoveOutput.status = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.Status().translateManagementStatusFromGRPC(input.status)
    return bluePrintRemoveOutput
}

fun org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.BluePrintRemoveInput.translateManagementBluePrintRemoveInputFromGRPC(input: BluePrintRemoveInput): org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.BluePrintRemoveInput {
    var bluePrintRemoveInput = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.BluePrintRemoveInput()
    bluePrintRemoveInput.blueprintName = input.blueprintName
    bluePrintRemoveInput.blueprintVersion = input.blueprintVersion
    bluePrintRemoveInput.commonHeader = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.CommonHeader().translateManagementCommonHeaderFromGRPC(input.commonHeader)
    return bluePrintRemoveInput
}

fun org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.BluePrintUploadOutput.translateManagementBluePrintUploadOutputFromGRPC(input: BluePrintUploadOutput): org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.BluePrintUploadOutput {
    var bluePrintUploadOutput = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.BluePrintUploadOutput()
    bluePrintUploadOutput.commonHeader = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.CommonHeader().translateManagementCommonHeaderFromGRPC(input.commonHeader)
    bluePrintUploadOutput.status = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.Status().translateManagementStatusFromGRPC(input.status)
    return bluePrintUploadOutput
}

fun org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.FileChunk.translateManagementFileChunkFromGRPC(input: FileChunk): org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.FileChunk {
    var fileChunk = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.FileChunk()
    fileChunk.chunk = input.chunk.toByteArray()
    return fileChunk
}

fun org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.BluePrintUploadInput.translateManagementBluePrintUploadInputFromGRPC(input: BluePrintUploadInput): org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.BluePrintUploadInput {
    var bluePrintUploadInput = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.BluePrintUploadInput()
    bluePrintUploadInput.blueprintName = input.blueprintName
    bluePrintUploadInput.blueprintVersion = input.blueprintVersion
    bluePrintUploadInput.commonHeader = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.CommonHeader().translateManagementCommonHeaderFromGRPC(input.commonHeader)
    bluePrintUploadInput.fileChunk = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.FileChunk().translateManagementFileChunkFromGRPC(input.fileChunk)
    return bluePrintUploadInput
}
