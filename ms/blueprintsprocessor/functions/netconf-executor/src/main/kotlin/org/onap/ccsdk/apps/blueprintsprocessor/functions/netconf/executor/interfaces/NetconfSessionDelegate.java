package org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.interfaces;

import org.onap.ccsdk.apps.blueprintsprocessor.functions.netconf.executor.data.NetconfDeviceOutputEvent;

public interface NetconfSessionDelegate {

    void notify(NetconfDeviceOutputEvent event);
}
