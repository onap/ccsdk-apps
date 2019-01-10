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

package org.onap.ccsdk.apps.controllerblueprints.db.resources

import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintConstants
import org.onap.ccsdk.apps.controllerblueprints.core.interfaces.BluePrintCatalogService
import org.onap.ccsdk.apps.controllerblueprints.core.interfaces.BluePrintValidatorService
import org.onap.ccsdk.apps.controllerblueprints.core.utils.BluePrintArchiveUtils
import org.onap.ccsdk.apps.controllerblueprints.core.utils.BluePrintFileUtils
import org.onap.ccsdk.apps.controllerblueprints.core.utils.BluePrintMetadataUtils
import org.slf4j.LoggerFactory
import java.io.File
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BlueprintCatalogServiceImpl(private val blueprintValidator: BluePrintValidatorService) : BluePrintCatalogService {

    private val log = LoggerFactory.getLogger(BlueprintCatalogServiceImpl::class.java)

    override fun save(blueprintFile: File, validate: Boolean): String? {
        val extractedDirectory: File
        val archivedPath: File
        val blueprintId: String

        if (blueprintFile.isDirectory) {
            blueprintId = blueprintFile.name

            extractedDirectory = blueprintFile
            archivedPath = File(":$blueprintFile.zip")

            if (!BluePrintArchiveUtils.compress(blueprintFile, archivedPath, true)) {
                return null
            }
        } else {
            blueprintId = BluePrintFileUtils.stripFileExtension(blueprintFile.name)
            val targetDir = blueprintFile.parent

            extractedDirectory = BluePrintArchiveUtils.deCompress(blueprintFile, targetDir)
            archivedPath = blueprintFile
        }

        if (validate) {
            blueprintValidator.validateBluePrints(extractedDirectory.path)
        }

        val bluePrintRuntimeService = BluePrintMetadataUtils.getBluePrintRuntime(blueprintId, extractedDirectory.path)
        val metadata = bluePrintRuntimeService.bluePrintContext().metadata!!
        metadata[BluePrintConstants.PROPERTY_BLUEPRINT_PROCESS_ID] = blueprintId

        saveToDataBase(metadata, archivedPath)

        return blueprintId
    }

    override fun get(name: String, version: String, path: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    abstract fun saveToDataBase(metadata: MutableMap<String, String>, archiveFile: File)
}