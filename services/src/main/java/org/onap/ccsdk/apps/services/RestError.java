package org.onap.ccsdk.apps.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="error")
public abstract class RestError {

    protected String errorType;
    protected String errorTag;
    protected String errorPath;
    protected String errorMessage;
    protected String errorInfo;


    @JsonProperty("error-type")
    public String getErrorType() {
        return errorType;
    }

    @JsonProperty("error-tag")
    public String getErrorTag() {
        return errorTag;
    }

    public void setErrorTag(String errorTag) {
        this.errorTag = errorTag;
    }

    @JsonProperty("error-path")
    public String getErrorPath() {
        return errorPath;
    }

    public void setErrorPath(String errorPath) {
        this.errorPath = errorPath;
    }

    @JsonProperty("error-message")
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @JsonProperty("error-info")
    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

}
