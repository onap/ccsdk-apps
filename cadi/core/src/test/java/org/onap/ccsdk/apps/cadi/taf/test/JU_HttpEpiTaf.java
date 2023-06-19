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
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.ccsdk.apps.cadi.Access.Level;
import org.onap.ccsdk.apps.cadi.CachedPrincipal.Resp;
import org.onap.ccsdk.apps.cadi.CadiException;
import org.onap.ccsdk.apps.cadi.Locator;
import org.onap.ccsdk.apps.cadi.PropAccess;
import org.onap.ccsdk.apps.cadi.Taf.LifeForm;
import org.onap.ccsdk.apps.cadi.TrustChecker;
import org.onap.ccsdk.apps.cadi.taf.HttpEpiTaf;
import org.onap.ccsdk.apps.cadi.taf.HttpTaf;
import org.onap.ccsdk.apps.cadi.taf.NullTaf;
import org.onap.ccsdk.apps.cadi.taf.Redirectable;
import org.onap.ccsdk.apps.cadi.taf.TafResp;
import org.onap.ccsdk.apps.cadi.taf.TafResp.RESP;

public class JU_HttpEpiTaf {

    private PropAccess access;

    @Mock private Locator<URI> locMock;
    @Mock private TrustChecker trustCheckerMock;
    @Mock private HttpServletRequest reqMock;
    @Mock private HttpServletResponse respMock;
    @Mock private HttpTaf tafMock;
    @Mock private TafResp trespMock;
    @Mock private Redirectable redirMock;

    @Before
    public void setup() throws URISyntaxException {
        MockitoAnnotations.initMocks(this);

        access = new PropAccess(new PrintStream(new ByteArrayOutputStream()), new String[0]);
    }

    @Test
    public void test() throws Exception {
        HttpEpiTaf taf;
        try {
            taf = new HttpEpiTaf(access, locMock, trustCheckerMock);
            fail("Should've thrown an exception");
        } catch (CadiException e) {
            assertThat(e.getMessage(), is("Need at least one HttpTaf implementation in constructor"));
        }

        taf = new HttpEpiTaf(access, locMock, trustCheckerMock, new NullTaf());
        taf.validate(LifeForm.CBLF, reqMock, respMock);

        // Coverage of tricorderScan
        taf.validate(LifeForm.LFN, reqMock, respMock);
        when(reqMock.getHeader("User-Agent")).thenReturn("Non-mozilla-header");
        taf.validate(LifeForm.LFN, reqMock, respMock);
        when(reqMock.getHeader("User-Agent")).thenReturn("Mozilla-header");
        taf.validate(LifeForm.LFN, reqMock, respMock);

        access.setLogLevel(Level.DEBUG);
        taf.validate(LifeForm.CBLF, reqMock, respMock);

        when(tafMock.validate(LifeForm.CBLF, reqMock, respMock)).thenReturn(trespMock);
        when(trespMock.isAuthenticated()).thenReturn(RESP.TRY_ANOTHER_TAF);
        taf = new HttpEpiTaf(access, locMock, trustCheckerMock, tafMock);
        taf.validate(LifeForm.CBLF, reqMock, respMock);

        when(trespMock.isAuthenticated()).thenReturn(RESP.IS_AUTHENTICATED);
        taf.validate(LifeForm.CBLF, reqMock, respMock);

        when(trespMock.isAuthenticated()).thenReturn(RESP.TRY_AUTHENTICATING);
        taf.validate(LifeForm.CBLF, reqMock, respMock);

        taf = new HttpEpiTaf(access, locMock, trustCheckerMock, tafMock, tafMock);
        taf.validate(LifeForm.CBLF, reqMock, respMock);

        when(tafMock.validate(LifeForm.CBLF, reqMock, respMock)).thenReturn(redirMock);
        when(redirMock.isAuthenticated()).thenReturn(RESP.TRY_AUTHENTICATING);
        taf.validate(LifeForm.CBLF, reqMock, respMock);

        taf = new HttpEpiTaf(access, locMock, trustCheckerMock, tafMock, tafMock);
        taf.validate(LifeForm.CBLF, reqMock, respMock);

        taf = new HttpEpiTaf(access, locMock, trustCheckerMock, tafMock);
        taf.validate(LifeForm.CBLF, reqMock, respMock);

        taf = new HttpEpiTaf(access, locMock, null, tafMock);
        when(redirMock.isAuthenticated()).thenReturn(RESP.IS_AUTHENTICATED);
        try {
            taf.validate(LifeForm.CBLF, reqMock, respMock);
            fail("Should've thrown an exception");
        } catch (Exception e) {
        }

        assertThat(taf.revalidate(null), is(false));
        assertThat(taf.revalidate(null), is(false));

        when(tafMock.revalidate(null, null)).thenReturn(Resp.NOT_MINE);
        assertThat(taf.revalidate(null, null), is(Resp.NOT_MINE));
        when(tafMock.revalidate(null, null)).thenReturn(Resp.REVALIDATED);
        assertThat(taf.revalidate(null, null), is(Resp.REVALIDATED));

        when(tafMock.revalidate(null, null)).thenReturn(Resp.NOT_MINE).thenReturn(Resp.NOT_MINE).thenReturn(Resp.REVALIDATED);
        taf = new HttpEpiTaf(access, locMock, trustCheckerMock, tafMock, tafMock, tafMock);
        assertThat(taf.revalidate(null, null), is(Resp.REVALIDATED));

        taf.toString();

    }

}
