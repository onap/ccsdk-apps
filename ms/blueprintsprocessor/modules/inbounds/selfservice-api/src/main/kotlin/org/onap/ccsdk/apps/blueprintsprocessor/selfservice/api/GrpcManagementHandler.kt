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

import io.grpc.stub.StreamObserver
import org.apache.commons.io.FileUtils
import org.onap.ccsdk.apps.blueprintsprocessor.core.BluePrintCoreConfiguration
import org.onap.ccsdk.apps.controllerblueprints.management.api.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.io.File
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.function.Supplier


@Service
class GrpcManagementHandler(private val bluePrintCoreConfiguration: BluePrintCoreConfiguration)
    : BluePrintManagementServiceGrpc.BluePrintManagementServiceImplBase() {

    private val log = LoggerFactory.getLogger(GrpcManagementHandler::class.java)

    @Autowired
    @Qualifier("grpcExecutor")
    lateinit var executor: ExecutorService

    override fun uploadBlueprint(request: BluePrintUploadInput, responseObserver: StreamObserver<BluePrintUploadOutput>) {

        val future = CompletableFuture.supplyAsync(Supplier { doUploadBlueprint(request) }, executor)

        future.whenComplete { result, e ->
            if (e == null) {
                responseObserver.onNext(result)
                responseObserver.onCompleted()
            } else {
                // unwrap the CompletionException
                responseObserver.onError(e.cause ?: e)
            }
        }
    }

    override fun removeBlueprint(request: BluePrintRemoveInput?, responseObserver: StreamObserver<BluePrintRemoveOutput>?) {
        //TODO
    }

    private fun doUploadBlueprint(request: BluePrintUploadInput): BluePrintUploadOutput? {
        val blueprintName = request.blueprintName
        val blueprintVersion = request.blueprintVersion
        val filePath = "${bluePrintCoreConfiguration.archivePath}/$blueprintName/$blueprintVersion"
        val blueprintDir = File(filePath)

        log.info("Re-creating blueprint directory(${blueprintDir.absolutePath})")
        FileUtils.deleteDirectory(blueprintDir)
        FileUtils.forceMkdir(blueprintDir)

        val file = File("${blueprintDir.absolutePath}/$blueprintName.zip")
        log.info("Writing CBA File under :${file.absolutePath}")

        val fileChunk = request.fileChunk

        file.writeBytes(fileChunk.chunk.toByteArray()).apply {
            log.info("CBA file(${file.absolutePath} written successfully")
        }

        val status = Status.newBuilder()
                .setCode(200) // TODO Define code strategy
                .setTimestamp(System.currentTimeMillis().toString()) // TODO ZULU format
                .build()
        return BluePrintUploadOutput.newBuilder()
                .setCommonHeader(request.commonHeader)
                .setStatus(status)
                .build()
    }
}