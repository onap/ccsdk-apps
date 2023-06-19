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
import org.onap.ccsdk.apps.cadi.CadiException;
import org.onap.ccsdk.apps.cadi.Taf;
import org.onap.ccsdk.apps.cadi.taf.TafResp;
import org.onap.ccsdk.apps.cadi.taf.TafResp.RESP;

import org.onap.ccsdk.apps.cadi.taf.EpiTaf;
import org.onap.ccsdk.apps.cadi.taf.NullTaf;
import org.onap.ccsdk.apps.cadi.Taf.LifeForm;
import org.onap.ccsdk.apps.cadi.principal.TaggedPrincipal;

public class JU_EpiTaf {

    @Test(expected = CadiException.class)
    @SuppressWarnings("unused")
    public void constructorTest() throws CadiException {
        EpiTaf et = new EpiTaf();
    }

    @Test
    public void validateTryAnotherTest() throws CadiException {
        EpiTaf et = new EpiTaf(new TryAnotherTaf());
        TafResp output = et.validate(LifeForm.CBLF);
        assertThat(output.isAuthenticated(), is(RESP.NO_FURTHER_PROCESSING));
    }

    @Test
    public void validateTryAuthenticatingTest() throws CadiException {
        EpiTaf et = new EpiTaf(new TryAuthenticatingTaf(), new TryAuthenticatingTaf());
        TafResp output = et.validate(LifeForm.CBLF);
        assertThat(output.isAuthenticated(), is(RESP.TRY_AUTHENTICATING));
        output = et.validate(LifeForm.CBLF);
        assertThat(output.isAuthenticated(), is(RESP.TRY_AUTHENTICATING));
    }

    @Test
    public void validateDefaultCaseTest() throws CadiException {
        EpiTaf et = new EpiTaf(new NullTaf());
        TafResp output = et.validate(LifeForm.CBLF);
        assertThat(output.isAuthenticated(), is(RESP.NO_FURTHER_PROCESSING));
    }

    class TryAnotherTafResp implements TafResp {
        @Override public boolean isValid() { return false; }
        @Override public String desc() { return null; }
        @Override public RESP isAuthenticated() { return RESP.TRY_ANOTHER_TAF; }
        @Override public RESP authenticate() throws IOException { return null; }
        @Override public TaggedPrincipal getPrincipal() { return null; }
        @Override public String getTarget() {return "unknown";}
        @Override public Access getAccess() { return null; }
        @Override public boolean isFailedAttempt() { return false; }
        @Override public float timing() { return 0; }
        @Override public void timing(long start) {}
        @Override public String taf() {return "JUnit";}
    }

    class TryAnotherTaf implements Taf {
        @Override public TafResp validate(LifeForm reading, String ... info) { return new TryAnotherTafResp(); }
    }

    class TryAuthenticatingResp implements TafResp {
        @Override public boolean isValid() { return false; }
        @Override public String desc() { return null; }
        @Override public RESP isAuthenticated() { return RESP.TRY_AUTHENTICATING; }
        @Override public RESP authenticate() throws IOException { return null; }
        @Override public TaggedPrincipal getPrincipal() { return null; }
        @Override public String getTarget() {return "unknown";}
        @Override public Access getAccess() { return null; }
        @Override public boolean isFailedAttempt() { return false; }
        @Override public float timing() { return 0; }
        @Override public void timing(long start) {}
        @Override public String taf() {return "JUnit";}
    }

    class TryAuthenticatingTaf implements Taf {
        @Override public TafResp validate(LifeForm reading, String ... info) { return new TryAuthenticatingResp(); }
    }

    class EpiTafStub extends EpiTaf {
        public EpiTafStub() throws CadiException { }
    }

}
