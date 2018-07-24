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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.ccsdk.apps.ms.neng.core.policy.PolicySequence;
import org.onap.ccsdk.apps.ms.neng.persistence.entity.ServiceParameter;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.GeneratedNameRespository;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.ServiceParameterRepository;


@RunWith(MockitoJUnitRunner.class)
public class TestSequenceGenerator {
    @Mock
    private GeneratedNameRespository genNameRepo = mock(GeneratedNameRespository.class);
    @Mock
    private ServiceParameterRepository servParamRepo = mock(ServiceParameterRepository.class);
    @Mock
    private PolicySequence params = mock(PolicySequence.class);
    @Mock
    private ServiceParameter sp = mock(ServiceParameter.class);
    @InjectMocks
    SequenceGenerator sg;

    @Test
    public void testGenerate() throws Exception {
        assertEquals(0, sg.generate("zSSRX1234", null, params, null, 1));
        
        Mockito.when(params.getLastReleaseSeqNumTried()).thenReturn(null);
        Mockito.when(genNameRepo.findMaxByPrefixAndSuffix("zSSRX1234", null)).thenReturn("4");

        assertEquals(0, sg.generate("zSSRX1234", null, params, null, 1));

        Mockito.when(genNameRepo.findMaxByPrefixAndSuffix("zSSRX1234", null)).thenReturn("2");
        Mockito.when(genNameRepo.findMaxByPrefixAndSuffix("zSSRX1234", null)).thenReturn(null);
        Mockito.when(servParamRepo.findByName("initial_increment")).thenReturn(sp);
        Mockito.when(sp.getValue()).thenReturn("1");

        assertEquals(0, sg.generate("zSSRX1234", null, params, 1L, 2));

        Mockito.when(genNameRepo.findNextReleasedSeq(0L, "zSSRX1234", null)).thenReturn(null);
        assertEquals(0, sg.generate("zSSRX1234", null, params, null, 1));
    }

    @Test(expected = Exception.class)
    public void exceltionTest() throws Exception {
        Mockito.when(genNameRepo.findNextReleasedSeq(1L, "zSSRX1234", null)).thenReturn(null);
        Mockito.when(params.getLastReleaseSeqNumTried()).thenReturn(1L);
        sg.generate("zSSRX1234", null, params, null, 1);
    }

    @Test
    public void testAlreadyUsedSequesnce() throws Exception {
        Mockito.when(genNameRepo.findMaxByPrefixAndSuffix("zSSRX1234", null)).thenReturn("1");
        Mockito.when(sp.getValue()).thenReturn("4");
        Mockito.when(params.getIncrement()).thenReturn(2L);
        assertEquals(0L, sg.generate("zSSRX1234", null, params, 2L, 0));
    }
}
