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

public class LocatorException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = -4267929804321134469L;

    public LocatorException(String arg0) {
        super(arg0);
    }

    public LocatorException(Throwable arg0) {
        super(arg0);
    }

    public LocatorException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public LocatorException(CharSequence cs) {
        super(cs.toString());
    }

}
