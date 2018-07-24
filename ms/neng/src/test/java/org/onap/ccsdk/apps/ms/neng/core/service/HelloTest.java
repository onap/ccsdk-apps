/*-
 * ============LICENSE_START=======================================================
 * ONAP : CCSDK.apps
 * ================================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
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

package org.onap.ccsdk.apps.ms.neng.core.service;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.ccsdk.apps.ms.neng.core.service.rs.RestService;
import org.onap.ccsdk.apps.ms.neng.persistence.repository.GeneratedNameRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("test")
public class HelloTest {
    @Autowired
    RestService service;
    @Autowired
    GeneratedNameRespository repo;

    @Test
    public void testQuickHello() throws Exception {
        Response response = service.getQuickHello("test");
        assertEquals("message = Hello test!", response.getEntity().toString());
    }
}
