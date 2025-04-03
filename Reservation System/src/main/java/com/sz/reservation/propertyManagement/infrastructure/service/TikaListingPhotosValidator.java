package com.sz.reservation.propertyManagement.infrastructure.service;

import com.google.common.io.Files;
import com.sz.reservation.accountManagement.infrastructure.exception.FileReadingException;
import com.sz.reservation.util.FileTypeValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class TikaListingPhotosValidator implements ListingPhotosValidator{
    private Logger logger = LogManager.getLogger(TikaListingPhotosValidator.class);

    private FileTypeValidator fileTypeValidator;
    private final String TYPE_JPEG = "jpeg";
    private final String TYPE_PNG = "png";
    private final String TYPE_JPG = "jpg";

    public TikaListingPhotosValidator(FileTypeValidator fileTypeValidator) {
        this.fileTypeValidator = fileTypeValidator;
    }

    @Override
    public boolean isPhotoListValid(List<MultipartFile> photoList) {
        if (photoList.isEmpty()) return false;
        for (MultipartFile listingPhoto: photoList){
            if (listingPhoto == null) throw new IllegalArgumentException("listing photo cannot be null");
            MediaType mediaType = null;
            String listingPhotoOriginalName = listingPhoto.getOriginalFilename();
            String fileExtension = getExtension(listingPhotoOriginalName);
            if (listingPhoto.isEmpty()) {
                logger.info("listing photo validation failed: listing photo is empty");
                return false;
            }
            if (!isListingPhotoExtensionValid(fileExtension)){
                logger.info("listing photo validation failed: listing photo extension is not valid, extension = {}",fileExtension);
                return false;
            }
            try {
                mediaType = fileTypeValidator.getRealFileType(listingPhoto.getInputStream());
                logger.debug("mediatype:{} ", mediaType);
            } catch (IOException e) {
                logger.error("IOException when trying to get input stream from listing photo: {}",listingPhotoOriginalName);
                throw new FileReadingException("IOException, failed to obtain input stream from listing photo file: "+listingPhotoOriginalName,e);
            }
            boolean isValid = mediaType.getSubtype().equals(fileExtension); //check if file extension matches with the mediaType returned from the file validator
            if (!isValid) return false;
        }
        return true;
    }
    

    private boolean isListingPhotoExtensionValid(String fileExtension) {
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
