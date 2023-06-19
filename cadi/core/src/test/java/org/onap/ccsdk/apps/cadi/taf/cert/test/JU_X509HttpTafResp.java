/**
 * ============LICENSE_START====================================================
 * org.onap.ccsdk
 * ===========================================================================
 * Copyright (c) 2023 AT&T Intellectual Property. All rights reserved.
 * ===========================================================================
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
 * ============LICENSE_END====================================================
 *
 */

package org.onap.ccsdk.apps.cadi.taf.cert.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.ccsdk.apps.cadi.PropAccess;
import org.onap.ccsdk.apps.cadi.principal.TaggedPrincipal;
import org.onap.ccsdk.apps.cadi.taf.TafResp.RESP;
import org.onap.ccsdk.apps.cadi.taf.cert.X509HttpTafResp;

public class JU_X509HttpTafResp {

    private final static String description = "description";
    private final static RESP status = RESP.IS_AUTHENTICATED;

    private PropAccess access;

    @Mock private TaggedPrincipal princMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        access = new PropAccess(new PrintStream(new ByteArrayOutputStream()), new String[0]);
    }

    @Test
    public void test() throws IOException {
        X509HttpTafResp resp = new X509HttpTafResp(access, princMock, description, status);
        assertThat(resp.authenticate(), is(RESP.TRY_ANOTHER_TAF));
        assertThat(resp.isAuthenticated(), is(status));
        assertThat(resp.toString(), is(status.name()));
    }

}
