package org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.interfaces

import java.util.concurrent.CompletableFuture



interface NetconfStreamHandler {
    fun sendMessage(request: String, messageId: String): CompletableFuture<String>

}