/*******************************************************************************
 * ============LICENSE_START====================================================
 * * org.onap.ccsdk
 * * ===========================================================================
 * * Copyright © 2023 AT&T Intellectual Property. All rights reserved.
 * * ===========================================================================
 * * Licensed under the Apache License, Version 2.0 (the "License");
 * * you may not use this file except in compliance with the License.
 * * You may obtain a copy of the License at
 * *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 * *
 *  * Unless required by applicable law or agreed to in writing, software
 * * distributed under the License is distributed on an "AS IS" BASIS,
 * * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * * See the License for the specific language governing permissions and
 * * limitations under the License.
 * * ============LICENSE_END====================================================
 * *
 * *
 ******************************************************************************/

package org.onap.ccsdk.apps.cadi.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.onap.ccsdk.apps.cadi.PropAccess;
import org.onap.ccsdk.apps.cadi.Access.Level;
import org.onap.ccsdk.apps.cadi.PropAccess.LogIt;

import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

import java.lang.reflect.Field;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.util.Properties;

@SuppressWarnings("unused")
public class JU_PropAccess {
    // Note: We can't actually get coverage of the protected constructor -
    // that will be done later, when testing the child class "ServletContextAccess"


    @Test
    public void ConstructorTest() throws Exception {
        PropAccess prop = new PropAccess();
        assertThat(prop.getProperties(), is(not(nullValue())));
    }

    @Test
    public void noPrintStreamConstructionTest() throws Exception {
        // Test for coverage
        PropAccess prop = new PropAccess((PrintStream)null, new String[]{"Invalid argument"});
    }

    @Test
    public void propertiesConstructionTest() throws Exception {
        // Coverage tests
        PropAccess prop = new PropAccess(System.getProperties());
        prop = new PropAccess((PrintStream)null, System.getProperties());
    }

    @Test
    public void stringConstructionTest() throws Exception {
        Properties testSystemProps = new Properties(System.getProperties());
        testSystemProps.setProperty("cadi_name", "user");
        System.setProperties(testSystemProps);
        PropAccess prop = new PropAccess("cadi_keyfile=src/test/resources/keyfile", "cadi_loglevel=DEBUG", "cadi_prop_files=test/cadi.properties:not_a_file");
    }

    @Test
    public void loadTest() throws Exception {
        // Coverage tests
        Properties props = mock(Properties.class);
        when(props.getProperty("cadi_prop_files")).thenReturn("test/cadi.properties").thenReturn(null);
        PropAccess pa = new PropAccess();
        Field props_field = PropAccess.class.getDeclaredField("props");
        props_field.setAccessible(true);
        props_field.set(pa, props);
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]);
        pa.load(bais);
    }

    @Test
    public void specialConversionsTest() throws Exception {
        // Coverage tests
        Properties testSystemProps = new Properties(System.getProperties());
        testSystemProps.setProperty("java.specification.version", "1.7");
        System.setProperties(testSystemProps);
        PropAccess pa = new PropAccess("AFT_LATITUDE=1", "AFT_LONGITUDE=1", "cadi_protocols=TLSv1.2");
    }

    @Test
    public void logTest() throws Exception {
        // Coverage tests
        PropAccess pa = new PropAccess();

        pa.log(Level.DEBUG);
        pa.printf(Level.DEBUG, "not a real format string");

        pa.setLogLevel(Level.DEBUG);
        pa.log(Level.DEBUG);
        pa.log(Level.DEBUG, 1, " ", null, "");
        pa.log(Level.DEBUG, "This is a string", "This is another");
        pa.set(new LogIt() {
            @Override public void push(Level level, Object ... elements) {}
        });
        try {
            pa.log(new Exception("This exception was thrown intentionally, please ignore it"));
        } catch (Exception e) {
            fail("Should have thrown an exception");
        }
    }

    @Test
    public void classLoaderTest() {
        PropAccess pa = new PropAccess();
        assertThat(pa.classLoader(), instanceOf(ClassLoader.class));
    }

    @Test
    public void encryptionTest() throws Exception {
        PropAccess pa = new PropAccess();
        String plainText = "This is a secret message";
        String secret_message = pa.encrypt(plainText);
        String modified = secret_message.substring(4);
        // Plenty of assertions to hit all branches
        assertThat(pa.decrypt(secret_message, false), is(plainText));
        assertThat(pa.decrypt(null, false), is(nullValue()));
        assertThat(pa.decrypt(modified, true), is(plainText));
        assertThat(pa.decrypt(modified, false), is(modified));
    }

    @Test
    public void setPropertyTest() {
        PropAccess pa = new PropAccess();
        pa.setProperty("test", null);
        String prop = "New Property";
        String val ="And it's faithful value";
        pa.setProperty(prop, val);

        assertThat(pa.getProperty(prop), is(val));
    }
}
