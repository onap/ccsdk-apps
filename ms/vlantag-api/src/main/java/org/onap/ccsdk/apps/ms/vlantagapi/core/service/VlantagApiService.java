/*******************************************************************************
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
 ******************************************************************************/
package org.onap.ccsdk.apps.ms.vlantagapi.core.service;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.onap.ccsdk.apps.ms.vlantagapi.core.exception.VlantagApiException;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.AssignVlanTagRequest;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.AssignVlanTagResponse;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.PingResponse;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.UnassignVlanTagRequest;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.UnassignVlanTagResponse;

/**
 * VlantagApiService.java Purpose: Provide Vlantag Assignment & UnAssignment
 * APIs interface for VNFs
 *
 * @author Saurav Paira
 * @version 1.0
 */
@Path("/")
public interface VlantagApiService {

    /**
     * This is a assignVlanTag service to assign Vlantags based on the
     * AssignVlanTagRequest and Policy instance.
     * 
     * @param AssignVlanTagRequest
     * @return AssignVlanTagResponse
     */
    @POST
    @Path("/v1/assign")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    AssignVlanTagResponse assignVlanTag(@Valid AssignVlanTagRequest body) throws VlantagApiException, Exception;

    /**
     * This is a unassignVlanTag service to unassign Vlantags based on the
     * UnassignVlanTagRequest and Policy instance.
     * 
     * @param UnassignVlanTagRequest
     * @return UnassignVlanTagResponse
     */
    @POST
    @Path("/v1/unassign")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    UnassignVlanTagResponse unassignVlanTag(@Valid UnassignVlanTagRequest body) throws VlantagApiException, Exception;

    /**
     * This is a ping service to check the Vlantag Api is Up and running.
     * 
     * @param name
     * @return PingResponse
     */
    @GET
    @Path("/v1/ping/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    PingResponse getPing(@PathParam("name") String name);

}
