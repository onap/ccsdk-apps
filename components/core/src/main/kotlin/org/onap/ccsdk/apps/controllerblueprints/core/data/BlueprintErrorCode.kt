package org.onap.ccsdk.apps.controllerblueprints.core.data

import java.util.HashMap

/**
 * ErrorCode.java Purpose: Maintain a list of HTTP status codes
 *
 * @author Steve Siani
 * @version 1.0
 */
enum class ErrorCode (val value: Int, val httpCode: Int) {

    // TODO: Add more attribute for each needed application protocol
    // TODO: Example: INVALID_FILE_EXTENSION(1, 500, 25)
    GENERIC_FAILURE(1, 500) {
        override fun message(detailMsg: String): String {
            return String.format("Generic failure. Details : {%s}", detailMsg)
        }
    },
    INVALID_FILE_EXTENSION(2, 415) {
        override fun message(detailMsg: String): String {
            return String.format("Unexpected file extension. Details : {%s}", detailMsg)
        }
    },
    BLUEPRINT_PATH_MISSING(3, 503) {
        override fun message(detailMsg: String): String {
            return String.format("Blueprint path missing or wrong. Details : {%s}", detailMsg)
        }
    },
    BLUEPRINT_WRITING_FAIL(4, 503) {
        override fun message(detailMsg: String): String {
            return String.format("Fail to write blueprint files. Details : {%s}", detailMsg)
        }
    },
    IO_FILE_INTERRUPT(5, 503) {
        override fun message(detailMsg: String): String {
            return String.format("IO file system interruption. Details : {%s}", detailMsg)
        }
    },
    INVALID_REQUEST_FORMAT(6, 400) {
        override fun message(detailMsg: String): String {
            return String.format("Bad request. Details : {%s}", detailMsg)
        }
    },
    UNAUTHORIZED_REQUEST(7, 401) {
        override fun message(detailMsg: String): String {
            return String.format("The request requires user authentication. Details : {%s}", detailMsg)
        }
    },
    REQUEST_NOT_FOUND(8, 404) {
        override fun message(detailMsg: String): String {
            return String.format("BA. Details : {%s}", detailMsg)
        }
    },
    RESOURCE_NOT_FOUND(9, 404) {
        override fun message(detailMsg: String): String {
            return String.format("No response was found for this request in the server. Details : {%s}", detailMsg)
        }
    },
    CONFLICT_ADDING_RESOURCE(10, 409) {
        override fun message(detailMsg: String): String {
            return String.format("Duplicated entry while saving Blueprint. Details : {%s}", detailMsg)
        }
    };

    abstract fun message(detailMsg: String): String

    companion object {

        private val map = HashMap<Int, ErrorCode>()

        init {
            for (errorCode in ErrorCode.values()) {
                map[errorCode.value] = errorCode
            }
        }

        fun valueOf(value: Int): ErrorCode? {
            return if (map.containsKey(value)) map[value] else map[1]
        }
    }
}