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
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.UnassignVlanTagResponse;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.UnassignVlanTagResponseOutput;

public class UnassignVlanTagResponseTest {

    private UnassignVlanTagResponse unassignVlanTagResponse;
    private UnassignVlanTagResponse unassignVlanTagResponse1;
    
    @Before
    public void setUp()
    {
        unassignVlanTagResponse= new UnassignVlanTagResponse();
        unassignVlanTagResponse1= new UnassignVlanTagResponse();
    }
    
    @Test
    public void testOutput()
    {
        List<UnassignVlanTagResponseOutput> output= new ArrayList<>();
        unassignVlanTagResponse.output(output);
        assertEquals(output, unassignVlanTagResponse.getOutput());
    }
    
    @Test
    public void testErrorCode()
    {
        unassignVlanTagResponse.errorCode(200);
        Integer errorCode=200;
        assertEquals(errorCode, unassignVlanTagResponse.getErrorCode());
    }
    
    @Test
    public void testErrorMessage()
    {
        unassignVlanTagResponse.errorMessage("testErrorMessage");
        assertEquals("testErrorMessage", unassignVlanTagResponse.getErrorMessage());
    }
    
    @Test
    public void testEqualsAndHashCode()
    {
        assertTrue(unassignVlanTagResponse.equals(unassignVlanTagResponse1));
        assertTrue((Integer)unassignVlanTagResponse.hashCode() instanceof Integer);
    }
}
