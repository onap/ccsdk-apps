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

package org.onap.ccsdk.apps.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.onap.ccsdk.sli.adaptors.resource.sql.SqlResource;
import org.onap.ccsdk.sli.core.sli.ConfigurationException;
import org.onap.ccsdk.sli.core.sli.SvcLogicJavaPlugin;
import org.onap.ccsdk.sli.core.sli.SvcLogicLoader;
import org.onap.ccsdk.sli.core.sli.SvcLogicRecorder;
import org.onap.ccsdk.sli.core.sli.SvcLogicResource;
import org.onap.ccsdk.sli.core.sli.SvcLogicStore;
import org.onap.ccsdk.sli.core.sli.SvcLogicStoreFactory;
import org.onap.ccsdk.sli.core.sli.provider.base.HashMapResolver;
import org.onap.ccsdk.sli.core.sli.provider.base.SvcLogicPropertiesProvider;
import org.onap.ccsdk.sli.core.sli.provider.base.SvcLogicServiceBase;
import org.onap.ccsdk.sli.core.sli.provider.base.SvcLogicServiceImplBase;
import org.onap.ccsdk.sli.core.sli.recording.Slf4jRecorder;
import org.onap.ccsdk.sli.core.slipluginutils.SliPluginUtils;
import org.onap.ccsdk.sli.core.slipluginutils.SliStringUtils;
import org.onap.ccsdk.sli.plugins.prop.PropertiesNode;
import org.onap.ccsdk.sli.plugins.restapicall.RestapiCallNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
@Service
public class SvcLogicFactory {
  private static final Logger log = LoggerFactory.getLogger(SvcLogicFactory.class);

  @Autowired
  List<SvcLogicRecorder> recorders;

  @Autowired
  List<SvcLogicJavaPlugin> plugins;

  @Autowired
  List<SvcLogicResource> svcLogicResources;

  @Bean
  public SvcLogicStore getStore() throws Exception {
    SvcLogicPropertiesProvider propProvider = new SvcLogicPropertiesProvider() {

      @Override
      public Properties getProperties() {
        Properties props = new Properties();


        String propPath = System.getProperty("serviceLogicProperties", "");

        if ("".equals(propPath)) {
          propPath = System.getenv("SVCLOGIC_PROPERTIES");
        }


        if ((propPath == null) || propPath.length() == 0) {
          propPath = "src/main/resources/svclogic.properties";
        }
        System.out.println(propPath);
        try (FileInputStream fileInputStream = new FileInputStream(propPath)) {
          props = new Properties();
          props.load(fileInputStream);
        } catch (final IOException e) {
          log.error("Failed to load properties for file: {}", propPath,
              new ConfigurationException("Failed to load properties for file: " + propPath, e));
        }
        return props;
      }
    };
    SvcLogicStore store = SvcLogicStoreFactory.getSvcLogicStore(propProvider.getProperties());
    return store;
  }

  @Bean
  public SvcLogicLoader createLoader() throws Exception {
    String serviceLogicDirectory = System.getProperty("serviceLogicDirectory");
    if (serviceLogicDirectory == null) {
      serviceLogicDirectory = "src/main/resources";
    }

    System.out.println("serviceLogicDirectory is " + serviceLogicDirectory);
    SvcLogicLoader loader = new SvcLogicLoader(serviceLogicDirectory, getStore());

    try {
      loader.loadAndActivate();
    } catch (IOException e) {
      log.error("Cannot load directed graphs", e);
    }
    return loader;
  }

  @Bean
  public SvcLogicServiceBase createService() throws Exception {
    HashMapResolver resolver = new HashMapResolver();
    for (SvcLogicRecorder recorder : recorders) {
      resolver.addSvcLogicRecorder(recorder.getClass().getName(), recorder);

    }
    for (SvcLogicJavaPlugin plugin : plugins) {
      resolver.addSvcLogicSvcLogicJavaPlugin(plugin.getClass().getName(), plugin);

    }
    for (SvcLogicResource svcLogicResource : svcLogicResources) {
      resolver.addSvcLogicResource(svcLogicResource.getClass().getName(), svcLogicResource);
    }

    return new SvcLogicServiceImplBase(getStore(), resolver);
  }

  @Bean
  public Slf4jRecorder slf4jRecorderNode() {
    return new Slf4jRecorder();
  }

  @Bean
  public SliPluginUtils sliPluginUtil() {
    return new SliPluginUtils();
  }

  @Bean
  public SliStringUtils sliStringUtils() {
    return new SliStringUtils();
  }
  
  @Bean
  public RestapiCallNode restapiCallNode() {
      return new RestapiCallNode();
  }
  
  @Bean
  public PropertiesNode propertiesNode() {
      return new PropertiesNode();
  }

  @Bean
  public SqlResource sqlResource() {
    return new SqlResource();
  }
  

}
