/*
 * ﻿============LICENSE_START=======================================================
 * org.onap.ccsdk
 * ================================================================================
 * Copyright © 2017-2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
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
 * ============LICENSE_END=========================================================
 *
 *
 */

package org.onap.ccsdk.apps.controllerblueprints.core.utils

import org.apache.commons.io.FileUtils
import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintException
import org.onap.ccsdk.apps.controllerblueprints.core.checkNotEmpty
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL
import java.nio.charset.Charset
/**
 *
 *
 * @author Brinda Santh
 */
object ResourceResolverUtils {
    private val logger: Logger = LoggerFactory.getLogger(this::class.toString())

    @JvmStatic
    fun getFileContent(filename : String, basePath : String?): String {
        logger.trace("file ({}), basePath ({}) ", filename, basePath)
        try{
            var resolvedFileName : String = filename
            if(filename.startsWith("http", true)
                    || filename.startsWith("https", true)){
                val givenUrl : String = URL(filename).toString()
                val systemUrl : String = File(".").toURI().toURL().toString()
                logger.trace("givenUrl ({}), systemUrl ({}) ", givenUrl, systemUrl)
                if(givenUrl.startsWith(systemUrl)){

                }
            }else{
                if(!filename.startsWith("/")){
                    if (checkNotEmpty(basePath)) {
                        resolvedFileName = basePath.plus(File.separator).plus(filename)
                    }else{
                        resolvedFileName = javaClass.classLoader.getResource(".").path.plus(filename)
                    }
                }
            }
            return FileUtils.readFileToString(File(resolvedFileName), Charset.defaultCharset())
        }catch (e : Exception){
            throw BluePrintException(e, "failed to file (%s), basePath (%s) ", filename, basePath)
        }
    }
}