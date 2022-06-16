package org.onap.ccsdk.apps.ms.neng.core.resource.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GetConfigRequestV2Test {
    private GetConfigRequestV2 configRequestV2;

    @Before
    public void setup() {
        configRequestV2 = new GetConfigRequestV2();
    }

    @Test
    public void testGetOnapName() {
        configRequestV2.setOnapName("OnapName");
        Assert.assertEquals("OnapName", configRequestV2.getOnapName());
    }

    @Test
    public void testGetOnapComponent() {
        configRequestV2.setOnapComponent("OnapComponent");
        Assert.assertEquals("OnapComponent", configRequestV2.getOnapComponent());
    }

    @Test
    public void testGetOnapInstance() {
        configRequestV2.setOnapInstance("OnapInstance");
        Assert.assertEquals("OnapInstance", configRequestV2.getOnapInstance());
    }

    @Test
    public void testGetRequestId() {
        configRequestV2.setRequestId("RequestId");
        Assert.assertEquals("RequestId", configRequestV2.getRequestId());
    }

    @Test
    public void testGetAction() {
        configRequestV2.setAction("Action");
        Assert.assertEquals("Action", configRequestV2.getAction());
    }
}
