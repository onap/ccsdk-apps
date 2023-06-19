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

package org.onap.ccsdk.apps.cadi.principal.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.onap.ccsdk.apps.cadi.principal.BasicPrincipal;
import org.onap.ccsdk.apps.cadi.principal.Kind;
import org.onap.ccsdk.apps.cadi.principal.OAuth2FormPrincipal;
import org.onap.ccsdk.apps.cadi.principal.TrustPrincipal;
import org.onap.ccsdk.apps.cadi.principal.X509Principal;

public class JU_Kind {

    @Mock
    private TrustPrincipal trust;

    @Mock
    private X509Principal x509;

    @Mock
    private OAuth2FormPrincipal oauth;

    @Mock
    private BasicPrincipal basic;

    @Before
    public void setup() throws SecurityException {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getKind() {
        assertThat(Kind.getKind(trust), is('U'));
        assertThat(Kind.getKind(x509), is('X'));
        assertThat(Kind.getKind(oauth), is('O'));
        assertThat(Kind.getKind(basic), is('B'));
    }

    @Test
    public void coverageTest() {
        @SuppressWarnings("unused")
        Kind kind = new Kind();
    }

}
