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

import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants
import org.onap.ccsdk.apps.controllerblueprints.core.data.BlueprintFileResponse
import org.onap.ccsdk.apps.controllerblueprints.core.data.BlueprintInfoResponse
import org.onap.ccsdk.apps.controllerblueprints.core.interfaces.BluePrintCatalogService
import org.onap.ccsdk.apps.controllerblueprints.core.interfaces.BluePrintValidatorService
import org.onap.ccsdk.apps.controllerblueprints.core.utils.BluePrintArchiveUtils
import org.onap.ccsdk.apps.controllerblueprints.core.utils.BluePrintMetadataUtils
import org.onap.ccsdk.apps.controllerblueprints.service.domain.ConfigModel
import org.onap.ccsdk.apps.controllerblueprints.service.domain.ConfigModelContent
import org.onap.ccsdk.apps.controllerblueprints.service.repository.ConfigModelRepository
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.util.*

@Service
class BluePrintCatalogServiceImpl(private val bluePrintLoadConfiguration: BluePrintLoadConfiguration,
                                  private val bluePrintValidatorService: BluePrintValidatorService,
                                  private val configModelRepository: ConfigModelRepository) : BluePrintCatalogService {

    override fun uploadToDataBase(file: String, validate: Boolean): BlueprintInfoResponse? {
        // The file name provided here is unique as we transform to UUID before storing
        var blueprintInfoResponse: BlueprintInfoResponse? = null
        val blueprintFile = File(file)
        // If the file is directory
        if (blueprintFile.isDirectory) {

            val zipFile = File("${bluePrintLoadConfiguration.blueprintArchivePath}/${UUID.randomUUID()}.zip")
            // zip the directory
            BluePrintArchiveUtils.compress(blueprintFile, zipFile, true)

            // Upload to the Data Base
            saveToDataBase(blueprintFile, zipFile)

            // After Upload to Database delete the zip file
            zipFile.delete()

        } else {
            // If the file is ZIP
            // unzip the CBA file to validate before store in database
            val targetDir = "${bluePrintLoadConfiguration.blueprintDeployPath}/${UUID.randomUUID()}"
            val extractedDirectory = BluePrintArchiveUtils.deCompress(blueprintFile, targetDir)

            // Upload to the Data Base
            saveToDataBase(extractedDirectory, blueprintFile)

            // After Upload to Database delete the zip file
            extractedDirectory.delete()
        }

        return blueprintInfoResponse
    }

    override fun downloadFromDataBase(name: String, version: String, path: String): BlueprintFileResponse {
        // If path ends with zip, then compress otherwise download as extracted folder

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun downloadFromDataBase(uuid: String, path: String): BlueprintFileResponse {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun prepareBluePrint(name: String, version: String): String {
        val preparedPath = "${bluePrintLoadConfiguration.blueprintDeployPath}/$name/$version"
        downloadFromDataBase(name, version, preparedPath)
        return preparedPath
    }

    private fun saveToDataBase(extractedDirectory: File, archiveFile: File, checkValidity: Boolean? = false) {
        // Upload to the Data Base
        val id = "save-${UUID.randomUUID()}"
        val bluePrintRuntimeService = BluePrintMetadataUtils.getBluePrintRuntime(id, extractedDirectory.absolutePath)
        // TODO("Check Validity based on indicator")

        val metaData = bluePrintRuntimeService.bluePrintContext().metadata!!
        // FIXME("Check Duplicate for Artifact Name and Artifact VErsion")
        val configModel = ConfigModel()
        configModel.id = id
        configModel.artifactName = metaData[BluePrintConstants.METADATA_TEMPLATE_NAME]
        configModel.artifactVersion = metaData[BluePrintConstants.METADATA_TEMPLATE_VERSION]
        configModel.tags = metaData[BluePrintConstants.METADATA_TEMPLATE_TAGS]
        configModel.artifactDescription = "Controller Blueprint for ${configModel.artifactName}:${configModel.artifactVersion}"


        val configModelContent = ConfigModelContent()
        configModelContent.id = id
        configModelContent.contentType = "CBA_ZIP"
        configModelContent.name = "${configModel.artifactName}:${configModel.artifactVersion}"
        configModelContent.description = "(${configModel.artifactName}:${configModel.artifactVersion} CBA Zip Content"
        configModelContent.content = Files.readAllBytes(archiveFile.toPath())
        configModel.configModelContent = configModelContent

        configModelRepository.saveAndFlush(configModel)
    }
}