/*******************************************************************************
 * Copyright © 2018 IBM.
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

package org.onap.ccsdk.apps.ms.vlangtagapi.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.VlanTag;

public class VlanTagTest {
    private VlanTag vlanTag;
    private VlanTag vlanTag1;

    @Before
    public void setUp() {
        vlanTag = new VlanTag();
        vlanTag1 = new VlanTag();
    }

    @Test
    public void testVlanUuid() {
        vlanTag.vlanUuid("vlanUuid");
        assertEquals("vlanUuid", vlanTag.getVlanUuid());
    }
    
    @Test
    public void testVlantagName() {
        vlanTag.vlantagName("vlantagName");
        assertEquals("vlantagName", vlanTag.getVlantagName());
    }

    @Test
    public void testVlantagValue() {
        vlanTag.vlantagValue("vlantagValue");
        assertEquals("vlantagValue", vlanTag.getVlantagValue());
    }

    @Test
    public void testEqualsAndHashCode() {
        assertTrue(vlanTag.equals(vlanTag1));
        assertTrue((Integer)vlanTag.hashCode() instanceof Integer);
    }

    @Test
    public void testElementVlanRole() {
        vlanTag.setElementVlanRole("role");
        assertEquals("role", vlanTag.getElementVlanRole());
        assertTrue(vlanTag.elementVlanRole("role") instanceof VlanTag);
    }

    @Test
    public void testToString() {
        String value = vlanTag.toString();
        assertEquals("class VlanTag {\n" +
                "    vlanUuid: null\n" +
                "    vlantagName: null\n" +
                "    vlantagValue: null\n" +
                "    elementVlanRole: null\n" +
                "}", value);
    }
    
}
