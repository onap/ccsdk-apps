package org.onap.ccsdk.apps.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;


@JsonRootName(value="error")
public class RestApplicationError extends RestError {


    public RestApplicationError() {
        this.errorType = "application";
    }

    public RestApplicationError(String errorTag, String errorMessage) {
        this.errorType = "application";
        this.errorTag = errorTag;
        this.errorMessage = errorMessage;
        this.errorInfo = errorMessage;
    }

    public RestApplicationError(String errorTag, String errorMessage, Throwable t) {
        this.errorType = "application";
        this.errorTag = errorTag;
        this.errorMessage = errorMessage;
        this.errorInfo = t.getLocalizedMessage();
    }


}
