/*-
 * ============LICENSE_START=======================================================
 * ONAP : CCSDK.apps
 * ================================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
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
 * ============LICENSE_END=========================================================
 */

package org.onap.ccsdk.apps.ms.neng.core.service.rs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.NameGenRequest;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.NameGenResponse;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * Specifies the properties of the REST-style interface/API to this micro-service.
 */
@Path("/service")
@Produces({MediaType.APPLICATION_JSON})
public interface RestService {
    /**
     * Name generation API.
     */
    @POST
    @Path("/v1/genNetworkElementName")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Generates network element names, based on naming policies")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "Service not available"),
                    @ApiResponse(responseCode = "500", description = "Unexpected Runtime error")})
    public Response generateNetworkElementName(@RequestBody @Valid NameGenRequest request);

    /**
     * Name removal API.
     */
    @DELETE
    @Path("/v1/genNetworkElementName")
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Releases network element names")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "Service not available"),
                    @ApiResponse(responseCode = "500", description = "Unexpected Runtime error")})
    public Response releaseNetworkElementName(@RequestBody @Valid NameGenRequest request);

    /**
     * API to return naming policy cached in this micro-service.
     *
     * <p/>This is not used by clients -- it is here to help with diagnostics.
     */
    @GET
    @Path("/v1/getpolicyresponse")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPolicyResponse(@QueryParam("policyName") String policyName) throws Exception;

    /**
     * API to add a naming policy to the database cache in this micro-service.
     *
     * <p/>This is not used by clients -- it is here to help with diagnostics.
     */
    @POST
    @Path("/v1/addPolicy")
    @Produces({MediaType.APPLICATION_JSON})
    public Response addPolicyToDb(Object request) throws Exception;

    /**
     * Heart-beat/ping API.
     *
     * <p/>This is not used by clients -- it is here to help with diagnostics.
     */
    @GET
    @Path("/hello")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getQuickHello(@QueryParam("name") String name);
}
