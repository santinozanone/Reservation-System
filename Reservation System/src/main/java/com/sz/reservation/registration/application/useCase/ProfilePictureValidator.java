package com.sz.reservation.registration.application.useCase;

import org.springframework.web.multipart.MultipartFile;

public interface ProfilePictureValidator {
    public boolean isValid(MultipartFile profilePicture);
}
