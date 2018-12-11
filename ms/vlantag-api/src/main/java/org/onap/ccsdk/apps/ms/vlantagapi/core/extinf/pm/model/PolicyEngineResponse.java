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
package org.onap.ccsdk.apps.ms.vlantagapi.core.extinf.pm.model;

import java.io.Serializable;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PolicyEngineResponse.java Purpose: POJO representing policy manager get-config response
 *
 * @author Saurav Paira
 * @version 1.0
 */
public class PolicyEngineResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("policyConfigMessage")
	private String policyConfigMessage;

	@JsonProperty("policyConfigStatus")
	private String policyConfigStatus;

	@JsonProperty("type")
	private String type;

	@JsonProperty("config")
	private String config;

	@JsonProperty("policyName")
	private String policyName;

	@JsonProperty("policyType")
	private String policyType;
	
	@JsonProperty("policyVersion")
	private String policyVersion;

	@JsonProperty("matchingConditions")
	private HashMap<String, String> matchingConditions;

	@JsonProperty("responseAttributes")
	private HashMap<String, String> responseAttributes;

	@JsonProperty("property")
	private String property;
	
	public PolicyEngineResponse() {
		// this method does nothing
	}

	public String getPolicyConfigMessage() {
		return policyConfigMessage;
	}
	public void setPolicyConfigMessage(String policyConfigMessage) {
		this.policyConfigMessage = policyConfigMessage;
	}
	public String getPolicyConfigStatus() {
		return policyConfigStatus;
	}
	public void setPolicyConfigStatus(String policyConfigStatus) {
		this.policyConfigStatus = policyConfigStatus;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getConfig() {
		return config;
	}
	@JsonProperty("config")
	public void setConfig(String config) {
		this.config = config;
	}
	public String getPolicyName() {
		return policyName;
	}
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}
	public String getPolicyType() {
		return policyType;
	}
	public void setPolicyType(String type) {
		this.policyType = type;
	}
	public String getPolicyVersion() {
		return policyVersion;
	}
	public void setPolicyVersion(String policyVersion) {
		this.policyVersion = policyVersion;
	}
	public HashMap<String, String> getMatchingConditions() {
		return matchingConditions;
	}
	@JsonProperty("matchingConditions")
	public void setMatchingConditions(HashMap<String, String> matchingConditions) {
		this.matchingConditions = matchingConditions;
	}
	public HashMap<String, String> getResponseAttributes() {
		return responseAttributes;
	}
	public void setResponseAttributes(HashMap<String, String> responseAttributes) {
		this.responseAttributes = responseAttributes;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}

	@Override
	public String toString() {
		return "PolicyEngineResponse [policyConfigMessage=" + policyConfigMessage + ", policyConfigStatus="
				+ policyConfigStatus + ", type=" + type + ", config=" + config + ", policyName=" + policyName
				+ ", policyType=" + policyType + ", policyVersion=" + policyVersion + ", matchingConditions="
				+ matchingConditions + ", responseAttributes=" + responseAttributes + ", property=" + property + "]";
	}

}
