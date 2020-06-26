package org.onap.ccsdk.apps.services;

abstract public class RestException extends Exception {
    protected RestError restError;
    protected int status;

    public int getStatus() {
        return status;
    }

    public RestError getRestError() {
        return restError;
    }
}
