package org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.utils


import org.apache.sshd.common.NamedFactory
import org.apache.sshd.server.Command
import java.util.ArrayList
import org.apache.sshd.server.auth.UserAuthNoneFactory
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider
import org.apache.sshd.server.SshServer
import org.apache.sshd.server.auth.UserAuth


class NetconfDeviceSimulator(private val port: Int) {

    private var sshd: SshServer? = null

    fun start() {
        sshd = SshServer.setUpDefaultServer()
        sshd!!.port = port
        sshd!!.keyPairProvider = SimpleGeneratorHostKeyProvider()

        val userAuthFactories = ArrayList<NamedFactory<UserAuth>>()
        userAuthFactories.add(UserAuthNoneFactory())
        sshd!!.userAuthFactories = userAuthFactories

        val namedFactoryList = ArrayList<NamedFactory<Command>>()
        namedFactoryList.add(NetconfSubsystemFactory())
        sshd!!.subsystemFactories = namedFactoryList

        try {
            sshd!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun stop() {
        try {
            sshd!!.stop(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}