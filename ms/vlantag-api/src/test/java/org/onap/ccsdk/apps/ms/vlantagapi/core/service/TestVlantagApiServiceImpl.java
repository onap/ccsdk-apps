/*******************************************************************************
 * Copyright © 2017-2018 AT&T Intellectual Property.
 * Modifications Copyright © 2018 IBM.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.onap.ccsdk.apps.ms.vlantagapi.core.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.onap.ccsdk.apps.ms.vlantagapi.core.exception.VlantagApiException;
import org.onap.ccsdk.apps.ms.vlantagapi.core.extinf.pm.model.AllowedRanges;
import org.onap.ccsdk.apps.ms.vlantagapi.core.extinf.pm.model.Elements;
import org.onap.ccsdk.apps.ms.vlantagapi.core.extinf.pm.model.ResourceModel;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.AssignVlanTagRequest;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.AssignVlanTagRequestInput;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.AssignVlanTagResponse;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.PingResponse;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.UnassignVlanTagRequest;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.UnassignVlanTagRequestInput;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.UnassignVlanTagResponse;
import org.onap.ccsdk.apps.ms.vlantagapi.core.service.VlantagApiServiceImpl;
import org.onap.ccsdk.apps.ms.vlantagapi.util.MockPolicyClient;
import org.onap.ccsdk.apps.ms.vlantagapi.util.MockResourceAllocator;
import org.onap.ccsdk.sli.adaptors.ra.comp.ResourceResponse;
import org.onap.ccsdk.sli.adaptors.util.str.StrUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RunWith(MockitoJUnitRunner.class)
public class TestVlantagApiServiceImpl {
private static final Logger log = LoggerFactory.getLogger(TestVlantagApiServiceImpl.class);
	
	VlantagApiServiceImpl service;
	@InjectMocks
	VlantagApiServiceImpl serviceSpy;
	
	@Spy
	protected static MockResourceAllocator mockRA2;
	
	@Spy 
	protected static MockPolicyClient policyClient; 

	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void setup() throws Exception {
		service = new VlantagApiServiceImpl();
	}
	
	@Test
	public void testAssignVlanTagForNullRequest() throws Exception
	{
		AssignVlanTagResponse response = service.assignVlanTag(null);
		Integer expectedErrorCode=500;
		assertEquals(expectedErrorCode, response.getErrorCode());
	}
	
	@Test
	public void testUnAssignVlanTagForNullRequest() throws Exception
	{
		UnassignVlanTagResponse response = service.unassignVlanTag(null);
		Integer expectedErrorCode=500;
		assertEquals(expectedErrorCode, response.getErrorCode());
	}
	
	@Test(expected = Test.None.class /* no exception expected */)
	public void test_assign_sucess_001() throws Exception {
		
		AssignVlanTagRequestInput input = new AssignVlanTagRequestInput();
		input.setPolicyInstanceName("some-policy-instance");
		input.setVlanType("vlan-id-outer");
		input.setScopeId("some-scope-id");
		input.setVlanTagKey("some-key");
		
		AssignVlanTagRequest request = new AssignVlanTagRequest();
		List<AssignVlanTagRequestInput> inputs = new ArrayList<>();
		inputs.add(input);
		request.setInput(inputs);
		
		//PowerMockito.doReturn(mockStatus.Success).when(mockRA).reserve(any(), any(), any(), any());		
		AssignVlanTagResponse response = serviceSpy.assignVlanTag(request);
		
		StrUtil.info(log, response);
	}
	
	@Test(expected = Test.None.class /* no exception expected */)
    public void test_assign_sucess_002() throws Exception {
        
        AssignVlanTagRequestInput input = new AssignVlanTagRequestInput();
        input.setPolicyInstanceName("some-policy-instance");
        input.setVlanType("vlan-id-filter");
        input.setScopeId("some-scope-id");
        input.setVlanTagKey("some-key");
        
        AssignVlanTagRequest request = new AssignVlanTagRequest();
        List<AssignVlanTagRequestInput> inputs = new ArrayList<>();
        inputs.add(input);
        request.setInput(inputs);
        
        //PowerMockito.doReturn(mockStatus.Success).when(mockRA).reserve(any(), any(), any(), any());       
        AssignVlanTagResponse response = serviceSpy.assignVlanTag(request);
        
        StrUtil.info(log, response);
    }
	
	@Test(expected = Test.None.class /* no exception expected */)
	public void test_unassign_sucess_001() throws Exception {
		
		UnassignVlanTagRequestInput input = new UnassignVlanTagRequestInput();
		input.setPolicyInstanceName("some-policy-instance");
		input.setVlanType("vlan-id-outer");
		input.setVlanTagKey("some-key");
		
		UnassignVlanTagRequest request = new UnassignVlanTagRequest();
		List<UnassignVlanTagRequestInput> inputs = new ArrayList<>();
		inputs.add(input);
		request.setInput(inputs);
		
		//Mockito.doReturn(AllocationStatus.Success).when(mockRA).release(any(), any());	

		
		UnassignVlanTagResponse response = serviceSpy.unassignVlanTag(request);
		
		StrUtil.info(log, response);
	}
	
	@Test(expected = Test.None.class /* no exception expected */)
	public void test_ping_sucess_001() throws Exception {	
		PingResponse response = serviceSpy.getPing("Vlantag API Service");
		
		StrUtil.info(log, response);
		Assert.assertTrue(response.getMessage().contains("Ping response : Vlantag API Service Time : "));
	}
	
	
	@Test
	public void test_resolveRecipe_001() {
		
		ResourceModel model = new ResourceModel();
		model.setVlanType("vlan-id-inner");
		model.setResourceResolutionRecipe("#BSB# VPE-Core1, #BSB# VPE-Core2 #ESB#, VPE-Core3 #ESB#");
		//model.setResourceResolutionRecipe("[ VPE-Core1, [ VPE-Core2 ], VPE-Core3 ]");
		
		List<ResourceResponse> rl = new ArrayList<>();
		ResourceResponse response = new ResourceResponse();
		response.endPointPosition = "VPE-Core1";
		response.resourceAllocated="3901";
		
		rl.add(response);
		
		response = new ResourceResponse();
		response.endPointPosition = "VPE-Core2";
		response.resourceAllocated="3902";
		
		rl.add(response);
		
		response = new ResourceResponse();
		response.endPointPosition = "VPE-Core3";
		response.resourceAllocated="3903";
		
		rl.add(response);
		
		service.resolveRecipe(model, rl);

	}
	
	@Test
	public void test_resolveResourceElementValue_001() throws VlantagApiException {
		
		AssignVlanTagRequestInput input = new AssignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance-name");
		input.setVlanType("vlan-id-outer");
		input.setResourceValue("[ 123, 123, 234 ]");
		input.setScopeId("scope-id");
		input.setVlanType("vlan-type");
		
		Elements element = new Elements();
		element.setElementVlanRole("element-vlan-role");
		element.setOverwrite("FALSE");
		element.setRecycleVlantagRange("TRUE");
		element.setVlantagName("VPE-Core2");
		
		
		ResourceModel model = new ResourceModel();
		model.setVlanType("vlan-id-inner");
		model.setResourceResolutionRecipe("#BSB# VPE-Core1, VPE-Core2, VPE-Core3 #ESB#");
		//model.setResourceResolutionRecipe("[ VPE-Core1, [ VPE-Core2 ], VPE-Core3 ]");
		
		List<ResourceResponse> rl = new ArrayList<>();
		ResourceResponse response = new ResourceResponse();
		response.endPointPosition = "VPE-Core1";
		response.resourceAllocated="3901";
		
		rl.add(response);
		
		response = new ResourceResponse();
		response.endPointPosition = "VPE-Core2";
		response.resourceAllocated="3902";
		
		rl.add(response);
		
		response = new ResourceResponse();
		response.endPointPosition = "VPE-Core3";
		response.resourceAllocated="3903";
		
		rl.add(response);
		
		String resourceValue = service.resolveResourceElementValue(input, model, element);
		
		 Assert.assertTrue(resourceValue.equals("123"));
	}
	
	@Test(expected = VlantagApiException.class)
	public void test_resolveResourceElementValue_002() throws VlantagApiException {
		
		AssignVlanTagRequestInput input = new AssignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance-name");
		input.setVlanType("vlan-id-outer");
		input.setResourceValue("[ 123]");
		input.setScopeId("scope-id");
		input.setVlanType("vlan-type");
		
		Elements element = new Elements();
		element.setElementVlanRole("element-vlan-role");
		element.setOverwrite("FALSE");
		element.setRecycleVlantagRange("TRUE");
		element.setVlantagName("VPE-Core2");
		
		
		ResourceModel model = new ResourceModel();
		model.setVlanType("vlan-id-inner");
		model.setResourceResolutionRecipe("#BSB# VPE-Core1, VPE-Core2, VPE-Core3 #ESB#");
		//model.setResourceResolutionRecipe("[ VPE-Core1, [ VPE-Core2 ], VPE-Core3 ]");
		
		List<ResourceResponse> rl = new ArrayList<>();
		ResourceResponse response = new ResourceResponse();
		response.endPointPosition = "VPE-Core1";
		response.resourceAllocated="3901";
		
		rl.add(response);
		
		response = new ResourceResponse();
		response.endPointPosition = "VPE-Core2";
		response.resourceAllocated="3902";
		
		rl.add(response);
		
		response = new ResourceResponse();
		response.endPointPosition = "VPE-Core3";
		response.resourceAllocated="3903";
		
		rl.add(response);
		
		service.resolveResourceElementValue(input, model, element);
	}
	
	
	@Test
	public void test_validateElements_assign_vlantagName_001() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("Vlantag Name missing for Element in Resource Model Policy for Vlan Type : vlan-id-outer");
		
		AssignVlanTagRequestInput input = new AssignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance-name");
		input.setVlanType("vlan-id-outer");
		input.setResourceValue("[ 123, 123, 234 ]");
		input.setScopeId("scope-id");
		
		Elements element = new Elements();
		element.setElementVlanRole("element-vlan-role");
		element.setOverwrite("FALSE");
		element.setRecycleVlantagRange("TRUE");
		
		List<Elements> elements = new ArrayList<>();
		elements.add(element);
		
		service.validateElements(elements, input);

	}
	
	@Test
	public void test_validateElements_assign_allowedRanges_002() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("Allowed Ranges missing for Element in Resource Model Policy for Vlan Type : vlan-id-outer");
		
		AssignVlanTagRequestInput input = new AssignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance-name");
		input.setVlanType("vlan-id-outer");
		input.setResourceValue("[ 123, 123, 234 ]");
		input.setScopeId("scope-id");
		
		Elements element = new Elements();
		element.setElementVlanRole("element-vlan-role");
		element.setOverwrite("FALSE");
		element.setRecycleVlantagRange("TRUE");
		element.setVlantagName("VPE-Core2");
		
		List<Elements> elements = new ArrayList<>();
		elements.add(element);
		
		service.validateElements(elements, input);

	}
	
	@Test
	public void test_validateElements_assign_vlantagElements_003() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("No Vlantag Elements found in Resource Model Policy for Vlan Type : vlan-id-outer");
		
		AssignVlanTagRequestInput input = new AssignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance-name");
		input.setVlanType("vlan-id-outer");
		input.setResourceValue("[ 123, 123, 234 ]");
		input.setScopeId("scope-id");		
		
		List<Elements> elements = new ArrayList<>();
		
		service.validateElements(elements, input);

	}
	
	@Test(expected = Test.None.class /* no exception expected */)
	public void test_validateElements_assign_Success_004() throws VlantagApiException {
			
		AssignVlanTagRequestInput input = new AssignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance-name");
		input.setVlanType("vlan-id-outer");
		input.setResourceValue("[ 123, 123, 234 ]");
		input.setScopeId("scope-id");
		
		Elements element = new Elements();
		element.setElementVlanRole("element-vlan-role");
		element.setOverwrite("FALSE");
		element.setRecycleVlantagRange("TRUE");
		element.setVlantagName("VPE-Core2");
		
		List<AllowedRanges> allowedRanges = new ArrayList<>();
		AllowedRanges range = new AllowedRanges();
		range.setMin("200");
		range.setMax("300");
		allowedRanges.add(range );
		element.setAllowedRanges(allowedRanges);
		
		List<Elements> elements = new ArrayList<>();
		elements.add(element);
		
		service.validateElements(elements, input);

	}
	
	@Test
	public void test_validateElements_unassign_vlantagName_001() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("Vlantag Name missing for Element in Resource Model Policy for Vlan Type : vlan-id-outer");
		
		UnassignVlanTagRequestInput input = new UnassignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance-name");
		input.setVlanType("vlan-id-outer");
		
		Elements element = new Elements();
		element.setElementVlanRole("element-vlan-role");
		element.setOverwrite("FALSE");
		element.setRecycleVlantagRange("TRUE");
		
		List<Elements> elements = new ArrayList<>();
		elements.add(element);
		
		service.validateElements(elements, input);

	}
	
	@Test
	public void test_validateElements_unassign_vlantagElements_003() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("No Vlantag Elements found in Resource Model Policy for Vlan Type : vlan-id-outer");
		
		UnassignVlanTagRequestInput input = new UnassignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance-name");
		input.setVlanType("vlan-id-outer");
		
		
		List<Elements> elements = new ArrayList<>();
		
		service.validateElements(elements, input);

	}
	
	@Test(expected = Test.None.class /* no exception expected */)
	public void test_validateElements_unassign_Success_004() throws VlantagApiException {
			
		UnassignVlanTagRequestInput input = new UnassignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance-name");
		input.setVlanType("vlan-id-outer");
		
		Elements element = new Elements();
		element.setElementVlanRole("element-vlan-role");
		element.setOverwrite("FALSE");
		element.setRecycleVlantagRange("TRUE");
		element.setVlantagName("VPE-Core2");
		
		List<AllowedRanges> allowedRanges = new ArrayList<>();
		AllowedRanges range = new AllowedRanges();
		range.setMin("200");
		range.setMax("300");
		allowedRanges.add(range );
		element.setAllowedRanges(allowedRanges);
		
		List<Elements> elements = new ArrayList<>();
		elements.add(element);
		
		service.validateElements(elements, input);

	}
	
	@Test
	public void test_validateModel_assign_resourceModel_001() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("No Matching Policy Resource Model found for Vlan Type : vlan-id-outer");
		
		AssignVlanTagRequestInput input = new AssignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance-name");
		input.setVlanType("vlan-id-outer");
		input.setResourceValue("[ 123]");
		input.setScopeId("scope-id");
		
		service.validateModel(null, input);
	}
	
	@Test
	public void test_validateModel_assign_resolutionRecipe_002() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("Resource Resolution Recipe is null in Resource Model for Vlan Type : vlan-id-outer");
		
		AssignVlanTagRequestInput input = new AssignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance-name");
		input.setVlanType("vlan-id-outer");
		input.setResourceValue("[ 123]");
		input.setScopeId("scope-id");		
			
		ResourceModel model = new ResourceModel();
		model.setVlanType("vlan-id-inner");
		
		service.validateModel(model, input);
	}
	
	@Test
	public void test_validateModel_assign_scope_003() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("Scope is null in Resource Model for Vlan Type : vlan-id-outer");
		
		AssignVlanTagRequestInput input = new AssignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance-name");
		input.setVlanType("vlan-id-outer");
		input.setResourceValue("[ 123]");
		input.setScopeId("scope-id");

		ResourceModel model = new ResourceModel();
		model.setVlanType("vlan-id-inner");
		model.setResourceResolutionRecipe("#BSB# VPE-Core1, VPE-Core2, VPE-Core3 #ESB#");
		
		service.validateModel(model, input);
	}
	
	@Test(expected = Test.None.class /* no exception expected */)
	public void test_validateModel_assign_success_004() throws VlantagApiException {
		
		AssignVlanTagRequestInput input = new AssignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance-name");
		input.setVlanType("vlan-id-outer");
		input.setResourceValue("[ 123]");
		input.setScopeId("scope-id");
		
		Elements element = new Elements();
		element.setElementVlanRole("element-vlan-role");
		element.setOverwrite("FALSE");
		element.setRecycleVlantagRange("TRUE");
		element.setVlantagName("VPE-Core2");
		
		List<AllowedRanges> allowedRanges = new ArrayList<>();
		AllowedRanges range = new AllowedRanges();
		range.setMin("200");
		range.setMax("300");
		allowedRanges.add(range );
		element.setAllowedRanges(allowedRanges);
		
		List<Elements> elements = new ArrayList<>();
		elements.add(element);
			
		ResourceModel model = new ResourceModel();
		model.setVlanType("vlan-id-inner");
		model.setResourceResolutionRecipe("#BSB# VPE-Core1, VPE-Core2, VPE-Core3 #ESB#");
		model.setScope("VPE");
		model.setElements(elements);
	
		service.validateModel(model, input);
	}
	
	@Test
	public void test_validateModel_unassign_resourceModel_001() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("No Matching Policy Resource Model found for Vlan Type : vlan-id-outer");
		
		UnassignVlanTagRequestInput input = new UnassignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance-name");
		input.setVlanType("vlan-id-outer");
		
		service.validateModel(null, input);
	}
	
	@Test
	public void test_validateModel_unassign_resolutionRecipe_002() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("Resource Resolution Recipe is null in Resource Model for Vlan Type : vlan-id-outer");
		
		UnassignVlanTagRequestInput input = new UnassignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance-name");
		input.setVlanType("vlan-id-outer");		
			
		ResourceModel model = new ResourceModel();
		model.setVlanType("vlan-id-inner");
		
		service.validateModel(model, input);
	}
	
	@Test
	public void test_validateModel_unassign_scope_003() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("Scope is null in Resource Model for Vlan Type : vlan-id-outer");
		
		UnassignVlanTagRequestInput input = new UnassignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance-name");
		input.setVlanType("vlan-id-outer");		
			
		ResourceModel model = new ResourceModel();
		model.setVlanType("vlan-id-inner");
		model.setResourceResolutionRecipe("#BSB# VPE-Core1, VPE-Core2, VPE-Core3 #ESB#");
		
		service.validateModel(model, input);
	}
	
	@Test(expected = Test.None.class /* no exception expected */)
	public void test_validateModel_unassign_success_004() throws VlantagApiException {
		
		UnassignVlanTagRequestInput input = new UnassignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance-name");
		input.setVlanType("vlan-id-outer");
		
		Elements element = new Elements();
		element.setElementVlanRole("element-vlan-role");
		element.setOverwrite("FALSE");
		element.setRecycleVlantagRange("TRUE");
		element.setVlantagName("VPE-Core2");
		
		List<AllowedRanges> allowedRanges = new ArrayList<>();
		AllowedRanges range = new AllowedRanges();
		range.setMin("200");
		range.setMax("300");
		allowedRanges.add(range );
		element.setAllowedRanges(allowedRanges);
		
		List<Elements> elements = new ArrayList<>();
		elements.add(element);
			
		ResourceModel model = new ResourceModel();
		model.setVlanType("vlan-id-inner");
		model.setResourceResolutionRecipe("#BSB# VPE-Core1, VPE-Core2, VPE-Core3 #ESB#");
		model.setScope("VPE");
		model.setElements(elements);
	
		service.validateModel(model, input);
	}
	
	@Test
	public void test_validateRequest_assign_request_001() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("VlanTag Assign Request is null.");
		
		AssignVlanTagRequest request = null;
		service.validateRequest(request);
	}
	
	@Test
	public void test_validateRequest_assign_requestInput_002() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("VlanTag Assign Request Input is null or empty.");
		
		AssignVlanTagRequest request = new AssignVlanTagRequest();
		
		service.validateRequest(request);
	}
	
	@Test
	public void test_validateRequest_assign_policyInstanceName_003() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("VlanTag Assign Request policy-instance-name is null.");
		
		AssignVlanTagRequestInput input = new AssignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setVlanType("vlan-id-outer");
		input.setResourceValue("[ 123]");
		input.setScopeId("scope-id");
		input.setVlanType("vlan-type");
		
		AssignVlanTagRequest request = new AssignVlanTagRequest();
		List<AssignVlanTagRequestInput> inputs = new ArrayList<>();
		inputs.add(input);
		
		request.setInput(inputs);
		
		service.validateRequest(request);
	}
	
	@Test
	public void test_validateRequest_assign_resourceName_004() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("VlanTag Assign Request vlan-type is null.");
		
		AssignVlanTagRequestInput input = new AssignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance");
		input.setResourceValue("[ 123]");
		input.setScopeId("scope-id");
		
		AssignVlanTagRequest request = new AssignVlanTagRequest();
		List<AssignVlanTagRequestInput> inputs = new ArrayList<>();
		inputs.add(input);
		
		request.setInput(inputs);
		
		service.validateRequest(request);
	}
	
	@Test
	public void test_validateRequest_assign_scopeId_005() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("VlanTag Assign Request scope-id is null.");
		
		AssignVlanTagRequestInput input = new AssignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance");
		input.setVlanType("vlan-id-outer");
		input.setResourceValue("[ 123]");
		input.setVlanType("vlan-type");
		
		AssignVlanTagRequest request = new AssignVlanTagRequest();
		List<AssignVlanTagRequestInput> inputs = new ArrayList<>();
		inputs.add(input);
		
		request.setInput(inputs);
		
		service.validateRequest(request);
	}
	
	@Test
	public void test_validateRequest_assign_resourceKey_006() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("VlanTag Assign Request key is null.");
		
		AssignVlanTagRequestInput input = new AssignVlanTagRequestInput();
		input.setPolicyInstanceName("some-policy-instance");
		input.setVlanType("vlan-id-outer");
		input.setScopeId("scope-id");
		input.setResourceValue("[ 123]");
		input.setVlanType("vlan-type");
		
		AssignVlanTagRequest request = new AssignVlanTagRequest();
		List<AssignVlanTagRequestInput> inputs = new ArrayList<>();
		inputs.add(input);
		
		request.setInput(inputs);
		
		service.validateRequest(request);
	}
	
	@Test(expected = Test.None.class /* no exception expected */)
	public void test_validateRequest_assign_success_007() throws VlantagApiException {
		
		AssignVlanTagRequestInput input = new AssignVlanTagRequestInput();
		input.setPolicyInstanceName("some-policy-instance");
		input.setVlanType("vlan-id-outer");
		input.setScopeId("scope-id");
		input.setResourceValue("[ 123]");
		input.setVlanType("vlan-type");
		input.setVlanTagKey("some-key");
		
		AssignVlanTagRequest request = new AssignVlanTagRequest();
		List<AssignVlanTagRequestInput> inputs = new ArrayList<>();
		inputs.add(input);
		
		request.setInput(inputs);
		
		service.validateRequest(request);
	}
	
	
	@Test
	public void test_validateRequest_unassign_request_001() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("VlanTag Unassign Request is null.");
		
		UnassignVlanTagRequest request = null;
		service.validateRequest(request);
	}
	
	@Test
	public void test_validateRequest_unassign_requestInput_002() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("VlanTag Unassign Request Input is null or empty.");
		
		UnassignVlanTagRequest request = new UnassignVlanTagRequest();
		service.validateRequest(request);
	}
	
	@Test
	public void test_validateRequest_unassign_policyInstanceName_003() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("VlanTag Unassign Request policy-instance-name is null.");
		
		UnassignVlanTagRequestInput input = new UnassignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setVlanType("vlan-id-outer");
		
		UnassignVlanTagRequest request = new UnassignVlanTagRequest();
		List<UnassignVlanTagRequestInput> inputs = new ArrayList<>();
		inputs.add(input);
		
		request.setInput(inputs);
		
		service.validateRequest(request);
	}
	
	@Test
	public void test_validateRequest_unassign_resourceName_004() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("VlanTag Unassign Request resource-name is null.");
		
		UnassignVlanTagRequestInput input = new UnassignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance");
		
		UnassignVlanTagRequest request = new UnassignVlanTagRequest();
		List<UnassignVlanTagRequestInput> inputs = new ArrayList<>();
		inputs.add(input);
		
		request.setInput(inputs);
		
		service.validateRequest(request);
	}
	
	
	@Test
	public void test_validateRequest_unassign_resourceKey_005() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("VlanTag Unassign Request key is null.");
		
		UnassignVlanTagRequestInput input = new UnassignVlanTagRequestInput();
		input.setPolicyInstanceName("some-policy-instance");
		input.setVlanType("vlan-id-outer");
		
		UnassignVlanTagRequest request = new UnassignVlanTagRequest();
		List<UnassignVlanTagRequestInput> inputs = new ArrayList<>();
		inputs.add(input);
		
		request.setInput(inputs);
		
		service.validateRequest(request);
	}
	
	@Test(expected = Test.None.class /* no exception expected */)
	public void test_validateRequest_unassign_success_006() throws VlantagApiException {
		
		UnassignVlanTagRequestInput input = new UnassignVlanTagRequestInput();
		input.setVlanTagKey("some-key");
		input.setPolicyInstanceName("some-policy-instance");
		input.setVlanType("vlan-id-outer");
		
		UnassignVlanTagRequest request = new UnassignVlanTagRequest();
		List<UnassignVlanTagRequestInput> inputs = new ArrayList<>();
		inputs.add(input);
		
		request.setInput(inputs);
		
		service.validateRequest(request);
	}
	
	@Test
	public void test_validate_assign_failure_001() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("No Resource Models available in Policy Manager for Policy Instance Name : some-policy-instance");
		
		AssignVlanTagRequestInput input = new AssignVlanTagRequestInput();
		input.setPolicyInstanceName("some-policy-instance");
		input.setVlanType("vlan-id-outer");
		
		List<ResourceModel> resourceModels = new ArrayList<>();
		
		service.validate(resourceModels, input);
	}
	
	@Test(expected = Test.None.class /* no exception expected */)
	public void test_validate_assign_sucess_002() throws VlantagApiException {
		
		AssignVlanTagRequestInput input = new AssignVlanTagRequestInput();
		input.setPolicyInstanceName("some-policy-instance");
		input.setVlanType("vlan-id-outer");
		
		Elements element = new Elements();
		element.setElementVlanRole("element-vlan-role");
		element.setOverwrite("FALSE");
		element.setRecycleVlantagRange("TRUE");
		element.setVlantagName("VPE-Core2");
		
		List<AllowedRanges> allowedRanges = new ArrayList<>();
		AllowedRanges range = new AllowedRanges();
		range.setMin("200");
		range.setMax("300");
		allowedRanges.add(range );
		element.setAllowedRanges(allowedRanges);
		
		List<Elements> elements = new ArrayList<>();
		elements.add(element);
			
		ResourceModel model = new ResourceModel();
		model.setVlanType("vlan-id-outer");
		model.setResourceResolutionRecipe("#BSB# VPE-Core1, VPE-Core2, VPE-Core3 #ESB#");
		model.setScope("VPE");
		model.setElements(elements);
		
		List<ResourceModel> resourceModels = new ArrayList<>();
		resourceModels.add(model);
		
		
		service.validate(resourceModels, input);
	}
	
	@Test
	public void test_validate_unassign_failure_001() throws VlantagApiException {
		
		expectedEx.expect(VlantagApiException.class);
		expectedEx.expectMessage("No Resource Models available in Policy Manager for Policy Instance Name : some-policy-instance");
		
		UnassignVlanTagRequestInput input = new UnassignVlanTagRequestInput();
		input.setPolicyInstanceName("some-policy-instance");
		input.setVlanType("vlan-id-outer");
		
		List<ResourceModel> resourceModels = new ArrayList<>();
		
		service.validate(resourceModels, input);
	}
	
	@Test(expected = Test.None.class /* no exception expected */)
	public void test_validate_unassign_sucess_002() throws VlantagApiException {
		
		UnassignVlanTagRequestInput input = new UnassignVlanTagRequestInput();
		input.setPolicyInstanceName("some-policy-instance");
		input.setVlanType("vlan-id-outer");
		
		Elements element = new Elements();
		element.setElementVlanRole("element-vlan-role");
		element.setOverwrite("FALSE");
		element.setRecycleVlantagRange("TRUE");
		element.setVlantagName("VPE-Core2");
		
		List<AllowedRanges> allowedRanges = new ArrayList<>();
		AllowedRanges range = new AllowedRanges();
		range.setMin("200");
		range.setMax("300");
		allowedRanges.add(range );
		element.setAllowedRanges(allowedRanges);
		
		List<Elements> elements = new ArrayList<>();
		elements.add(element);
			
		ResourceModel model = new ResourceModel();
		model.setVlanType("vlan-id-outer");
		model.setResourceResolutionRecipe("#BSB# VPE-Core1, VPE-Core2, VPE-Core3 #ESB#");
		model.setScope("VPE");
		model.setElements(elements);
		
		List<ResourceModel> resourceModels = new ArrayList<>();
		resourceModels.add(model);
		
		
		service.validate(resourceModels, input);
	}
	
	   @Test(expected = VlantagApiException.class)
	    public void testGetPolicyFromPDPFailure() throws Exception {
	        Mockito.doThrow(new VlantagApiException()).when(policyClient).getConfigUsingPost(any());
	        policyClient.getPolicyFromPDP("sonme_random_policy_name");
	    }
}
