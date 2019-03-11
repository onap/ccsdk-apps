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

import io.swagger.annotations.ApiOperation
import org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ACTION_MODE_ASYNC
import org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ExecutionServiceInput
import org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ExecutionServiceOutput
import org.onap.ccsdk.apps.blueprintsprocessor.selfservice.api.utils.FileExtract
import org.onap.ccsdk.apps.blueprintsprocessor.selfservice.api.utils.FileStorage
import org.onap.ccsdk.apps.controllerblueprints.core.interfaces.BluePrintEnhancerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import reactor.core.publisher.Mono
import java.nio.file.Paths

@RestController
@RequestMapping("/api/v1/execution-service")
class ExecutionServiceController {

    @Autowired
    lateinit var fileStorage: FileStorage

    @Autowired
    private val bluePrintEnhancerService: BluePrintEnhancerService? = null

    @Autowired
    lateinit var executionServiceHandler: ExecutionServiceHandler

    @Value("\${blueprintsprocessor.blueprintDeployPath}")
    lateinit var deployPath: String

    @Value("\${blueprintsprocessor.blueprintArchivePath}")
    lateinit var archivePath: String

    @RequestMapping(path = ["/ping"], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun ping(): Mono<String> {
        return Mono.just("Success")
    }

    @PostMapping(path = ["/upload"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ApiOperation(value = "Upload CBA", notes = "Takes a File and load it in the runtime database")
    @ResponseBody
    fun upload(@RequestPart("file") parts: Mono<FilePart>): Mono<String> {
        return parts
                .filter { it is FilePart }
                .ofType(FilePart::class.java)
                .flatMap(executionServiceHandler::upload)
    }

    @RequestMapping(path = ["/process"], method = [RequestMethod.POST], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(value = "Resolve Resource Mappings",
            notes = "Takes the blueprint information and process as per the payload")
    @ResponseBody
    fun process(@RequestBody executionServiceInput: ExecutionServiceInput): ExecutionServiceOutput {
        if (executionServiceInput.actionIdentifiers.mode == ACTION_MODE_ASYNC) {
            throw IllegalStateException("Can't process async request through the REST endpoint. Use gRPC for async processing.")
        }
        return executionServiceHandler.processSync(executionServiceInput)
    }

    @PostMapping(path = ["/enrichment"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ApiOperation(value = "Enrichment of uploaded json",
            notes = "Enrichment of uploaded json")
    @ResponseBody
    fun enrichment(@RequestBody file: MultipartFile): ResponseEntity<org.springframework.core.io.Resource> {
        fileStorage.store(file);
        FileExtract().extract(deployPath+file.originalFilename,deployPath)
        val bluePrintContext = bluePrintEnhancerService!!.enhance(deployPath+file.originalFilename.replace(".zip",""), deployPath+System.currentTimeMillis()+"\\")
        //after enrichment need to zip the file
        val fileName:String=FileExtract().zip(deployPath+System.currentTimeMillis()+"\\");
        //need to send the file
        val resource:org.springframework.core.io.Resource = UrlResource(Paths.get(fileName).toUri())
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}
