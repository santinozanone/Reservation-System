package com.sz.reservation.registration.infrastructure.service;

import com.google.common.io.Files;
import com.sz.reservation.registration.application.useCase.ProfilePictureValidator;
import com.sz.reservation.util.FileTypeValidator;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class StandardProfilePictureValidator implements ProfilePictureValidator {
    private FileTypeValidator fileTypeValidator;

    public StandardProfilePictureValidator(FileTypeValidator fileTypeValidator) {
        this.fileTypeValidator = fileTypeValidator;
    }

    public boolean isValid(MultipartFile profilePicture) {
        MediaType mediaType = null;
        try {
            if (profilePicture.isEmpty()) return false;
            if (!isProfilePictureExtensionValid(profilePicture)) return false;
            mediaType = fileTypeValidator.getRealFileType(profilePicture.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return mediaType.equals(getExtension(profilePicture.getOriginalFilename()));
    }

    private boolean isProfilePictureExtensionValid(MultipartFile profilePicture) {
        //check if file extension matches with the mediaType returned from the file validator
        if (!getExtension(profilePicture.getOriginalFilename()).equals(MediaType.IMAGE_PNG_VALUE) ||
                !getExtension(profilePicture.getOriginalFilename()).equals(MediaType.IMAGE_JPEG_VALUE)) {
            return false;
        }
        return true;
    }

    private String getExtension(String filename){
        return Files.getFileExtension(filename);
    }
}
