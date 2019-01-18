package org.onap.ccsdk.apps.blueprintsprocessor.selfservice.api.org.onap.ccsdk.apps.blueprintprocessor.selfservice.api.utils

import com.google.common.collect.Maps
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import java.text.SimpleDateFormat
import com.google.protobuf.Struct
import com.google.protobuf.Value
import org.onap.ccsdk.apps.controllerblueprints.processing.api.*
import java.util.*

@RunWith(SpringRunner::class)
class BluePrintMappingsTest {

    @Test
    fun translateProcessingFlagsFromGRPCTest() {
        val flag: Flag = Flag.newBuilder().setIsForce(false).setTtl(1).build()
        //  Function to test
        val pojoFlag: org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.Flags = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.Flags().translateProcessingFlagsFromGRPC(flag)
        Assert.assertNotNull(pojoFlag)
        Assert.assertTrue(false == pojoFlag.isForce)
        Assert.assertTrue(1 == pojoFlag.ttl)
    }

    @Test
    fun translateProcessingStatusFromGRPCTest() {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val dateString = "2019-01-16T18:25:43.511Z"
        val dateForTest: Date = formatter.parse(dateString)

        val status: Status = Status.newBuilder().setCode(400).setErrorMessage("Concurrent modification exception").setEventType("Update").setMessage("Error uploading data").setTimestamp(dateString).build()
        //  Function to test
        val pojoStatus: org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.Status = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.Status().translateProcessingStatusFromGRPC(status)

        Assert.assertNotNull(pojoStatus)
        Assert.assertTrue(400 == pojoStatus.code)
        Assert.assertEquals("Concurrent modification exception", pojoStatus.errorMessage)
        Assert.assertEquals("Update", pojoStatus.eventType)
        Assert.assertEquals("Error uploading data", pojoStatus.message)
        Assert.assertTrue(pojoStatus.timestamp.equals(dateForTest))
    }

    @Test
    fun translateManagementStatusFromGRPCTest() {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val dateString = "2019-01-16T18:25:43.511Z"
        val dateForTest: Date = formatter.parse(dateString)

        val status: org.onap.ccsdk.apps.controllerblueprints.management.api.Status = org.onap.ccsdk.apps.controllerblueprints.management.api.Status.newBuilder().setCode(500).setErrorMessage("Concurrent management modification exception").setMessage("Error management uploading data").setTimestamp(dateString).build()
        //  Function to test
        val pojoStatus: org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.Status = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.Status().translateManagementStatusFromGRPC(status)

        Assert.assertNotNull(pojoStatus)
        Assert.assertTrue(500 == pojoStatus.code)
        Assert.assertEquals("Concurrent management modification exception", pojoStatus.errorMessage)
        Assert.assertEquals("Error management uploading data", pojoStatus.message)
        Assert.assertTrue(pojoStatus.timestamp.equals(dateForTest))
    }

    @Test
    fun translateProcessingCommonHeaderFromGRPCTest() {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val dateString = "2019-01-16T18:25:43.511Z"
        val dateForTest: Date = formatter.parse(dateString)
        val flag: Flag = Flag.newBuilder().setIsForce(true).setTtl(2).build()

        val commonHeader: CommonHeader = CommonHeader.newBuilder().setOriginatorId("Origin").setRequestId("requestID").setSubRequestId("subRequestID").setTimestamp(dateString).setFlag(flag).build()
        //  Function to test
        val pojoCommonHeader: org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.CommonHeader = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.CommonHeader().translateProcessingCommonHeaderFromGRPC(commonHeader)

        Assert.assertNotNull(pojoCommonHeader)
        Assert.assertEquals("Origin", pojoCommonHeader.originatorId)
        Assert.assertEquals("requestID", pojoCommonHeader.requestId)
        Assert.assertEquals("subRequestID", pojoCommonHeader.subRequestId)
        Assert.assertTrue(pojoCommonHeader.timestamp.equals(dateForTest))
        Assert.assertNotNull(pojoCommonHeader.flags)
        Assert.assertTrue(pojoCommonHeader.flags!!.isForce)
        Assert.assertTrue(2 == pojoCommonHeader.flags!!.ttl)

    }

    @Test
    fun translateManagementCommonHeaderFromGRPCTest() {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val dateString = "2019-01-18T18:25:43.511Z"
        val dateForTest: Date = formatter.parse(dateString)

        val commonHeader: org.onap.ccsdk.apps.controllerblueprints.management.api.CommonHeader = org.onap.ccsdk.apps.controllerblueprints.management.api.CommonHeader.newBuilder().setOriginatorId("Management Origin").setRequestId("requestIDManagement").setSubRequestId("subRequestIDManagement").setTimestamp(dateString).build()
        //  Function to test
        val pojoCommonHeader: org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.CommonHeader = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.CommonHeader().translateManagementCommonHeaderFromGRPC(commonHeader)

        Assert.assertNotNull(pojoCommonHeader)
        Assert.assertEquals("Management Origin", pojoCommonHeader.originatorId)
        Assert.assertEquals("requestIDManagement", pojoCommonHeader.requestId)
        Assert.assertEquals("subRequestIDManagement", pojoCommonHeader.subRequestId)
        Assert.assertTrue(pojoCommonHeader.timestamp.equals(dateForTest))

    }

