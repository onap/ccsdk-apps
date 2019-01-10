/*-
 * ============LICENSE_START=======================================================
 * ONAP : APPC
 * ================================================================================
 * Copyright (C) 2019 IBM.
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
 *
 * ============LICENSE_END=========================================================
 */
package org.onap.ccsdk.apps.ms.neng.service.extinf.impl;

import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import org.onap.ccsdk.apps.ms.neng.persistence.repository.PolicyDetailsRepository;

@RunWith(MockitoJUnitRunner.class)
public class PolicyFinderServiceDbImplTest {
    @InjectMocks
    @Spy
    PolicyFinderServiceDbImpl policyFinderServiceDb;
  
    @Before
    void setup(){
      policyFinderServiceDb = new PolicyFinderServiceDbImpl();
    }
  
    @Test
    public void testConfig() throws NullPointerException {
        assertNull(policyFinderServiceDb.getConfig("policy"));
    }
}
