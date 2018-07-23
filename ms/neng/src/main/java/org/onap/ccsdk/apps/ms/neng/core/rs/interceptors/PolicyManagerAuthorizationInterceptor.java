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

package org.onap.ccsdk.apps.ms.neng.core.rs.interceptors;

import java.io.IOException;
import org.onap.ccsdk.apps.ms.neng.extinf.props.PolicyManagerProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

/**
 * Interceptor for adding authorization headers in the request to policy manager.
 */
@Component
public class PolicyManagerAuthorizationInterceptor implements ClientHttpRequestInterceptor {
    @Autowired
    PolicyManagerProps props;

    /**
     * Intercepts the given request and adds additional headers, mainly for authorization.
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, 
                    ClientHttpRequestExecution executionChain) throws IOException {
        httpRequest.getHeaders().add("ClientAuth", props.getClientAuth());
        httpRequest.getHeaders().add("Environment", props.getEnvironment());
        httpRequest.getHeaders().add("X-ECOMP-RequestID", props.getEcompRequestId());
        httpRequest.getHeaders().add("Authorization", props.getBasicAuth());
        return executionChain.execute(httpRequest, body);
    }
}
