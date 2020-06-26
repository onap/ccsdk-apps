package org.onap.ccsdk.apps.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;



public class RestExceptionHandlerTest {
    private static final Logger log = LoggerFactory.getLogger("RestExceptionHandler");
    private class RestExceptionHandlerWrapper extends RestExceptionHandler {
        @Override
        public  ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
            return super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
        }

        @Override
        public ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
            return super.handleHttpMediaTypeNotSupported(ex, headers, status, request);
        }

        @Override
        public ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
            return super.handleHttpMediaTypeNotAcceptable(ex, headers, status, request);
        }

        @Override
        protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
            return super.handleMissingPathVariable(ex, headers, status, request);
        }

        @Override
        public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
            return super.handleMissingServletRequestParameter(ex, headers, status, request);
        }

        @Override
        public ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
            return super.handleServletRequestBindingException(ex, headers, status, request);
        }

        @Override
        public ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
            return super.handleConversionNotSupported(ex, headers, status, request);
        }

        @Override
        public ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
            return super.handleTypeMismatch(ex, headers, status, request);
        }

        @Override
        public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
            return super.handleHttpMessageNotReadable(ex, headers, status, request);
        }

        @Override
        public ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
            return super.handleHttpMessageNotWritable(ex, headers, status, request);
        }

        @Override
        public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
            return super.handleMethodArgumentNotValid(ex, headers, status, request);
        }

        @Override
        public ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
            return super.handleMissingServletRequestPart(ex, headers, status, request);
        }

        @Override
        public ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
            return super.handleBindException(ex, headers, status, request);
        }

        @Override
        public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
            return super.handleNoHandlerFoundException(ex, headers, status, request);
        }

        @Override
        public ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
            return super.handleAsyncRequestTimeoutException(ex, headers, status, webRequest);
        }

        @Override
        public ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
            return super.handleExceptionInternal(ex, body, headers, status, request);
        }
    }

    RestExceptionHandler handler;

    @Before
    public void setUp() {
        handler = new RestExceptionHandlerWrapper();
    }

    @Test
    public void handleHttpRequestMethodNotSupported() throws JsonProcessingException {
        String[] supportedMethods = {"GET", "POST", "PUT", "DELETE"};


        ResponseEntity<Object> respEntity = handler.handleHttpRequestMethodNotSupported(new HttpRequestMethodNotSupportedException("PATCH", supportedMethods),
                null, HttpStatus.METHOD_NOT_ALLOWED, null);

        assertTrue(respEntity.hasBody());
        assertTrue(respEntity.getBody() instanceof RestErrors);

        RestErrors restErrors = (RestErrors) respEntity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        log.info("response : {}", objectMapper.writeValueAsString(restErrors));


    }

    @Test
    public void handleHttpMediaTypeNotSupported() throws JsonProcessingException {
        MediaType[] supportedMediaTypeArray = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML};

        ResponseEntity<Object> respEntity = handler.handleHttpMediaTypeNotSupported(new HttpMediaTypeNotSupportedException(MediaType.MULTIPART_MIXED, Arrays.asList(supportedMediaTypeArray)),
                null, HttpStatus.UNSUPPORTED_MEDIA_TYPE, null);

        assertTrue(respEntity.hasBody());
        assertTrue(respEntity.getBody() instanceof RestErrors);

        RestErrors restErrors = (RestErrors) respEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("response : {}", objectMapper.writeValueAsString(restErrors));
    }

    @Test
    public void handleHttpMediaTypeNotAcceptable() throws JsonProcessingException {
        MediaType[] supportedMediaTypeArray = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML};

        ResponseEntity<Object> respEntity = handler.handleHttpMediaTypeNotAcceptable(new HttpMediaTypeNotAcceptableException(Arrays.asList(supportedMediaTypeArray)),
                null, HttpStatus.UNSUPPORTED_MEDIA_TYPE, null);

        assertTrue(respEntity.hasBody());
        assertTrue(respEntity.getBody() instanceof RestErrors);

        RestErrors restErrors = (RestErrors) respEntity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        log.info("response : {}", objectMapper.writeValueAsString(restErrors));

    }

    @Test
    public void handleMissingPathVariable() throws JsonProcessingException {
        ResponseEntity<Object> respEntity = handler.handleMissingPathVariable(new MissingPathVariableException("test", new MethodParameter(RestApplicationError.class.getDeclaredConstructors()[0],-1)),
                null, HttpStatus.INTERNAL_SERVER_ERROR, null);

        assertTrue(respEntity.hasBody());
        assertTrue(respEntity.getBody() instanceof RestErrors);

        RestErrors restErrors = (RestErrors) respEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("response : {}", objectMapper.writeValueAsString(restErrors));
    }

    @Test
    public void handleMissingServletRequestParameter() throws JsonProcessingException {
        ResponseEntity<Object> respEntity = handler.handleMissingServletRequestParameter(new MissingServletRequestParameterException("test", "string"),
                null, HttpStatus.INTERNAL_SERVER_ERROR, null);

        assertTrue(respEntity.hasBody());
        assertTrue(respEntity.getBody() instanceof RestErrors);

        RestErrors restErrors = (RestErrors) respEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("response : {}", objectMapper.writeValueAsString(restErrors));
    }

    @Test
    public void handleServletRequestBindingException() throws JsonProcessingException {
        ResponseEntity<Object> respEntity = handler.handleServletRequestBindingException(new ServletRequestBindingException("servlet request binding error"),
                null, HttpStatus.INTERNAL_SERVER_ERROR, null);

        assertTrue(respEntity.hasBody());
        assertTrue(respEntity.getBody() instanceof RestErrors);

        RestErrors restErrors = (RestErrors) respEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("response : {}", objectMapper.writeValueAsString(restErrors));
    }

    @Test
    public void handleConversionNotSupported() throws JsonProcessingException {
        ResponseEntity<Object> respEntity = handler.handleConversionNotSupported(new ConversionNotSupportedException("hello", Integer.class, new NumberFormatException()),
                null, HttpStatus.INTERNAL_SERVER_ERROR, null);

        assertTrue(respEntity.hasBody());
        assertTrue(respEntity.getBody() instanceof RestErrors);

        RestErrors restErrors = (RestErrors) respEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("response : {}", objectMapper.writeValueAsString(restErrors));
    }

    @Test
    public void handleTypeMismatch() throws JsonProcessingException {
        ResponseEntity<Object> respEntity = handler.handleTypeMismatch(new TypeMismatchException("hello", Integer.class, new NumberFormatException()),
                null, HttpStatus.INTERNAL_SERVER_ERROR, null);

        assertTrue(respEntity.hasBody());
        assertTrue(respEntity.getBody() instanceof RestErrors);

        RestErrors restErrors = (RestErrors) respEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("response : {}", objectMapper.writeValueAsString(restErrors));
    }

    @Test
    public void handleHttpMessageNotReadable() throws JsonProcessingException {
        ResponseEntity<Object> respEntity = handler.handleHttpMessageNotReadable(new HttpMessageNotReadableException("Message not readable"),
                null, HttpStatus.INTERNAL_SERVER_ERROR, null);

        assertTrue(respEntity.hasBody());
        assertTrue(respEntity.getBody() instanceof RestErrors);

        RestErrors restErrors = (RestErrors) respEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("response : {}", objectMapper.writeValueAsString(restErrors));
    }

    @Test
    public void handleHttpMessageNotWritable() throws JsonProcessingException {
        ResponseEntity<Object> respEntity = handler.handleHttpMessageNotWritable(new HttpMessageNotWritableException("Message not writable"),
                null, HttpStatus.INTERNAL_SERVER_ERROR, null);

        assertTrue(respEntity.hasBody());
        assertTrue(respEntity.getBody() instanceof RestErrors);

        RestErrors restErrors = (RestErrors) respEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("response : {}", objectMapper.writeValueAsString(restErrors));
    }

    @Test
    public void handleMethodArgumentNotValid() throws JsonProcessingException {
        ResponseEntity<Object> respEntity = handler.handleMethodArgumentNotValid(new MethodArgumentNotValidException(new MethodParameter(RestApplicationError.class.getDeclaredConstructors()[0],-1),
                        new BindException("target", "objectName")),
                null, HttpStatus.INTERNAL_SERVER_ERROR, null);

        assertTrue(respEntity.hasBody());
        assertTrue(respEntity.getBody() instanceof RestErrors);

        RestErrors restErrors = (RestErrors) respEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("response : {}", objectMapper.writeValueAsString(restErrors));
    }

    @Test
    public void handleMissingServletRequestPart() throws JsonProcessingException {
        ResponseEntity<Object> respEntity = handler.handleMissingServletRequestPart(new MissingServletRequestPartException("test"),
                null, HttpStatus.INTERNAL_SERVER_ERROR, null);

        assertTrue(respEntity.hasBody());
        assertTrue(respEntity.getBody() instanceof RestErrors);

        RestErrors restErrors = (RestErrors) respEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("response : {}", objectMapper.writeValueAsString(restErrors));
    }

    @Test
    public void handleBindException() throws JsonProcessingException {
        ResponseEntity<Object> respEntity = handler.handleBindException(new BindException("target", "objectName"),
                null, HttpStatus.INTERNAL_SERVER_ERROR, null);

        assertTrue(respEntity.hasBody());
        assertTrue(respEntity.getBody() instanceof RestErrors);

        RestErrors restErrors = (RestErrors) respEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("response : {}", objectMapper.writeValueAsString(restErrors));
    }

    @Test
    public void handleNoHandlerFoundException() throws JsonProcessingException {
        ResponseEntity<Object> respEntity = handler.handleNoHandlerFoundException(new NoHandlerFoundException("GET", "restconf/bogus", null),
                null, HttpStatus.INTERNAL_SERVER_ERROR, null);

        assertTrue(respEntity.hasBody());
        assertTrue(respEntity.getBody() instanceof RestErrors);

        RestErrors restErrors = (RestErrors) respEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("response : {}", objectMapper.writeValueAsString(restErrors));
    }

    @Test
    public void handleAsyncRequestTimeoutException() throws JsonProcessingException {
        ResponseEntity<Object> respEntity = handler.handleAsyncRequestTimeoutException(new AsyncRequestTimeoutException(),
                null, HttpStatus.INTERNAL_SERVER_ERROR, null);

        assertTrue(respEntity.hasBody());
        assertTrue(respEntity.getBody() instanceof RestErrors);

        RestErrors restErrors = (RestErrors) respEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("response : {}", objectMapper.writeValueAsString(restErrors));
    }

    @Test
    public void handleExceptionInternal() throws JsonProcessingException {
        ResponseEntity<Object> respEntity = handler.handleExceptionInternal(new NullPointerException(), null,
                null, HttpStatus.INTERNAL_SERVER_ERROR, null);

        assertTrue(respEntity.hasBody());
        assertTrue(respEntity.getBody() instanceof RestErrors);

        RestErrors restErrors = (RestErrors) respEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("response : {}", objectMapper.writeValueAsString(restErrors));
    }

    @Test
    public void handleRestException() throws JsonProcessingException {

        ResponseEntity<Object> respEntity = handler.handleRestException(new RestApplicationException("no-data-found", "No data found", HttpStatus.NOT_FOUND.value()), null);
        assertTrue(respEntity.hasBody());
        assertTrue(respEntity.getBody() instanceof RestErrors);

        RestErrors restErrors = (RestErrors) respEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("response : {}", objectMapper.writeValueAsString(restErrors));
    }

}