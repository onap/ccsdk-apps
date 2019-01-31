package org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.core.NetconfSessionImpl
import org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.interfaces.DeviceInfo
import org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.utils.NetconfDeviceSimulator


class NetconfSessionImplTest {

    private var device: NetconfDeviceSimulator? = null
    private var deviceInfo: DeviceInfo? = null

    @Before
    fun before() {
        deviceInfo = DeviceInfo("name", "password", "localhost", 2224, "10")

        device = NetconfDeviceSimulator(deviceInfo!!.port)
        device!!.start()
    }

    @After
    fun after() {
        device!!.stop()
    }


    @Throws(Exception::class)
    fun testNetconfSession() {
        val netconfSession = NetconfSessionImpl(deviceInfo!!)

        Assert.assertNotNull(netconfSession.getSessionId())
        Assert.assertEquals("localhost:2224", netconfSession.getDeviceInfo().toString())

        netconfSession.checkAndReestablish()

        Assert.assertNotNull(netconfSession.getSessionId())
        Assert.assertEquals("localhost:2224", netconfSession.getDeviceInfo().toString())

        Assert.assertTrue(!netconfSession.getDeviceCapabilitiesSet().isEmpty())
    }

}
