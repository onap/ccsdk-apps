package org.onap.ccsdk.apps.filters;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.onap.logging.filter.base.AbstractServletFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PayloadLoggingFilter extends AbstractServletFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(PayloadLoggingFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		RequestWrapper req = new RequestWrapper((HttpServletRequest) request);
		Request requestData = req.getMessageRequest();
		
		StringBuilder requestHeaders = new StringBuilder("REQUEST|");
	    requestHeaders.append(requestData.method);
	    requestHeaders.append(":");
	    requestHeaders.append(requestData.uri);
	    requestHeaders.append("|");
	    mapstr(requestHeaders, requestData.headers);

	    log.info(requestHeaders.toString());
	    log.info("REQUEST BODY|{}", requestData.body);

		ResponseWrapper res = new ResponseWrapper((HttpServletResponse) response);

		chain.doFilter(req, res);

		Response responseData = res.getMessageResponse();
		
		StringBuilder responseHeaders = new StringBuilder();
        responseHeaders.append("RESPONSE HEADERS|");
        mapstr(responseHeaders, responseData.headers);
        responseHeaders.append("Status:").append(responseData.code);
        responseHeaders.append(";IsCommitted:").append(res.isCommitted());

        log.info(responseHeaders.toString());
		log.info("RESPONSE BODY|{}", responseData.body);

		res.writeBody();
	}

	private static class Request {

		public String method;
		public String uri;
		public Map<String, Object> headers;
		public Map<String, Object> param;
		public String body;

		@Override
		public String toString() {
			StringBuilder ss = new StringBuilder();
			ss.append("REQUEST|").append(method).append(":").append(uri).append("|");
			ss.append("Headers: ");
			mapstr(ss, headers);
			if (param != null && !param.isEmpty()) {
				ss.append("Parameters: ");
				mapstr(ss, param);
			}
			ss.append("REQUEST BODY|\n");
			ss.append(body);
			return ss.toString();
		}
	}

	private static class Response {

		public int code;
		public String message;
		public Map<String, Object> headers;
		public String body;

		@Override
		public String toString() {
			StringBuilder ss = new StringBuilder();
			ss.append("HTTP Response: ").append(code).append(" ").append(message).append("\n");
			ss.append("Headers:\n");
			mapstr(ss, headers);
			ss.append("Body:\n");
			ss.append(body);
			return ss.toString();
		}
	}

	private static class RequestWrapper extends HttpServletRequestWrapper {

		private final String body;

		public RequestWrapper(HttpServletRequest request) throws IOException {
			super(request);

			StringBuilder stringBuilder = new StringBuilder();
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
					char[] charBuffer = new char[128];
					int bytesRead = -1;
					while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
						stringBuilder.append(charBuffer, 0, bytesRead);
					}
				}
			}
			body = stringBuilder.toString();
		}

		@Override
		public ServletInputStream getInputStream() throws IOException {
			final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
			ServletInputStream servletInputStream = new ServletInputStream() {

				@Override
				public int read() throws IOException {
					return byteArrayInputStream.read();
				}

				@Override
				public boolean isFinished() {
					return byteArrayInputStream.available() == 0;
				}

				@Override
				public boolean isReady() {
					return true;
				}

				@Override
				public void setReadListener(ReadListener listener) {
				}
			};
			return servletInputStream;
		}

		@Override
		public BufferedReader getReader() throws IOException {
			return new BufferedReader(new InputStreamReader(getInputStream()));
		}

		public String getBody() {
			return body;
		}

		public Request getMessageRequest() {
			Request r = new Request();
			r.method = getMethod();
			r.uri = getRequestURI();
			r.param = getParamMap();

			r.headers = new HashMap<>();
			Enumeration<String> headerNames = getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String name = headerNames.nextElement();

				if (name.equalsIgnoreCase("authorization")) {
					r.headers.put(name, "***REDACTED***");
					continue;
				}

				Enumeration<String> values = getHeaders(name);
				List<String> valueList = new ArrayList<>();
				while (values.hasMoreElements()) {
					valueList.add(values.nextElement());
				}
				if (valueList.size() > 1) {
					r.headers.put(name, valueList);
				} else if (valueList.size() > 0) {
					r.headers.put(name, valueList.get(0));
				}
			}

			r.body = getBody();

			return r;
		}

		private Map<String, Object> getParamMap() {
			Map<String, String[]> parameterMap = getParameterMap();
			Map<String, Object> paramMap = new HashMap<>();
			if (parameterMap != null) {
				for (Entry<String, String[]> entry : parameterMap.entrySet()) {
					String name = entry.getKey();
					String[] values = entry.getValue();
					if (values != null && values.length > 0) {
						if (values.length == 1) {
							paramMap.put(name, values[0]);
						} else {
							paramMap.put(name, Arrays.<String> asList(values));
						}
					}
				}
			}
			return paramMap;
		}
	}

	public class ResponseWrapper extends HttpServletResponseWrapper {

		private CharArrayWriter writer = new CharArrayWriter();

		private String statusMessage;

		public ResponseWrapper(HttpServletResponse response) {
			super(response);
		}

		@Override
		public PrintWriter getWriter() {
			return new PrintWriter(writer);
		}

		@Override
		public ServletOutputStream getOutputStream() {
			return new ServletOutputStream() {

				@Override
				public void write(int b) throws IOException {
					writer.write(b);
				}

				@Override
				public void setWriteListener(WriteListener listener) {
				}

				@Override
				public boolean isReady() {
					return true;
				}
			};
		}

		@SuppressWarnings("deprecation")
		@Override
		public void setStatus(int sc, String sm) {
			super.setStatus(sc, sm);
			statusMessage = sm;
		}

		public Response getMessageResponse() {
			Response r = new Response();
			r.code = getStatus();
			r.message = statusMessage == null ? "" : statusMessage;

			r.headers = new HashMap<>();
			Collection<String> headerNames = getHeaderNames();
			for (String name : headerNames) {

				if (name.equalsIgnoreCase("authorization")) {
					r.headers.put(name, "***REDACTED***");
					continue;
				}

				Collection<String> values = getHeaders(name);
				List<String> valueList = new ArrayList<>(values);
				if (valueList.size() > 1) {
					r.headers.put(name, valueList);
				} else {
					r.headers.put(name, valueList.get(0));
				}
			}

			r.body = writer.toString();

			return r;
		}

		public void writeBody() throws IOException {
			String body = writer.toString();
			setContentLength(body.length());
			super.getWriter().write(body);
		}
	}

	private static void mapstr(StringBuilder ss, Map<String, Object> m) {
		if (m != null) {
			for (Entry<String, Object> entry : m.entrySet()) {
				ss.append(entry.getKey()).append(": ").append(entry.getValue()).append(";");
			}
		}
	}
}