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

package org.onap.ccsdk.apps.blueprintsprocessor.selfservice.api.processing

import io.grpc.stub.StreamObserver
import org.onap.ccsdk.apps.controllerblueprints.processing.api.BluePrintProcessingServiceGrpc
import org.onap.ccsdk.apps.controllerblueprints.processing.api.ExecutionServiceInput
import org.onap.ccsdk.apps.controllerblueprints.processing.api.ExecutionServiceOutput
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.function.Supplier

@Service
class GrpcProcessingHandler(private val processingHandler: ProcessingHanlder)
    : BluePrintProcessingServiceGrpc.BluePrintProcessingServiceImplBase() {

    private val log = LoggerFactory.getLogger(GrpcProcessingHandler::class.java)

    @Autowired
    @Qualifier("grpcExecutor")
    lateinit var executor: ExecutorService

    override fun process(request: ExecutionServiceInput,
                         responseObserver: StreamObserver<ExecutionServiceOutput>) {

        log.debug("Processing request=%s", request.commonHeader.requestId)

        // TODO visit with Coroutine
        val future = CompletableFuture.supplyAsync(Supplier {

            // TODO mapping from GPRC to POJO
            // processingHandler.process(request)
            ExecutionServiceOutput.newBuilder().setCommonHeader(request.commonHeader).build()
        }, executor)

        future.whenComplete { result, e ->
            if (e == null) {
                responseObserver.onNext(result)
                responseObserver.onCompleted()
            } else {
                // unwrap the CompletionException
                responseObserver.onError(e.cause ?: e)
            }
        }
    }
}