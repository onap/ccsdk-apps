package org.onap.ccsdk.apps.controllerblueprints.service.model;

public class ModelResponse {
    private short status;
    private int code;
    private String message;

    public ModelResponse(short status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
