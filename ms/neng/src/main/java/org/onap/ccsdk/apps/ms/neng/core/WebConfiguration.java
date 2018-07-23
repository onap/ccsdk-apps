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

package org.onap.ccsdk.apps.ms.neng.core;

import org.onap.ccsdk.apps.ms.neng.core.rs.interceptors.AaiAuthorizationInterceptor;
import org.onap.ccsdk.apps.ms.neng.core.rs.interceptors.PolicyManagerAuthorizationInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Configuration for the API part of the micro-service.
 */
@Configuration
public class WebConfiguration {
    /**
     * Creates the bean for configuring swagger.
     */
    @Bean
    public WebMvcConfigurerAdapter forwardToIndex() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/swagger").setViewName("redirect:/swagger/index.html");
                registry.addViewController("/swagger/").setViewName("redirect:/swagger/index.html");
                registry.addViewController("/docs").setViewName("redirect:/docs/html/index.html");
                registry.addViewController("/docs/").setViewName("redirect:/docs/html/index.html");
            }
        };
    }

    /**
     * Creates the bean for configuring policy manager interceptor.
     */
    @Bean
    public RestTemplateBuilder policyMgrRestTempBuilder(PolicyManagerAuthorizationInterceptor auth) {
        RestTemplateBuilder restTemplateBuiler = new RestTemplateBuilder();
        return restTemplateBuiler.additionalInterceptors(auth);
    }

    /**
     * Creates the bean for configuring A&AI interceptor.
     */
    @Bean
    public RestTemplateBuilder aaiRestTempBuilder(AaiAuthorizationInterceptor auth) {
        RestTemplateBuilder restTemplateBuiler = new RestTemplateBuilder();
        return restTemplateBuiler.additionalInterceptors(auth);
    }
}
