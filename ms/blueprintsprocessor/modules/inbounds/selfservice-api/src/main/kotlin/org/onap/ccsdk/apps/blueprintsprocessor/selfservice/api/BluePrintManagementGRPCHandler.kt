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
import org.onap.ccsdk.apps.controllerblueprints.core.interfaces.BluePrintCatalogService
import org.onap.ccsdk.apps.controllerblueprints.management.api.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Service
class BluePrintManagementGRPCHandler(private val bluePrintCoreConfiguration: BluePrintCoreConfiguration,
                                     private val bluePrintCatalogService: BluePrintCatalogService)
    : BluePrintManagementServiceGrpc.BluePrintManagementServiceImplBase() {

    private val log = LoggerFactory.getLogger(BluePrintManagementGRPCHandler::class.java)

    override fun uploadBlueprint(request: BluePrintUploadInput, responseObserver: StreamObserver<BluePrintUploadOutput>) {
        log.info("Received request(${request.commonHeader.requestId}) to uploadBlueprint")

        val blueprintName = request.blueprintName
        val blueprintVersion = request.blueprintVersion
        val blueprintArchivedFilePath = "${bluePrintCoreConfiguration.archivePath}/$blueprintName/$blueprintVersion/$blueprintName.zip"
        try {
            val blueprintArchivedFile = File(blueprintArchivedFilePath)

            saveToDisk(request, blueprintArchivedFile)
            val blueprintId = bluePrintCatalogService.save(blueprintArchivedFile, true)

            File("${bluePrintCoreConfiguration.archivePath}/$blueprintName").deleteRecursively()

            val response = BluePrintUploadOutput.newBuilder()
                    .setCommonHeader(request.commonHeader)
                    .setStatus(Status.newBuilder()
                            // FIXME Extract timestamp formatter
                            .setTimestamp(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(Date()))
                            .setMessage("Successfully uploaded blueprint:$blueprintName version: $blueprintVersion id:$blueprintId")
                            .setCode(200)
                            .build())
                    .build()

            responseObserver.onNext(response)
            responseObserver.onCompleted()
        } catch (e: Exception) {
            log.error("Failed to upload blueprint:$blueprintName version: $blueprintVersion at path :$blueprintArchivedFilePath ", e)
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Fail to upload blueprint:$blueprintName version: $blueprintVersion")
                    .withCause(e)
                    .asException())
        }
    }

    override fun removeBlueprint(request: BluePrintRemoveInput?, responseObserver: StreamObserver<BluePrintRemoveOutput>?) {
        //TODO
    }

    private fun saveToDisk(request: BluePrintUploadInput, blueprintDir: File) {
        log.debug("Writing CBA File under :${blueprintDir.absolutePath}")
        if (blueprintDir.exists()) {
            log.debug("Re-creating blueprint directory(${blueprintDir.absolutePath})")
            FileUtils.deleteDirectory(blueprintDir.parentFile)
        }
        FileUtils.forceMkdir(blueprintDir.parentFile)
        blueprintDir.writeBytes(request.fileChunk.chunk.toByteArray()).apply {
            log.debug("CBA file(${blueprintDir.absolutePath} written successfully")
        }
    }
}