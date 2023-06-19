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
import org.onap.ccsdk.apps.cadi.taf.TrustTafResp;
import org.onap.ccsdk.apps.cadi.principal.TaggedPrincipal;

public class JU_TrustTafResp {

    @Mock
    TafResp delegateMock;

    @Mock
    TaggedPrincipal principalMock;

    @Mock
    Access accessMock;

    private final String description = "Example Description";
    private final String anotherDescription = "Another Description";
    private final String name = "name";

    private final RESP resp = RESP.IS_AUTHENTICATED;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        when(delegateMock.desc()).thenReturn(anotherDescription);
        when(delegateMock.isValid()).thenReturn(true);
        when(delegateMock.isAuthenticated()).thenReturn(resp);
        when(delegateMock.authenticate()).thenReturn(resp);
        when(delegateMock.getAccess()).thenReturn(accessMock);
        when(delegateMock.isFailedAttempt()).thenReturn(true);

        when(principalMock.getName()).thenReturn(name);
    }

    @Test
    public void test() throws IOException {
        TrustTafResp ttr = new TrustTafResp(delegateMock, principalMock, description);
        assertThat(ttr.isValid(), is(true));
        assertThat(ttr.desc(), is(description + ' ' + anotherDescription));
        assertThat(ttr.authenticate(), is(resp));
        assertThat(ttr.isAuthenticated(), is(resp));
        assertThat(ttr.getPrincipal(), is(principalMock));
        assertThat(ttr.getAccess(), is(accessMock));
        assertThat(ttr.isFailedAttempt(), is(true));
        assertThat(ttr.toString(), is(name + " by trust of " + description + ' ' + anotherDescription));
    }

}
