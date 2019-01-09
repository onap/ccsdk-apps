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

package org.onap.ccsdk.apps.controllerblueprints.core.validation

import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito.*
import org.onap.ccsdk.apps.controllerblueprints.core.data.NodeTemplate
import org.onap.ccsdk.apps.controllerblueprints.core.data.Step
import org.onap.ccsdk.apps.controllerblueprints.core.data.Workflow
import org.onap.ccsdk.apps.controllerblueprints.core.mock.MockBluePrintTypeValidatorService
import org.onap.ccsdk.apps.controllerblueprints.core.utils.BluePrintMetadataUtils
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BluePrintValidatorServiceImplTest {

    private val blueprintBasePath: String = ("./../model-catalog/blueprint-model/starter-blueprint/baseconfiguration")
    private val bluePrintRuntime = BluePrintMetadataUtils.getBluePrintRuntime("1234", blueprintBasePath)
    private val mockBluePrintTypeValidatorService = MockBluePrintTypeValidatorService()
    private val defaultBluePrintValidatorService = BluePrintValidatorServiceImpl(mockBluePrintTypeValidatorService)
    private val workflowValidator = BluePrintWorkflowValidatorImpl(mockBluePrintTypeValidatorService)

    @Test
    fun testValidateOfType() {
        val valid = defaultBluePrintValidatorService.validateBluePrints(bluePrintRuntime)
        assertTrue(valid, "failed in blueprint Validation")
    }

    @Test
    fun testValidateWorkflowFailToFoundNodeTemplate() {
        val workflowName = "resource-assignment"

        val step = Step()
        step.target = "TestCaseFailNoNodeTemplate"
        val workflow = Workflow()
        workflow.steps = mutableMapOf("test" to step)
        workflowValidator.validate(bluePrintRuntime, workflowName, workflow)

        assertEquals(1, bluePrintRuntime.getBluePrintError().errors.size)
        assertEquals("Failed to validate Workflow(resource-assignment)'s step(test)'s definition: could't get node template for the name(TestCaseFailNoNodeTemplate)", bluePrintRuntime.getBluePrintError().errors[0])
    }

    @Test
    @Ignore
    fun testValidateWorkflowFailNodeTemplateNotDgGeneric() {
        val workflowName = "resource-assignment"
        val nodeTemplateName = "resource-assignment-process"

        val nodeTemplate = mock(NodeTemplate::class.java)
//        `when`(nodeTemplate.type).thenReturn("")
//        assertEquals("", nodeTemplate.type)
//        doReturn("WRONG_TYPE").`when`(nodeTemplate.type)
//
//        val blueprintContent = mock<BluePrintContext>(BluePrintContext::class.java)
//        doReturn("").`when`(blueprintContent).nodeTemplateByName(nodeTemplateName)
//
//        val bluePrintRuntimeSpy = spy(bluePrintRuntime)
//        doReturn("").`when`(bluePrintRuntimeSpy).bluePrintContext()

//
//        val step = Step()
//        step.target = nodeTemplateName
//        val workflow = Workflow()
//        workflow.steps = mutableMapOf("test" to step)
//        try {
//            workflowValidator.validate(bluePrintRuntimeSpy, workflowName, workflow)
//        } catch (e: BluePrintException) {
//            assertEquals("Failed to validate Workflow(resource-assignment)'s step(test)'s definition: couldn't find NodeTemplate(TestCaseFailNoNodeTemplate)", e.message)
//            return
//        }
//        fail()
    }

    @Test
    fun testValidateWorkflowSuccess() {
        val workflowName = "resource-assignment"
        workflowValidator.validate(bluePrintRuntime, workflowName, bluePrintRuntime.bluePrintContext().workflowByName(workflowName))
    }

}

