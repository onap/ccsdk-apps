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

package org.onap.ccsdk.apps.ms.neng.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.onap.ccsdk.apps.ms.neng.core.exceptions.NengException;
import org.onap.ccsdk.apps.ms.neng.core.persistence.NamePersister;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.NameGenRequest;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.NameGenResponse;
import org.onap.ccsdk.apps.ms.neng.core.service.rs.RestServiceImpl;
import org.onap.ccsdk.apps.ms.neng.persistence.entity.ExternalInterface;
import org.onap.ccsdk.apps.ms.neng.persistence.entity.GeneratedName;
import org.onap.ccsdk.apps.ms.neng.persistence.entity.PolicyDetails;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.ExternalInterfaceRespository;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.PolicyDetailsRepository;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.ServiceParameterRepository;
import org.onap.ccsdk.apps.ms.neng.service.extinf.impl.AaiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class SpringServiceIntTest {
    @Autowired
    TestEntityManager entityManager;
    @SpyBean
    SpringService springService;
    @Autowired
    NamePersister namePersister;
    @Autowired
    @Qualifier("policyMgrRestTempBuilder")
    RestTemplateBuilder policyMgrRestTempBuilder;
    @Mock
    RestTemplate restTemplate;
    @Autowired
    PolicyDetailsRepository policyDetailsRepo;
    @Autowired
    ServiceParameterRepository serviceParamRepo;
    @Autowired
    AaiServiceImpl aaiServiceImpl;
    @Autowired
    RestServiceImpl restServiceImpl;
    @Autowired
    ExternalInterfaceRespository extIntRepo;

    @Before
    public void setup() {
        doReturn(restTemplate).when(policyMgrRestTempBuilder).build();
    }

    @Test
    public void testObjects() {
        assertNotNull(entityManager);
        assertNotNull(namePersister);
        assertNotNull(springService);
    }

    @Test
    public void testNamePersiser() throws Exception {
        GeneratedName name = new GeneratedName();
        name.setName("abcd6ytx");
        name.setPrefix("dlpv");
        name.setSuffix("ytx");
        name.setSequenceNumber(006L);
        name.setElementType("VNF");
        name.setGeneratedNameId(1000);
        name.setExternalId("EXT-11");

        namePersister.persist(name);
        name = namePersister.findBy("VNF", "abcd6ytx", null);
        assertNotNull(name);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGenName_1() throws Exception {
        ResponseEntity<Object> resp = new ResponseEntity<Object>(
                        getConfigResponse("JQINSRIOV.Config_MS_SriovBigJson.1.xml"), HttpStatus.OK);
        when(restTemplate.exchange(Matchers.any(RequestEntity.class), Matchers.any(Class.class))).thenReturn(resp);
        when(aaiServiceImpl.validate(Matchers.anyString(), Matchers.anyString())).thenReturn(true);
        NameGenRequest request = nameGenRequest_1();
        NameGenResponse genresp = springService.genNetworkElementName(request);
        assertTrue("vnf-name".equals(genresp.getElements().get(0).get("resource-name")));
    }

    NameGenRequest nameGenRequest_1() {
        Map<String, String> vnfMap = new HashMap<>();
        vnfMap.put("external-key", "VQA-UN8");
        vnfMap.put("policy-instance-name", "JQINSRIOV.Config_MS_SriovBigJson.1.xml");
        vnfMap.put("complex", "vnfunfc");
        vnfMap.put("NF_NAMING_CODE", "xyFg12");
        vnfMap.put("resource-name", "vnf-name");
        vnfMap.put("naming-type", "VNF");
        vnfMap.put("nf-role", "vPE");

        List<Map<String, String>> elements = new ArrayList<>();
        elements.add(vnfMap);
        NameGenRequest request = new NameGenRequest();
        request.setElements(elements);

        return request;
    }

    NameGenRequest nameGenRequestRelease() {
        NameGenRequest request = new NameGenRequest();
        Map<String, String> vnfMap = new HashMap<>();
        vnfMap.put("external-key", "VQA-UN8");
        List<Map<String, String>> elements = new ArrayList<>();
        elements.add(vnfMap);
        request.setElements(elements);

        return request;
    }

    Object getConfigResponse(String policyName) throws Exception {
        ObjectMapper objectmapper = new ObjectMapper();
        objectmapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        PolicyDetails policyDetails = policyDetailsRepo.findPolicyResponseByName(policyName);
        List<Map<Object, Object>> respObj = objectmapper.readValue(policyDetails.getPolicyResponse(),
                        new TypeReference<List<Map<Object, Object>>>() {});
        return respObj;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRestGenerateNetworkElementName() throws Exception {
        NameGenRequest request = nameGenRequest_1();
        ResponseEntity<Object> resp = new ResponseEntity<Object>(
                        getConfigResponse("JQINSRIOV.Config_MS_SriovBigJson.1.xml"), HttpStatus.OK);
        when(restTemplate.exchange(Matchers.any(RequestEntity.class), Matchers.any(Class.class))).thenReturn(resp);
        when(aaiServiceImpl.validate(Matchers.anyString(), Matchers.anyString())).thenReturn(true);
        restServiceImpl.generateNetworkElementName(request);
    }

    @Test
    public void testRestGenerateNetworkElementName_exp() throws Exception {
        NameGenRequest request = nameGenRequest_1();
        doThrow(new NengException("")).when(springService).genNetworkElementName(request);
        restServiceImpl.generateNetworkElementName(request);
    }

    @Test
    public void testRestReleaseNetworkElementName() throws Exception {
        NameGenRequest request = nameGenRequestRelease();
        restServiceImpl.releaseNetworkElementName(request);
    }

    @Test
    public void testRestReleaseNetworkElementName_exp() throws Exception {
        NameGenRequest request = nameGenRequestRelease();
        doThrow(new NengException("")).when(springService).releaseNetworkElementName(request);
        restServiceImpl.releaseNetworkElementName(request);
    }

    @Test
    public void testRestAddPolicyToDb() throws Exception {
        Map<String, Object> policy = new HashMap<>();
        policy.put("policyName", "JQINSRIOV.Config_MS_SriovBigJson.1.xml");
        policy.put("policyValue", "some policy");
        restServiceImpl.addPolicyToDb(policy);
        
        Response policyResponse = restServiceImpl.getPolicyResponse("JQINSRIOV.Config_MS_SriovBigJson.1.xml");
        assertNotNull(policyResponse);
    }

    @Test
    public void testRestAddPolicyToDB_exp() throws Exception {
        Map<String, Object> policy = new HashMap<>();
        policy.put("policyName", "policyname");
        policy.put("policyValue", "policyname");

        doThrow(new NengException("")).when(springService).addPolicy(policy);
        restServiceImpl.addPolicyToDb(policy);
    }
    
    @Test
    public void testExternalInterfaceRepo() throws Exception {
        ExternalInterface extInt = new ExternalInterface();
        extInt.setCreatedBy("user");
        extInt.setCreatedTime(null);
        extInt.setExternalInteraceId(100);
        extInt.setLastUpdatedBy("user");
        extInt.setParam("VNF");
        extInt.setSystem("AAI");
        extInt.setUrlSuffix("nodes/generic-vnfs?vnf-name=");
        
        extIntRepo.save(extInt);
        ExternalInterface extIntDb = extIntRepo.findOne(100);
        
        assertNotNull(extIntDb);
        assertEquals("nodes/generic-vnfs?vnf-name=",extIntDb.getUrlSuffix());
    }
    
}
