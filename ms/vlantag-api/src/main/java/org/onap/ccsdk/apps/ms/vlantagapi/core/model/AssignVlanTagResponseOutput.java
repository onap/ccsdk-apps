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
package org.onap.ccsdk.apps.ms.vlantagapi.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * AssignVlanTagResponseOutput.java Purpose: Provide Assign VlanTag Response Output Model
 *
 * @author Saurav Paira
 * @version 1.0
 */
public class AssignVlanTagResponseOutput {

	private @Valid String resourceName = null;
	private @Valid String resourceValue = null;
	private @Valid String resourceVlanRole = null;
	private @Valid List<VlanTag> storedElements = new ArrayList<>();

	/**
	 **/
	public AssignVlanTagResponseOutput resourceName(String resourceName) {
		this.resourceName = resourceName;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("resource-name")
	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	/**
	 **/
	public AssignVlanTagResponseOutput resourceValue(String resourceValue) {
		this.resourceValue = resourceValue;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("resource-value")
	public String getResourceValue() {
		return resourceValue;
	}

	public void setResourceValue(String resourceValue) {
		this.resourceValue = resourceValue;
	}

	/**
	 **/
	public AssignVlanTagResponseOutput resourceVlanRole(String resourceVlanRole) {
		this.resourceVlanRole = resourceVlanRole;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("resource-vlan-role")
	public String getResourceVlanRole() {
		return resourceVlanRole;
	}

	public void setResourceVlanRole(String resourceVlanRole) {
		this.resourceVlanRole = resourceVlanRole;
	}

	/**
	 **/
	public AssignVlanTagResponseOutput storedElements(List<VlanTag> storedElements) {
		this.storedElements = storedElements;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("stored-elements")
	public List<VlanTag> getStoredElements() {
		return storedElements;
	}

	public void setStoredElements(List<VlanTag> storedElements) {
		this.storedElements = storedElements;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AssignVlanTagResponseOutput assignVlanTagResponseOutput = (AssignVlanTagResponseOutput) o;
		return Objects.equals(resourceName, assignVlanTagResponseOutput.resourceName)
				&& Objects.equals(resourceValue, assignVlanTagResponseOutput.resourceValue)
				&& Objects.equals(resourceVlanRole, assignVlanTagResponseOutput.resourceVlanRole)
				&& Objects.equals(storedElements, assignVlanTagResponseOutput.storedElements);
	}

	@Override
	public int hashCode() {
		return Objects.hash(resourceName, resourceValue, resourceVlanRole, storedElements);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class AssignVlanTagResponseOutput {\n");

		sb.append("    resourceName: ").append(toIndentedString(resourceName)).append("\n");
		sb.append("    resourceValue: ").append(toIndentedString(resourceValue)).append("\n");
		sb.append("    resourceVlanRole: ").append(toIndentedString(resourceVlanRole)).append("\n");
		sb.append("    storedElements: ").append(toIndentedString(storedElements)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

}
