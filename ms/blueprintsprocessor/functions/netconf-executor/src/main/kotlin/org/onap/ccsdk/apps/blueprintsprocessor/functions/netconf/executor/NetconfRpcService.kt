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

import org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.core.NetconfSessionFactory
import org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.interfaces.DeviceInfo
import org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.interfaces.NetconfSession
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

@Service("netconf-rpc-service")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class NetconfRpcService {

    lateinit var deviceInfo: DeviceInfo
    lateinit var netconfSession: NetconfSession

    fun connect(netconfDeviceInfo: DeviceInfo) {
        netconfSession = NetconfSessionFactory.instance("DEFAULT_NETCONF_SESSION", netconfDeviceInfo)
        // TODO
    }

    fun disconnect() {
        netconfSession.close()
    }

    fun reconnect() {
        disconnect()
        connect(deviceInfo)
    }
}