package org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.mocks

import org.apache.commons.io.IOUtils
import org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.interfaces.DeviceInfo
import org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.interfaces.NetconfSession
import java.util.concurrent.CompletableFuture
import java.nio.charset.Charset






class MockNetconfSessionManager : NetconfSession {

    private val deviceInfo: DeviceInfo = DeviceInfo("user", "ps", "localhost", 830, 30.toString())


    override fun asyncRpc(request: String, msgId: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        try {
            future.complete(this.getMockNetconfResponseMessage(msgId))
        } catch (e: Exception) {
            val errorReply = ("<rpc-reply xmlns:junos=\"http://xml.juniper.net/junos/15.1X49/junos\" message-id=\"get-config-123456\"  xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">\n"
                    + "<rpc-error>\n" + "<error-type>protocol</error-type>\n"
                    + "<error-tag>operation-failed</error-tag>\n" + "<error-severity>error</error-severity>\n"
                    + "<error-message>" + e.message + "</error-message>\n" + "<error-info>\n"
                    + "<bad-element>get-config</bad-element>\n" + "</error-info>\n" + "</rpc-error>\n"
                    + "</rpc-reply>")
            future.complete(errorReply)
        }

        return future
    }

    override fun close(): Boolean {
        return true
    }

    override fun getSessionId(): String? {
        return "session-1234"
    }

    override fun getDeviceCapabilitiesSet(): Set<String> {
        return emptySet()
    }

    override fun getDeviceInfo(): DeviceInfo {
        return this.deviceInfo
    }

    @Throws(Exception::class)
    fun getMockNetconfResponseMessage(messageId: String): String {
        val contentPath = "response/$messageId.xml"
        return getFileContent(contentPath)
    }

    @Throws(Exception::class)
    fun getFileContent(filePath: String): String {
        var content: String? = null
        try {
            content = IOUtils.toString(MockNetconfSessionManager::class.java!!.getClassLoader().getResourceAsStream(filePath),
                    Charset.defaultCharset())
        } catch (e: Exception) {
            throw Exception("Couldn't file file $filePath")
        }

        return content
    }

    override fun syncRpc(request: String, msgId: String): String {
        val future = CompletableFuture<String>()
        try {
            future.complete(this.getMockNetconfResponseMessage(msgId))
        } catch (e: Exception) {
            val errorReply = ("<rpc-reply xmlns:junos=\"http://xml.juniper.net/junos/15.1X49/junos\" message-id=\"get-config-123456\"  xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">\n"
                    + "<rpc-error>\n" + "<error-type>protocol</error-type>\n"
                    + "<error-tag>operation-failed</error-tag>\n" + "<error-severity>error</error-severity>\n"
                    + "<error-message>" + e.message + "</error-message>\n" + "<error-info>\n"
                    + "<bad-element>get-config</bad-element>\n" + "</error-info>\n" + "</rpc-error>\n"
                    + "</rpc-reply>")
            future.complete(errorReply)
        }

        return future.toString()
    }

}