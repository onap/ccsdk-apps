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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * AllowedRanges.java Purpose: POJO representing policy manager
 * get-config response AllowedRanges model defines Vlantag Range
 *
 * @author Saurav Paira
 * @version 1.0
 */
public class AllowedRanges {
	
	@JsonProperty("min")
	String min;
	
	@JsonProperty("max")
	String max;

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	@Override
	public String toString() {
		return "AllowedRanges [min=" + min + ", max=" + max + "]";
	}
	
	
}
