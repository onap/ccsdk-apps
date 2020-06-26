package org.onap.ccsdk.apps.services;

public class RestApplicationException extends RestException {

    public RestApplicationException(String errorTag, String errorMessage, int status) {
        this.restError = new RestApplicationError(errorTag, errorMessage);
        this.status = status;
    }

    public RestApplicationException(String errorTag, String errorMessage, Throwable t, int status) {
        this.restError = new RestApplicationError(errorTag, errorMessage, t);
        this.status = status;
    }
}
