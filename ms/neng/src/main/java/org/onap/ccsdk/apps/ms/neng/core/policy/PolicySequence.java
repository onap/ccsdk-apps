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

package org.onap.ccsdk.apps.ms.neng.core.policy;

/**
 * Represents a sequence object in the policy, as a POJO.
 */
public class PolicySequence {
    /**
     * The type of the policy sequence. 
     */
    public enum Type { 
        ALPHA, NUMERIC 
    }

    private long startValue;
    private long increment;
    private long length;
    private Long maxValue;
    private String maxValueString;
    private Type type = Type.NUMERIC;
    private String scope;
    private String key;
    private String value;
    private Long lastReleaseSeqNumTried;

    public long getStartValue() {
        return startValue;
    }

    public void setStartValue(long startValue) {
        this.startValue = startValue;
    }

    public long getIncrement() {
        return increment;
    }

    public void setIncrement(long increment) {
        this.increment = increment;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    /**
     * Generate and return the maximum value for the sequence.
     */
    public long getMaxValue() {
        if (this.maxValue == null) {
            int base = 10;
            if (this.type == Type.ALPHA) {
                base = 36;
            }
            if (this.maxValueString != null) {
                try {
                    this.maxValue = Long.valueOf(this.maxValueString, base);
                } catch (Exception e) {
                    this.maxValue = null;
                }
            }
            if (this.maxValue == null) {
                long mlength = this.length;
                if (mlength <= 0) {
                    mlength = 3;
                }
                this.maxValue = (long) Math.pow(base, mlength) - 1;
            }
        }
        return maxValue;
    }

    public void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
    }

    public String getMaxValueString() {
        return maxValueString;
    }

    public void setMaxValueString(String maxValueString) {
        this.maxValueString = maxValueString;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Sets the type.
     */
    public void setType(String typeStr) {
        if ("numeric".equalsIgnoreCase(typeStr)) {
            setType(Type.NUMERIC);
        } else if ("alpha-numeric".equalsIgnoreCase(typeStr)) {
            setType(Type.ALPHA);
        }
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getLastReleaseSeqNumTried() {
        return lastReleaseSeqNumTried;
    }

    public void setLastReleaseSeqNumTried(Long lastReleaseSeqNumTried) {
        this.lastReleaseSeqNumTried = lastReleaseSeqNumTried;
    }
}
