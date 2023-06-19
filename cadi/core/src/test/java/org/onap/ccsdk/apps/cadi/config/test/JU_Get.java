/*******************************************************************************
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

package org.onap.ccsdk.apps.cadi.config.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.onap.ccsdk.apps.cadi.PropAccess;
import org.onap.ccsdk.apps.cadi.config.Get;

public class JU_Get {

    private String defaultVal = "some default value";

    private ByteArrayOutputStream outStream;

    private TestBean tb;

    @Before
    public void setup() {
        outStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outStream));
    }

    @After
    public void tearDown() {
        System.setOut(System.out);
    }

    @Test
    public void beanTest() {
        tb = new TestBean();
        tb.setProperty1("prop1");

        Get.Bean testBean = new Get.Bean(tb);
        assertThat(testBean.get("property1", defaultVal, true), is("prop1"));
        assertThat(testBean.get("property2", defaultVal, true), is(defaultVal));
        assertThat(testBean.get("thrower", defaultVal, true), is(defaultVal));
    }

    @Test
    public void nullTest() {
        assertThat(Get.NULL.get("name", defaultVal, true), is(defaultVal));
    }

    @Test
    public void accessTest() {

        PropAccess access = new PropAccess();
        access.setProperty("tag", "value");
        Get.AccessGet accessGet = new Get.AccessGet(access);

        assertThat(accessGet.get("tag", defaultVal, true), is("value"));
        outStream.reset();

        assertThat(accessGet.get("not a real tag", defaultVal, true), is(defaultVal));
        outStream.reset();

        assertThat(accessGet.get("not a real tag", null, true), is(nullValue()));

        outStream.reset();

        assertThat(accessGet.get("tag", defaultVal, false), is("value"));
        assertThat(outStream.toString(), is(""));
    }

    public class TestBean implements java.io.Serializable {

        private static final long serialVersionUID = 1L;
        private String property1 = null;
        private String property2 = null;
        @SuppressWarnings("unused")
        private String thrower = null;

        public TestBean() { }
        public String getProperty1() { return property1; }
        public void setProperty1(final String value) { this.property1 = value; }
        public String getProperty2() { return property2; }
        public void setProperty2(final String value) { this.property2 = value; }
        public String getThrower() throws Exception { throw new Exception(); }
        public void setThrower(final String value) { this.thrower = value; }

    }
}
