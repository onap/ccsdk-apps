package org.onap.ccsdk.apps.ms.sliboot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

public class SlibootIT {
    @Test
    public void healthcheckTest() throws ClientProtocolException, IOException {
        String slibootPort = System.getenv("SLIBOOT_PORT");
        if ((slibootPort == null) || (slibootPort.length() == 0)) {
            slibootPort = "8080";
        }

        String testUrl = "http://localhost:" + slibootPort + "/restconf/operations/SLI-API:healthcheck/";

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost postCmd = new HttpPost(testUrl);
        postCmd.addHeader("Content-Type", "application/json");
        
        CloseableHttpResponse resp = client.execute(postCmd);
        assertEquals(200, resp.getStatusLine().getStatusCode());
    }
}
