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

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.UnassignVlanTagRequest;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.UnassignVlanTagRequestInput;

public class UnassignVlanTagRequestTest {
    
    private UnassignVlanTagRequest unassignVlanTagRequest;
    private UnassignVlanTagRequest unassignVlanTagRequest1;
    
    @Before
    public void setUp()
    {
        unassignVlanTagRequest= new UnassignVlanTagRequest();
        unassignVlanTagRequest1= new UnassignVlanTagRequest();
    }
    
    @Test
    public void testInput()
    {
        List<UnassignVlanTagRequestInput> input = new ArrayList<>();
        unassignVlanTagRequest.input(input);
        assertEquals(input, unassignVlanTagRequest.getInput());
    }
    
    @Test
    public void testEquals()
    {
        assertTrue(unassignVlanTagRequest.equals(unassignVlanTagRequest1));
    }
    
    @Test
    public void testToStringAndHashcode()
    {
        assertTrue(unassignVlanTagRequest.toString() instanceof String);
        assertTrue((Integer)unassignVlanTagRequest.hashCode() instanceof Integer);
    }

}
