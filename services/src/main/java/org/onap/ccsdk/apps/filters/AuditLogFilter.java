package org.onap.ccsdk.apps.filters
;

import javax.servlet.http.HttpServletRequest;
import org.onap.logging.filter.base.AuditLogServletFilter;
import org.onap.logging.ref.slf4j.ONAPLogConstants;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class AuditLogFilter extends AuditLogServletFilter {
    private static final String MDC_HTTP_METHOD_KEY = "HttpMethod";

    @Override
    protected void additionalPreHandling(HttpServletRequest httpServletRequest) {
        // Don't overwrite service instance id if it was set outside of this automated method
        if (MDC.get(ONAPLogConstants.MDCs.SERVICE_INSTANCE_ID) == null) {
            String serviceInstanceId = getServiceInstanceId(httpServletRequest.getRequestURI());
            if (serviceInstanceId != null) {
                MDC.put(ONAPLogConstants.MDCs.SERVICE_INSTANCE_ID, serviceInstanceId);
            }
        }
        MDC.put(MDC_HTTP_METHOD_KEY, httpServletRequest.getMethod());
    }

    // restconf URLs follow a pattern, this method attempts to extract the service instance id according to that pattern
    protected String getServiceInstanceId(String path) {
        int idx = path.indexOf("service-list");
        if (idx != -1) {
            // chomp off service-list/
            String str = path.substring(idx + 13);
            idx = str.indexOf("/");
            //if there is another forward slash with more information chomp it off
            if (idx != -1) {
                return str.substring(0, idx);
            } else {
                return str;
            }
        }
        return null;
    }

}