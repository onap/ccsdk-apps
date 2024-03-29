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

package org.onap.ccsdk.apps.cadi;

import java.security.Principal;

import org.onap.ccsdk.apps.cadi.CachedPrincipal.Resp;


public interface CachingLur<PERM extends Permission> extends Lur {
    public abstract void remove(String user);
    public abstract Resp reload(User<PERM> user);
    public abstract void setDebug(String commaDelimIDsOrNull);
    public abstract void clear(Principal p, StringBuilder sb);
}
