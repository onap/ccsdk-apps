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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PolicyPropertyMethodUtilsTest {

    @Test
    public void testAll() {
        assertEquals("TRLAK", PolicyPropertyMethodUtils.substring("TRLAKDG", "5"));
        assertEquals("KDG", PolicyPropertyMethodUtils.substring("TRLAKDG", "-3"));
        assertEquals("TRLAKDG", PolicyPropertyMethodUtils.substring("TRLAKDG", "8"));
        assertEquals("TRLAKDG", PolicyPropertyMethodUtils.substring("TRLAKDG", "-11"));
        assertEquals("XYSZ1NNN", PolicyPropertyMethodUtils.toUpperCase("XySz1NNN"));
        assertEquals("xysz1nnn", PolicyPropertyMethodUtils.toLowerCase("XySz1NNN"));
    }
}
