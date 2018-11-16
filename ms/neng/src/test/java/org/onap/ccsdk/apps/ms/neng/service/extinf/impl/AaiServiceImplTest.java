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

package org.onap.ccsdk.apps.ms.neng.service.extinf.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.onap.ccsdk.apps.ms.neng.core.resource.model.AaiResponse;
import org.onap.ccsdk.apps.ms.neng.extinf.props.AaiProps;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

public class AaiServiceImplTest {
    private AaiServiceImpl aaiServiceImpl;

    @Before
    public void setUp() {
        aaiServiceImpl = new AaiServiceImpl();
    }

    @Test
    public void testSetAaiRestTempBuilder() throws Exception {
        RestTemplateBuilder aaiRestTempBuilder = new RestTemplateBuilder();
        aaiServiceImpl.setAaiRestTempBuilder(aaiRestTempBuilder);
        Assert.assertEquals(aaiRestTempBuilder, aaiServiceImpl.aaiRestTempBuilder);
    }

    @Test
    public void testBuildResponse() throws Exception {
        AaiResponse aaiResponse = aaiServiceImpl.buildResponse(true);
        Assert.assertEquals(aaiResponse.isRecFound(), true);
    }
    
    @Test(expected= Exception.class)
    public void testValidate() throws Exception {
    	AaiProps aaiProps=new AaiProps();
    	aaiProps.setUriBase("http://");
    	aaiServiceImpl.setAaiProps(aaiProps);
    	aaiServiceImpl.setRestTemplate(new RestTemplate());
        aaiServiceImpl.validate("testUrl/","testName");
        
    }
}
