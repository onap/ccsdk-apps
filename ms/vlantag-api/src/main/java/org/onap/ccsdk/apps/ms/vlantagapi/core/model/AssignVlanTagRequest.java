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
 * AssignVlanTagRequest.java Purpose: Provide Assign VlanTag Request Model
 *
 * @author Saurav Paira
 * @version 1.0
 */
public class AssignVlanTagRequest {
	
	private @Valid List<AssignVlanTagRequestInput> input = new ArrayList<AssignVlanTagRequestInput>();

	  /**
	   **/
	  public AssignVlanTagRequest input(List<AssignVlanTagRequestInput> input) {
	    this.input = input;
	    return this;
	  }

	  
	  @ApiModelProperty(value = "")
	  @JsonProperty("input")
	  public List<AssignVlanTagRequestInput> getInput() {
	    return input;
	  }
	  public void setInput(List<AssignVlanTagRequestInput> input) {
	    this.input = input;
	  }


	  @Override
	  public boolean equals(java.lang.Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }
	    AssignVlanTagRequest assignVlanTagRequest = (AssignVlanTagRequest) o;
	    return Objects.equals(input, assignVlanTagRequest.input);
	  }

	  @Override
	  public int hashCode() {
	    return Objects.hash(input);
	  }

	  @Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class AssignVlanTagRequest {\n");
	    
	    sb.append("    input: ").append(toIndentedString(input)).append("\n");
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
