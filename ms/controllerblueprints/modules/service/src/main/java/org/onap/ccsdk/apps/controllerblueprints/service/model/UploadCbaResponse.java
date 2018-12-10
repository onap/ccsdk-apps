package org.onap.ccsdk.apps.controllerblueprints.service.model;

public class UploadCbaResponse extends ModelResponse{
    private Long cbaID;
    private String cbaDescription;

    public UploadCbaResponse(Long cbaID, String cbaDescription) {
        super((short) 1, 200, "Success!");
        this.cbaID = cbaID;
        this.cbaDescription = cbaDescription;
    }

    public UploadCbaResponse(short status, int code, String message) {
        super(status, code, message);
    }

    public Long getCbaID() {
        return cbaID;
    }

    public void setCbaID(Long cbaID) {
        this.cbaID = cbaID;
    }

    public String getCbaDescription() {
        return cbaDescription;
    }

    public void setCbaDescription(String cbaDescription) {
        this.cbaDescription = cbaDescription;
    }
}