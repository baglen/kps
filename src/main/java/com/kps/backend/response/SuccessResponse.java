package com.kps.backend.response;

public class SuccessResponse {
    private Boolean success;

    public SuccessResponse(Boolean success) {
        this.success = success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }
}
