/*
 * Copyright © 2017-2018 AT&T Intellectual Property.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onap.ccsdk.apps.controllerblueprints.core.interfaces

import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintProcessorException
import java.io.File

interface BluePrintCatalogService {

    /**
     * Save the CBA to database
     * @param blueprintFile Can be either a directory, or an archive
     * @param validate whether to validate before saving
     * @return Blueprint ID
     * @throws BluePrintException if blueprint validation failed
     * @throws BluePrintProcessorException if saving to DB failed
     */
    @Throws(BluePrintException::class, BluePrintProcessorException::class)
    fun save(blueprintFile: File, validate: Boolean): String?

    /**
     * Retrieve the CBA from database
     * @param name Name of the blueprint
     * @param version Version of the blueprint
     * @param path Path where to output the CBA. If the path contains '.zip' we will
     * retrieve the archive only, else we will extract the content to the given path.
     */
    fun get(name: String, version: String, path: String)
}