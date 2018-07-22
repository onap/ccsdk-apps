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
 * Represents a generated name.
 */
@Entity
@Table(name = "GENERATED_NAME")
public class GeneratedName implements Serializable {
    private static final long serialVersionUID = 1L;

    Integer generatedNameId;
    String externalId;
    Long sequenceNumber;
    String sequenceNumberEnc;
    String elementType;
    String name;
    String prefix;
    String suffix;
    String isReleased;
    Timestamp createdTime;
    String createdBy;
    Timestamp lastUpdatedTime;
    String lastUpdatedBy;

    /**
     * Primary key for this entity.
     */
    @Id
    @Column(name = "GENERATED_NAME_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getGeneratedNameId() {
        return generatedNameId;
    }

    public void setGeneratedNameId(Integer generatedNameId) {
        this.generatedNameId = generatedNameId;
    }

    /**
     * Sequence number used for generation of this entity, as an integer.
     */
    @Column(name = "SEQUNCE_NUMBER")
    public Long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Sequence number used for generation of this entity, in the form encoded in the name.
     */
    @Column(name = "SEQUENCE_NUMBER_ENC")
    public String getSequenceNumberEnc() {
        return sequenceNumberEnc;
    }

    public void setSequenceNumberEnc(String sequenceNumberEnc) {
        this.sequenceNumberEnc = sequenceNumberEnc;
    }

    /**
     * Type of the element.
     */
    @Column(name = "ELEMENT_TYPE")
    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    /**
     * The generated name.
     */
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Prefix of the name.
     */
    @Column(name = "PREFIX")
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Suffix of the name.
     */
    @Column(name = "SUFFIX")
    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * Indicator telling if the name is released from active use.
     */
    @Column(name = "IS_RELEASED")
    public String getIsReleased() {
        return isReleased;
    }

    public void setIsReleased(String isReleased) {
        this.isReleased = isReleased;
    }

    /**
     * Time-stamp for this entity creation.
     */
    @Column(name = "CREATED_TIME", insertable = false)
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

    /**
     * External system ID mapped to this entity/name.
     */
    @Column(name = "EXTERNAL_ID")
    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
