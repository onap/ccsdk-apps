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
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
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
public class NameGeneratorDependencyOnUnnamedTest {
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

    /**
     * Setup policy params.
     */
    @Before
    public void setupPolicyParams() throws Exception {
        when(policyParams.mapFunction("substr")).thenReturn("substring");
        when(policyParams.mapFunction("to_lower_case")).thenReturn("toLowerCase");
        when(policyParams.mapFunction("to_upper_case")).thenReturn("toUpperCase");
    }

    protected Map<String, String> makeVmRequest(String policy) {
        Map<String, String> requestElement = new HashMap<>();
        requestElement.put("external-key", "923456");
        requestElement.put("policy-instance-name", policy);
        requestElement.put("naming-type", "VM");
        requestElement.put("resource-name", "vm-name");
        requestElement.put("complex", "abcdeasdf");
        return requestElement;
    }

    @Test
    public void generate() throws Exception {
        String policyName = "SDNC_Policy.Config_MS_VNF_VM_NamingPolicy";
        Map<String, String> requestElement2 = makeVmRequest(policyName);
        List<Map<String, String>> allElements = new ArrayList<>();
        allElements.add(requestElement2);

        Map<String, Object> policy = new FilePolicyReader("vnf_and_vm_policy.json").getPolicy();
        when(policyFinder.findPolicy(policyName)).thenReturn(policy);
        when(aaiValidator.validate(anyObject(), anyObject())).thenReturn(true);
        when(dbValidator.validate(anyObject(), anyObject())).thenReturn(true);
        when(sequenceGenerator.generate(anyObject(), anyObject(), anyObject(), anyObject(), anyInt())).thenReturn(1L);

        final List<Object> savedNames = new ArrayList<>();
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                savedNames.add(invocation.getArguments()[0]);
                return null;
            }
        }).when(namePresister).persist(anyObject());

        NameGenerator gen2 = new NameGenerator(policyFinder, policyParams, sequenceGenerator, dbValidator, aaiValidator,
                        namePresister, requestElement2, allElements, earlierNames, policyCache, new ArrayList<>());

        Map<String, String> resp2 = gen2.generate();
        assertEquals("vm-name", resp2.get("resource-name"));
        assertEquals("923456", resp2.get("external-key"));
        assertEquals("abcde001ve1mts001", resp2.get("resource-value"));

        assertEquals(2, savedNames.size());

        assertEquals("abcde001ve1", ((GeneratedName) savedNames.get(0)).getName());
        assertEquals(null, ((GeneratedName) savedNames.get(0)).getExternalId());
        assertEquals("VNF", ((GeneratedName) savedNames.get(0)).getElementType());
        assertEquals("abcde", ((GeneratedName) savedNames.get(0)).getPrefix());
        assertEquals(1, ((GeneratedName) savedNames.get(0)).getSequenceNumber().longValue());
        assertEquals("001", ((GeneratedName) savedNames.get(0)).getSequenceNumberEnc());
        assertEquals("ve1", ((GeneratedName) savedNames.get(0)).getSuffix());

        assertEquals("abcde001ve1mts001", ((GeneratedName) savedNames.get(1)).getName());
        assertEquals("923456", ((GeneratedName) savedNames.get(1)).getExternalId());
        assertEquals("VM", ((GeneratedName) savedNames.get(1)).getElementType());
        assertEquals("abcde001ve1mts", ((GeneratedName) savedNames.get(1)).getPrefix());
        assertEquals(1, ((GeneratedName) savedNames.get(1)).getSequenceNumber().longValue());
        assertEquals("001", ((GeneratedName) savedNames.get(1)).getSequenceNumberEnc());
        assertEquals(null, ((GeneratedName) savedNames.get(1)).getSuffix());
    }
}

