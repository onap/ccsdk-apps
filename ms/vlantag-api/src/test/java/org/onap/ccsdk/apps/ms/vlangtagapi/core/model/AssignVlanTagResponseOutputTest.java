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

package org.onap.ccsdk.apps.ms.vlangtagapi.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.AssignVlanTagResponseOutput;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.VlanTag;

public class AssignVlanTagResponseOutputTest {
    
    private AssignVlanTagResponseOutput assignVlanTagResponseOutput;
    private AssignVlanTagResponseOutput assignVlanTagResponseOutput1;
    
    @Before
    public void setUp()
    {
        assignVlanTagResponseOutput= new AssignVlanTagResponseOutput();
        assignVlanTagResponseOutput1= new AssignVlanTagResponseOutput();
    }
    
    @Test
    public void testResourceName()
    {
        assignVlanTagResponseOutput.resourceName("resourceName");
        assertEquals("resourceName", assignVlanTagResponseOutput.getResourceName());
    }
    
    @Test
    public void testResourceValue()
    {
        assignVlanTagResponseOutput.resourceValue("resourceValue");
        assertEquals("resourceValue", assignVlanTagResponseOutput.getResourceValue());
    }
    
    @Test
    public void testResourceVlanRole()
    {
        assignVlanTagResponseOutput.resourceVlanRole("resourceVlanRole");
        assertEquals("resourceVlanRole", assignVlanTagResponseOutput.getResourceVlanRole());
    }
    
    @Test
    public void testStoredElements()
    {
        List<VlanTag> storedElements = new ArrayList<>();
        assignVlanTagResponseOutput.storedElements(storedElements);
        assertEquals(storedElements, assignVlanTagResponseOutput.getStoredElements());
    }
    
    @Test
    public void testEqualsAndHashCode()
    {
        assertTrue(assignVlanTagResponseOutput.equals(assignVlanTagResponseOutput1));
        assertTrue((Integer)assignVlanTagResponseOutput.hashCode() instanceof Integer);
    }
}
