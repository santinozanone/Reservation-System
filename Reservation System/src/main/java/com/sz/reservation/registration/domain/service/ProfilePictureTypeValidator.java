package com.sz.reservation.registration.domain.service;

import org.springframework.web.multipart.MultipartFile;

public interface ProfilePictureTypeValidator {
    public boolean isValid(MultipartFile profilePicture);
}
