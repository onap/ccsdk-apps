/*-
 * ============LICENSE_START=======================================================
 * ONAP : CCSDK.apps
 * ================================================================================
 * Copyright (C) 2026 Deutsche Telekom AG.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Map;
import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.test.util.ReflectionTestUtils;

class ApplicationConfigTest {

    private static final String AAI_BASIC_AUTH = "Basic YWFpOnNlY3JldA==";

    private Properties collect(Map<String, Object> source) {
        StandardEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addFirst(new MapPropertySource("test", source));
        ApplicationConfig config = new ApplicationConfig();
        ReflectionTestUtils.setField(config, "environment", environment);
        return config.collectProperties();
    }

    @Test
    void credentialValuesAreMasked() {
        Properties props = collect(Map.of(
                "aai.basicAuth", AAI_BASIC_AUTH,
                "policymgr.basicAuth", "Basic cG9saWN5OnNlY3JldA==",
                "policymgr.clientAuth", "Basic Y2xpZW50OnNlY3JldA==",
                "datasource.db.password", "dbsecret",
                "SOME_DB_PASSWORD", "envsecret"));

        assertEquals(ApplicationConfig.MASK, props.getProperty("aai.basicAuth"));
        assertEquals(ApplicationConfig.MASK, props.getProperty("policymgr.basicAuth"));
        assertEquals(ApplicationConfig.MASK, props.getProperty("policymgr.clientAuth"));
        assertEquals(ApplicationConfig.MASK, props.getProperty("datasource.db.password"));
        assertEquals(ApplicationConfig.MASK, props.getProperty("SOME_DB_PASSWORD"));
        assertFalse(props.toString().contains(AAI_BASIC_AUTH));
    }

    @Test
    void ordinaryValuesAreKept() {
        Properties props = collect(Map.of(
                "datasource.db.url", "jdbc:mariadb://db:3306/nengdb",
                "server.port", "8080"));

        assertEquals("jdbc:mariadb://db:3306/nengdb", props.getProperty("datasource.db.url"));
        assertEquals("8080", props.getProperty("server.port"));
    }
}
