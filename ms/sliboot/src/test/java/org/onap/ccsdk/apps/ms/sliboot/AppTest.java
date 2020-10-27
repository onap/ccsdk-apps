package org.onap.ccsdk.apps.ms.sliboot;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.PropertiesRealm;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class AppTest {

    SlibootApp app;

    @Before
    public void setUp() throws Exception {
        app = new SlibootApp();
        System.setProperty("serviceLogicProperties", "src/test/resources/svclogic.properties");
    }

    @Test
    public void realm() {
        Realm realm = app.realm();
        assertTrue(realm instanceof PropertiesRealm);


    }

    @Test
    public void shiroFilterChainDefinition() {
        ShiroFilterChainDefinition chainDefinition = app.shiroFilterChainDefinition();
        Map<String, String> chainMap = chainDefinition.getFilterChainMap();
        assertEquals("anon", chainMap.get("/**"));


    }
}