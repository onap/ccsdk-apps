/*
 * Copyright © 2019 Bell Canada Intellectual Property.
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

package org.onap.ccsdk.apps.controllerblueprints.service.controller

import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException
import org.onap.ccsdk.apps.controllerblueprints.service.BlueprintModelService
import org.onap.ccsdk.apps.controllerblueprints.service.domain.BlueprintModelSearch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

/**
 * BlueprintModelRest Purpose: Handle controllerBlueprint API request
 *
 * @author Vinal Patel
 * @version 1.0
 */
@RestController
@RequestMapping(value = arrayOf("/api/v1/blueprint-model"))
class BlueprintModelRest {

    @Autowired
    private val blueprintModelService: BlueprintModelService? = null

    @PostMapping(path = arrayOf(""), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE), consumes = arrayOf(MediaType.MULTIPART_FORM_DATA_VALUE))
    @ResponseBody
    @Throws(BluePrintException::class)
    fun saveBlueprint(@RequestPart("file") file: FilePart): Mono<BlueprintModelSearch> {
        return blueprintModelService!!.saveBlueprintModel(file)
    }

    @GetMapping(path = arrayOf(""), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ResponseBody
    fun allBlueprintModel(): List<BlueprintModelSearch> {
    return this.blueprintModelService!!.allBlueprintModel
    }

    @DeleteMapping(path = arrayOf("/{id}"))
    @Throws(BluePrintException::class)
    fun deleteBlueprint(@PathVariable(value = "id") id: String) {
        this.blueprintModelService!!.deleteBlueprintModel(id)
    }

    @GetMapping(path = arrayOf("/by-name/{name}/version/{version}"), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ResponseBody
    @Throws(BluePrintException::class)
    fun getBlueprintByNameAndVersion(@PathVariable(value = "name") name: String,
                                     @PathVariable(value = "version") version: String): BlueprintModelSearch {
        return this.blueprintModelService!!.getBlueprintModelSearchByNameAndVersion(name, version)
    }

    @GetMapping(path = arrayOf("/download/by-name/{name}/version/{version}"), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ResponseBody
    @Throws(BluePrintException::class)
    fun downloadBlueprintByNameAndVersion(@PathVariable(value = "name") name: String,
                                          @PathVariable(value = "version") version: String): ResponseEntity<Resource> {
        return this.blueprintModelService!!.downloadBlueprintModelFileByNameAndVersion(name, version)
    }

    @GetMapping(path = arrayOf("/{id}"), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ResponseBody
    @Throws(BluePrintException::class)
    fun getBlueprintModel(@PathVariable(value = "id") id: String): BlueprintModelSearch {
        return this.blueprintModelService!!.getBlueprintModelSearch(id)
    }

    @GetMapping(path = arrayOf("/download/{id}"), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ResponseBody
    @Throws(BluePrintException::class)
    fun downloadBluePrint(@PathVariable(value = "id") id: String): ResponseEntity<Resource> {
        return this.blueprintModelService!!.downloadBlueprintModelFile(id)
    }

    @PutMapping(path = arrayOf("/publish/{id}"), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ResponseBody
    @Throws(BluePrintException::class)
    fun publishBlueprintModel(@PathVariable(value = "id") id: String): BlueprintModelSearch {
        return this.blueprintModelService!!.publishBlueprintModel(id)
    }

    @GetMapping(path = arrayOf("/search/{tags}"), produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    @ResponseBody
    fun searchBlueprintModels(@PathVariable(value = "tags") tags: String): List<BlueprintModelSearch> {
        return this.blueprintModelService!!.searchBlueprintModels(tags)
    }
}
