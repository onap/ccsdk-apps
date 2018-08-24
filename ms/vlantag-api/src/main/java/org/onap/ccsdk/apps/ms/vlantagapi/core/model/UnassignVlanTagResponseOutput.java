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
 * UnassignVlanTagResponseOutput.java Purpose: Provide Unassign VlanTag Response Output Model
 *
 * @author Saurav Paira
 * @version 1.0
 */
public class UnassignVlanTagResponseOutput {
	private @Valid String vlanType = null;
	private @Valid String vlanTagKey = null;
	private @Valid String vlantagName = null;

	/**
	 **/
	public UnassignVlanTagResponseOutput vlanType(String vlanType) {
		this.vlanType = vlanType;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("vlan-type")
	public String getVlanType() {
		return vlanType;
	}

	public void setVlanType(String vlanType) {
		this.vlanType = vlanType;
	}

	/**
	 **/
	public UnassignVlanTagResponseOutput key(String key) {
		this.vlanTagKey = key;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("vlan-tag-key")
	public String getVlanTagKey() {
		return vlanTagKey;
	}

	public void setVlanTagKey(String vlanTagKey) {
		this.vlanTagKey = vlanTagKey;
	}
	
	/**
	 **/
	public UnassignVlanTagResponseOutput vlantagName(String vlantagName) {
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

	

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UnassignVlanTagResponseOutput unassignVlanTagResponseOutput = (UnassignVlanTagResponseOutput) o;
		return Objects.equals(vlanType, unassignVlanTagResponseOutput.vlanType)
				&& Objects.equals(vlanTagKey, unassignVlanTagResponseOutput.vlanTagKey)
				&& Objects.equals(vlantagName, unassignVlanTagResponseOutput.vlantagName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(vlanType, vlanTagKey, vlantagName);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class UnassignVlanTagResponseOutput {\n");

		sb.append("    vlanType: ").append(toIndentedString(vlanType)).append("\n");
		sb.append("    vlanTagKey: ").append(toIndentedString(vlanTagKey)).append("\n");
		sb.append("    vlantagName: ").append(toIndentedString(vlantagName)).append("\n");
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
