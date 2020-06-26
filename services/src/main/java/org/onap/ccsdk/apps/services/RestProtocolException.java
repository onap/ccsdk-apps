package org.onap.ccsdk.apps.services;

public class RestProtocolException extends RestException {

    public RestProtocolException(String errorTag, String errorMessage, int status) {
        this.restError = new RestProtocolError(errorTag, errorMessage);
        this.status = status;
    }

    public RestProtocolException(String errorTag, String errorMessage, Throwable t, int status) {
        this.restError = new RestProtocolError(errorTag, errorMessage, t);
        this.status = status;
    }
}
