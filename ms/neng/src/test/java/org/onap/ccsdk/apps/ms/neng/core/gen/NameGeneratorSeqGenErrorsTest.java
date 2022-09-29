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
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.onap.ccsdk.apps.ms.neng.core.persistence.NamePersister;
import org.onap.ccsdk.apps.ms.neng.core.policy.FilePolicyReader;
import org.onap.ccsdk.apps.ms.neng.core.policy.PolicyFinder;
import org.onap.ccsdk.apps.ms.neng.core.policy.PolicyParameters;
import org.onap.ccsdk.apps.ms.neng.core.seq.SequenceGenerator;
import org.onap.ccsdk.apps.ms.neng.core.validator.AaiNameValidator;
import org.onap.ccsdk.apps.ms.neng.core.validator.DbNameValidator;
import org.onap.ccsdk.apps.ms.neng.persistence.entity.GeneratedName;

@RunWith(MockitoJUnitRunner.class)
public class NameGeneratorSeqGenErrorsTest {
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

    protected Map<String, String> makeVmRequest(String policy) {
        Map<String, String> requestElement = new HashMap<>();
        requestElement.put("external-key", "923456");
        requestElement.put("policy-instance-name", policy);
        requestElement.put("naming-type", "VM");
        requestElement.put("resource-name", "vm-name");
        requestElement.put("complex", "abcdeasdf");
        return requestElement;
    }

    protected Map<String, String> makeVnfRequest(String policy) {
        Map<String, String> requestElement = new HashMap<>();
        requestElement.put("external-key", "123456");
        requestElement.put("policy-instance-name", policy);
        requestElement.put("naming-type", "VNF");
        requestElement.put("resource-name", "vnf-name");
        requestElement.put("complex", "abcdeasdf");
        return requestElement;
    }

    /**
     * Setup policy params.
     */
    @Before
    public void setupPolicyParams() throws Exception {
        Mockito.lenient().when(policyParams.mapFunction("substr")).thenReturn("substring");
        Mockito.lenient().when(policyParams.mapFunction("to_lower_case")).thenReturn("toLowerCase");
        Mockito.lenient().when(policyParams.mapFunction("to_upper_case")).thenReturn("toUpperCase");
    }

    /*
    @Test
    public void someNamesRejectedByAai() throws Exception {
        String policyName = "SDNC_Policy.Config_MS_VNF_VM_NamingPolicy";
        Map<String, String> requestElement2 = makeVmRequest(policyName);
        List<Map<String, String>> allElements = new ArrayList<>();
        allElements.add(requestElement2);

        Map<String, Object> policy = new FilePolicyReader("vnf_and_vm_policy.json").getPolicy();
        Mockito.lenient().when(policyParams.getMaxGenAttempt()).thenReturn(100);
        Mockito.lenient().when(policyFinder.findPolicy(policyName)).thenReturn(policy);

        Mockito.lenient().when(aaiValidator.validate(any(), any())).thenReturn(true);
        Mockito.lenient().when(aaiValidator.validate(eq("VNF"), any())).thenReturn(true);
        Mockito.lenient().when(aaiValidator.validate(eq("VNF"), eq("abcde001ve1"))).thenReturn(false);

        Mockito.lenient().when(dbValidator.validate(any(), any())).thenReturn(true);
        Mockito.lenient().when(sequenceGenerator.generate(any(), any(), any(), any(), eq(1))).thenReturn(1L);
        Mockito.lenient().when(sequenceGenerator.generate(eq("abcde"), any(), any(), any(), eq(2))).thenReturn(2L);

        final List<Object> savedNames = new ArrayList<>();
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                savedNames.add(invocation.getArguments()[0]);
                return null;
            }
        }).when(namePresister).persist(any());

        NameGenerator gen2 = new NameGenerator(policyFinder, policyParams, sequenceGenerator, dbValidator, aaiValidator,
                        namePresister, requestElement2, allElements, earlierNames, policyCache, new ArrayList<>());

        Map<String, String> resp2 = gen2.generate();
        assertEquals("vm-name", resp2.get("resource-name"));
        assertEquals("923456", resp2.get("external-key"));
        assertEquals("abcde002ve1mts001", resp2.get("resource-value"));

        assertEquals(3, savedNames.size());

        {
            GeneratedName genName = (GeneratedName) savedNames.get(0);
            assertEquals("abcde001ve1", genName.getName());
            assertEquals("AAI-BACKPOPULATE", genName.getExternalId());
            assertEquals("VNF", genName.getElementType());
            assertEquals("abcde", genName.getPrefix());
            assertEquals(1, genName.getSequenceNumber().longValue());
            assertEquals("001", genName.getSequenceNumberEnc());
            assertEquals("ve1", genName.getSuffix());
        }
        {
            GeneratedName genName = (GeneratedName) savedNames.get(1);
            assertEquals("abcde002ve1", genName.getName());
            assertEquals(null, genName.getExternalId());
            assertEquals("VNF", genName.getElementType());
            assertEquals("abcde", genName.getPrefix());
            assertEquals(2, genName.getSequenceNumber().longValue());
            assertEquals("002", genName.getSequenceNumberEnc());
            assertEquals("ve1", genName.getSuffix());

        }
        {
            GeneratedName genName = (GeneratedName) savedNames.get(2);
            assertEquals("abcde002ve1mts001", genName.getName());
            assertEquals("923456", genName.getExternalId());
            assertEquals("VM", genName.getElementType());
            assertEquals("abcde002ve1mts", genName.getPrefix());
            assertEquals(1, genName.getSequenceNumber().longValue());
            assertEquals("001", genName.getSequenceNumberEnc());
            assertEquals(null, genName.getSuffix());

        }
    }
    */

