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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/*
CREATE TABLE IF NOT EXISTS sdnctl.CONFIG_PROPERTY_MAP (
   reference_key  VARCHAR(100) NOT NULL,
   reference_value  VARCHAR(250) NOT NULL,
   CONSTRAINT PK_CONFIG_PROPERTY_MAP PRIMARY KEY (reference_key)
   ) ENGINE=InnoDB DEFAULT CHARSET=latin1;
 */


@Entity
@Table(name = "CONFIG_PROPERTY_MAP")
public class ConfigPropertyMap implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "reference_key")
    private String referenceKey;

    @NotNull
    @Column(name = "reference_value")
    private String referenceValue;

    public String getReferenceKey() {
        return referenceKey;
    }

    public void setReferenceKey(String referenceKey) {
        this.referenceKey = referenceKey;
    }

    public String getReferenceValue() {
        return referenceValue;
    }

    public void setReferenceValue(String referenceValue) {
        this.referenceValue = referenceValue;
    }

    @Override
    public String toString() {
        return "ConfigPropertyMap [referenceKey=" + referenceKey + ", referenceValue=" + referenceValue + "]";
    }

}
