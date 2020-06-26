package org.onap.ccsdk.apps.services;

public class RestRpcException extends RestException {

    public RestRpcException(String errorTag, String errorMessage, int status) {
        this.restError = new RestRpcError(errorTag, errorMessage);
        this.status = status;
    }

    public RestRpcException(String errorTag, String errorMessage, Throwable t, int status) {
        this.restError = new RestRpcError(errorTag, errorMessage, t);
        this.status = status;
    }
}
