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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.NameGenRequest;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.NameGenResponse;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * Specifies the properties of the REST-style interface/API to this micro-service.
 */
@Api
@Path("/service")
@Produces({MediaType.APPLICATION_JSON})
public interface RestService {
    /**
     * Name generation API.
     */
    @POST
    @Path("/v1/genNetworkElementName")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Generates network element names, based on naming policies", 
                  response = NameGenResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Service not available"),
                    @ApiResponse(code = 500, message = "Unexpected Runtime error")})
    public Response generateNetworkElementName(@RequestBody @Valid NameGenRequest request);

    /**
     * Name removal API.
     */
    @DELETE
    @Path("/v1/genNetworkElementName")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Releases network element names", response = NameGenResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Service not available"),
                    @ApiResponse(code = 500, message = "Unexpected Runtime error")})
    public Response releaseNetworkElementName(@RequestBody @Valid NameGenRequest request);

    /**
     * API to return naming policy cached in this micro-service.
     * 
     * <p/>This is not used by clients -- it is here to help with diagnostics.
     */
    @GET
    @Path("/v1/getpolicyresponse/{policyName}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Map<String, Object>> getPolicyResponse(@QueryParam("policyName") String policyName) throws Exception;

    /**
     * API to add a naming policy to the database cache in this micro-service.
     * 
     * <p/>This is not used by clients -- it is here to help with diagnostics.
     */
    @POST
    @Path("/v1/addPolicy")
    @Produces({MediaType.APPLICATION_JSON})
    public Map<String, Object> addPolicyToDb(Object request) throws Exception;

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
