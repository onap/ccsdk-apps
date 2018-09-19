/*-
 * ============LICENSE_START=======================================================
 * ONAP : CCSDK.apps
 * ================================================================================
 * Copyright (C) 2018 IBM.
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

package org.onap.ccsdk.apps.ms.neng.persistence.entity;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IdentifierMapTest {
	private IdentifierMap identifierMap;

    @Before
    public void setUp() {
    	identifierMap = new IdentifierMap();
    }

    @Test
    public void testGetSetIdentifierMapId() {
    	identifierMap.setIdentifierMapId(1);
        Integer expected = 1;
        Assert.assertEquals(expected, identifierMap.getIdentifierMapId());
    }
    
    @Test
    public void testGetSetPolicyFnName() {
    	identifierMap.setPolicyFnName("PolicyFnName");
        String expected = "PolicyFnName";
        Assert.assertEquals(expected, identifierMap.getPolicyFnName());
    }
    
    @Test
    public void testGetSetJsFnName() {
    	identifierMap.setJsFnName("JsFnName");
        String expected = "JsFnName";
        Assert.assertEquals(expected, identifierMap.getJsFnName());
    }
    
    @Test
    public void testGetSetCreatedTime() throws ParseException {
    	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2007");
        long time = date.getTime();
        Timestamp timeStamp = new Timestamp(time);
    	identifierMap.setCreatedTime(timeStamp);
    	Timestamp expected = timeStamp;
        Assert.assertEquals(expected, identifierMap.getCreatedTime());
    }
    
    @Test
    public void testGetSetCreatedBy() {
    	identifierMap.setCreatedBy("Name");
        String expected = "Name";
        Assert.assertEquals(expected, identifierMap.getCreatedBy());
    }
    
    @Test
    public void testGetSetLastUpdatedTime() throws ParseException {
    	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2007");
        long time = date.getTime();
        Timestamp timeStamp = new Timestamp(time);
    	identifierMap.setLastUpdatedTime(timeStamp);
    	Timestamp expected = timeStamp;
        Assert.assertEquals(expected, identifierMap.getLastUpdatedTime());
    }
    
    @Test
    public void testGetSetLastUpdatedBy() {
    	identifierMap.setLastUpdatedBy("Name");
        String expected = "Name";
        Assert.assertEquals(expected, identifierMap.getLastUpdatedBy());
    }
    
}
