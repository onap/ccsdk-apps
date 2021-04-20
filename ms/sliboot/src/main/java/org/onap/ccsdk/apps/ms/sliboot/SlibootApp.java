/*-
 * ============LICENSE_START=======================================================
 * ONAP - CCSDK
 * ================================================================================
 * Copyright (C) 2020 AT&T Intellectual Property. All rights reserved.
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

package org.onap.ccsdk.apps.ms.sliboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.context.annotation.Bean;

import org.onap.aaf.cadi.filter.CadiFilter;

@SpringBootApplication(scanBasePackages={ "org.onap.ccsdk.apps.ms.sliboot.*", "org.onap.ccsdk.apps.services" })
@EnableJpaRepositories("org.onap.ccsdk.apps.ms.sliboot.*")
@EntityScan("org.onap.ccsdk.apps.ms.sliboot.*")
@EnableTransactionManagement
@EnableSwagger2
public class SlibootApp {

  private static final Logger log = LoggerFactory.getLogger(SlibootApp.class);

  public static void main(String[] args) throws Exception {
    SpringApplication.run(SlibootApp.class, args);
  }

  @Bean
	@Order(1)
	public FilterRegistrationBean<CadiFilter> cadiFilter() {
		CadiFilter filter = new CadiFilter();

		FilterRegistrationBean<CadiFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(filter);
		if ("none".equals(System.getProperty("cadi_prop_files", "none"))) {
            log.info("cadi_prop_files undefined, AAF CADI disabled");
			registrationBean.addUrlPatterns("/xxxx/*");
		} else {
			registrationBean.addUrlPatterns("/*");
			registrationBean.addInitParameter("cadi_prop_files", System.getProperty("cadi_prop_files"));
		}

		return registrationBean;
	} 

}
