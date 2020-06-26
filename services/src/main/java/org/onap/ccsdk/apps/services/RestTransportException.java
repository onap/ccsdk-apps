package org.onap.ccsdk.apps.services;

public class RestTransportException extends RestException {

    public RestTransportException(String errorTag, String errorMessage, int status) {
        this.restError = new RestTransportError(errorTag, errorMessage);
        this.status = status;
    }
}
