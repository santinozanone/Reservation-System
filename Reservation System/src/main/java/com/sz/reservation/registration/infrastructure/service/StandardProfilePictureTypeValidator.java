package com.sz.reservation.registration.infrastructure.service;

import com.google.common.io.Files;
import com.sz.reservation.registration.domain.service.ProfilePictureTypeValidator;
import com.sz.reservation.registration.infrastructure.exception.FileReadingException;
import com.sz.reservation.util.FileTypeValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class StandardProfilePictureTypeValidator implements ProfilePictureTypeValidator {
    private Logger logger = LogManager.getLogger(StandardProfilePictureTypeValidator.class);

    private FileTypeValidator fileTypeValidator;
    private final String TYPE_JPEG = "jpeg";
    private final String TYPE_PNG = "png";
    private final String TYPE_JPG = "jpg";
    public StandardProfilePictureTypeValidator(FileTypeValidator fileTypeValidator) {
        this.fileTypeValidator = fileTypeValidator;
    }

    public boolean isValid(MultipartFile profilePicture) {
        if (profilePicture == null) throw new IllegalArgumentException("profile picture cannot be null");
        MediaType mediaType = null;
        String profilePictureOriginalName = profilePicture.getOriginalFilename();
        String fileExtension = getExtension(profilePictureOriginalName);
        logger.info("starting profile picture validation for profile picture: {}",profilePictureOriginalName);
        if (profilePicture.isEmpty()) {
            logger.info("profile picture validation failed: profile picture is empty");
            return false;
        }
        if (!isProfilePictureExtensionValid(fileExtension)){
            logger.info("profile picture validation failed: profile picture extension is not valid, extension = {}",fileExtension);
            return false;
        }
        try {
            mediaType = fileTypeValidator.getRealFileType(profilePicture.getInputStream());
            logger.debug("mediatype:{} ", mediaType);
        } catch (IOException e) {
            logger.error("IOException when trying to get input stream from profile picture: {}",profilePictureOriginalName);
            throw new FileReadingException("IOException, failed to obtain input stream from profile picture file: "+profilePictureOriginalName,e);
        }
        boolean isValid = mediaType.getSubtype().equals(fileExtension); //check if file extension matches with the mediaType returned from the file validator
        if (isValid) logger.info("successful profile picture validation for profile picture: {}",profilePictureOriginalName);
        return isValid;
    }

    private boolean isProfilePictureExtensionValid(String fileExtension) {
        if (fileExtension.equals(TYPE_PNG) || fileExtension.equals(TYPE_JPEG)) {
            return true;
        }
        return false;
    }

    private String getExtension(String filename){
        String extension = Files.getFileExtension(filename);
        if (extension.equals(TYPE_JPG)) return TYPE_JPEG; // apache tika detects jpg as jpeg, so if an extension is jpg we need it to be jpeg for the later comparison
        return extension;
    }
}
