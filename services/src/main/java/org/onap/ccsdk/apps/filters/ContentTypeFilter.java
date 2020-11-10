package org.onap.ccsdk.apps.filters;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ContentTypeFilter implements Filter {

    String DEFAULT_CONTENT_TYPE = "application/json";

    @Override
    public void doFilter(ServletRequest httpReq, ServletResponse httpResp, FilterChain chain)
            throws IOException, ServletException {
                String defaultContentType = System.getProperty("ccsdk.defaults.content-type", DEFAULT_CONTENT_TYPE);

                chain.doFilter(new DefaultContentTypeHttpRequest((HttpServletRequest) httpReq, defaultContentType), httpResp);

    }

    private class DefaultContentTypeHttpRequest extends HttpServletRequestWrapper {
        HttpServletRequest httpRequest;
        String defaultContentType;
        boolean hasContentType;

        List<String> headerNames; 

        public DefaultContentTypeHttpRequest(HttpServletRequest httpRequest, String defaultContentType) {
            super(httpRequest);
            this.httpRequest = httpRequest;
            this.defaultContentType = defaultContentType;
            headerNames = new LinkedList<String>();
            Enumeration<String> headerNameEnum = httpRequest.getHeaderNames();
            hasContentType = false;
            while (headerNameEnum.hasMoreElements()) {
                String curHeaderName = headerNameEnum.nextElement();
                if ("Content-Type".equalsIgnoreCase(curHeaderName)) {
                    hasContentType = true;
                }
                headerNames.add(curHeaderName);
            }
            if (!hasContentType) {
                headerNames.add("Content-Type");
            }

        }

        @Override
        public String getHeader(String name) {
            if (name.equalsIgnoreCase("Content-Type")) {
                return getContentType();
            } else {
                return super.getHeader(name);
            }
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            return(Collections.enumeration(headerNames));
        }

        @Override
        public String getContentType() {
            if (hasContentType) {
                return super.getContentType();
            } else {
                return defaultContentType;
            }
        }
        
    }
    
    
}
