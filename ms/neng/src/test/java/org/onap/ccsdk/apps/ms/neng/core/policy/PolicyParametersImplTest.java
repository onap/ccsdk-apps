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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.ccsdk.apps.ms.neng.persistence.entity.IdentifierMap;
import org.onap.ccsdk.apps.ms.neng.persistence.entity.ServiceParameter;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.IdentifierMapRespository;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.ServiceParameterRepository;

@RunWith(MockitoJUnitRunner.class)
public class PolicyParametersImplTest {
    @Mock
    IdentifierMapRespository identifierMapRepository;
    @Mock
    ServiceParameterRepository serviceParameterRepository;
    @Mock
    IdentifierMap identifierMap;
    @InjectMocks
    PolicyParametersImpl policyParametersImpl;
    @Mock
    ServiceParameter sp;
    
    static final String RECIPE_SEPERATOR_PARAM = "recipe_separator";
    static final String MAX_GEN_ATTEMPT_PARAM = "max_gen_attempt";

    @Test
    public void policyParameterTest() throws Exception {
        Mockito.when(serviceParameterRepository.findByName(RECIPE_SEPERATOR_PARAM)).thenReturn(sp);
        Mockito.when(sp.getValue()).thenReturn("value");
        assertEquals("value", policyParametersImpl.getRecipeSeparator());

        Mockito.when(identifierMapRepository.findByPolicyFnName("name")).thenReturn(identifierMap);
        Mockito.when(identifierMap.getJsFnName()).thenReturn("jsFnName");
        assertEquals("jsFnName", policyParametersImpl.mapFunction("name"));

        Mockito.when(sp.getValue()).thenReturn("1");
        Mockito.when(serviceParameterRepository.findByName(MAX_GEN_ATTEMPT_PARAM)).thenReturn(sp);
        assertEquals(1, policyParametersImpl.getMaxGenAttempt());
    }
}
