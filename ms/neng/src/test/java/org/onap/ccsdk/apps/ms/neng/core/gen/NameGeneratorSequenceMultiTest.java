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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.ccsdk.apps.ms.neng.core.persistence.NamePersister;
import org.onap.ccsdk.apps.ms.neng.core.policy.FilePolicyReader;
import org.onap.ccsdk.apps.ms.neng.core.policy.PolicyFinder;
import org.onap.ccsdk.apps.ms.neng.core.policy.PolicyParameters;
import org.onap.ccsdk.apps.ms.neng.core.seq.SequenceGenerator;
import org.onap.ccsdk.apps.ms.neng.core.validator.AaiNameValidator;
import org.onap.ccsdk.apps.ms.neng.core.validator.DbNameValidator;

@RunWith(MockitoJUnitRunner.class)
public class NameGeneratorSequenceMultiTest {
    @Mock
    private PolicyParameters policyParams = mock(PolicyParameters.class);
    @Mock
    private PolicyFinder policyFinder = mock(PolicyFinder.class);
    @Mock
    private SequenceGenerator sequenceGenerator = mock(SequenceGenerator.class);
    @Mock
    private DbNameValidator dbValidator = mock(DbNameValidator.class);
    @Mock
    private AaiNameValidator aaiValidator = mock(AaiNameValidator.class);
    @Mock
    private NamePersister namePresister = mock(NamePersister.class);
    private Map<String, Map<String, String>> earlierNames = new HashMap<>();
    private Map<String, Map<String, ?>> policyCache = new HashMap<>();

    protected Map<String, String> makeOneRequest(String policy) {
        Map<String, String> requestElement = new HashMap<>();
        requestElement.put("external-key", "123456");
        requestElement.put("policy-instance-name", policy);
        requestElement.put("complex", "abcdeasdf");
        requestElement.put("naming-type", "VNF");
        requestElement.put("nf-naming-code", "ve1");
        requestElement.put("resource-name", "vnf-name");
        return requestElement;
    }

    /**
     * Setup for policy params.
     */
    @Before
    public void setupPolicyParams() throws Exception {
        Mockito.lenient().when(policyParams.mapFunction("substr")).thenReturn("substring");
        Mockito.lenient().when(policyParams.mapFunction("to_lower_case")).thenReturn("toLowerCase");
        Mockito.lenient().when(policyParams.mapFunction("to_upper_case")).thenReturn("toUpperCase");
    }

    @Test
    public void generate() throws Exception {
        String policyName = "SDNC_Policy.Config_MS_VNFCNamingPolicy_no_seq";
        Map<String, String> requestElement = makeOneRequest(policyName);
        List<Map<String, String>> allElements = new ArrayList<>();
        allElements.add(requestElement);

        Map<String, Object> policy = new FilePolicyReader("vnf_policy_seq.json").getPolicy();
        Mockito.lenient().when(policyParams.getMaxGenAttempt()).thenReturn(100);
        Mockito.lenient().when(policyFinder.findPolicy(policyName)).thenReturn(policy);
        Mockito.lenient().when(aaiValidator.validate(any(), any())).thenReturn(true);
        Mockito.lenient().when(dbValidator.validate(any(), any())).thenReturn(true);
        Mockito.lenient().when(dbValidator.validate(any(), eq("abcde001ve1"))).thenReturn(false);
        Mockito.lenient().when(dbValidator.validate(any(), eq("abcde002ve1"))).thenReturn(false);
        Mockito.lenient().when(dbValidator.validate(any(), eq("abcde003ve1"))).thenReturn(false);
        Mockito.lenient().when(dbValidator.validate(any(), eq("abcde004ve1"))).thenReturn(true);
        Mockito.lenient().when(sequenceGenerator.generate(any(), any(), any(), any(), eq(1))).thenReturn(1L);
        Mockito.lenient().when(sequenceGenerator.generate(any(), any(), any(), any(), eq(2))).thenReturn(2L);
        Mockito.lenient().when(sequenceGenerator.generate(any(), any(), any(), any(), eq(3))).thenReturn(3L);
        Mockito.lenient().when(sequenceGenerator.generate(any(), any(), any(), any(), eq(4))).thenReturn(4L);

        NameGenerator gen = new NameGenerator(policyFinder, policyParams, sequenceGenerator, dbValidator, aaiValidator,
                        namePresister, requestElement, allElements, earlierNames, policyCache, new ArrayList<>());

        Map<String, String> resp = gen.generate();
        assertEquals("vnf-name", resp.get("resource-name"));
        assertEquals("123456", resp.get("external-key"));
        assertEquals("abcde004ve1", resp.get("resource-value"));
    }
}

