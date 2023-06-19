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

import java.security.Principal;

import org.onap.ccsdk.apps.cadi.UserChain;
import org.onap.ccsdk.apps.cadi.principal.TaggedPrincipal;
import org.onap.ccsdk.apps.cadi.principal.TrustPrincipal;

public class JU_TrustPrincipal {

    private final String ucName = "UserChain";
    private final String uc = "This is a UserChain";
    private final String taggedName = "TaggedPrincipal";
    private final String tag = "tag";
    private final String pName = "Principal";

    private class UserChainPrincipalStub implements Principal, UserChain {
        @Override public String userChain() { return uc; }
        @Override public String getName() { return ucName; }
    }

    private class TaggedPrincipalStub extends TaggedPrincipal {
        public TaggedPrincipalStub() { super(); }
        @Override public String getName() { return taggedName; }
        @Override public String tag() { return tag; }
    }

    private class PrincipalStub implements Principal {
        @Override public String getName() { return pName; }
    }

    @Test
    public void userChainConstructorTest() {
        UserChainPrincipalStub ucps = new UserChainPrincipalStub();
        TrustPrincipal tp = new TrustPrincipal(ucps, taggedName);
        assertThat(tp.getName(), is(taggedName));
        assertThat(tp.userChain(), is(uc));
        assertSame(tp.original(), ucps);
        assertThat(tp.tag(), is(uc));
        assertThat(tp.personalName(), is(ucName + '[' + uc + ']'));
    }

    @Test
    public void taggedPrincipalConstructorTest() {
        TaggedPrincipal tagged = new TaggedPrincipalStub();
        TrustPrincipal tp = new TrustPrincipal(tagged, taggedName);
        assertThat(tp.getName(), is(taggedName));
        assertThat(tp.userChain(), is(tag));
        assertSame(tp.original(), tagged);
        assertThat(tp.tag(), is(tag));
        assertThat(tp.personalName(), is(taggedName + '[' + tag + ']'));
    }

    @Test
    public void principalConstructorTest() {
        Principal principal = new PrincipalStub();
        TrustPrincipal tp = new TrustPrincipal(principal, pName);
        assertThat(tp.getName(), is(pName));
        assertThat(tp.userChain(), is(principal.getClass().getSimpleName()));
        assertSame(tp.original(), principal);
        assertThat(tp.tag(), is(principal.getClass().getSimpleName()));
        assertThat(tp.personalName(), is(pName + '[' + principal.getClass().getSimpleName() + ']'));
    }

}
