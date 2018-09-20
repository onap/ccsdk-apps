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

public class PolicyDetailsTest {
	private PolicyDetails policyDetails;

    @Before
    public void setUp() {
    	policyDetails = new PolicyDetails();
    }

    @Test
    public void testGetSetPolicyId() {
    	policyDetails.setPolicyId(1);
        Integer expected = 1;
        Assert.assertEquals(expected, policyDetails.getPolicyId());
    }
    
    @Test
    public void testGetSetPolicyName() {
    	policyDetails.setPolicyName("PolicyName");
        String expected = "PolicyName";
        Assert.assertEquals(expected, policyDetails.getPolicyName());
    }
    
    @Test
    public void testGetSetPolicyResponse() {
    	policyDetails.setPolicyResponse("PolicyResponse");
        String expected = "PolicyResponse";
        Assert.assertEquals(expected, policyDetails.getPolicyResponse());
    }
    
    @Test
    public void testGetSetCreatedTime() throws ParseException {
    	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2007");
        long time = date.getTime();
        Timestamp timeStamp = new Timestamp(time);
    	policyDetails.setCreatedTime(timeStamp);
        String expected = "PolicyResponse";
        Assert.assertEquals(timeStamp, policyDetails.getCreatedTime());
    }
    
    
}
