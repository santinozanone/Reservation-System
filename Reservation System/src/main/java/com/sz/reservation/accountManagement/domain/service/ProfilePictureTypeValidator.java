package com.sz.reservation.accountManagement.domain.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface ProfilePictureTypeValidator {
    public boolean isValid(String profilePictureOriginalName,InputStream inputStream);
}
