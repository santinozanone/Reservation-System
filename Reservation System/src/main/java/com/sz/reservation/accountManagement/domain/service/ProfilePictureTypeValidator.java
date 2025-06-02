package com.sz.reservation.accountManagement.domain.service;

import java.io.InputStream;

public interface ProfilePictureTypeValidator {
    public ProfilePictureValidationResult getValidationResult(String profilePictureOriginalName, InputStream inputStream);
}
