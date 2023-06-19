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
import org.junit.*;

import java.io.IOException;

import org.onap.ccsdk.apps.cadi.Access;
import org.onap.ccsdk.apps.cadi.CachedPrincipal.Resp;
import org.onap.ccsdk.apps.cadi.taf.TafResp;
import org.onap.ccsdk.apps.cadi.taf.TafResp.RESP;

import org.onap.ccsdk.apps.cadi.taf.NullTaf;

public class JU_NullTaf {

    @Test
    public void test() throws IOException {
        NullTaf nt = new NullTaf();
        TafResp singleton1 = nt.validate(null);
        TafResp singleton2 = nt.validate(null, null, null);
        Resp singleton3 = nt.revalidate(null, null);

        assertThat(singleton1, is(singleton2));

        assertFalse(singleton1.isValid());

        assertThat(singleton1.isAuthenticated(), is(RESP.NO_FURTHER_PROCESSING));

        assertThat(singleton1.desc(), is("All Authentication denied"));

        assertThat(singleton1.authenticate(), is(RESP.NO_FURTHER_PROCESSING));

        assertThat(singleton1.getPrincipal(), is(nullValue()));

        assertThat(singleton1.getAccess(), is(Access.NULL));

        assertTrue(singleton1.isFailedAttempt());

        assertThat(singleton3, is(Resp.NOT_MINE));
    }

}
