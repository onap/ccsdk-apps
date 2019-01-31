package org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.data

import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class NetconfSshClientLibTest {

    @Test
    fun getEnum() {
        val messageId = "APACHE_MINA"
        println(NetconfSshClientLib.APACHE_MINA)
       // Assert.assertEquals(NetconfSshClientLib.valueOf(messageId),messageId)
    }

    @Test
    fun getSshClientString() {
    }
}