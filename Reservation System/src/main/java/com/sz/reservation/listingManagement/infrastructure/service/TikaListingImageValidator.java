package com.sz.reservation.listingManagement.infrastructure.service;

import com.google.common.io.Files;
import com.sz.reservation.listingManagement.application.useCase.listing.ListingImageValidator;
import com.sz.reservation.util.FileTypeValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;

@Service
public class TikaListingImageValidator implements ListingImageValidator {
    private final String TYPE_JPEG = "jpeg";
    private final String TYPE_PNG = "png";
    private final String TYPE_JPG = "jpg";
    private Logger logger = LogManager.getLogger(TikaListingImageValidator.class);
    private FileTypeValidator fileTypeValidator;

    public TikaListingImageValidator(FileTypeValidator fileTypeValidator) {
        this.fileTypeValidator = fileTypeValidator;
    }

    @Override
    public boolean isListingImageValid(String originalFileName, BufferedInputStream ImageBufferedStream) {
        MediaType mediaType = null;
        String fileExtension = getExtension(originalFileName);

        if (!isListingPhotoExtensionValid(fileExtension)) {
            logger.info("listing photo validation failed: listing photo extension is not valid, extension = {}", fileExtension);
            return false;
        }
        mediaType = fileTypeValidator.getRealFileType(ImageBufferedStream);
        logger.debug("mediatype:{} ", mediaType);
        boolean isValid = mediaType.getSubtype().equals(fileExtension); //check if file extension matches with the mediaType returned from the file validator
        if (!isValid) return false;
        return true;
    }


    private boolean isListingPhotoExtensionValid(String fileExtension) {
        if (fileExtension.equals(TYPE_PNG) || fileExtension.equals(TYPE_JPEG)) {
            return true;
        }
        return false;
    }

    private String getExtension(String filename) {
        String extension = Files.getFileExtension(filename);
        if (extension.equals(TYPE_JPG))
            return TYPE_JPEG; // apache tika detects jpg as jpeg, so if an extension is jpg we need it to be jpeg for the later comparison
        return extension;
    }
}
