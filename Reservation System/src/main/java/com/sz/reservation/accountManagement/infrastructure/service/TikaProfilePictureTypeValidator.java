package com.sz.reservation.accountManagement.infrastructure.service;

import com.google.common.io.Files;
import com.sz.reservation.accountManagement.domain.service.ProfilePictureValidationResult;
import com.sz.reservation.accountManagement.domain.service.ProfilePictureTypeValidator;
import com.sz.reservation.globalConfiguration.exception.FileWritingException;
import com.sz.reservation.util.FileTypeValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class TikaProfilePictureTypeValidator implements ProfilePictureTypeValidator {
    private Logger logger = LogManager.getLogger(TikaProfilePictureTypeValidator.class);

    private FileTypeValidator fileTypeValidator;
    private final String TYPE_JPEG = "jpeg";
    private final String TYPE_PNG = "png";
    private final String TYPE_JPG = "jpg";
    private final int MAX_PHOTO_SIZE = 15728641;

    public TikaProfilePictureTypeValidator(FileTypeValidator fileTypeValidator) {
        this.fileTypeValidator = fileTypeValidator;
    }

    public ProfilePictureValidationResult getValidationResult(String profilePictureOriginalName, InputStream profilePictureStream) {
        if (profilePictureStream == null) throw new IllegalArgumentException("Profile picture stream cannot be null");
        MediaType mediaType = null;

        String fileExtension = getExtension(profilePictureOriginalName);
        logger.info("Starting profile picture validation for profile picture: {}",profilePictureOriginalName);


        if (!isSizeValid(profilePictureStream)){
            logger.info("Profile validation FAILED: Size limit exceeded or empty picture");
            return new ProfilePictureValidationResult(false,"Profile validation FAILED: Size limit exceeded or empty picture");
        }

        if (!isProfilePictureExtensionValid(fileExtension)){
            logger.info("Profile validation FAILED: profile picture extension is not valid, extension = {}",fileExtension);
            return new ProfilePictureValidationResult(false,"Profile validation FAILED: file extension is not valid");
        }

        mediaType = fileTypeValidator.getRealFileType(profilePictureStream);
        logger.debug("mediatype:{} ", mediaType);

        boolean isValid = mediaType.getSubtype().equals(fileExtension); //check if file extension matches with the mediaType returned from the file validator
        if (isValid){
            logger.info("Successful profile picture validation for profile picture: {}",profilePictureOriginalName);
            return new ProfilePictureValidationResult(true,"Profile validation Succeeded");
        }
        return new ProfilePictureValidationResult(false,"Profile validation FAILED: media type not supported");

    }

    private boolean isSizeValid(InputStream profilePictureStream){
        byte [] data = new byte[16 * 1024]; //16 Kilobyte]
        int fileSize = 0;
        if (!profilePictureStream.markSupported()) {
            throw new IllegalArgumentException("The stream does not support the mark method");
        }
        profilePictureStream.mark(MAX_PHOTO_SIZE+1);
        try {
            int bytesRead = profilePictureStream.read(data);
            while (bytesRead != -1){
                fileSize += bytesRead;
                if (fileSize > MAX_PHOTO_SIZE){
                    return false;
                }
                bytesRead = profilePictureStream.read(data);
            }
            profilePictureStream.reset();
        }catch (IOException ioException){
            throw new FileWritingException("IOException when reading Profile Picture",ioException);
        }
        return fileSize > 0;
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
