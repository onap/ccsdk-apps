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

package org.onap.ccsdk.apps.ms.neng.core.seq;

import java.util.regex.Pattern;
import org.onap.ccsdk.apps.ms.neng.core.exceptions.NengException;
import org.onap.ccsdk.apps.ms.neng.core.policy.PolicySequence;
import org.onap.ccsdk.apps.ms.neng.persistence.entity.ServiceParameter;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.GeneratedNameRespository;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.ServiceParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Generates the sequence number part of the name.
 */
@Component
public class SequenceGenerator {
    @Autowired
    GeneratedNameRespository genNameRepo;

    @Autowired
    ServiceParameterRepository servParamRepo;

    /**
     * Returns the next free item in the sequence that can be used for a generated name.
     *  
     * @param prefix              the prefix for the generated name
     * @param suffix              the suffix for the generated name
     * @param seqParams           the sequence parameters as defined in the policy
     * @param lastSequenceNumber  the last sequence number used for name generation
     * @param attemptCount        the number of times a free sequence item was requested in this transaction
     */
    public long generate(String prefix, String suffix, PolicySequence seqParams, 
                    Long lastSequenceNumber, int attemptCount) throws Exception {
        Long newSeqNum = null;
        long nextIncrement = 1;
        if (seqParams.getLastReleaseSeqNumTried() != null) {
            newSeqNum = genNameRepo.findNextReleasedSeq(seqParams.getLastReleaseSeqNumTried(), prefix, suffix);
            if (newSeqNum == null) {
                throw new Exception("Name generation failed since all available sequence numbers are already used.");
            }
            return newSeqNum;
        }
        String dbMaxSeqNum = genNameRepo.findMaxByPrefixAndSuffix(prefix, suffix);
        if (attemptCount == 1) {
            if (dbMaxSeqNum != null) {
                newSeqNum = Long.parseLong(dbMaxSeqNum) + seqParams.getIncrement();
            } else {
                newSeqNum = seqParams.getStartValue();
            }
        } else {
            if (dbMaxSeqNum == null) {
                ServiceParameter incrementParam = servParamRepo.findByName("initial_increment");
                if (incrementParam != null) {
                    String[] increments = Pattern.compile(",").split(incrementParam.getValue());
                    if (attemptCount <= increments.length) {
                        nextIncrement = Long.parseLong(increments[attemptCount - 2]);
                    } else {
                        nextIncrement = Long.parseLong(increments[increments.length - 1]);
                    }
                    newSeqNum = lastSequenceNumber + nextIncrement;
                } else {
                    throw new NengException(
                                    "Name generation failed since initial_increment parameter was not found.");
                }
            } else {
                newSeqNum = lastSequenceNumber + seqParams.getIncrement();
            }
        }
        if (newSeqNum > seqParams.getMaxValue()) {
            newSeqNum = genNameRepo.findNextReleasedSeq(seqParams.getStartValue(), prefix, suffix);
            if (newSeqNum != null) {
                seqParams.setLastReleaseSeqNumTried(newSeqNum);
                return newSeqNum;
            } else {
                throw new NengException(
                                "Name generation failed since all available sequence numbers are already used.");
            }
        }
        return newSeqNum;
    }
}
