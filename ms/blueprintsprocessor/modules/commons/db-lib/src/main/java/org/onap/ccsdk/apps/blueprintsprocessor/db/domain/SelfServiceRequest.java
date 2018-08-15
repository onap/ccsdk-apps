/*
 *  Copyright © 2017-2018 AT&T Intellectual Property.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.onap.ccsdk.apps.blueprintsprocessor.db.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/*
CREATE TABLE IF NOT EXISTS sdnctl.SELF_SERVICE_REQUEST (
   self_service_request_id  VARCHAR(50) NOT NULL,
   sub_request_id           VARCHAR(50)   NULL          DEFAULT NULL,
   originator           VARCHAR(50)   NOT NULL,
   mode                 VARCHAR(50)   NOT NULL,
   ttl                  INT   NOT NULL,
   status               VARCHAR(50)   NOT NULL,
   request_date         DATETIME      NOT NULL      DEFAULT CURRENT_TIMESTAMP,
   ack_date             DATETIME      NULL          DEFAULT NULL,
   response_date        DATETIME      NULL          DEFAULT NULL,
   failed_date        DATETIME      NULL          DEFAULT NULL,
   request_message      LONGTEXT      NOT NULL,
   ack_message          LONGTEXT      NULL          DEFAULT NULL,
   response_message     LONGTEXT      NULL          DEFAULT NULL,
   failed_message     LONGTEXT      NULL          DEFAULT NULL,
   PRIMARY KEY PK_TRANSACTION_MESSAGE (self_service_request_id)
   ) ENGINE=InnoDB DEFAULT CHARSET=latin1;
 */
@Entity
@Table(name = "SELF_SERVICE_REQUEST")
public class SelfServiceRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "self_service_request_id")
    private String id = UUID.randomUUID().toString();

    @Column(name = "sub_request_id")
    private String subRequestId;

    @NotNull
    @Column(name = "originator")
    private String originator;

    @NotNull
    @Column(name = "mode")
    private String mode;
    @NotNull
    @Column(name = "ttl")
    private Integer ttl;

    @NotNull
    @Column(name = "status")
    private String status;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "request_date")
    private Date requestDate;

    @Column(name = "ack_date")
    private Date ackDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "response_date")
    private Date responseDate;

    @NotNull
    @Lob
    @Column(name = "request_message")
    private String requestMessage;

    @Lob
    @Column(name = "ack_message")
    private String ackMessage;

    @Lob
    @Column(name = "response_message")
    private String responseMessage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "failed_date")
    private Date failedDate;

    @Lob
    @Column(name = "failed_message")
    private String failedMessage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubRequestId() {
        return subRequestId;
    }

    public void setSubRequestId(String subRequestId) {
        this.subRequestId = subRequestId;
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getTtl() {
        return ttl;
    }

    public void setTtl(Integer ttl) {
        this.ttl = ttl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getAckDate() {
        return ackDate;
    }

    public void setAckDate(Date ackDate) {
        this.ackDate = ackDate;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public String getAckMessage() {
        return ackMessage;
    }

    public void setAckMessage(String ackMessage) {
        this.ackMessage = ackMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Date getFailedDate() {
        return failedDate;
    }

    public void setFailedDate(Date failedDate) {
        this.failedDate = failedDate;
    }

    public String getFailedMessage() {
        return failedMessage;
    }

    public void setFailedMessage(String failedMessage) {
        this.failedMessage = failedMessage;
    }
}
