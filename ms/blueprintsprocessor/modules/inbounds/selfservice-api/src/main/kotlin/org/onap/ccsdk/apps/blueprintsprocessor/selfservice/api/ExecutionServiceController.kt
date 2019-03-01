/*
 * Copyright © 2017-2018 AT&T Intellectual Property.
 * Modifications Copyright © 2019 IBM.
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

import io.swagger.v3.oas.annotations.media.Schema
import org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ACTION_MODE_ASYNC
import org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ExecutionServiceInput
import org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ExecutionServiceOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/execution-service")
class ExecutionServiceController {

    @Autowired
    lateinit var executionServiceHandler: ExecutionServiceHandler

    @RequestMapping(path = ["/ping"], method = [RequestMethod.GET], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun ping(): Mono<String> {
        return Mono.just("Success")
    }

    @PostMapping(path = ["/upload"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Schema(description = "Upload CBA", title = "Takes a File and load it in the runtime database")
    @ResponseBody
    fun upload(@RequestPart("file") parts: Mono<FilePart>): Mono<String> {
        return parts
            .filter { it is FilePart }
            .ofType(FilePart::class.java)
            .flatMap(executionServiceHandler::upload)
    }

    @RequestMapping(path = ["/process"], method = [RequestMethod.POST], produces = [MediaType.APPLICATION_JSON_VALUE])
    @Schema(description = "Resolve Resource Mappings",
        title = "Takes the blueprint information and process as per the payload")
    @ResponseBody
    fun process(@RequestBody executionServiceInput: ExecutionServiceInput): ExecutionServiceOutput {
        if (executionServiceInput.actionIdentifiers.mode == ACTION_MODE_ASYNC) {
            throw IllegalStateException("Can't process async request through the REST endpoint. Use gRPC for async processing.")
        }
        return executionServiceHandler.processSync(executionServiceInput)
    }
}
