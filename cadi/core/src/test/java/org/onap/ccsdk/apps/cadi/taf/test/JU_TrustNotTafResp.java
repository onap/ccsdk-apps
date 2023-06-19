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

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.*;
import org.mockito.*;

import java.io.IOException;

import org.onap.ccsdk.apps.cadi.Access;
import org.onap.ccsdk.apps.cadi.taf.TafResp;
import org.onap.ccsdk.apps.cadi.taf.TafResp.RESP;
import org.onap.ccsdk.apps.cadi.taf.TrustNotTafResp;
import org.onap.ccsdk.apps.cadi.principal.TaggedPrincipal;

public class JU_TrustNotTafResp {

    @Mock
    TafResp delegateMock;

    @Mock
    TaggedPrincipal principalMock;

    @Mock
    Access accessMock;

    private final String description = "Example Description";

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        when(delegateMock.getPrincipal()).thenReturn(principalMock);
        when(delegateMock.getAccess()).thenReturn(accessMock);
    }

    @Test
    public void test() throws IOException {
        TrustNotTafResp ttr = new TrustNotTafResp(delegateMock, description);
        assertThat(ttr.isValid(), is(false));
        assertThat(ttr.desc(), is(description));
        assertThat(ttr.authenticate(), is(RESP.NO_FURTHER_PROCESSING));
        assertThat(ttr.isAuthenticated(), is(RESP.NO_FURTHER_PROCESSING));
        assertThat(ttr.getPrincipal(), is(principalMock));
        assertThat(ttr.getAccess(), is(accessMock));
        assertThat(ttr.isFailedAttempt(), is(true));
        assertThat(ttr.toString(), is(description));
    }

}
