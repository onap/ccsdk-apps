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
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/*
CREATE TABLE IF NOT EXISTS sdnctl.CONFIG_TRANSACTION_LOG (
   config_transaction_log_id  VARCHAR(50) NOT NULL,
   request_id                 VARCHAR(50)   NULL          DEFAULT NULL,
   message_type               VARCHAR(100)  NULL          DEFAULT NULL,
   creation_date              DATETIME      NOT NULL      DEFAULT CURRENT_TIMESTAMP,
   message                           LONGTEXT      NULL          DEFAULT NULL,
   PRIMARY KEY PK_CONFIG_TRANSACTION_LOG (config_transaction_log_id)
   ) ENGINE=InnoDB DEFAULT CHARSET=latin1;
 */
@Entity
@Table(name = "CONFIG_TRANSACTION_LOG")
public class TransactionLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "config_transaction_log_id")
    private String transactionLogId = UUID.randomUUID().toString();

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "message_type")
    private String messageType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    private Date creationDate;
    @Lob
    @Column(name = "message")
    private String message;

    public TransactionLog() {

    }

    public TransactionLog(String requestId, String messageType, String message) {
        this.requestId = requestId;
        this.messageType = messageType;
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder("[");
        buffer.append("transactionLogId =" + transactionLogId);
        buffer.append(", requestId =" + requestId);
        buffer.append(", messageType =" + messageType);
        buffer.append(", creationDate =" + creationDate);
        buffer.append("]");
        return buffer.toString();
    }

    public String getTransactionLogId() {
        return transactionLogId;
    }

    public void setTransactionLogId(String transactionLogId) {
        this.transactionLogId = transactionLogId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUniqueId() {
        return UUID.randomUUID().toString();
    }


}
