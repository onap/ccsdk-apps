package org.onap.ccsdk.apps.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;


@JsonRootName(value="error")
public class RestProtocolError extends RestError {


    public RestProtocolError() {
        this.errorType = "protocol";
    }

    public RestProtocolError(String errorTag, String errorMessage) {
        this.errorType = "protocol";
        this.errorTag = errorTag;
        this.errorMessage = errorMessage;
        this.errorInfo = errorMessage;
    }

    public RestProtocolError(String errorTag, String errorMessage, Throwable t) {
        this.errorType = "protocol";
        this.errorTag = errorTag;
        this.errorMessage = errorMessage;
        this.errorInfo = t.getLocalizedMessage();
    }

}
