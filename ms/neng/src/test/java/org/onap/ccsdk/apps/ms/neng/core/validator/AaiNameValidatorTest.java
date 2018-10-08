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

package org.onap.ccsdk.apps.ms.neng.core.validator;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.ExternalInterfaceRespository;
import org.onap.ccsdk.apps.ms.neng.service.extinf.impl.AaiServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class AaiNameValidatorTest {
    @Mock
    AaiServiceImpl aaiImpl;
    
    @Mock
    ExternalInterfaceRespository dbStuff;
    
    @InjectMocks
    AaiNameValidator nameValidator;
    
    @Test
    public void testValidate() throws Exception {
        when(dbStuff.getUriByNameType(Matchers.anyString())).thenReturn(null);
        assertTrue(nameValidator.validate("VNF", "xyz112uyv"));
    }
}

