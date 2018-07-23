/*-
 * ============LICENSE_START=======================================================
 * ONAP : CCSDK.apps
 * ================================================================================
 * Copyright (C) 2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
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
 * ============LICENSE_END=========================================================
 */

package org.onap.ccsdk.apps.ms.neng.core.policy;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Reads policy from a file.
 */
public class FilePolicyReader extends PolicyReader {
    private String file;

    public FilePolicyReader(String file) {
        this.file = file;
    }

    public Map<String, Object> findPolicy(String name) throws Exception {
        return getPolicy();
    }

    public Map<String, Object> getPolicy() throws Exception {
        return getPolicy(readFromFile(file));
    }

    protected String readFromFile(String name) throws Exception {
        String path = "src/test/resources/" + name;
        String value = new String(Files.readAllBytes(Paths.get(path)));
        return value;
    }
}
