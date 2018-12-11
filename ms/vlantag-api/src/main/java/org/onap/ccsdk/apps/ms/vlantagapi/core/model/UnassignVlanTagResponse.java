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
package org.onap.ccsdk.apps.ms.vlantagapi.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * UnassignVlanTagResponse.java Purpose: Provide Unassign VlanTag Response Model
 *
 * @author Saurav Paira
 * @version 1.0
 */
public class UnassignVlanTagResponse {
	private @Valid List<UnassignVlanTagResponseOutput> output = new ArrayList<>();
	private @Valid Integer errorCode = null;
	private @Valid String errorMessage = null;

	/**
	 **/
	public UnassignVlanTagResponse output(List<UnassignVlanTagResponseOutput> output) {
		this.output = output;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("output")
	public List<UnassignVlanTagResponseOutput> getOutput() {
		return output;
	}

	public void setOutput(List<UnassignVlanTagResponseOutput> output) {
		this.output = output;
	}
	
	/**
	 **/
	public UnassignVlanTagResponse errorCode(Integer errorCode) {
		this.errorCode = errorCode;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("error-code")
	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 **/
	public UnassignVlanTagResponse errorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		return this;
	}

	@ApiModelProperty(value = "")
	@JsonProperty("error-message")
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		UnassignVlanTagResponse unassignVlanTagResponse = (UnassignVlanTagResponse) o;
		return Objects.equals(output, unassignVlanTagResponse.output)
				&& Objects.equals(errorCode, unassignVlanTagResponse.errorCode)
				&& Objects.equals(errorMessage, unassignVlanTagResponse.errorMessage);
	}

	@Override
	public int hashCode() {
		return Objects.hash(output, errorCode, errorMessage);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class UnassignVlanTagResponse {\n");

		sb.append("    output: ").append(toIndentedString(output)).append("\n");
		sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
		sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
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
