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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents an entity representing policies stored in this micro-service (temporarily).
 */
@Entity
@Table(name = "POLICY_MAN_SIM")
public class PolicyDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    Integer policyId;
    String policyName;
    String policyResponse;
    Timestamp createdTime;

    /**
     * Primary key for this entity.
     */
    @Id
    @Column(name = "POLICY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Integer policyId) {
        this.policyId = policyId;
    }

    /**
     * Name of the policy.
     */
    @Column(name = "POLICY_NAME")
    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    /**
     * The text form of the policy.
     */
    @Column(name = "POLICY_RESPONSE")
    public String getPolicyResponse() {
        return policyResponse;
    }

    public void setPolicyResponse(String policyResponse) {
        this.policyResponse = policyResponse;
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
}
