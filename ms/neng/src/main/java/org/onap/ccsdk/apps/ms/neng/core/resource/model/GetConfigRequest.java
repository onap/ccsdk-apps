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

package org.onap.ccsdk.apps.ms.neng.core.resource.model;

import java.io.Serializable;
import java.util.Map;

/**
 * POJO representing policy manager get-config request.
 */
public class GetConfigRequest implements Serializable {
    private static final long serialVersionUID = -8039686696076337053L;

    private static Map<String, Object> configAttributes;
    private static String configName;
    private static String ecompName;
    private static String policyName;
    boolean unique;

    public Map<String, Object> getConfigAttributes() {
        return configAttributes;
    }

    public void setConfigAttributes(Map<String, Object> configAttributes) {
        this.configAttributes = configAttributes;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getEcompName() {
        return ecompName;
    }

    public void setEcompName(String ecompName) {
        this.ecompName = ecompName;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }
}
