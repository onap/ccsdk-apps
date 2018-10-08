/*-
 * ============LICENSE_START=======================================================
 * ONAP : CCSDK.apps
 * ================================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Modifications Copyright (C) 2018 IBM.
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

package org.onap.ccsdk.apps.ms.neng.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.ccsdk.apps.ms.neng.core.exceptions.NengException;
import org.onap.ccsdk.apps.ms.neng.core.persistence.NamePersister;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.HelloWorld;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.NameGenRequest;
import org.onap.ccsdk.apps.ms.neng.core.validator.AaiNameValidator;
import org.onap.ccsdk.apps.ms.neng.core.validator.ExternalKeyValidator;
import org.onap.ccsdk.apps.ms.neng.persistence.entity.GeneratedName;
import org.onap.ccsdk.apps.ms.neng.persistence.entity.PolicyDetails;
import org.onap.ccsdk.apps.ms.neng.persistence.entity.ServiceParameter;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.GeneratedNameRespository;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.PolicyDetailsRepository;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.ServiceParameterRepository;

@RunWith(MockitoJUnitRunner.class)
public class SpringServiceTest {

    @Mock
    ExternalKeyValidator externalKeyValidator;
    @Mock
    ServiceParameter param;
    @Mock
    ServiceParameterRepository serviceParamRepo;
    @Mock
    PolicyDetails policyDetails;
    @Mock
    PolicyDetailsRepository policyDetailsRepository;
    @Mock
    GeneratedNameRespository generatedNameRepository;
    @Mock
    AaiNameValidator aaiNameValidator;
    @Mock
    NamePersister namePersister;

    @InjectMocks
    SpringServiceImpl springserviceImpl;

    Map<String, String> req = new HashMap<>();
    List<Map<String, String>> rsp = new ArrayList<>();
    NameGenRequest request = new NameGenRequest();

    {
        req.put("external-key", "Xyx-zzk");
        req.put("policy-instance-name", "testDbPolicy66");
        req.put("COMPLEX", "TRLAKDG");
        req.put("NFC-NAMING-CODE", "ESP");
        req.put("CLOUD_REGION_ID", "SSR");
        req.put("NF_CODE", "X1234");
        req.put("resource-name", "vm-name");
        req.put("naming-type", "VM");
        req.put("nf-role", "vPE");
        rsp.add(req);
        request.setElements(rsp);
    }

    @Test(expected = Exception.class)
    public void genNetworkElementNameTest() throws Exception {
        Mockito.when(externalKeyValidator.isPresent(req.get("external-key"))).thenReturn(false);
        Mockito.when(serviceParamRepo.findByName("use_db_policy")).thenReturn(param);
        springserviceImpl.genNetworkElementName(request);
    }
    
    @Test
    public void genNetworkElementNameTest_empty_elements() throws Exception {
        NameGenRequest request = new NameGenRequest();
        request.setElements(new ArrayList<>());
        springserviceImpl.genNetworkElementName(request);

    }
    
    @Test
    public void genNetworkElementNameTest_extkey_error_3() throws Exception {
        NameGenRequest request = new NameGenRequest();
        List<Map<String, String>> rsp = new ArrayList<>();
        request.setElements(rsp);
        springserviceImpl.genNetworkElementName(request);
    }
    
    @Test(expected = Exception.class)
    public void genNetworkElementNameTest_useDb() throws Exception {
        Mockito.when(externalKeyValidator.isPresent(req.get("external-key"))).thenReturn(false);
        Mockito.when(serviceParamRepo.findByName("use_db_policy")).thenReturn(param);
        request.setUseDb(true);
        springserviceImpl.genNetworkElementName(request);
    }

    @Test
    public void getPolicyDetailsTest() {
        Mockito.when(policyDetailsRepository.findPolicyResponseByName("testDbPolicy66")).thenReturn(policyDetails);
        org.junit.Assert.assertNotNull(springserviceImpl.getPolicyDetails("testDbPolicy66"));
    }

    @Test
    public void addPolicy() throws Exception {
        springserviceImpl.addPolicy(req);
    }

    @Test
    public void releaseNetworkElementNameTest() throws Exception {
        GeneratedName gn = new GeneratedName();
        List<GeneratedName> generatedNameList = new ArrayList<>();
        generatedNameList.add(gn);

        Mockito.when(generatedNameRepository.findByExternalId(req.get("external-key"))).thenReturn(generatedNameList);
        Assert.assertNotNull(springserviceImpl.releaseNetworkElementName(request));
    }
    
    @Test(expected = Exception.class)
    public void releaseNetworkElementNameTestForNull() throws Exception {
        springserviceImpl.releaseNetworkElementName(null);
    }
    
    @Test(expected = Exception.class)
    public void releaseNetworkElementNameTest_exp() throws Exception {
        GeneratedName gn = new GeneratedName();
        List<GeneratedName> generatedNameList = new ArrayList<>();
        generatedNameList.add(gn);

        Mockito.when(generatedNameRepository.findByExternalId(req.get("external-key"))).thenThrow(new Exception());
        springserviceImpl.releaseNetworkElementName(request);
    }

    @Test(expected = Exception.class)
    public void releaseNetworkElementNameTest_exp_neng() throws Exception {
        GeneratedName gn = new GeneratedName();
        List<GeneratedName> generatedNameList = new ArrayList<>();
        generatedNameList.add(gn);

        Mockito.when(generatedNameRepository.findByExternalId(req.get("external-key"))).thenThrow(
                        new NengException("Failed"));
        springserviceImpl.releaseNetworkElementName(request);
    }
    
    @Test
    public void testGetQuickHello() {
        Assert.assertTrue(springserviceImpl.getQuickHello("testMessage") instanceof HelloWorld);
    }
    
    @Test
    public void testGetQuickHelloForNullMessage() {
        Assert.assertTrue(springserviceImpl.getQuickHello("") instanceof HelloWorld);
    }
    
    @Test
    public void updateNetworkElementName() throws Exception {
        req.clear();
        req.put("external-key", "TST-UV1");
        req.put("resource-name", "vnf-name");
        req.put("resource-value", "dst1000tv1");
        req.put("naming-type", "vnf");
        
        GeneratedName gn = new GeneratedName();
        gn.setExternalId("TST-UV1");
        gn.setElementType("VNF");
        gn.setName("dst1000tv1");
        
        List<GeneratedName> generatedNameList = new ArrayList<>();
        generatedNameList.add(gn);
        Mockito.when(namePersister.findByExternalIdAndElementType(req.get("external-key"), "VNF")).thenReturn(gn);
        Mockito.when(aaiNameValidator.validate(Matchers.anyString(), Matchers.anyString())).thenReturn(true);
        Assert.assertNotNull(springserviceImpl.updateNetworkElementName(request));
        Mockito.verify(namePersister, Mockito.times(1)).persist(gn);
    }
    
    @Test(expected = NengException.class)
    public void updateNetworkElementName_Aai_Fail() throws Exception {
        req.clear();
        req.put("external-key", "TST-UV1");
        req.put("resource-name", "vnf-name");
        req.put("resource-value", "dst1000tv1");
        req.put("naming-type", "vnf");
        
        GeneratedName gn = new GeneratedName();
        gn.setExternalId("TST-UV1");
        gn.setElementType("VNF");
        gn.setName("dst1000tv1");
        
        Mockito.when(namePersister.findByExternalIdAndElementType(req.get("external-key"), "VNF")).thenReturn(null);
        Mockito.when(aaiNameValidator.validate(Matchers.anyString(), Matchers.anyString())).thenReturn(false);
        springserviceImpl.updateNetworkElementName(request);
    }
}
