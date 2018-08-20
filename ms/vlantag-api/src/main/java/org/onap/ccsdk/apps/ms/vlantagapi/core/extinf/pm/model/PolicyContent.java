/*******************************************************************************
 * Copyright © 2017-2018 AT&T Intellectual Property.
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
package org.onap.ccsdk.apps.ms.vlantagapi.core.extinf.pm.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PolicyContent.java Purpose: POJO representing policy manager
 * get-config response contents
 *
 * @author Saurav Paira
 * @version 1.0
 */
public class PolicyContent {

	@JsonProperty("resource-models")
	private List<ResourceModel> resourceModels;
	
	@JsonProperty("policy-instance-name")
	private String policyInstanceName;
	
	public String getPolicyInstanceName() {
		return policyInstanceName;
	}

	public void setPolicyInstanceName(String policyInstanceName) {
		this.policyInstanceName = policyInstanceName;
	}

	public List<ResourceModel> getResourceModels() {
		return resourceModels;
	}

	public void setResourceModels(List<ResourceModel> resourceModels) {
		this.resourceModels = resourceModels;
	}

	@Override
	public String toString() {
		return "PolicyContent [resourceModels=" + resourceModels + ", policyInstanceName=" + policyInstanceName + "]";
	}
	

}
