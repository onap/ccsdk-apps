package org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.data

class NetconfAdaptorConstant {
    companion object{
        const val STATUS_CODE_SUCCESS = "200"
        const val STATUS_CODE_FAILURE = "400"

        const val STATUS_SUCCESS = "success"
        const val STATUS_FAILURE = "failure"
        const val STATUS_SKIPPED = "skipped"
        const val LOG_MESSAGE_TYPE_LOG = "Log"

        const val CONFIG_TARGET_RUNNING = "running"
        const val CONFIG_TARGET_CANDIDATE = "candidate"
        const val CONFIG_DEFAULT_OPERATION_MERGE = "merge"
        const val CONFIG_DEFAULT_OPERATION_REPLACE = "replace"

        const val DEFAULT_NETCONF_SESSION_MANAGER_TYPE = "DEFAULT_NETCONF_SESSION"

        const val CONFIG_STATUS_PENDING = "pending"
        const val CONFIG_STATUS_FAILED = "failed"
        const val CONFIG_STATUS_SUCCESS = "success"

        const val DEFAULT_MESSAGE_TIME_OUT = 30


    }
}