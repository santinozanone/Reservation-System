package com.sz.reservation.accountManagement.infrastructure.service;

import com.google.common.io.Files;
import com.sz.reservation.accountManagement.domain.service.ProfilePictureTypeValidator;
import com.sz.reservation.globalConfiguration.exception.FileReadingException;
import com.sz.reservation.util.FileTypeValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class TikaProfilePictureTypeValidator implements ProfilePictureTypeValidator {
    private Logger logger = LogManager.getLogger(TikaProfilePictureTypeValidator.class);

    private FileTypeValidator fileTypeValidator;
    private final String TYPE_JPEG = "jpeg";
    private final String TYPE_PNG = "png";
    private final String TYPE_JPG = "jpg";
    public TikaProfilePictureTypeValidator(FileTypeValidator fileTypeValidator) {
        this.fileTypeValidator = fileTypeValidator;
    }

    public boolean isValid(String profilePictureOriginalName, InputStream profilePictureStream) {
        if (profilePictureStream == null) throw new IllegalArgumentException("profile picture stream cannot be null");
        MediaType mediaType = null;

        String fileExtension = getExtension(profilePictureOriginalName);
        logger.info("starting profile picture validation for profile picture: {}",profilePictureOriginalName);



       /* if (profilePicture.isEmpty()) {
            logger.info("profile picture validation failed: profile picture is empty");
            return false;
        }*/

        if (!isProfilePictureExtensionValid(fileExtension)){
            logger.info("profile picture validation failed: profile picture extension is not valid, extension = {}",fileExtension);
            return false;
        }

        mediaType = fileTypeValidator.getRealFileType(new BufferedInputStream(profilePictureStream));
        logger.debug("mediatype:{} ", mediaType);

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
