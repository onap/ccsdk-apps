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

package org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor

import com.fasterxml.jackson.databind.JsonNode
import org.onap.ccsdk.apps.blueprintsprocessor.core.api.data.ExecutionServiceInput
import org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.interfaces.DeviceInfo
import org.onap.ccsdk.apps.blueprintsprocessor.functions.python.executor.ComponentJythonExecutor
import org.onap.ccsdk.apps.blueprintsprocessor.functions.python.executor.PythonExecutorProperty
import org.onap.ccsdk.apps.controllerblueprints.core.putJsonElement
import org.onap.ccsdk.apps.controllerblueprints.core.utils.JacksonUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component("component-netconf-executor")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
open class ComponentNetconfExecutor(private val netconfExecutorConfiguration: NetconfExecutorConfiguration,
                                    private val pythonExecutorProperty: PythonExecutorProperty)
    : ComponentJythonExecutor(pythonExecutorProperty) {

    private val log = LoggerFactory.getLogger(ComponentNetconfExecutor::class.java)
    lateinit var deviceInfo: DeviceInfo


    override fun process(executionServiceInput: ExecutionServiceInput) {
        val capabilityProperty: MutableMap<String, JsonNode> = bluePrintRuntimeService.resolveNodeTemplateCapabilityProperties("sample-netconf-device","netconf")
        val deviceInfo = JacksonUtils.getInstanceFromMap(capabilityProperty, DeviceInfo::class.java)
        log.info("Processing NetconfExecutor : $operationInputs")
        operationInputs.putJsonElement("deviceInfo",deviceInfo)
        val configContext = applicationContext as ConfigurableApplicationContext
        val beanRegistry = configContext.beanFactory
        beanRegistry.registerSingleton("deviceInfo", deviceInfo);

         super.process(executionServiceInput)


    }

    fun setdeviceInfo(deviceInfo: DeviceInfo) {
        this.deviceInfo = deviceInfo
    }
}