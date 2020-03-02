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

package org.onap.ccsdk.apps.ms.neng.service.extinf.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.onap.ccsdk.apps.ms.neng.core.exceptions.NengException;
import org.onap.ccsdk.apps.ms.neng.core.policy.PolicyFinder;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.GetConfigRequest;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.GetConfigRequestV2;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.GetConfigResponse;
import org.onap.ccsdk.apps.ms.neng.core.rs.interceptors.PolicyManagerAuthorizationInterceptor;
import org.onap.ccsdk.apps.ms.neng.extinf.props.PolicyManagerProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * Finds policies from policy manager.
 */
@Component
@Qualifier("PolicyFinderServiceImpl")
public class PolicyFinderServiceImpl implements PolicyFinder {
    private static Logger log = Logger.getLogger(PolicyFinderServiceImpl.class.getName());

    @Autowired PolicyManagerProps policManProps;
    @Autowired @Qualifier("policyMgrRestTempBuilder") RestTemplateBuilder policyMgrRestTempBuilder;
    @Autowired PolicyManagerAuthorizationInterceptor authInt;
    RestTemplate restTemplate;

    /**
     * Find policy with the given name from policy manager.
     */
    @Override
    public Map<String, Object> findPolicy(String policyName) throws Exception {
        Object response = getConfig(policyName).getResponse();
        if (response instanceof List) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> policyList = (List<Map<String, Object>>) response;
            return ((!policyList.isEmpty()) ? policyList.get(0) : null);
        } else {
            return null;
        }
    }

    protected boolean shouldUsePolicyV2 () {
        String version = policManProps.getVersion();
        log.info("Policy Manager Version - " + version );

        try {
            int vnum = Integer.parseInt(version);
            if ( vnum <= 1 ) {
                return false;
            }
        } catch ( Exception e ) {
            return true;
        }
       
        return true;
    }

    GetConfigResponse getConfig(String policyName) throws Exception {

        Object request;
        if ( shouldUsePolicyV2() ) {
           GetConfigRequestV2 req = new GetConfigRequestV2();

           req.setOnapName("SDNC");
           req.setOnapComponent("CCSDK");
           req.setOnapInstance("CCSDK-ms-neng");
           req.setRequestId( UUID.randomUUID().toString() );
           req.setAction("naming");

           Map<String,Object> resource = new HashMap<>();
           resource.put("policy-id", policyName);
           req.setResource(resource);

           request = req;
        } else {
           GetConfigRequest getConfigRequest = new GetConfigRequest();

           getConfigRequest.setPolicyName(policyName);

           request = getConfigRequest;
        }

        ObjectMapper reqmapper = new ObjectMapper();
        String reqStr = reqmapper.writeValueAsString(request);
        log.info("Request  - " + reqStr);

        return (makeOutboundCall( policyName, request, GetConfigResponse.class));
    }

    <T, R> GetConfigResponse makeOutboundCall( String policyName, T request, Class<R> response) throws Exception {
        log.info("Policy Manager  - " + policManProps.getUrl());

        RequestEntity<T> re = RequestEntity.post(new URI(policManProps.getUrl()))
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).body(request);
        try {
            ResponseEntity<Object> resp = getRestTemplate().exchange(re, Object.class);
            if (HttpStatus.OK.equals(resp.getStatusCode())) {
                ObjectMapper objectmapper = new ObjectMapper();
                String bodyStr = objectmapper.writeValueAsString(resp.getBody());
                return handleResponse( bodyStr );
            }
        } catch (HttpStatusCodeException e) {
            handleError(e);
        }
        throw new NengException("Error while retrieving policy " + policyName +" from policy manager.");
    }

    GetConfigResponse handleResponse ( String body ) throws Exception {
        log.info(body);

        ObjectMapper objectmapper = new ObjectMapper();
        GetConfigResponse getConfigResp = new GetConfigResponse();
        try {
            Map<Object, Object> respObj = objectmapper.readValue( body, new TypeReference<Map<Object, Object>>() {});
            List<Map<Object, Object>> respList = transformConfigObjectV2(objectmapper, respObj);
            getConfigResp.setResponse(respList);
        } catch ( Exception e ) {
            List<Map<Object, Object>> respObj = objectmapper.readValue( body, new TypeReference<List<Map<Object, Object>>>() {});
            transformConfigObject(objectmapper, respObj);
            getConfigResp.setResponse(respObj);
        }
        return getConfigResp;
    }

    void handleError(HttpStatusCodeException e) throws Exception {
        String respString = e.getResponseBodyAsString();
        log.info(respString);
        if (e.getStatusText() != null) {
            log.info(e.getStatusText());
        }
        if (e.getResponseHeaders() != null && e.getResponseHeaders().toSingleValueMap() != null) {
            log.info(e.getResponseHeaders().toSingleValueMap().toString());
        }
        if (HttpStatus.NOT_FOUND.equals(e.getStatusCode()) && (respString != null && respString.contains(""))) {
            throw new NengException("Policy not found in policy manager.");
        }
        throw new NengException("Error while retrieving policy from policy manager.");
    }

    /**
     * Transforms the policy-V2 response in a form compatible with V1.
     */
    List<Map<Object,Object>>  transformConfigObjectV2(ObjectMapper objectmapper, Map<Object, Object> respObj) throws Exception {
        List<Map<Object,Object>> policyList = new ArrayList<>();

        Object policies = respObj.get("policies");
        if (policies != null && policies instanceof Map<?, ?> ) {
            Map<Object, Object> policiesMap = (Map<Object,Object>)policies;
            if ( policiesMap.size() > 0 ) {
                Object policy = policiesMap.entrySet().iterator().next().getValue();
                if ( policy != null && policy instanceof Map<?, ?> ) {
                    Map<Object, Object> thePolicyMap = (Map<Object,Object>)policy;
                    Object properties = thePolicyMap.get("properties");
                    if ( properties != null && properties instanceof Map<?, ?> ) {
                        Map<Object, Object> propertiesMap = (Map<Object,Object>)properties;

                        Map<Object,Object> top = new HashMap<>();
                        Map<Object,Object> config = new HashMap<>();
                        top.put("config", config );
                        config.put("content", propertiesMap );
                        policyList.add(top);
                    } 
                } 
            } 
        }
        return policyList;
    }


    /**
     * Transforms the 'config' element (which is received as a JSON string) to a map like a JSON object.
     */
    void transformConfigObject(ObjectMapper objectmapper, List<Map<Object, Object>> respObj) throws Exception {
        Object configElement = respObj.get(0).get("config");
        if (configElement instanceof String) {
            Map<Object, Object> obj = objectmapper.readValue(configElement.toString(),
                            new TypeReference<Map<Object, Object>>() {});
            respObj.get(0).put("config", obj);
        }
    }

    RestTemplate getRestTemplate() throws Exception {
        if (restTemplate != null) {
            return restTemplate;
        }
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                        .loadTrustMaterial(null, acceptingTrustStrategy).build();
        HostnameVerifier verifier = (String arg0, SSLSession arg1) -> true;
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, verifier);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        restTemplate = new RestTemplate(requestFactory);
        restTemplate.getInterceptors().add(getAuthInt());
        return restTemplate;
    }

    RestTemplateBuilder getPolicyMgrRestTempBuilder() {
        return policyMgrRestTempBuilder;
    }

    void setPolicyMgrRestTempBuilder(RestTemplateBuilder policyMgrRestTempBuilder) {
        this.policyMgrRestTempBuilder = policyMgrRestTempBuilder;
    }

    PolicyManagerAuthorizationInterceptor getAuthInt() {
        return authInt;
    }

    void setAuthInt(PolicyManagerAuthorizationInterceptor authInt) {
        this.authInt = authInt;
    }
}