    @Test
    fun translateProcessingActionIdentifiersFromGRPCTest(){
        val actionIdentifiers : ActionIdentifiers = ActionIdentifiers.newBuilder().setActionName("Process Action").setBlueprintName("BlueprintName").setBlueprintVersion("3.0").setMode("Execution").build()
        //  Function to test
        val pojoActionIdentifiers:  org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ActionIdentifiers = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ActionIdentifiers().translateProcessingActionIdentifiersFromGRPC(actionIdentifiers)

        Assert.assertNotNull(pojoActionIdentifiers)
        Assert.assertEquals("Process Action", pojoActionIdentifiers.actionName)
        Assert.assertEquals("BlueprintName", pojoActionIdentifiers.blueprintName)
        Assert.assertEquals("3.0", pojoActionIdentifiers.blueprintVersion)
        Assert.assertEquals("Execution", pojoActionIdentifiers.mode)

    }

    @Test
    fun translateManagementActionIdentifiersFromGRPCTest(){
        val actionIdentifiers : org.onap.ccsdk.apps.controllerblueprints.management.api.ActionIdentifiers = org.onap.ccsdk.apps.controllerblueprints.management.api.ActionIdentifiers.newBuilder().setActionName("Process Management Action").setBlueprintName("Management BlueprintName").setBlueprintVersion("3.0").setMode("Execution").build()
        //  Function to test
        val pojoActionIdentifiers:  org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ActionIdentifiers = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ActionIdentifiers().translateManagementActionIdentifiersFromGRPC(actionIdentifiers)

        Assert.assertNotNull(pojoActionIdentifiers)
        Assert.assertEquals("Process Management Action", pojoActionIdentifiers.actionName)
        Assert.assertEquals("Management BlueprintName", pojoActionIdentifiers.blueprintName)
        Assert.assertEquals("3.0", pojoActionIdentifiers.blueprintVersion)
        Assert.assertEquals("Execution", pojoActionIdentifiers.mode)
    }

    @Test
    fun translateProcessingExecutionServiceOutputFromGRPC(){
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val dateString = "2019-01-16T18:25:43.511Z"
        val dateForTest: Date = formatter.parse(dateString)
        val flag: Flag = Flag.newBuilder().setIsForce(true).setTtl(2).build()

        val actionIdentifiers : ActionIdentifiers = ActionIdentifiers.newBuilder().setActionName("Process Action").setBlueprintName("BlueprintName").setBlueprintVersion("3.0").setMode("Execution").build()
        val commonHeader: CommonHeader = CommonHeader.newBuilder().setOriginatorId("Origin").setRequestId("requestID").setSubRequestId("subRequestID").setTimestamp(dateString).setFlag(flag).build()
        val status: Status = Status.newBuilder().setCode(400).setErrorMessage("Concurrent modification exception").setEventType("Update").setMessage("Error uploading data").setTimestamp(dateString).build()

        val vb = Value.newBuilder()
        val values = Maps.newHashMap<String, Value>()

        values.put("http_response_code", vb.setStringValue("200").build())
        values.put("error_code", vb.setStringValue("ErrorCode").build())
        values.put("timestamp", vb.setNumberValue(Date().time.toDouble()).build())

        val payload: Struct = Struct.newBuilder().putAllFields(values).build()

        val executionServiceOutput: ExecutionServiceOutput = ExecutionServiceOutput.newBuilder().setActionIdentifiers(actionIdentifiers).setCommonHeader(commonHeader).setStatus(status).setPayload(payload).build()
        //  Function to test
        val pojoExecutionServiceOutput: org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ExecutionServiceOutput = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ExecutionServiceOutput().translateProcessingExecutionServiceOutputFromGRPC(executionServiceOutput)

        Assert.assertNotNull(pojoExecutionServiceOutput)
        Assert.assertNotNull(pojoExecutionServiceOutput.actionIdentifiers)
        Assert.assertTrue("Process Action".equals(pojoExecutionServiceOutput.actionIdentifiers!!.actionName))
        Assert.assertTrue("BlueprintName".equals(pojoExecutionServiceOutput.actionIdentifiers!!.blueprintName))
        Assert.assertTrue("3.0".equals(pojoExecutionServiceOutput.actionIdentifiers!!.blueprintVersion))
        Assert.assertTrue("Execution".equals(pojoExecutionServiceOutput.actionIdentifiers!!.mode))
        Assert.assertNotNull(pojoExecutionServiceOutput.commonHeader)
        Assert.assertTrue("Origin".equals(pojoExecutionServiceOutput.commonHeader!!.originatorId))
        Assert.assertTrue("requestID".equals(pojoExecutionServiceOutput.commonHeader!!.requestId))
        Assert.assertTrue("subRequestID".equals(pojoExecutionServiceOutput.commonHeader!!.subRequestId))
        Assert.assertTrue(pojoExecutionServiceOutput.commonHeader!!.timestamp.equals(dateForTest))
        Assert.assertNotNull(pojoExecutionServiceOutput.commonHeader!!.flags)
        Assert.assertTrue(pojoExecutionServiceOutput.commonHeader!!.flags!!.isForce)
        Assert.assertTrue(pojoExecutionServiceOutput.commonHeader!!.flags!!.ttl == 2)
        Assert.assertNotNull(pojoExecutionServiceOutput.status)
        Assert.assertTrue(pojoExecutionServiceOutput.status!!.code == 400)
        Assert.assertTrue("Concurrent modification exception".equals(pojoExecutionServiceOutput.status!!.errorMessage))
        Assert.assertTrue("Update".equals(pojoExecutionServiceOutput.status!!.eventType))
        Assert.assertTrue("Error uploading data".equals(pojoExecutionServiceOutput.status!!.message))
        Assert.assertTrue(pojoExecutionServiceOutput.status!!.timestamp.equals(dateForTest))
        Assert.assertNotNull(pojoExecutionServiceOutput.payload)
        Assert.assertTrue(pojoExecutionServiceOutput.payload!!.has("timestamp"))
        Assert.assertTrue("\"200\"".equals(pojoExecutionServiceOutput.payload!!.get("http_response_code").toString()))
        Assert.assertTrue("\"ErrorCode\"".equals(pojoExecutionServiceOutput.payload!!.get("error_code").toString()))
        Assert.assertFalse(pojoExecutionServiceOutput.payload!!.has("vnf"))
    }

