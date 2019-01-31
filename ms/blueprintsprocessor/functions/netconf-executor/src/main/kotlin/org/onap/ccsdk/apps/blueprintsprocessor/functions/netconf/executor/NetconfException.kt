package org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor

import java.io.IOException

class NetconfException : IOException {

    var code: Int = 100

    constructor(cause: Throwable) : super(cause)
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable, message: String, vararg args: Any?) : super(String.format(message, *args), cause)

    constructor(code: Int, cause: Throwable) : super(cause) {
        this.code = code
    }

    constructor(code: Int, message: String) : super(message) {
        this.code = code
    }

    constructor(code: Int, message: String, cause: Throwable) : super(message, cause) {
        this.code = code
    }

    constructor(code: Int, cause: Throwable, message: String, vararg args: Any?)
            : super(String.format(message, *args), cause) {
        this.code = code
    }
}
