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
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.AssignVlanTagRequest;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.AssignVlanTagRequestInput;

public class AssignVlanTagRequestTest {
    
    private AssignVlanTagRequest assignVlanTagRequest;
    private AssignVlanTagRequest assignVlanTagRequest1;
    private List<AssignVlanTagRequestInput> input;
    
    @Before
    public void setUp()
    {
        assignVlanTagRequest = new AssignVlanTagRequest();
        assignVlanTagRequest1 = new AssignVlanTagRequest();
        input= new ArrayList<>();
    }
    
    @Test
    public void testGetSetOutput()
    {
        assignVlanTagRequest.setInput(input);
        assignVlanTagRequest.input(assignVlanTagRequest.getInput());
        assertEquals(input, assignVlanTagRequest.getInput());
    }
    
    @Test
    public void testEquals()
    {
        assignVlanTagRequest.setInput(input);
        assignVlanTagRequest1.setInput(input);
        assertEquals(true, assignVlanTagRequest.equals(assignVlanTagRequest1));
    }
    
    @Test
    public void testToStringAndHashCode()
    {
        assertTrue(assignVlanTagRequest.toString() instanceof String);
        assertTrue((Integer)assignVlanTagRequest.hashCode() instanceof Integer);
    }

}
