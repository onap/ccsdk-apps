/*
 *  Copyright © 2017-2018 AT&T Intellectual Property.
 *  Modifications Copyright © 2018 IBM.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.onap.ccsdk.apps.blueprintsprocessor.core.api.data

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.node.ObjectNode
import io.swagger.v3.oas.annotations.media.Schema

import java.util.*

/**
 * BlueprintProcessorData
 * @author Brinda Santh
 * DATE : 8/15/2018
 */

open class ExecutionServiceInput {
    @get:Schema(required = true)
    lateinit var commonHeader: CommonHeader
    @get:Schema(required = true)
    lateinit var actionIdentifiers: ActionIdentifiers
    @get:Schema(required = true)
    lateinit var payload: ObjectNode
}

open class ExecutionServiceOutput {
    @get:Schema(required = true)
    lateinit var commonHeader: CommonHeader
    @get:Schema(required = true)
    lateinit var actionIdentifiers: ActionIdentifiers
    @get:Schema(required = true)
    var status: Status = Status()
    @get:Schema(required = true)
    lateinit var payload: ObjectNode
}

const val ACTION_MODE_ASYNC = "async"
const val ACTION_MODE_SYNC = "sync"

open class ActionIdentifiers {
    @get:Schema(required = false)
    lateinit var blueprintName: String
    @get:Schema(required = false)
    lateinit var blueprintVersion: String
    @get:Schema(required = true)
    lateinit var actionName: String
    @get:Schema(required = true, allowableValues = ["sync", "async"])
    lateinit var mode: String
}

open class CommonHeader {
    @get:Schema(required = true, example = "2012-04-23T18:25:43.511Z")
    @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    var timestamp: Date = Date()
    @get:Schema(required = true)
    lateinit var originatorId: String
    @get:Schema(required = true)
    lateinit var requestId: String
    @get:Schema(required = true)
    lateinit var subRequestId: String
    @get:Schema(required = false)
    var flags: Flags? = null
}

open class Flags {
    var isForce: Boolean = false
    @get:Schema(defaultValue = "3600")
    var ttl: Int = 3600
}

open class Status {
    @get:Schema(required = true)
    var code: Int = 200
    @get:Schema(required = true)
    var eventType: String = "EVENT-ACTION-RESPONSE"
    @get:Schema(required = true, example = "2012-04-23T18:25:43.511Z")
    @get:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    var timestamp: Date = Date()
    @get:Schema(required = false)
    var errorMessage: String? = null
    @get:Schema(required = true)
    var message: String = "success"
}

open class BluePrintManagementInput {
    @get:Schema(required = true)
    lateinit var commonHeader: CommonHeader
    @get:Schema(required = false)
    lateinit var blueprintName: String
    @get:Schema(required = false)
    lateinit var blueprintVersion: String
    @get:Schema(required = true)
    lateinit var fileChunk: FileChunk
}

open class FileChunk {
    @get:Schema(required = true)
    lateinit var chunk: ByteArray
}

open class BluePrintManagementOutput {
    @get:Schema(required = true)
    lateinit var commonHeader: CommonHeader
    @get:Schema(required = true)
    var status: Status = Status()
}



