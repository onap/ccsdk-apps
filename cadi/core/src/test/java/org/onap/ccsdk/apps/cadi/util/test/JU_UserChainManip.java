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

package org.onap.ccsdk.apps.cadi.util.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.*;

import org.onap.ccsdk.apps.cadi.UserChain;
import org.onap.ccsdk.apps.cadi.util.UserChainManip;

public class JU_UserChainManip {

    @Test
    public void build(){
        UserChain.Protocol baseAuth=UserChain.Protocol.BasicAuth;
        StringBuilder sb = UserChainManip.build(new StringBuilder(""), "app", "id", baseAuth, true);
        assertThat(sb.toString(), is("app:id:BasicAuth:AS"));

        // for coverage
        sb = UserChainManip.build(sb, "app", "id", baseAuth, true);
        assertThat(sb.toString(), is("app:id:BasicAuth:AS,app:id:BasicAuth"));

        sb = UserChainManip.build(new StringBuilder(""), "app", "id", baseAuth, false);
        assertThat(sb.toString(), is("app:id:BasicAuth"));
    }

    @Test
    public void idToNSTEST() {
        assertThat(UserChainManip.idToNS(null), is(""));
        assertThat(UserChainManip.idToNS(""), is(""));
        assertThat(UserChainManip.idToNS("something"), is(""));
        assertThat(UserChainManip.idToNS("something@@"), is(""));
        assertThat(UserChainManip.idToNS("something@@."), is(""));
        assertThat(UserChainManip.idToNS("something@com"), is("com"));
        assertThat(UserChainManip.idToNS("something@random.com"), is("com.random"));
        assertThat(UserChainManip.idToNS("@random.com"), is("com.random"));
        assertThat(UserChainManip.idToNS("something@random.com."), is("com.random"));
        assertThat(UserChainManip.idToNS("something@..random...com..."), is("com.random"));
        assertThat(UserChainManip.idToNS("something@this.random.com"), is("com.random.this"));
    }

    @Test
    public void coverageTest() {
        @SuppressWarnings("unused")
        UserChainManip ucm = new UserChainManip();
    }

}
