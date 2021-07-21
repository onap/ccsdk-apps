package org.onap.ccsdk.apps.filters;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        String contentType;
        List<String> acceptList;

        List<String> headerNames; 

        public DefaultContentTypeHttpRequest(HttpServletRequest httpRequest, String defaultContentType) {
            super(httpRequest);

            this.httpRequest = httpRequest;
            this.contentType = defaultContentType;
            this.acceptList = null;

            boolean hasContentType = false;
            headerNames = new LinkedList<String>();
            Enumeration<String> headerNameEnum = httpRequest.getHeaderNames();
            while (headerNameEnum.hasMoreElements()) {
                String curHeaderName = headerNameEnum.nextElement();
                if ("Content-Type".equalsIgnoreCase(curHeaderName)) {
                    hasContentType = true;
                    contentType = super.getContentType();
                    if ("application/yang-data+json".equalsIgnoreCase(contentType)) {
                        contentType = "application/json";
                    } else if ("application/yang-data+xml".equalsIgnoreCase(contentType)) {
                        contentType = "application/xml";
                    } else if (contentType.startsWith("text/plain")) {
                        // Use Accept header, if present, to determine content type.
                        boolean acceptsXml = false;
                        boolean acceptsJson = false;
                        for (Enumeration<String> e = getHeaders("Accept") ; e.hasMoreElements() ;) {
                            String curAcceptValue = e.nextElement();
                            if ("application/json".equalsIgnoreCase(curAcceptValue)) {
                                acceptsJson = true;
                            } else if ("application/yang-data+json".equalsIgnoreCase(curAcceptValue)) {
                                acceptsJson = true;
                            } else if ("application/xml".equalsIgnoreCase(curAcceptValue)) {
                                acceptsXml = true;
                            } else if ("application/yang-data+xml".equalsIgnoreCase(curAcceptValue)) {
                                acceptsXml = true;
                            }
                        }
                        if (acceptsJson) {
                            contentType = "application/json";
                        } else if (acceptsXml) {
                            contentType = "application/xml";
                        } else {
                            // If Accept does not specify XML or JSON (could be Accept is missing), use default content type
                            contentType = defaultContentType;
                        }
                    }
                } else if ("Accept".equalsIgnoreCase(curHeaderName)) {
                    acceptList = new LinkedList<String>();
                    for (Enumeration<String> e = getHeaders("Accept") ; e.hasMoreElements() ;) {
                        String acceptValue = e.nextElement();
                        if ("application/yang-data+json".equalsIgnoreCase(acceptValue)) {
                            if (!acceptList.contains("application/json")) {
                                acceptList.add("application/json");
                            }
                        } else if ("application/yang-data+xml".equalsIgnoreCase(acceptValue)) {
                            if (!acceptList.contains("application/xml")) {
                                acceptList.add("application/xml");
                            }
                        } else {
                            if (!acceptList.contains(acceptValue)) {
                                acceptList.add(acceptValue);
                            }
                        }
                    }
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
        public Enumeration<String> getHeaders(String name) {
            if ("Accept".equalsIgnoreCase(name) && (acceptList != null)) {
                return Collections.enumeration(acceptList);
            } else {
                return super.getHeaders(name);
            }
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            return(Collections.enumeration(headerNames));
        }

        @Override
        public String getContentType() {
           return contentType;
        }
        
    }
    
    
}
