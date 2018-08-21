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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * ResourceModel.java Purpose: POJO representing policy manager get-config response Resource Model
 *
 * @author Saurav Paira
 * @version 1.0
 */
@JsonDeserialize(as = ResourceModel.class)
public class ResourceModel implements PolicyData {
	
	@JsonProperty("key-type")
	private String keyType;
	
	@JsonProperty("scope")
	private String scope;
	
	@JsonProperty("resource-resolution-recipe")
	private String resourceResolutionRecipe;
	
	@JsonProperty("resource-name")
	private String resourceName;
	
	@JsonProperty("data-store-object")
	private String dataStoreObject;
	
	@JsonProperty("data-store")
	private String dataStore;
	
	@JsonProperty("elements")
	private List<Elements> elements;
	
	@JsonProperty("resource-vlan-role")
	private String resourceVlanRole;
	
	@JsonProperty("vlan-type")
	private String vlanType;


	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getResourceResolutionRecipe() {
		return resourceResolutionRecipe;
	}

	public void setResourceResolutionRecipe(String resourceResolutionRecipe) {
		this.resourceResolutionRecipe = resourceResolutionRecipe;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getDataStoreObject() {
		return dataStoreObject;
	}

	public void setDataStoreObject(String dataStoreObject) {
		this.dataStoreObject = dataStoreObject;
	}

	public String getDataStore() {
		return dataStore;
	}

	public void setDataStore(String dataStore) {
		this.dataStore = dataStore;
	}

	public List<Elements> getElements() {
		return elements;
	}

	public void setElements(List<Elements> elements) {
		this.elements = elements;
	}

	public String getResourceVlanRole() {
		return resourceVlanRole;
	}

	public void setResourceVlanRole(String resourceVlanRole) {
		this.resourceVlanRole = resourceVlanRole;
	}

	
	public String getVlanType() {
		return vlanType;
	}

	public void setVlanType(String vlanType) {
		this.vlanType = vlanType;
	}

	@Override
	public String toString() {
		return "ResourceModel [keyType=" + keyType + ", scope=" + scope + ", resourceResolutionRecipe="
				+ resourceResolutionRecipe + ", resourceName=" + resourceName + ", dataStoreObject=" + dataStoreObject
				+ ", dataStore=" + dataStore + ", elements=" + elements + ", resourceVlanRole=" + resourceVlanRole
				+ ", vlanType=" + vlanType + "]";
	}

	

	

}
