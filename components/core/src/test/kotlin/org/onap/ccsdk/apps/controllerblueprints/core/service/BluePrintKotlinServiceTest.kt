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

import org.junit.Test
import org.onap.ccsdk.apps.controllerblueprints.core.interfaces.BlueprintFunctionNode
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class BluePrintKotlinServiceTest {

    @Test
    fun `invoke script`() {
        val scriptContent = "11 + 11"
        val value = BluePrintKotlinService()
                .load<Int>(scriptContent)
        assertEquals(22, value, "failed to execute command")
    }

    @Test
    fun `invoke script component node`() {

        val scriptReader = Files.newBufferedReader(Paths.get("src/test/resources/scripts/SampleBlueprintFunctionNode.kts"))

        val functionNode = BluePrintKotlinService()
                .load<BlueprintFunctionNode<String, String>>(scriptReader)
        assertNotNull(functionNode, " Failed to get instance from script")
        assertEquals("Kotlin-Script-Function-Node", functionNode.getName(), "failed to get expected result")
    }

}