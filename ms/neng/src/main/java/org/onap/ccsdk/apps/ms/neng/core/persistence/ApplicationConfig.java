/*-
 * ============LICENSE_START=======================================================
 * ONAP : CCSDK.apps
 * ================================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * *================================================================================
 * Modifications Copyright (C) 2018 IBM.
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

package org.onap.ccsdk.apps.ms.neng.core.persistence;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.StreamSupport;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the overall configuration of the application related to persistence.
 */
@Configuration
@EnableJpaRepositories(basePackages = "org.onap.ccsdk.apps.ms.neng.persistence.repository")
@EntityScan("org.onap.ccsdk.apps.ms.neng.persistence.entity")
@EnableTransactionManagement
public class ApplicationConfig {

    @Autowired
    private Environment environment;
    private Logger logger = LoggerFactory.getLogger(Application.class);

    @SuppressWarnings("rawtypes")
    void debugProperties() {
        Properties props = new Properties();
        MutablePropertySources propSrcs = ((AbstractEnvironment)this.environment).getPropertySources();
        StreamSupport.stream(propSrcs.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::<String>stream)
                .forEach(propName -> props.setProperty(propName, this.environment.getProperty(propName)));
        logger.info("Properties: " + props);
    }
    
    /**
     * Builds and returns the DataSource used for persisting the data managed by this micro-service.
     */
    @Bean
    public DataSource dataSource() {
        debugProperties();
        
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("datasource.db.driver-class-name"));
        dataSource.setUrl(environment.getProperty("datasource.db.url"));
        dataSource.setUsername(environment.getProperty("datasource.db.username"));
        dataSource.setPassword(environment.getProperty("datasource.db.password"));
        return dataSource;
    }

    /**
     * Builds and returns the JpaProperties used for configuration.
     */
    @Bean
    @ConfigurationProperties(prefix = "jpa")
    public JpaProperties jpaProperties() {
        return new JpaProperties();
    }

    /**
     * Builds and returns the JpaVendorAdapter used for configuration.
     */
    @Bean
    public JpaVendorAdapter jpaVendorAdapter(JpaProperties jpaProperties) {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(jpaProperties.isShowSql());
        hibernateJpaVendorAdapter.setGenerateDdl(jpaProperties.isGenerateDdl());
        return hibernateJpaVendorAdapter;
    }

    /**
     * Builds and returns the Properties used for Hibernate configuration.
     */
    @Bean
    public Map<String, String> hibProperties() {
        Map<String, String> hibProperties = new HashMap<>();
        String[] propertyNames = {
            "hibernate.dialect",
            "hibernate.show_sql",
            "hibernate.format_sql",
            "hibernate.cache.provider_class",
            "hibernate.id.new_generator_mappings",
            "hibernate.hbm2ddl.auto",
        };
        for (String name : propertyNames) {
            hibProperties.put(name,  environment.getProperty(name));
        }
        return hibProperties;
    }

    /**
     * Builds and returns the JpaTransactionManager.
     */
    @Bean
    public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }

    /**
     * Builds and returns the PersistenceExceptionTranslationPostProcessor.
     * 
     * <p/>PersistenceExceptionTranslationPostProcessor is a bean post processor which adds an advisor to any bean 
     * annotated with Repository so that any platform-specific exceptions are caught and then re-thrown 
     * as one of Spring's unchecked data access exceptions (i.e. a subclass of DataAccessException).
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    /**
     * Builds and returns the Factory used for building entities.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
                    JpaVendorAdapter jpaVendAdapter, 
                    Map<String, String> hibProps, 
                    DataSource dataSource) {
        debugProperties();
        EntityManagerFactoryBuilder factBuilder = new EntityManagerFactoryBuilder(jpaVendAdapter, hibProps, null);
        String pkgToScan = "org.onap.ccsdk.apps.ms.neng.persistence.entity";
        return factBuilder.dataSource(dataSource).packages(pkgToScan).build();
    }
}
