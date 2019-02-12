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

package org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.interfaces

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

class DeviceInfo {
    @get:JsonProperty("login-account")
    var name: String? = null
    @get:JsonProperty("login-key")
    var pass: String? = null
    @get:JsonProperty("target-ip-address")
    var ipAddress: String? = null
    @get:JsonProperty("port-number")
    var port: Int = 0
    @get:JsonIgnore
    var key: String? = null
    @get:JsonProperty("source")
    var source: String? = null
    @get:JsonProperty("connection-time-out")
    var connectTimeoutSec: Long = 30
    @get:JsonIgnore
    var replyTimeout: Int = 60
    @get:JsonIgnore
    var idleTimeout: Int = 45
    @get:JsonIgnore
    var deviceId: String = "$ipAddress:$port"
}