    @Test
    fun translateProcessingExecutionServiceInputFromGRPCTest(){

        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val dateString = "2019-01-16T18:25:43.511Z"
        val dateForTest: Date = formatter.parse(dateString)
        val flag: Flag = Flag.newBuilder().setIsForce(true).setTtl(2).build()

        val actionIdentifiers : ActionIdentifiers = ActionIdentifiers.newBuilder().setActionName("Process Action").setBlueprintName("BlueprintName").setBlueprintVersion("3.0").setMode("Execution").build()
        val commonHeader: CommonHeader = CommonHeader.newBuilder().setOriginatorId("Origin").setRequestId("requestID").setSubRequestId("subRequestID").setTimestamp(dateString).setFlag(flag).build()

        val vb = Value.newBuilder()
        val values = Maps.newHashMap<String, Value>()

        values.put("http_response_code", vb.setStringValue("200").build())
        values.put("error_code", vb.setStringValue("ErrorCode").build())
        values.put("timestamp", vb.setNumberValue(Date().time.toDouble()).build())

        val payload: Struct = Struct.newBuilder().putAllFields(values).build()

        val executionServiceInput: ExecutionServiceInput = ExecutionServiceInput.newBuilder().setActionIdentifiers(actionIdentifiers).setCommonHeader(commonHeader).setPayload(payload).build()
        //  Function to test
        val pojoExecutionServiceInput: org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ExecutionServiceInput = org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ExecutionServiceInput().translateProcessingExecutionServiceInputFromGRPC(executionServiceInput)

        Assert.assertNotNull(pojoExecutionServiceInput)
        Assert.assertNotNull(pojoExecutionServiceInput.actionIdentifiers)
        Assert.assertTrue("Process Action".equals(pojoExecutionServiceInput.actionIdentifiers!!.actionName))
        Assert.assertTrue("BlueprintName".equals(pojoExecutionServiceInput.actionIdentifiers!!.blueprintName))
        Assert.assertTrue("3.0".equals(pojoExecutionServiceInput.actionIdentifiers!!.blueprintVersion))
        Assert.assertTrue("Execution".equals(pojoExecutionServiceInput.actionIdentifiers!!.mode))
        Assert.assertNotNull(pojoExecutionServiceInput.commonHeader)
        Assert.assertTrue("Origin".equals(pojoExecutionServiceInput.commonHeader!!.originatorId))
        Assert.assertTrue("requestID".equals(pojoExecutionServiceInput.commonHeader!!.requestId))
        Assert.assertTrue("subRequestID".equals(pojoExecutionServiceInput.commonHeader!!.subRequestId))
        Assert.assertTrue(pojoExecutionServiceInput.commonHeader!!.timestamp.equals(dateForTest))
        Assert.assertNotNull(pojoExecutionServiceInput.commonHeader!!.flags)
        Assert.assertTrue(pojoExecutionServiceInput.commonHeader!!.flags!!.isForce)
        Assert.assertTrue(pojoExecutionServiceInput.commonHeader!!.flags!!.ttl == 2)
        Assert.assertNotNull(pojoExecutionServiceInput.payload)
        Assert.assertTrue(pojoExecutionServiceInput.payload!!.has("timestamp"))
        Assert.assertTrue("\"200\"".equals(pojoExecutionServiceInput.payload!!.get("http_response_code").toString()))
        Assert.assertTrue("\"ErrorCode\"".equals(pojoExecutionServiceInput.payload!!.get("error_code").toString()))
        Assert.assertFalse(pojoExecutionServiceInput.payload!!.has("vnf"))
    }


}