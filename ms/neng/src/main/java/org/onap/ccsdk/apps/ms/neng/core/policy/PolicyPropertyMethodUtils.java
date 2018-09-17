/*-
 * ============LICENSE_START=======================================================
 * ONAP : CCSDK.apps
 * ================================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.ccsdk.apps.ms.neng.core.policy;

/**
 * Utility methods equivalent to the JavaScript like functions used in policies by the policy-manager.
 */
public class PolicyPropertyMethodUtils {

	private PolicyPropertyMethodUtils() {
	}
	
    /**
     * Equivalent to the substring function used by policy-manager (which works similar to JavaScript
     * substring function).
     */
    public static String substring(String sourceStr, String startIndex, String endIndex) {
        return sourceStr.substring(Integer.parseInt(startIndex), Integer.parseInt(endIndex));
    }

    /**
     * Equivalent to the substring function used by policy-manager (which works similar to JavaScript
     * substring function).
     */
    public static String substring(String sourceStr, String length) {

        int startIndexInt = 0;
        int subStrLength = Integer.parseInt(length);
        if (subStrLength < 0) {

            startIndexInt = sourceStr.length() + subStrLength;
            startIndexInt = (startIndexInt < 0) ? 0 : startIndexInt;

            return sourceStr.substring(startIndexInt);
        } else if (subStrLength > sourceStr.length()) {
            subStrLength = sourceStr.length();
        }
        return sourceStr.substring(startIndexInt, subStrLength);
    }

    /**
     * Equivalent to the to_upper_case function used by policy-manager.
     */
    public static String toUpperCase(String sourceStr) {
        return sourceStr.toUpperCase();
    }

    /**
     * Equivalent to the to_lower_case function used by policy-manager.
     */
    public static String toLowerCase(String sourceStr) {
        return sourceStr.toLowerCase();
    }
}
