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
 * Elements.java Purpose: POJO representing policy manager
 * get-config response Element model within a Resource Model
 *
 * @author Saurav Paira
 * @version 1.0
 */
public class Elements {
	
	@JsonProperty("recycle-vlantag-range")
	String recycleVlantagRange;
	
	@JsonProperty("overwrite")
	String overwrite;
	
	@JsonProperty("vlantag-name")
	String vlantagName;
	
	@JsonProperty("allowed-range")
	private List<AllowedRanges> allowedRanges;
	
	@JsonProperty("shared-range")
    private String sharedRange;
	
	@JsonProperty("element-vlan-role")
	String elementVlanRole;
	

	public String getRecycleVlantagRange() {
		return recycleVlantagRange;
	}

	public void setRecycleVlantagRange(String recycleVlantagRange) {
		this.recycleVlantagRange = recycleVlantagRange;
	}

	public String getOverwrite() {
		return overwrite;
	}

	public void setOverwrite(String overwrite) {
		this.overwrite = overwrite;
	}

	public String getVlantagName() {
		return vlantagName;
	}

	public void setVlantagName(String vlantagName) {
		this.vlantagName = vlantagName;
	}

	public List<AllowedRanges> getAllowedRanges() {
		return allowedRanges;
	}

	public void setAllowedRanges(List<AllowedRanges> allowedRanges) {
		this.allowedRanges = allowedRanges;
	}

	public String getSharedRange() {
        return sharedRange;
    }

    public void setSharedRange(String sharedRange) {
        this.sharedRange = sharedRange;
    }

    public String getElementVlanRole() {
		return elementVlanRole;
	}

	public void setElementVlanRole(String elementVlanRole) {
		this.elementVlanRole = elementVlanRole;
	}

    @Override
    public String toString() {
        return "Elements [recycleVlantagRange=" + recycleVlantagRange + ", overwrite=" + overwrite + ", vlantagName="
                + vlantagName + ", allowedRanges=" + allowedRanges + ", sharedRange=" + sharedRange
                + ", elementVlanRole=" + elementVlanRole + "]";
    }

	

	
	
}
