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

import static org.mockito.Mockito.spy;

import org.mockito.Mockito;
import org.onap.ccsdk.apps.ms.neng.core.rs.interceptors.PolicyManagerAuthorizationInterceptor;
import org.onap.ccsdk.apps.ms.neng.service.extinf.impl.AaiServiceImpl;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootConfiguration
@ActiveProfiles("test")
@ComponentScan(basePackages = {"org.onap.ccsdk.apps.ms.neng"})
@EnableJpaRepositories(basePackages = "org.onap.ccsdk.apps.ms.neng.persistence.repository")
@EntityScan(basePackages = {"org.onap.ccsdk.apps.ms.neng.persistence.entity"})
@EnableTransactionManagement
public class TestApplicationConfig {

    @Bean
    @Primary
    public RestTemplateBuilder policyMgrRestTempBuilder(PolicyManagerAuthorizationInterceptor auth) {
        RestTemplateBuilder restTemplateBuiler = new RestTemplateBuilder();
        return spy(restTemplateBuiler.additionalInterceptors(auth));
    }

    @Bean
    @Primary
    AaiServiceImpl aaiServiceImpl() {
        return Mockito.mock(AaiServiceImpl.class);
    }
}
