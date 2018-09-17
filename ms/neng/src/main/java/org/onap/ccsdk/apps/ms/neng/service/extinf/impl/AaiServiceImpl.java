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

package org.onap.ccsdk.apps.ms.neng.service.extinf.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyStore;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.onap.ccsdk.apps.ms.neng.core.exceptions.NengException;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.AaiResponse;
import org.onap.ccsdk.apps.ms.neng.core.rs.interceptors.AaiAuthorizationInterceptor;
import org.onap.ccsdk.apps.ms.neng.extinf.props.AaiProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Implements interface with A&AI.
 */
@Service
public class AaiServiceImpl {
    private static final Logger log = Logger.getLogger(AaiServiceImpl.class.getName());

    @Autowired AaiProps aaiProps;
    RestTemplate restTemplate;
    @Autowired AaiAuthorizationInterceptor authInt;

    @Autowired
    @Qualifier("aaiRestTempBuilder")
    RestTemplateBuilder aaiRestTempBuilder;

    /**
     * Validates the given network element name against A&AI, using the given URL.
     * @param url   the URL for A&AI
     * @param name  a generated network element name
     * @return      true if the element name is valid
     */
    public boolean validate(String url, String name) throws Exception {
        AaiResponse resp = makeOutboundCall(url, name);
        return !resp.isRecFound();
    }

    
    public void setAaiRestTempBuilder(RestTemplateBuilder aaiRestTempBuilder) {
        this.aaiRestTempBuilder = aaiRestTempBuilder;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }    
    
    AaiResponse makeOutboundCall(String url, String name) throws Exception {
        String uri = aaiProps.getUriBase() + url + name;
        log.info("AAI URI - " + uri);
        ResponseEntity<Object> resp = null;
        try {
            RequestEntity<Void> re = RequestEntity.get(new URI(uri)).build();
            resp = getRestTemplate().exchange(re, Object.class);
            if (HttpStatus.OK.equals(resp.getStatusCode())) {
                ObjectMapper objectmapper = new ObjectMapper();
                log.info(objectmapper.writeValueAsString(resp.getBody()));
                return buildResponse(true);
            } else if (HttpStatus.NOT_FOUND.equals(resp.getStatusCode())) {
                log.warning(resp.toString());
                return buildResponse(false);
            } else {
                log.warning(resp.toString());
                throw new NengException("Error while validating name with A&AI");
            }
        } catch (HttpClientErrorException e) {
            log.warning(e.getStatusCode().name() + " -- " + e.getResponseBodyAsString());
            if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
                return buildResponse(false);
            }
            throw new NengException("Error while validating name with AAI");
        }
    }
    
    AaiResponse buildResponse(boolean found) {
        AaiResponse aaiResp = new AaiResponse();
        aaiResp.setRecFound(found);
        return aaiResp;
    }

    RestTemplate getRestTemplate() throws Exception {
        if (this.restTemplate == null) {
            char[] password = aaiProps.getCertPassword().toCharArray();
            KeyStore ks = keyStore(aaiProps.getCert(), password);
            SSLContextBuilder builder = SSLContextBuilder.create().loadKeyMaterial(ks, password); 
            SSLContext sslContext = builder.loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            HttpClient client = HttpClients.custom().setSSLContext(sslContext).build();
            RestTemplateBuilder restBld = aaiRestTempBuilder.additionalInterceptors(authInt); 
            this.restTemplate = restBld.requestFactory(new HttpComponentsClientHttpRequestFactory(client)).build();
        }
        return this.restTemplate;
    }

    KeyStore keyStore(String file, char[] password) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        File key = ResourceUtils.getFile(file);
        try (InputStream in = new FileInputStream(key)) {
            keyStore.load(in, password);
        }
        return keyStore;
    }
    
}
