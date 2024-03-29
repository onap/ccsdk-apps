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

/**
 * CADI Specific Exception
 */
public class CadiException extends Exception {
    /**
     *  Generated ID
     */
    private static final long serialVersionUID = -4180145363107742619L;

    public CadiException() {
        super();
    }

    public CadiException(String message) {
        super(message);
    }

    public CadiException(Throwable cause) {
        super(cause);
    }

    public CadiException(String message, Throwable cause) {
        super(message, cause);
    }

}
