/*-
 * ============LICENSE_START=======================================================
 * ONAP : CCSDK.apps
 * ================================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Modifications Copyright (C) 2018 IBM.
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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.validation.Valid;
import javax.ws.rs.core.Response;
import org.onap.ccsdk.apps.ms.neng.core.exceptions.NengException;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.HelloWorld;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.NameGenRequest;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.NameGenResponse;
import org.onap.ccsdk.apps.ms.neng.core.service.SpringService;
import org.onap.ccsdk.apps.ms.neng.persistence.entity.PolicyDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Implementation of the REST-style interface/API to this micro-service.
 */
@Component
public class RestServiceImpl implements RestService {

    private static final Logger log = Logger.getLogger(RestServiceImpl.class.getName());
    private static final String INTERNAL_ERROR_MSG = "Internal error occurred while processing the request: ";
    private static final String JDBC_CONNECTION_ERROR_MSG = "Error during JDBC transaction creation: ";
    private static final String ERROR="error";
    private static final String ERROR_500="err-0500";
    private static final int MAX_RETRY = 5;
    private int retry = 0;

    @Autowired SpringService service;

    /**
     * Heart-beat/ping API.
     */
    @Override
    public Response getQuickHello(String name) {
        log.info(name);
        HelloWorld hw = service.getQuickHello(name);
        return Response.ok().entity(hw).build();
    }

    /**
     * Name generation API.
     */
    @Override
    public Response generateNetworkElementName(@RequestBody @Valid NameGenRequest request) {
        log.info("Received request: " + request.toString());
        try {
            NameGenResponse resp = service.generateOrUpdateName(request);
            return buildResponse(resp);
        } catch (NengException e) {
            log.warning(e.getMessage());
            return handleException("NELGEN-0003", e.getMessage());
        } catch (CannotCreateTransactionException e) {
            log.warning(e.getMessage());
            return handleJDBCConnectionException(request, e);
        } catch (Exception e) {
            log.warning(e.getMessage());
            return handleException(ERROR_500, INTERNAL_ERROR_MSG);
        }
    }

    /**
     * Name removal API.
     */
    @Override
    public Response releaseNetworkElementName(NameGenRequest request) {
        NameGenResponse resp;
        Map<String, Object> response = new HashMap<>();
        try {
            resp = service.releaseNetworkElementName(request);
            return buildResponse(resp);
        } catch (NengException e) {
            log.warning(e.getMessage());
            response.put(ERROR, buildErrorResponse("NELGEN-0002", e.getMessage()));
            return buildErrorResponse(response);
        } catch (Exception e) {
            log.warning(e.getMessage());
            response.put(ERROR, buildErrorResponse(ERROR_500, INTERNAL_ERROR_MSG));
            return buildErrorResponse(response);
        }
    }

    /**
     * API to return naming policy cached in this micro-service.
     */
    @Override
    public Response getPolicyResponse(String policyName) throws Exception {
        log.info("get-policy: " + policyName);
        
        PolicyDetails policyDetails = service.getPolicyDetails(policyName);
        return Response.ok().entity(policyDetails.getPolicyResponse()).build();
    }

    /**
     * API to add a naming policy to the database cache in this micro-service.
     */
    @Override
    public Response addPolicyToDb(Object request) throws Exception {
        Map<String, Object> response = new HashMap<>();
        try {
            service.addPolicy(request);
            response.put("status", "Policy added successfully");
            return buildResponse(response);
        } catch (Exception e) {
            log.warning(e.getMessage());
            response.put(ERROR, buildErrorResponse(ERROR_500, e.getMessage()));
            return buildErrorResponse(response);
        }
    }

    private Response handleException(String statusCode, String statusMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put(ERROR, buildErrorResponse(statusCode, statusMessage));

        return buildErrorResponse(response);
    }

    private Response handleJDBCConnectionException(NameGenRequest request, CannotCreateTransactionException e) {
        retry += 1;
        if (retry <= MAX_RETRY) {
            log.info("Try to generate network element name again! Attempt: " + retry);
            Response response = generateNetworkElementName(request);
            if (response.getStatus() != 200) {
                retry = 0;
            }
            return response;
        } else {
            retry = 0;
            return handleException(ERROR_500, JDBC_CONNECTION_ERROR_MSG + e.getMessage());
        }
    }

    private Response buildResponse(Object response) {
        return Response.ok().entity(response).build();
    }

    private Response buildErrorResponse(Map<String, Object> response) {
        return Response.status(500).entity(response).build();
    }

    private Map<String,Object> buildErrorResponse(String errorCode, String message) {
        Map<String,Object> error = new HashMap<>();
        error.put("errorId", errorCode);
        error.put("message", message);
        return error;
    }
}
