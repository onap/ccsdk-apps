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

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.net.URI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.GetConfigRequest;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.GetConfigResponse;
import org.onap.ccsdk.apps.ms.neng.extinf.props.PolicyManagerProps;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class PolicyManagerAuthorizationInterceptorTest {
    MockRestServiceServer mockServer;
    RestTemplate restTemplate;
    String policyManagerHostname = "http://0.0.0.1:8080/";
    String policyManagerResPath = "services/service/networkInfrastructureResourcesSample/v1";
    @InjectMocks
    PolicyManagerAuthorizationInterceptor policyManagerInterceptor;
    @Spy
    PolicyManagerProps props = new PolicyManagerProps();

    /**
     * Does the setup for tests.
     */
    @Before
    public void setUp() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
        }
        props.setBasicAuth("Basic bnVsbDpudWxs");
        props.setClientAuth("Basic bnVsbDpudWxs");
        props.setEcompRequestId("xxuv");
        props.setEnvironment("TEST");
        restTemplate.getInterceptors().add(policyManagerInterceptor);
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    public void testAuth() throws Exception {
        mockServer.expect(ExpectedCount.once(), requestTo(policyManagerHostname + policyManagerResPath))
                        .andExpect(method(HttpMethod.POST))
                        .andExpect(header("Authorization", new String[] {"Basic bnVsbDpudWxs"}))
                        .andRespond(withSuccess("", MediaType.APPLICATION_JSON));
        GetConfigRequest req = new GetConfigRequest();
        RequestEntity<GetConfigRequest> re = RequestEntity.post(new URI(policyManagerHostname + policyManagerResPath))
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).body(req);
        ResponseEntity<GetConfigResponse> resp = restTemplate.exchange(re, GetConfigResponse.class);
        mockServer.verify();
        assertNotNull(resp);
    }
}
