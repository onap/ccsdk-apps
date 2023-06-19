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

package org.onap.ccsdk.apps.cadi.taf.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;
import org.onap.ccsdk.apps.cadi.Access;
import org.onap.ccsdk.apps.cadi.PropAccess;
import org.onap.ccsdk.apps.cadi.principal.TaggedPrincipal;
import org.onap.ccsdk.apps.cadi.taf.AbsTafResp;
import org.onap.ccsdk.apps.cadi.taf.TafResp.RESP;

public class JU_AbsTafResp {

    private static final String JUNIT = "Junit";
    private static final String name = "name";
    private static final String tag = "tag";
    private static final String description = "description";

    private Access access;
    private TaggedPrincipal taggedPrinc;

    @Before
    public void setup() {
        access = new PropAccess(new PrintStream(new ByteArrayOutputStream()), new String[0]);
        taggedPrinc = new TaggedPrincipal() {
            @Override public String getName() { return name; }
            @Override public String tag() { return tag; }
        };
    }

    @Test
    public void test() {
        AbsTafResp tafResp = new AbsTafResp(access, JUNIT, taggedPrinc, description) {
            @Override public RESP authenticate() throws IOException {
                return null;
            }
        };

        assertThat(tafResp.isValid(), is(true));
        assertThat(tafResp.desc(), is(description));
        assertThat(tafResp.taf(), is(JUNIT));
        assertThat(tafResp.isAuthenticated(), is(RESP.IS_AUTHENTICATED));
        assertThat(tafResp.getPrincipal(), is(taggedPrinc));
        assertThat(tafResp.getAccess(), is(access));
        assertThat(tafResp.isFailedAttempt(), is(false));

        tafResp = new AbsTafResp(null, JUNIT, "unknown", null) {
            @Override public RESP authenticate() throws IOException {
                return null;
            }
        };

        assertThat(tafResp.isValid(), is(false));
        assertThat(tafResp.isAuthenticated(), is(RESP.TRY_ANOTHER_TAF));
        assertThat(tafResp.getPrincipal(), is(nullValue()));
        assertThat(tafResp.getTarget(), is("unknown"));
        assertThat(tafResp.getAccess(), is(nullValue()));
        assertThat(tafResp.taf(), is(JUNIT));
        assertThat(tafResp.isFailedAttempt(), is(false));
    }

}
