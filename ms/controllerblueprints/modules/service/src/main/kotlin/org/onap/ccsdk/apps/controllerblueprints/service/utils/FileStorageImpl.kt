/*
 * Copyright © 2017-2018 IBM Intellectual Property.
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
package org.onap.ccsdk.apps.controllerblueprints.service.utils

import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
class FileStorageImpl: FileStorage {

    @Value("\${blueprintsprocessor.blueprintDeployPath}")
    lateinit var deployPath: String

    val log = LoggerFactory.getLogger(this::class.java)
    val rootLocation = Paths.get(deployPath)

    override fun store(file: MultipartFile) {
        Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()))
    }

    override fun loadFile(filename: String): Resource {
        val file = rootLocation.resolve(filename)
        val resource = UrlResource(file.toUri())

        if (resource.exists() || resource.isReadable()) {
            return resource
        } else {
            throw RuntimeException("FAIL!")
        }
    }
}