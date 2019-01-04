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

package org.onap.ccsdk.apps.blueprintsprocessor;

import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import java.util.concurrent.ExecutorService;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.onap.ccsdk.apps.blueprintsprocessor.selfservice.api.GrpcManagementHandler;
import org.onap.ccsdk.apps.blueprintsprocessor.selfservice.api.processing.GrpcProcessingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "blueprintsprocessor.grpcEnable", havingValue = "true")
@Component
public class BlueprintGRPCServer {

    private static Logger log = LoggerFactory.getLogger(BlueprintGRPCServer.class);

    @Autowired
    private GrpcProcessingHandler bluePrintProcessingGRPCHandler;
    @Autowired
    private GrpcManagementHandler bluePrintManagementGRPCHandler;
    @Autowired
    @Qualifier("grpcExecutor")
    private ExecutorService executor;

    @Value("${blueprintsprocessor.grpcPort}")
    private Integer grpcPort;

    private Server server;

    @PostConstruct
    public void start() {
        try {
            log.info("Starting Blueprint Processor GRPC server..");

            this.server = NettyServerBuilder.forPort(grpcPort)
                .addService(bluePrintProcessingGRPCHandler)
                .addService(bluePrintManagementGRPCHandler)
                .executor(executor)
                .build();

            server.start();
            log.info("Blueprint Processor GRPC server started and ready to serve on port({})...", server.getPort());
            server.awaitTermination();
        } catch (Exception e) {
            throw new BlueprintGRPCException("Could not start server", e);
        }
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            log.info("Stopping Blueprint Processor GRPC server");
            executor.shutdown();
            server.shutdown();
        }
    }
}
