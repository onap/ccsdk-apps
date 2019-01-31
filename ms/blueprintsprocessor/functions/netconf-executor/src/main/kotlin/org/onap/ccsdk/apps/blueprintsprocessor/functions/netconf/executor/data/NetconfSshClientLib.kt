package org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.data

enum class NetconfSshClientLib(val sshClientString :String) {
     APACHE_MINA("apache-mina"),
     ETHZ_SSH2("ethz-ssh2");

    fun getEnum(valueOf: String): NetconfSshClientLib {
        return NetconfSshClientLib.valueOf(valueOf.toUpperCase().replace('-', '_'))
    }

}