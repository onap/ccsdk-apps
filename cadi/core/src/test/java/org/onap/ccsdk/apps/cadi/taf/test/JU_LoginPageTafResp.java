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

package org.onap.ccsdk.apps.cadi.taf.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.ccsdk.apps.cadi.Access;
import org.onap.ccsdk.apps.cadi.Locator;
import org.onap.ccsdk.apps.cadi.Locator.Item;
import org.onap.ccsdk.apps.cadi.LocatorException;
import org.onap.ccsdk.apps.cadi.PropAccess;
import org.onap.ccsdk.apps.cadi.taf.LoginPageTafResp;
import org.onap.ccsdk.apps.cadi.taf.Redirectable;
import org.onap.ccsdk.apps.cadi.taf.TafResp;
import org.onap.ccsdk.apps.cadi.taf.TafResp.RESP;

public class JU_LoginPageTafResp {

    private static final String uriString = "example.com";

    private URI uri;
    private Access access;
    private List<Redirectable> redirectables;

    @Mock private HttpServletResponse respMock;
    @Mock private Locator<URI> locatorMock;
    @Mock private Redirectable redirMock;

    @Before
    public void setup() throws URISyntaxException {
        MockitoAnnotations.initMocks(this);

        access = new PropAccess(new PrintStream(new ByteArrayOutputStream()), new String[0]);

        redirectables = new ArrayList<>();
        uri = new URI(uriString);
    }

    @Test
    public void test() throws LocatorException, IOException {
        TafResp resp;
        resp = LoginPageTafResp.create(access, null, respMock, redirectables);
        assertThat(resp.desc(), is("All Authentication denied"));

        redirectables.add(redirMock);
        redirectables.add(redirMock);
        resp = LoginPageTafResp.create(access, null, respMock, redirectables);
        assertThat((Redirectable)resp, is(redirMock));

        resp = LoginPageTafResp.create(access, locatorMock, respMock, redirectables);
        assertThat(resp.desc(), is("All Authentication denied"));

        when(locatorMock.get((Item)any())).thenReturn(uri);
        resp = LoginPageTafResp.create(access, locatorMock, respMock, redirectables);
        assertThat(resp.desc(), is("Multiple Possible HTTP Logins available.  Redirecting to Login Choice Page"));
        assertThat(resp.authenticate(), is(RESP.HTTP_REDIRECT_INVOKED));
        assertThat(resp.isAuthenticated(), is(RESP.TRY_AUTHENTICATING));

        redirectables = new ArrayList<>();
        resp = LoginPageTafResp.create(access, locatorMock, respMock, redirectables);
        assertThat(resp.desc(), is("All Authentication denied"));

    }

}
