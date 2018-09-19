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
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.AssignVlanTagResponse;
import org.onap.ccsdk.apps.ms.vlantagapi.core.model.AssignVlanTagResponseOutput;;

public class AssignVlanTagResponseTest {
    private AssignVlanTagResponse assignVlanTagResponse;
    
    private AssignVlanTagResponse assignVlanTagResponse1;
    
    @Before
    public void setUp()
    {
        assignVlanTagResponse= new AssignVlanTagResponse();
        assignVlanTagResponse1= new AssignVlanTagResponse();
    }
    
    @Test
    public void testOutput()
    {
        List<AssignVlanTagResponseOutput> output=new ArrayList<AssignVlanTagResponseOutput>();
        assignVlanTagResponse.output(output);
        assertEquals(output, assignVlanTagResponse.getOutput());
    }
    
    @Test
    public void testErrorCode()
    {
        assignVlanTagResponse.errorCode(200);
        Integer expected=200;
        assertEquals(expected, assignVlanTagResponse.getErrorCode());
    }
    
    @Test
    public void testEqualsAndHashcode()
    {
        assertTrue(assignVlanTagResponse.equals(assignVlanTagResponse1));
        assertTrue((Integer)assignVlanTagResponse.hashCode() instanceof Integer);
    }
    
   
}
