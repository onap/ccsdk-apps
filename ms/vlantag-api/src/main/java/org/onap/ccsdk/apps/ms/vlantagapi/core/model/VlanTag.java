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

import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * VlanTag.java Purpose: Provide VlanTag Model for the Assign Response Output
 *
 * @author Saurav Paira
 * @version 1.0
 */
public class VlanTag {

	private @Valid String vlanUuid = null;
	private @Valid String vlantagName = null;
	private @Valid String vlantagValue = null;
	private @Valid String elementVlanRole = null;

	/**
	 **/
	public VlanTag vlanUuid(String vlanUuid) {
		this.vlanUuid = vlanUuid;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("vlan-uuid")
	public String getVlanUuid() {
		return vlanUuid;
	}

	public void setVlanUuid(String vlanUuid) {
		this.vlanUuid = vlanUuid;
	}

	/**
	 **/
	public VlanTag vlantagName(String vlantagName) {
		this.vlantagName = vlantagName;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("vlantag-name")
	public String getVlantagName() {
		return vlantagName;
	}

	public void setVlantagName(String vlantagName) {
		this.vlantagName = vlantagName;
	}

	/**
	 **/
	public VlanTag vlantagValue(String vlantagValue) {
		this.vlantagValue = vlantagValue;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("vlantag-value")
	public String getVlantagValue() {
		return vlantagValue;
	}

	public void setVlantagValue(String vlantagValue) {
		this.vlantagValue = vlantagValue;
	}

	/**
	 **/
	public VlanTag elementVlanRole(String elementVlanRole) {
		this.elementVlanRole = elementVlanRole;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("element-vlan-role")
	public String getElementVlanRole() {
		return elementVlanRole;
	}

	public void setElementVlanRole(String elementVlanRole) {
		this.elementVlanRole = elementVlanRole;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		VlanTag vlanTag = (VlanTag) o;
		return Objects.equals(vlanUuid, vlanTag.vlanUuid) && Objects.equals(vlantagName, vlanTag.vlantagName)
				&& Objects.equals(vlantagValue, vlanTag.vlantagValue)
				&& Objects.equals(elementVlanRole, vlanTag.elementVlanRole);
	}

	@Override
	public int hashCode() {
		return Objects.hash(vlanUuid, vlantagName, vlantagValue, elementVlanRole);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class VlanTag {\n");

		sb.append("    vlanUuid: ").append(toIndentedString(vlanUuid)).append("\n");
		sb.append("    vlantagName: ").append(toIndentedString(vlantagName)).append("\n");
		sb.append("    vlantagValue: ").append(toIndentedString(vlantagValue)).append("\n");
		sb.append("    elementVlanRole: ").append(toIndentedString(elementVlanRole)).append("\n");
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
