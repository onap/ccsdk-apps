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

package org.onap.ccsdk.apps.ms.neng.core.gen;

/**
 * POJO representing a 'sequence' object defined in the policy, that controls the behavior
 * of sequences used as part of a generated name.
 */
public class SeqGenData {
    private String prefix;
    private String suffix;
    private long seq;
    private String seqEncoded;
    private String name;

    /**
     * Prefix for the sequence.
     */
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Suffix for the sequence.
     */
    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * The sequence item as an integer.
     */
    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    /**
     * The name generated from the sequence.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The sequence item encoded into the appropriate form.
     */
    public String getSeqEncoded() {
        return seqEncoded;
    }

    public void setSeqEncoded(String seqEncoded) {
        this.seqEncoded = seqEncoded;
    }
}
