package org.onap.ccsdk.apps.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;


@JsonRootName(value="error")
public class RestRpcError extends RestError {


    public RestRpcError() {
        this.errorType = "rpc";
    }

    public RestRpcError(String errorTag, String errorMessage) {
        this.errorType = "rpc";
        this.errorTag = errorTag;
        this.errorMessage = errorMessage;
        this.errorInfo = errorMessage;
    }

    public RestRpcError(String errorTag, String errorMessage, Throwable t) {
        this.errorType = "rpc";
        this.errorTag = errorTag;
        this.errorMessage = errorMessage;
        this.errorInfo = t.getLocalizedMessage();
    }


}
