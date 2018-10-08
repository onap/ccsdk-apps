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

public class ServiceParameterTest {
    private ServiceParameter serviceParameter;

    @Before
    public void setUp() {
        serviceParameter = new ServiceParameter();
    }

    @Test
    public void testGetSetServiceParameterId() {
        serviceParameter.setServiceParameterId(1);
        Integer expected = 1;
        Assert.assertEquals(expected, serviceParameter.getServiceParameterId());
    }
    
    @Test
    public void testGetSetName() {
        serviceParameter.setName("Name");
        String expected = "Name";
        Assert.assertEquals(expected, serviceParameter.getName());
    }
    
    @Test
    public void testGetSetValue() {
        serviceParameter.setValue("Value");
        String expected = "Value";
        Assert.assertEquals(expected, serviceParameter.getValue());
    }
    
    @Test
    public void testGetSetCreatedTime() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2007");
        long time = date.getTime();
        Timestamp timeStamp = new Timestamp(time);
        serviceParameter.setCreatedTime(timeStamp);
        Assert.assertEquals(timeStamp, serviceParameter.getCreatedTime());
    }
    
    @Test
    public void testGetSetCreatedBy() {
        serviceParameter.setCreatedBy("CreatedBy");
        String expected = "CreatedBy";
        Assert.assertEquals(expected, serviceParameter.getCreatedBy());
    }
    
    @Test
    public void testGetSetLastUpdatedBy() {
        serviceParameter.setLastUpdatedBy("LastUpdatedBy");
        String expected = "LastUpdatedBy";
        Assert.assertEquals(expected, serviceParameter.getLastUpdatedBy());
    }
    
    @Test
    public void testGetSetLastUpdatedTime() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2007");
        long time = date.getTime();
        Timestamp timeStamp = new Timestamp(time);
        serviceParameter.setLastUpdatedTime(timeStamp);
        Assert.assertEquals(timeStamp, serviceParameter.getLastUpdatedTime());
    }
}

