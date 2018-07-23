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

package org.onap.ccsdk.apps.ms.neng.extinf.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * POJO for representing properties related to policy manager.
 */
@Configuration
@ConfigurationProperties(prefix = "policymgr")
public class PolicyManagerProps {
    String clientAuth;
    String basicAuth;
    String url;
    String environment;
    String ecompRequestId;

    /**
     * Property passed to policy manager in the ClientAuth header.
     */
    public String getClientAuth() {
        return clientAuth;
    }

    public void setClientAuth(String clientAuth) {
        this.clientAuth = clientAuth;
    }

    /**
     * Property passed to policy manager in the BasicAuth header.
     */
    public String getBasicAuth() {
        return basicAuth;
    }

    public void setBasicAuth(String basicAuth) {
        this.basicAuth = basicAuth;
    }

    /**
     * URL of the policy manager.
     */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Property passed to policy manager in the Environment header.
     */
    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    /**
     * Property passed to policy manager in the X-ECOMP-RequestID header.
     */
    public String getEcompRequestId() {
        return ecompRequestId;
    }

    public void setEcompRequestId(String ecompRequestId) {
        this.ecompRequestId = ecompRequestId;
    }
}
