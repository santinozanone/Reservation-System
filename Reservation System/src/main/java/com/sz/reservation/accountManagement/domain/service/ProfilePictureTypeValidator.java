package com.sz.reservation.accountManagement.domain.service;

import org.springframework.web.multipart.MultipartFile;

public interface ProfilePictureTypeValidator {
    public boolean isValid(MultipartFile profilePicture);
}
