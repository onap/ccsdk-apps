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

package org.onap.ccsdk.apps.ms.neng.persistence.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity representing the parameters and configuration of an external system/sub-system/application interface
 * from this micro-service.
 */
@Entity
@Table(name = "EXTERNAL_INTERFACE")
public class ExternalInterface implements Serializable {

    private static final long serialVersionUID = 1L;

    Integer externalInteraceId;
    String system;
    String param;
    String urlSuffix;
    Timestamp createdTime;
    String createdBy;
    Timestamp lastUpdatedTime;
    String lastUpdatedBy;

    /**
     * Primary key for this entity.
     */
    @Id
    @Column(name = "EXTERNAL_INTERFACE_ID")
    public Integer getExternalInteraceId() {
        return externalInteraceId;
    }

    public void setExternalInteraceId(Integer externalInteraceId) {
        this.externalInteraceId = externalInteraceId;
    }

    /**
     * Name of the interfacing system.
     */
    @Column(name = "SYSTEM")
    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    /**
     * A parameter related to the interfacing system.
     */
    @Column(name = "PARAM")
    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    /**
     * URL suffix for the interfacing system.
     */
    @Column(name = "URL_SUFFIX")
    public String getUrlSuffix() {
        return urlSuffix;
    }

    public void setUrlSuffix(String urlSuffix) {
        this.urlSuffix = urlSuffix;
    }

    /**
     * Time-stamp for this entity creation.
     */
    @Column(name = "CREATED_TIME")
    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * Identifier for the entity creation.
     */
    @Column(name = "CREATED_BY")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Time-stamp for this entity update.
     */
    @Column(name = "LAST_UPDATED_TIME")
    public Timestamp getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Timestamp lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    /**
     * Identifier for this entity update.
     */
    @Column(name = "LAST_UPDATED_BY")
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