    @Test
    public void allRejected() throws Exception {
        String policyName = "SDNC_Policy.Config_MS_VNF_VM_NamingPolicy";
        Map<String, String> requestElement2 = makeVmRequest(policyName);
        List<Map<String, String>> allElements = new ArrayList<>();
        allElements.add(requestElement2);

        Map<String, Object> policy = new FilePolicyReader("vnf_and_vm_policy.json").getPolicy();
        Mockito.lenient().when(policyParams.getMaxGenAttempt()).thenReturn(3);
        Mockito.lenient().when(policyFinder.findPolicy(policyName)).thenReturn(policy);
        Mockito.lenient().when(dbValidator.validate(any(), any())).thenReturn(false);
        Mockito.lenient().when(sequenceGenerator.generate(any(), any(), any(), any(), eq(1))).thenReturn(1L);
        Mockito.lenient().when(sequenceGenerator.generate(any(), any(), any(), any(), eq(2))).thenReturn(2L);
        Mockito.lenient().when(sequenceGenerator.generate(any(), any(), any(), any(), eq(3))).thenReturn(3L);

        NameGenerator gen = new NameGenerator(policyFinder, policyParams, sequenceGenerator, dbValidator, aaiValidator,
                        namePresister, requestElement2, allElements, earlierNames, policyCache, new ArrayList<>());

        try {
            gen.generate();
            fail("Expecting exception");
        } catch (Exception e) {
            assertEquals("Could not generate a name successfully for policy "
                            + "SDNC_Policy.Config_MS_VNF_VM_NamingPolicy and naming-type VNF "
                            + "even after 3 attempts.", e.getMessage());
            return;
        }
        fail("Expecting exception");
    }

    @Test
    public void allRejectedSequenceLess() throws Exception {
        String policyName = "SDNC_Policy.Config_MS_VNF_VM_NamingPolicy";
        Map<String, String> requestElement2 = makeVnfRequest(policyName);
        List<Map<String, String>> allElements = new ArrayList<>();
        allElements.add(requestElement2);

        Map<String, Object> policy = new FilePolicyReader("vnf_policy_no_seq.json").getPolicy();
        Mockito.lenient().when(policyParams.getMaxGenAttempt()).thenReturn(3);
        Mockito.lenient().when(policyFinder.findPolicy(policyName)).thenReturn(policy);
        Mockito.lenient().when(dbValidator.validate(any(), any())).thenReturn(false);

        NameGenerator gen = new NameGenerator(policyFinder, policyParams, sequenceGenerator, dbValidator, aaiValidator,
                        namePresister, requestElement2, allElements, earlierNames, policyCache, new ArrayList<>());

        try {
            gen.generate();
            fail("Expecting exception");
        } catch (Exception e) {
            assertEquals("Could not generate a valid name successfully for policy "
                            + "SDNC_Policy.Config_MS_VNF_VM_NamingPolicy "
                            + "and naming-type VNF. DB Name validation failed", e.getMessage());
            return;
        }
        fail("Expecting exception");
    }
}

