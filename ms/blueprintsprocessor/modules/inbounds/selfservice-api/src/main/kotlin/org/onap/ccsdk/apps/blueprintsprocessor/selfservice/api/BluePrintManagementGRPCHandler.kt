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

@Service
class BluePrintManagementGRPCHandler(private val bluePrintCoreConfiguration: BluePrintCoreConfiguration,
                                     private val bluePrintCatalogService: BluePrintCatalogService)
    : BluePrintManagementServiceGrpc.BluePrintManagementServiceImplBase() {

    private val log = LoggerFactory.getLogger(BluePrintManagementGRPCHandler::class.java)

    override fun uploadBlueprint(request: BluePrintUploadInput, responseObserver: StreamObserver<BluePrintUploadOutput>) {
        val response = BluePrintUploadOutput.newBuilder().setCommonHeader(request.commonHeader).build()
        try {
            val blueprintName = request.blueprintName
            val blueprintVersion = request.blueprintVersion

            val blueprintArchivedFilePath = "${bluePrintCoreConfiguration.archivePath}/$blueprintName/$blueprintVersion/$blueprintName.zip"
            val blueprintArchivedFile = File(blueprintArchivedFilePath)

            saveToDisk(request, blueprintArchivedFile)
            bluePrintCatalogService.uploadToDataBase(blueprintArchivedFilePath, validate = true)

            responseObserver.onNext(response)
            responseObserver.onCompleted()
        } catch (e: Exception) {
            log.error("failed to upload file ", e)
            responseObserver.onError(e)
        }
    }

    override fun removeBlueprint(request: BluePrintRemoveInput?, responseObserver: StreamObserver<BluePrintRemoveOutput>?) {
        //TODO
    }

    private fun saveToDisk(request: BluePrintUploadInput, blueprintDir: File) {
        log.info("Writing CBA File under :${blueprintDir.absolutePath}")
        if (blueprintDir.exists()) {
            log.info("Re-creating blueprint directory(${blueprintDir.absolutePath})")
            FileUtils.deleteDirectory(blueprintDir)
            FileUtils.forceMkdir(blueprintDir)
        }

        blueprintDir.writeBytes(request.fileChunk.chunk.toByteArray()).apply {
            log.info("CBA file(${blueprintDir.absolutePath} written successfully")
        }
    }
}