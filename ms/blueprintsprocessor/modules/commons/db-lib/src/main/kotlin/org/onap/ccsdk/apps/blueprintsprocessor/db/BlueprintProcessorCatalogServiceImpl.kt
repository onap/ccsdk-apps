/*
 * Copyright © 2017-2018 AT&T Intellectual Property.
 * Modifications Copyright © 2019 Bell Canada.
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

package org.onap.ccsdk.apps.blueprintsprocessor.db

import org.onap.ccsdk.apps.blueprintsprocessor.db.primary.domain.BlueprintProcessorModel
import org.onap.ccsdk.apps.blueprintsprocessor.db.primary.domain.BlueprintProcessorModelContent
import org.onap.ccsdk.apps.blueprintsprocessor.db.primary.repository.BlueprintProcessorModelContentRepository
import org.onap.ccsdk.apps.blueprintsprocessor.db.primary.repository.BlueprintProcessorModelRepository
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants
import org.onap.ccsdk.apps.controllerblueprints.core.common.ApplicationConstants
import org.onap.ccsdk.apps.controllerblueprints.core.interfaces.BluePrintValidatorService
import org.onap.ccsdk.apps.controllerblueprints.db.resources.BlueprintCatalogServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files

/**
Similar/Duplicate implementation in [org.onap.ccsdk.apps.controllerblueprints.service.load.ControllerBlueprintCatalogServiceImpl]
 */
@Service
class BlueprintProcessorCatalogServiceImpl(bluePrintValidatorService: BluePrintValidatorService,
                                           private val blueprintModelContentRepository: BlueprintProcessorModelContentRepository,
                                           private val blueprintModelRepository: BlueprintProcessorModelRepository)
    : BlueprintCatalogServiceImpl(bluePrintValidatorService) {

    private val log = LoggerFactory.getLogger(BlueprintProcessorCatalogServiceImpl::class.toString())

    init {
        log.info("BlueprintProcessorCatalogServiceImpl initialized")
    }

    override fun getFromDatabase(name: String, version: String, blueprintFile: File) {
        blueprintModelRepository.findByArtifactNameAndArtifactVersion(name, version)?.apply {

        }
    }

    override fun saveToDataBase(metadata: MutableMap<String, String>, archiveFile: File) {

        val artifactName = metadata[BluePrintConstants.METADATA_TEMPLATE_NAME]
        val artifactVersion = metadata[BluePrintConstants.METADATA_TEMPLATE_VERSION]

        log.isDebugEnabled.apply {
            blueprintModelRepository.findByArtifactNameAndArtifactVersion(artifactName!!, artifactVersion!!)?.let {
                log.debug("Overwriting blueprint model :$artifactName::$artifactVersion")
            }
        }

        val blueprintModel = BlueprintProcessorModel()
        blueprintModel.id = metadata[BluePrintConstants.PROPERTY_BLUEPRINT_PROCESS_ID]
        blueprintModel.artifactType = ApplicationConstants.ASDC_ARTIFACT_TYPE_SDNC_MODEL
        blueprintModel.artifactName = artifactName
        blueprintModel.artifactVersion = artifactVersion
        blueprintModel.updatedBy = metadata[BluePrintConstants.METADATA_TEMPLATE_AUTHOR]
        blueprintModel.tags = metadata[BluePrintConstants.METADATA_TEMPLATE_TAGS]
        blueprintModel.artifactDescription = "Controller Blueprint for $artifactName:$artifactVersion"

        val blueprintModelContent = BlueprintProcessorModelContent()
        blueprintModelContent.id = metadata[BluePrintConstants.PROPERTY_BLUEPRINT_PROCESS_ID]
        blueprintModelContent.contentType = "CBA_ZIP"
        blueprintModelContent.name = "$artifactName:$artifactVersion"
        blueprintModelContent.description = "$artifactName:$artifactVersion CBA Zip Content"
        blueprintModelContent.content = Files.readAllBytes(archiveFile.toPath())
        blueprintModelContent.blueprintModel = blueprintModel

        blueprintModel.blueprintModelContent = blueprintModelContent

        blueprintModelRepository.saveAndFlush(blueprintModel)
        blueprintModelContentRepository.saveAndFlush(blueprintModelContent)
    }
}