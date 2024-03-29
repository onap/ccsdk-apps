/**
 * ============LICENSE_START====================================================
 * org.onap.ccsdk
 * ===========================================================================
 * Copyright (c) 2018 AT&T Intellectual Property. All rights reserved.
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

package org.onap.ccsdk.apps.cadi.lur;

import org.onap.ccsdk.apps.cadi.Permission;

public class LocalPermission implements Permission {
    private String key;

    public LocalPermission(String role) {
        this.key = role;
    }

    public String getKey() {
        return key;
    }

    public String toString() {
        return key;
    }

    public boolean match(Permission p) {
        return key.equals(p.getKey());
    }

    public String permType() {
        return "LOCAL";
    }


}
