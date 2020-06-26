package org.onap.ccsdk.apps.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;


@JsonRootName(value = "error")
public class RestTransportError extends RestError {


    public RestTransportError() {
        this.errorType = "transport";
    }

    public RestTransportError(String errorTag, String errorMessage) {
        this.errorType = "transport";
        this.errorTag = errorTag;
        this.errorMessage = errorMessage;
        this.errorInfo = errorMessage;
    }

    public RestTransportError(String errorTag, String errorMessage, Throwable t) {
        this.errorType = "transport";
        this.errorTag = errorTag;
        this.errorMessage = errorMessage;
        this.errorInfo = t.getLocalizedMessage();
    }

}
