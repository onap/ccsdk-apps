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
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException
import org.onap.ccsdk.apps.controllerblueprints.core.interfaces.BluePrintCatalogService
import org.onap.ccsdk.apps.controllerblueprints.core.interfaces.BluePrintValidatorService
import org.onap.ccsdk.apps.controllerblueprints.core.utils.BluePrintArchiveUtils
import org.onap.ccsdk.apps.controllerblueprints.core.utils.BluePrintFileUtils
import org.onap.ccsdk.apps.controllerblueprints.core.utils.BluePrintMetadataUtils
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Path
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BlueprintCatalogServiceImpl(protected val blueprintValidator: BluePrintValidatorService)
    : BluePrintCatalogService {

    private val log = LoggerFactory.getLogger(BlueprintCatalogServiceImpl::class.java)

    override fun saveToDatabase(blueprintFile: File, validate: Boolean): String {
        val extractedDirectory: File
        val archivedDirectory: File
        val toDeleteDirectory: File
        val blueprintId: String

        if (blueprintFile.isDirectory) {
            blueprintId = blueprintFile.name

            extractedDirectory = blueprintFile
            archivedDirectory = File(":$blueprintFile.zip")
            toDeleteDirectory = archivedDirectory

            if (!BluePrintArchiveUtils.compress(blueprintFile, archivedDirectory, true)) {
                throw BluePrintException("Fail to compress blueprint identified by id: $blueprintId")
            }
        } else {
            blueprintId = BluePrintFileUtils.stripFileExtension(blueprintFile.name)
            val targetDir = "${blueprintFile.parent}/${BluePrintFileUtils.stripFileExtension(blueprintFile.name)}"

            extractedDirectory = BluePrintArchiveUtils.deCompress(blueprintFile, targetDir)
            archivedDirectory = blueprintFile
            toDeleteDirectory = extractedDirectory
        }

        if (validate) {
            blueprintValidator.validateBluePrints(extractedDirectory.path)
        }

        val bluePrintRuntimeService = BluePrintMetadataUtils.getBluePrintRuntime(blueprintId, extractedDirectory.path)
        val metadata = bluePrintRuntimeService.bluePrintContext().metadata!!
        metadata[BluePrintConstants.PROPERTY_BLUEPRINT_PROCESS_ID] = blueprintId

        save(metadata, archivedDirectory)

        toDeleteDirectory.deleteRecursively()

        return blueprintId
    }

    override fun getFromDatabase(name: String, version: String, extract: Boolean): Path {
        return get(name, version, extract)
                ?: throw BluePrintException("Fail to get blueprint $name:$version from database")
    }

    abstract fun save(metadata: MutableMap<String, String>, archiveFile: File)
    abstract fun get(name: String, version: String, extract: Boolean): Path?

}