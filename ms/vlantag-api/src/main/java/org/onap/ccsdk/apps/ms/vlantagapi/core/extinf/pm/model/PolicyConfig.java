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

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PolicyConfig.java Purpose: POJO representing policy manager
 * get-config response
 *
 * @author Saurav Paira
 * @version 1.0
 */
public class PolicyConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("configName")
	private String configName;
	
	@JsonProperty("riskLevel")
	private String riskLevel;

	@JsonProperty("policyName")
	private String policyName;

	@JsonProperty("policyScope")
	private String policyScope;

	@JsonProperty("guard")
	private String guard;

	@JsonProperty("description")
	private String description;

	@JsonProperty("priority")
	private String priority;

	@JsonProperty("uuid")
	private String uuid;

	@JsonProperty("version")
	private String version;

	@JsonProperty("content")
	private PolicyContent content;

	@JsonProperty("riskType")
	private String riskType;

	@JsonProperty("service")
	private String service;

	@JsonProperty("location")
	private String location;

	@JsonProperty("templateVersion")
	private String templateVersion;

	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getPolicyName() {
		return policyName;
	}
	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}
	public String getPolicyScope() {
		return policyScope;
	}
	public void setPolicyScope(String policyScope) {
		this.policyScope = policyScope;
	}
	public String getGuard() {
		return guard;
	}
	public void setGuard(String guard) {
		this.guard = guard;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	public PolicyContent getContent() {
		return content;
	}
	public void setContent(PolicyContent content) {
		this.content = content;
	}
	public String getRiskType() {
		return riskType;
	}
	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTemplateVersion() {
		return templateVersion;
	}
	public void setTemplateVersion(String templateVersion) {
		this.templateVersion = templateVersion;
	}
	@Override
	public String toString() {
		return "PolicyConfig [configName=" + configName + ", riskLevel=" + riskLevel + ", policyName=" + policyName
				+ ", policyScope=" + policyScope + ", guard=" + guard + ", description=" + description + ", priority="
				+ priority + ", uuid=" + uuid + ", version=" + version + ", content=" + content + ", riskType="
				+ riskType + ", service=" + service + ", location=" + location + ", templateVersion=" + templateVersion
				+ "]";
	}
	
}
