package org.onap.ccsdk.apps.services;

public class RestRpcException extends RestException {

    RestRpcException(String errorTag, String errorMessage, int status) {
        this.restError = new RestRpcError(errorTag, errorMessage);
        this.status = status;
    }
}
