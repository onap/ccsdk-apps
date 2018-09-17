/*
 * Copyright © 2017-2018 AT&T Intellectual Property.
 * Modifications Copyright © 2018 IBM.
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
 */

package org.onap.ccsdk.apps.controllerblueprints;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.ccsdk.apps.controllerblueprints.service.domain.ConfigModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.att.eelf.configuration.EELFLogger;
import com.att.eelf.configuration.EELFManager;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ControllerBluprintsApplicationTest {
    private static EELFLogger log = EELFManager.getInstance().getLogger(ControllerBluprintsApplicationTest.class);

    @Autowired
    private TestRestTemplate restTemplate;
    private HttpHeaders headers;
    private ResponseEntity<ConfigModel> entity;

    @Before
    public void setUp(){
        headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        entity = this.restTemplate
                .exchange("/api/v1/config-model/1", HttpMethod.GET, new HttpEntity<>(headers),ConfigModel.class);
        
    }

    @Test
    public void testConfigModel() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        ResponseEntity<ConfigModel> entity = this.restTemplate
                .exchange("/api/v1/config-model/1", HttpMethod.GET, new HttpEntity<>(headers),ConfigModel.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assert.assertNotNull("failed to get response Config model",entity.getBody());
    }

    @Test
    public void testConfigModelFailure() {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        ResponseEntity<ConfigModel> entity = this.restTemplate
                .exchange("/api/v1/config-model-not-found/1", HttpMethod.GET, new HttpEntity<>(headers),ConfigModel.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assert.assertNotNull("failed to get response Config model",entity.getBody());
    }
}
