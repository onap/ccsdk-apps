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

import org.onap.ccsdk.apps.cadi.principal.TaggedPrincipal;
import org.onap.ccsdk.apps.cadi.principal.TaggedPrincipal.TagLookup;
import org.onap.ccsdk.apps.cadi.CadiException;
import org.onap.ccsdk.apps.cadi.principal.StringTagLookup;

public class JU_TaggedPrincipal {

    private final String name = "stubbedName";
    private final String tag = "tag";

    private class TaggedPrincipalStub extends TaggedPrincipal {
        public TaggedPrincipalStub() { super(); }
        public TaggedPrincipalStub(final TagLookup tl) { super(tl); }
        @Override public String getName() { return name; }
        @Override public String tag() { return null; }
    }

    private class WhinyTagLookup implements TagLookup {
        public WhinyTagLookup(final String tag) { }
        @Override
        public String lookup() throws CadiException {
            throw new CadiException();
        }
    }

    @Test
    public void personalNameTest() {
        TaggedPrincipal tp = new TaggedPrincipalStub();
        assertThat(tp.personalName(), is(name));

        StringTagLookup stl = new StringTagLookup(tag);
        tp = new TaggedPrincipalStub(stl);
        assertThat(tp.personalName(), is(tag));

        WhinyTagLookup wtl = new WhinyTagLookup(tag);
        tp.setTagLookup(wtl);
        assertThat(tp.personalName(), is(name));
    }

}
