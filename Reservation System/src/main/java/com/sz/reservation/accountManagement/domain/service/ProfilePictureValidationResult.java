package com.sz.reservation.accountManagement.domain.service;

public class ProfilePictureValidationResult {
    private String message;
    private boolean succeeds;

    public ProfilePictureValidationResult(boolean succeeds,String message) {
        this.succeeds = succeeds;
        this.message = message;
    }

    public boolean isSuccessful() {
        return succeeds;
    }

    public String getMessage() {
        return message;
    }

}
