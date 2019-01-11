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

package org.onap.ccsdk.apps.controllerblueprints.core.service

import org.onap.ccsdk.apps.controllerblueprints.core.BluePrintProcessorException
import java.io.InputStream
import java.io.Reader
import javax.script.ScriptEngineManager

/**
 * This class is not thread-safe, don't use it for parallel executions and create new instances instead.
 */
open class BluePrintKotlinService(classLoader: ClassLoader? = Thread.currentThread().contextClassLoader) {

    val engine = ScriptEngineManager(classLoader).getEngineByExtension("kts")

    inline fun <R> safeEval(evaluation: () -> R?) = try {
        evaluation()
    } catch (e: Exception) {
        throw BluePrintProcessorException("Cannot load script", e)
    }

    inline fun <reified T> Any?.castOrError() = takeIf { it is T }?.let { it as T }
            ?: throw IllegalArgumentException("Cannot cast $this to expected type ${T::class}")

    inline fun <reified T> load(script: String): T = safeEval { engine.eval(script) }.castOrError()

    inline fun <reified T> load(reader: Reader): T = safeEval { engine.eval(reader) }.castOrError()

    inline fun <reified T> load(inputStream: InputStream): T = load(inputStream.reader())

    inline fun <reified T> loadAll(vararg inputStream: InputStream): List<T> = inputStream.map(::load)
}
