/*
 * Copyright (C) 2019 Bell Canada.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onap.ccsdk.apps.blueprintsprocessor.selfservice.api

import com.google.common.util.concurrent.ThreadFactoryBuilder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class GrpcServerConfig {
    private val log = LoggerFactory.getLogger(GrpcServerConfig::class.java)
    private val NUM_CONCURRENT_REQUESTS = 10

    private val threadFactory = ThreadFactoryBuilder()
            .setNameFormat("BluePrintGRPCServer-%d")
            .setUncaughtExceptionHandler { t, e -> log.error("Caught an uncaught exception on thread=" + t.name, e) }
            .build()

    @Bean(value = ["grpcExecutor"])
    open fun executor(): ExecutorService {
        return Executors.newFixedThreadPool(NUM_CONCURRENT_REQUESTS, threadFactory)
    }
}
