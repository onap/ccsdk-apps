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

package org.onap.ccsdk.apps.cadi.taf.basic;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

import org.onap.ccsdk.apps.cadi.Access;
import org.onap.ccsdk.apps.cadi.principal.TaggedPrincipal;
import org.onap.ccsdk.apps.cadi.taf.AbsTafResp;
import org.onap.ccsdk.apps.cadi.taf.TafResp;

public class BasicHttpTafResp extends AbsTafResp implements TafResp {
    private static final String tafName = BasicHttpTaf.class.getSimpleName();
    private HttpServletResponse httpResp;
    private String realm;
    private RESP status;
    private final boolean wasFailed;

    public BasicHttpTafResp(Access access, TaggedPrincipal principal, String description, RESP status, HttpServletResponse resp, String realm, boolean wasFailed) {
        super(access, tafName, principal, description);
        httpResp = resp;
        this.realm = realm;
        this.status = status;
        this.wasFailed = wasFailed;
    }

    public BasicHttpTafResp(Access access, String target, String description, RESP status, HttpServletResponse resp, String realm, boolean wasFailed) {
        super(access, tafName, target, description);
        httpResp = resp;
        this.realm = realm;
        this.status = status;
        this.wasFailed = wasFailed;
    }

    public RESP authenticate() throws IOException {
        httpResp.setStatus(401); // Unauthorized
        httpResp.setHeader("WWW-Authenticate", "Basic realm=\""+realm+'"');
        return RESP.HTTP_REDIRECT_INVOKED;
    }

    public RESP isAuthenticated() {
        return status;
    }

    public boolean isFailedAttempt() {
        return wasFailed;
    }
}
