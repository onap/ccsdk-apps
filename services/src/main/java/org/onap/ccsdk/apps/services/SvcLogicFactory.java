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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.att.sdnctl.sli.adaptor.mso.MSOAdaptor;
import com.att.sdnctl.sli.plugin.eipam.EIPAMPlugin;
import com.att.sdnctl.sli.plugin.ipAddressTools.IpAddressTool;

import org.onap.ccsdk.sli.adaptors.aai.AAIService;
import org.onap.ccsdk.sli.adaptors.aai.AAIServiceProvider;
import org.onap.ccsdk.sli.adaptors.messagerouter.publisher.api.PublisherApi;
import org.onap.ccsdk.sli.adaptors.messagerouter.publisher.provider.impl.PublisherApiImpl;
import org.onap.ccsdk.sli.adaptors.netbox.api.NetboxClient;
import org.onap.ccsdk.sli.adaptors.netbox.impl.NetboxClientImpl;
import org.onap.ccsdk.sli.adaptors.netbox.impl.NetboxHttpClient;
import org.onap.ccsdk.sli.adaptors.netbox.property.NetboxProperties;
import org.onap.ccsdk.sli.adaptors.resource.mdsal.ConfigResource;
import org.onap.ccsdk.sli.adaptors.resource.mdsal.MdsalResourcePropertiesProviderImpl;
import org.onap.ccsdk.sli.adaptors.resource.mdsal.OperationalResource;
import org.onap.ccsdk.sli.adaptors.resource.sql.SqlResource;
import org.onap.ccsdk.sli.core.dblib.DBLIBResourceProvider;
import org.onap.ccsdk.sli.core.dblib.DBResourceManager;
import org.onap.ccsdk.sli.core.dblib.DbLibService;
import org.onap.ccsdk.sli.core.sli.ConfigurationException;
import org.onap.ccsdk.sli.core.sli.SvcLogicAdaptor;
import org.onap.ccsdk.sli.core.sli.SvcLogicException;
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
import org.onap.ccsdk.sli.core.utils.common.EnvProperties;
import org.onap.ccsdk.sli.plugins.contrail.ContrailAdaptor;
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
    private static final String SDNC_CONFIG_DIR = "SDNC_CONFIG_DIR";
    private static final String CONTRAIL_PROPERTIES = "contrail-adaptor.properties";

    @Autowired
    List<SvcLogicRecorder> recorders;

    @Autowired
    List<SvcLogicJavaPlugin> plugins;

    @Autowired
    List<SvcLogicResource> svcLogicResources;

    @Autowired
    List<SvcLogicAdaptor> adaptors;

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
                    props = new EnvProperties();
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
            log.info("Registering SvcLogicRecorder {}", recorder.getClass().getName());
            resolver.addSvcLogicRecorder(recorder.getClass().getName(), recorder);

        }
        for (SvcLogicAdaptor adaptor : adaptors) {
            log.info("Registering SvcLogicAdaptor {}", adaptor.getClass().getName());
            resolver.addSvcLogicAdaptor(adaptor.getClass().getName(), adaptor);

        }
        for (SvcLogicJavaPlugin plugin : plugins) {
            log.info("Registering SvcLogicJavaPlugin {}", plugin.getClass().getName());
            resolver.addSvcLogicSvcLogicJavaPlugin(plugin.getClass().getName(), plugin);

        }
        for (SvcLogicResource svcLogicResource : svcLogicResources) {
            log.info("Registering SvcLogicResource {}", svcLogicResource.getClass().getName());
            resolver.addSvcLogicResource(svcLogicResource.getClass().getName(), svcLogicResource);
        }

        return new SvcLogicServiceImplBase(getStore(), resolver);
    }

    @Bean
    public Slf4jRecorder slf4jRecorderNode() {
        return new Slf4jRecorder();
    }

    // Beans from sli/core

    @Bean
    public SliPluginUtils sliPluginUtil() {
        return new SliPluginUtils();
    }

    @Bean
    public SliStringUtils sliStringUtils() {
        return new SliStringUtils();
    }

    // Beans from sli/adaptors

    @Bean
    AAIService aaiService() {
        return new AAIService(new AAIServiceProvider());
    }

    @Bean
    public ConfigResource configResource() {
        return new ConfigResource(new MdsalResourcePropertiesProviderImpl());
    }

    @Bean
    public OperationalResource operationalResource() {
        return new OperationalResource(new MdsalResourcePropertiesProviderImpl());
    }

    @Bean
    public PublisherApi publisherApi() {
        return new PublisherApiImpl();
    }

    @Bean
    public NetboxClient netboxClient() {
        return new NetboxClientImpl();
    }

    @Bean
    public SqlResource sqlResource() {
        return new SqlResource();
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
    public ContrailAdaptor contrailAdaptor() {
        return new ContrailAdaptor(getPropertiesFromFile(CONTRAIL_PROPERTIES));
    }

    @Autowired
    RestapiCallNode restApiCallNode;

  @Bean
  public EIPAMPlugin eipamPlugin() throws SvcLogicException {
      EIPAMPlugin retval = new EIPAMPlugin();

      log.debug("Setting EIPAMPlugin restapiCallNode");
      retval.setRestapiCallNode(new RestapiCallNode());

      DbLibService dblibSvc = null;
      Properties dblibProps = System.getProperties();
      String cfgDir = System.getenv("SDNC_CONFIG_DIR");

      if ((cfgDir == null) || (cfgDir.length() == 0)) {
          cfgDir = "/opt/sdnc/data/properties";
      }

      File dblibPropFile = new File(cfgDir + "/dblib.properties");
      if (dblibPropFile.exists()) {
          try {
              dblibProps = new Properties();
              dblibProps.load(new FileInputStream(dblibPropFile));
          } catch (Exception e) {
              log.warn("Could not load properties file {}", dblibPropFile.getAbsolutePath(), e);
              return retval;
          }

          try {
              dblibSvc = new DBResourceManager(dblibProps);
          } catch (Exception e) {
              log.error("Caught exception trying to create dblib service", e);
              return retval;
          }
      }
      log.debug("Setting EIPAMPlugin dbLibService");
      retval.setDbLibService(dblibSvc);

      return retval;
  }


}
