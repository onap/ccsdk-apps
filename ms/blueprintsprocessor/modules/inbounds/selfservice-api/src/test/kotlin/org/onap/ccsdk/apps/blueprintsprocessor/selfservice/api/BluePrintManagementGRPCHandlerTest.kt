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

package org.onap.ccsdk.apps.blueprintsprocessor.selfservice.api

import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.protobuf.ByteString
import io.grpc.testing.GrpcServerRule
import org.apache.commons.io.FileUtils
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.onap.ccsdk.apps.blueprintsprocessor.core.BluePrintCoreConfiguration
import org.onap.ccsdk.apps.controllerblueprints.management.api.BluePrintManagementServiceGrpc
import org.onap.ccsdk.apps.controllerblueprints.management.api.BluePrintUploadInput
import org.onap.ccsdk.apps.controllerblueprints.management.api.BluePrintUploadOutput
import org.onap.ccsdk.apps.controllerblueprints.management.api.CommonHeader
import org.onap.ccsdk.apps.controllerblueprints.management.api.FileChunk
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import java.io.File
import java.lang.Exception
import java.nio.file.Paths
import java.util.concurrent.Executors
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [GrpcServerConfig::class, GrpcManagementHandler::class, BluePrintCoreConfiguration::class])
@TestPropertySource(locations = ["classpath:application-test.properties"])
class BluePrintManagementGRPCHandlerTest {

    private val log = LoggerFactory.getLogger(BluePrintProcessingGRPCHandlerTest::class.java)!!

    @get:Rule
    val grpcServerRule = GrpcServerRule().directExecutor()

    @Autowired
    lateinit var bluePrintManagementGRPCHandler: GrpcManagementHandler

    @BeforeTest
    fun init() {
        // Create a server, add service, start, and register for automatic graceful shutdown.
        grpcServerRule.serviceRegistry.addService(bluePrintManagementGRPCHandler)
    }

    @AfterTest
    fun cleanDir() {
        FileUtils.deleteDirectory(File("./target/blueprints"))
    }

    @Test
    fun testFileUpload() {
        val stub = BluePrintManagementServiceGrpc.newFutureStub(grpcServerRule.channel)

        val file = Paths.get("./src/test/resources/test-cba.zip").toFile()
        assertTrue(file.exists(), "couldnt get file ${file.absolutePath}")

        val commonHeader = CommonHeader.newBuilder()
                .setTimestamp("2012-04-23T18:25:43.511Z")
                .setOriginatorId("System")
                .setRequestId("1234")
                .setSubRequestId("1234-56").build()

        val fileChunk = FileChunk.newBuilder().setChunk(ByteString.copyFrom(file.inputStream().readBytes()))
                .build()

        val input = BluePrintUploadInput.newBuilder()
                .setCommonHeader(commonHeader)
                .setBlueprintName("sample")
                .setBlueprintVersion("1.0.0")
                .setFileChunk(fileChunk)
                .build()

        Futures.addCallback(stub.uploadBlueprint(input), object : FutureCallback<BluePrintUploadOutput> {
            override fun onSuccess(s: BluePrintUploadOutput?) {
                assertNotNull(s, "failed to get upload response")
            }

            override fun onFailure(throwable: Throwable) {
                fail()
            }
        }, Executors.newSingleThreadExecutor())
    }
}