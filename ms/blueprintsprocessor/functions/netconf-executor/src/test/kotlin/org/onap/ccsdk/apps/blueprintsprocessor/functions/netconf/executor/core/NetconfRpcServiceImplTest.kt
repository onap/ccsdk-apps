package org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.core

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.api.DeviceInfo
import org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.mocks.NetconfDeviceSimulator

class NetconfRpcServiceImplTest {

    private var device: NetconfDeviceSimulator? = null
    private var deviceInfo: DeviceInfo? = null

    @Before
    fun before() {
        deviceInfo = DeviceInfo().apply {
            username = "username"
            password = "password"
            ipAddress = "localhost"
            port = 2224
            connectTimeout = 10
        }

        device = NetconfDeviceSimulator(deviceInfo!!.port)
        device!!.start()
    }

    @After
    fun after() {
        device!!.stop()
    }

    @Test
    fun setNetconfSession() {

    }

    @Test
    fun getConfig() {
    }

    @Test
    fun deleteConfig() {
    }

    @Test
    fun lock() {
    }

    @Test
    fun unLock() {
    }

    @Test
    fun commit() {
    }

    @Test
    fun cancelCommit() {
    }

    @Test
    fun discardConfig() {
    }

    @Test
    fun editConfig() {
    }

    @Test
    fun validate() {
    }

    @Test
    fun closeSession() {
    }

    @Test
    fun asyncRpc() {
    }
}