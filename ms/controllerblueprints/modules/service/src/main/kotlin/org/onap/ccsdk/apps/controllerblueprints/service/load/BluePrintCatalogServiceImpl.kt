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

package org.onap.ccsdk.apps.controllerblueprints.service.load

import org.onap.ccsdk.apps.controllerblueprints.core.data.BlueprintFileResponse
import org.onap.ccsdk.apps.controllerblueprints.core.data.BlueprintInfoResponse
import org.onap.ccsdk.apps.controllerblueprints.core.interfaces.BluePrintCatalogService
import org.onap.ccsdk.apps.controllerblueprints.core.interfaces.BluePrintValidatorService
import org.onap.ccsdk.apps.controllerblueprints.core.utils.BluePrintArchiveUtils
import org.onap.ccsdk.apps.controllerblueprints.core.utils.BluePrintMetadataUtils
import org.onap.ccsdk.apps.controllerblueprints.core.utils.BluePrintFileUtils
import org.onap.ccsdk.apps.controllerblueprints.service.BluePrintDatabaseService
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Paths

@Service
class BluePrintCatalogServiceImpl(private val bluePrintLoadConfiguration: BluePrintLoadConfiguration,
                                  private val bluePrintValidatorService: BluePrintValidatorService,
                                  private val bluePrintDatabaseService: BluePrintDatabaseService) : BluePrintCatalogService {

    override fun uploadToDataBase(file: String): BlueprintInfoResponse? {
        // The file name provided here is unique as we transform to UUID before storing
        var blueprintInfoResponse: BlueprintInfoResponse? = null
        val blueprintFile = File(file)
        val fileName = blueprintFile.name
        val id = BluePrintFileUtils.stripFileExtension(fileName)
        // If the file is directory
        if (blueprintFile.isDirectory) {
            val bluePrintRuntimeService = BluePrintMetadataUtils.getBluePrintRuntime(id, blueprintFile.absolutePath)
            val valid = bluePrintValidatorService.validateBluePrints(bluePrintRuntimeService)
            if (valid) {
                val zipFile = File("${bluePrintLoadConfiguration.blueprintArchivePath}/$fileName")
                // zip the directory
                BluePrintArchiveUtils.compress(blueprintFile, zipFile, true)

                // Upload to the Data Base
                blueprintInfoResponse = bluePrintDatabaseService.storeBluePrints(file, id, zipFile)

                // After Upload to Database delete the zip file
                zipFile.delete()
            }
        } else {
            // If the file is ZIP
            // unzip the CBA file to validate before store in database
            val targetDir = Paths.get("${bluePrintLoadConfiguration.blueprintArchivePath}/$id/")
            val blueprintUnzipDir = BluePrintArchiveUtils.deCompress(blueprintFile, targetDir.toString())

            val firstItem = BluePrintArchiveUtils.getFirstItemInDirectory(blueprintUnzipDir)

            val blueprintBaseDirectory = blueprintUnzipDir.absolutePath + "/" + firstItem
            // Validate Blueprint
            val bluePrintRuntimeService = BluePrintMetadataUtils.getBluePrintRuntime(id, blueprintBaseDirectory)
            val valid = bluePrintValidatorService.validateBluePrints(bluePrintRuntimeService)
            if (valid) {
                // Upload to the Data Base
                blueprintInfoResponse = bluePrintDatabaseService.storeBluePrints(blueprintBaseDirectory, id, blueprintFile)

                // After Upload to Database delete the zip file
                blueprintFile.delete()
                blueprintUnzipDir.deleteRecursively()
            }
        }

        return blueprintInfoResponse
    }

    override fun downloadFromDataBase(name: String, version: String, path: String): String {
        // If path ends with zip, then compress otherwise download as extracted folder

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun prepareBluePrint(name: String, version: String): String {
        val preparedPath = "${bluePrintLoadConfiguration.blueprintDeployPath}/$name/$version"
        downloadFromDataBase(name, version, preparedPath)
        return preparedPath
    }

    override fun findBlueprintById(id: String): BlueprintInfoResponse {
        return this.bluePrintDatabaseService.getBlueprintByUUID(id)
    }

    override fun findAllBlueprint(): List<BlueprintInfoResponse> {
        return this.bluePrintDatabaseService.listAllBlueprintArchive()
    }


    override fun downloadBlueprintArchive(uuid: String): BlueprintFileResponse {
        return this.bluePrintDatabaseService.getBlueprintArchiveByUUID(uuid)
    }
}