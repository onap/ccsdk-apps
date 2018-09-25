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

package org.onap.ccsdk.apps.ms.neng.service.extinf.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.ccsdk.apps.ms.neng.core.exceptions.NengException;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.GetConfigRequest;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.GetConfigResponse;
import org.onap.ccsdk.apps.ms.neng.core.rs.interceptors.PolicyManagerAuthorizationInterceptor;
import org.onap.ccsdk.apps.ms.neng.extinf.props.PolicyManagerProps;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PolicyFinderServiceImplTest {
    @InjectMocks
    @Spy
    PolicyFinderServiceImpl policyFinder;
    @Spy
    PolicyManagerProps policManProps;
    @Mock
    RestTemplateBuilder policyMgrRestTempBuilder;
    @Mock
    PolicyManagerAuthorizationInterceptor authInt;
    @Mock
    RestTemplate restTemplate;

    @Test
    public void testConfig() throws Exception {
        doReturn(new GetConfigResponse()).when(policyFinder).makeOutboundCall(Matchers.any(), Matchers.any());
        assertNotNull(policyFinder.getConfig("policy"));
    }
    
    @Test
    public void testFindPolicy() throws Exception {
        doReturn(new GetConfigResponse()).when(policyFinder).makeOutboundCall(Matchers.any(), Matchers.any());
        assertNull(policyFinder.findPolicy("policy"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testmakeOutboundCall() throws Exception {
        Map<String, Object> configMap = buildPolicyResponse();
        Object resp = Arrays.asList(new Object[] {configMap});
        ResponseEntity<Object> respEn = new ResponseEntity<>(resp, HttpStatus.OK);
        when(restTemplate.exchange(Matchers.any(RequestEntity.class), Matchers.any(Class.class))).thenReturn(respEn);

        policManProps.setUrl("http://policyManager.onap.org");

        GetConfigRequest request = new GetConfigRequest();
        request.setPolicyName("policy");
        GetConfigResponse configResp = policyFinder.makeOutboundCall(request, GetConfigResponse.class);
        assertNotNull(configResp);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NengException.class)
    public void testmakeOutboundCall_500() throws Exception {
        Map<String, Object> configMap = buildPolicyResponse();
        Object resp = Arrays.asList(new Object[] {configMap});
        ResponseEntity<Object> respEn = new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.exchange(Matchers.any(RequestEntity.class), Matchers.any(Class.class))).thenReturn(respEn);

        policManProps.setUrl("http://policyManager.onap.org");

        GetConfigRequest request = new GetConfigRequest();
        request.setPolicyName("policy");
        policyFinder.makeOutboundCall(request, GetConfigResponse.class);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NengException.class)
    public void testHandleError_NOT_FOUND() throws Exception{
        HttpStatusCodeException e = new HttpClientErrorException(HttpStatus.NOT_FOUND,"",null,StandardCharsets.US_ASCII);
        policyFinder.handleError(e);

    }

    @Test
    public void testGetRestTemplate() throws Exception {
        PolicyFinderServiceImpl service = new PolicyFinderServiceImpl();
        RestTemplateBuilder policyRestTemplateBuilder = new RestTemplateBuilder();
        service.setPolicyMgrRestTempBuilder(policyRestTemplateBuilder);
        service.setAuthInt(new PolicyManagerAuthorizationInterceptor());

        assertNotNull(service.getPolicyMgrRestTempBuilder());
        assertNotNull(service.getAuthInt());
        assertNotNull(service.getRestTemplate());
    }

    @Test
    public void testTransformConfigObject() throws Exception {
        String config = "{\"riskLevel\":\"4\",\"riskType\":\"test\","
                        + "\"policyName\":\"1806SriovBigJson\",\"service\":\"SDNC-GenerateName\","
                        + "\"guard\":\"False\",\"description\":\"1806SriovBigJson\","
                        + "\"templateVersion\":\"1607\",\"priority\":\"4\",\"version\":\"pannny_nnnn\","
                        + "\"content\":{\"policy-instance-name\":\"1806NameGenerationPolicyForSRIOV\","
                        + "\"naming-models\":[{\"naming-properties\":[{\"property-operation\":\"substr(5)\","
                        + "\"property-name\":\"COMPLEX\"},{\"property-name\":\"SEQUENCE\","
                        + "\"increment-sequence\":{\"max\":\"zzz\",\"scope\":\"ENTIRETY\","
                        + "\"start-value\":\"001\",\"length\":\"3\",\"increment\":\"1\","
                        + "\"sequence-type\":\"alpha-numeric\"}},{\"property-name\":\"NF_NAMING_CODE\"}],"
                        + "\"naming-type\":\"VNF\",\"nfRole\":\"vPE\","
                        + "\"naming-recipe\":\"COMPLEX|SEQUENCE|NF_NAMING_CODE\"},"
                        + "{\"naming-properties\":[{\"property-name\":\"VNF_NAME\"},"
                        + "{\"property-name\":\"SEQUENCE\",\"increment-sequence\":"
                        + "{\"max\":\"999\",\"scope\":\"ENTIRETY\",\"start-value\":\"001\",\"length\":\"3\","
                        + "\"increment\":\"1\",\"sequence-type\":\"numeric\"}},"
                        + "{\"property-operation\":\"substr(-3)\",\"property-name\":\"NFC_NAMING_CODE\"}],"
                        + "\"naming-type\":\"VM\",\"nfRole\":\"vPE\","
                        + "\"naming-recipe\":\"VNF_NAME|SEQUENCE|NFC_NAMING_CODE\"},"
                        + "{\"naming-properties\":[{\"property-name\":\"VNF_NAME\"},"
                        + "{\"property-value\":\"-\",\"property-name\":\"DELIMITER\"},"
                        + "{\"property-name\":\"VF_MODULE_LABEL\"},{\"property-name\":\"VF_MODULE_TYPE\"},"
                        + "{\"property-name\":\"SEQUENCE\",\"increment-sequence\":"
                        + "{\"max\":\"99\",\"scope\":\"PRECEEDING\",\"start-value\":\"01\",\"length\":\"2\","
                        + "\"increment\":\"1\",\"sequence-type\":\"numeric\"}}],"
                        + "\"naming-type\":\"VF-MODULE\",\"nfRole\":\"vPE\","
                        + "\"naming-recipe\":\"VNF_NAME|DELIMITER|VF_MODULE_LABEL|DELIMITER"
                        + "|VF_MODULE_TYPE|DELIMITER|SEQUENCE\"},"
                        + "{\"naming-properties\":[{\"property-name\":\"VF-MODULE_NAME\"},"
                        + "{\"property-value\":\"-\",\"property-name\":\"DELIMITER\"},"
                        + "{\"property-value\":\"volumegroup\",\"property-name\":\"CONSTANT\"}],"
                        + "\"naming-type\":\"VOLUME_GROUP\",\"nfRole\":\"vPE\","
                        + "\"naming-recipe\":\"VF-MODULE_NAME|DELIMITER|CONSTANT\"},"
                        + "{\"naming-properties\":[{\"property-name\":\"VOLUME_GROUP_NAME\"},"
                        + "{\"property-value\":\"-\",\"property-name\":\"DELIMITER\"},"
                        + "{\"property-value\":\"volume\",\"property-name\":\"CONSTANT\"},"
                        + "{\"property-name\":\"SEQUENCE\",\"increment-sequence\":"
                        + "{\"max\":\"99\",\"scope\":\"PRECEEDING\",\"start-value\":\"01\","
                        + "\"length\":\"2\",\"increment\":\"1\",\"sequence-type\":\"numeric\"}}],"
                        + "\"naming-type\":\"VOLUME\",\"nfRole\":\"vPE\","
                        + "\"naming-recipe\":\"VOLUME_GROUP_NAME|DELIMITER|CONSTANT|DELIMITER|SEQUENCE\"},"
                        + "{\"naming-properties\":[{\"property-name\":\"VNF_NAME\"},"
                        + "{\"property-value\":\"-\",\"property-name\":\"DELIMITER\"},"
                        + "{\"property-value\":\"affinity\",\"property-name\":\"CONSTANT\"}],"
                        + "\"naming-type\":\"AFFINITY\",\"nfRole\":\"vPE\","
                        + "\"naming-recipe\":\"VNF_NAME|DELIMITER|CONSTANT\"},"
                        + "{\"naming-properties\":[{\"property-name\":\"VNF_NAME\"},"
                        + "{\"property-value\":\"-\",\"property-name\":\"DELIMITER\"},"
                        + "{\"property-value\":\"INT\",\"property-name\":\"CONSTANT\"},"
                        + "{\"property-name\":\"SEQUENCE\",\"increment-sequence\":"
                        + "{\"max\":\"99\",\"scope\":\"PRECEEDING\",\"start-value\":\"01\","
                        + "\"length\":\"2\",\"increment\":\"1\",\"sequence-type\":\"numeric\"}}],"
                        + "\"naming-type\":\"INTERNAL_NETWORK\",\"nfRole\":\"vPE\","
                        + "\"naming-recipe\":\"VNF_NAME|DELIMITER|CONSTANT|SEQUENCE\"}]}}";
        Map<Object, Object> configMap = new HashMap<>();
        configMap.put("config", config);
        ObjectMapper objectmapper = new ObjectMapper();
        List<Map<Object, Object>> respList = new ArrayList<>();
        respList.add(configMap);
        policyFinder.transformConfigObject(objectmapper, respList);
        assertNotNull(respList.get(0).get("config"));
    }

    Map<String, Object> buildPolicyResponse() {
        Map<String, Object> policyDataMap = new HashMap<>();
        policyDataMap.put("policy-instance-name", "SDNC_Policy.Config_MS_VNFCNamingPolicy");
        Map<String, Object> namingModelMap = new HashMap<>();
        namingModelMap.put("nf-role", "vPE");
        namingModelMap.put("naming-type", "VNF");
        namingModelMap.put("naming-recipe", "COMPLEX|NF-NAMING-CODE|Field2|Field3|Field4");
        Map<String, Object> namingPropertyMap = new HashMap<>();
        Map<String, Object> propertyMap1 = new HashMap<>();
        propertyMap1.put("property-name", "COMPLEX");
        Map<String, Object> propertyMap2 = new HashMap<>();
        propertyMap2.put("property-name", "NF-NAMING-CODE");
        namingPropertyMap.put("", Arrays.asList(new Object[] {propertyMap1, propertyMap2}));
        namingModelMap.put("naming-properties", namingPropertyMap);
        policyDataMap.put("naming-models", Arrays.asList(new Object[] {namingModelMap}));
        Map<String, Object> configMap = new HashMap<>();
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("content", policyDataMap);
        configMap.put("config", contentMap);
        return configMap;
    }
}
