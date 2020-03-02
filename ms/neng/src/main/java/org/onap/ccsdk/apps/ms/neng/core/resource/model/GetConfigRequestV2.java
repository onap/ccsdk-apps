/*-
 * ============LICENSE_START=======================================================
 * ONAP : CCSDK.apps
 * ================================================================================
 * Copyright (C) 2020 AT&T Intellectual Property. All rights reserved.
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

package org.onap.ccsdk.apps.ms.neng.core.resource.model;

import java.io.Serializable;
import java.util.Map;

/**
 * POJO representing policy manager get-config request, V2.
 */
public class GetConfigRequestV2 implements Serializable {
    private static final long serialVersionUID = -8039686696076337054L;

    private String onapName;
    private String onapComponent;
    private String onapInstance;
    private String requestId;
    private String action;
    private Map<String,Object> resource;

    public String getOnapName() {
        return onapName;
    }

    public void setOnapName(String onapName) {
        this.onapName = onapName;
    }

    public String getOnapComponent() {
        return onapComponent;
    }

    public void setOnapComponent(String onapComponent) {
        this.onapComponent = onapComponent;
    }

    public String getOnapInstance() {
        return onapInstance;
    }

    public void setOnapInstance(String onapInstance) {
        this.onapInstance = onapInstance;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Map<String, Object> getResource() {
        return resource;
    }

    public void setResource(Map<String, Object> resource) {
        this.resource = resource;
    }

}
