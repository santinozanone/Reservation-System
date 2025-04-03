package com.sz.reservation.accountManagement.application.service;

import com.sz.reservation.globalConfiguration.exception.MediaNotSupportedException;
import com.sz.reservation.accountManagement.domain.port.outbound.ProfilePictureStorage;
import com.sz.reservation.accountManagement.domain.service.MultipartImageResizingService;
import com.sz.reservation.accountManagement.domain.service.ProfilePictureTypeValidator;
import com.sz.reservation.accountManagement.infrastructure.dto.AccountCreationRequest;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ProfilePictureService {
    private ProfilePictureStorage profilePictureStorage;
    private ProfilePictureTypeValidator profilePictureTypeValidator;
    private MultipartImageResizingService multipartImageResizingService;

    public ProfilePictureService(ProfilePictureStorage profilePictureStorage, ProfilePictureTypeValidator profilePictureTypeValidator, MultipartImageResizingService multipartImageResizingService) {
        this.profilePictureStorage = profilePictureStorage;
        this.profilePictureTypeValidator = profilePictureTypeValidator;
        this.multipartImageResizingService = multipartImageResizingService;
    }

    public void validate(AccountCreationRequest accountCreationRequest){
        MultipartFile profileImageMultipart = accountCreationRequest.getProfilePicture();
        if (!profilePictureTypeValidator.isValid(profileImageMultipart)) {
            throw new MediaNotSupportedException("FAILED profile picture validation for email "+accountCreationRequest.getEmail() +" , profile picture extension/content is not valid");
        }
    }

    public Image resize(MultipartFile profileImageMultipart){
        return multipartImageResizingService.resizeImage(profileImageMultipart);
    }

    public String store(String originalImageFilename,Image resizedImage){
        String profilePictureStoringPath = generateProfilePicturePathName(originalImageFilename);
        profilePictureStorage.store(resizedImage,profilePictureStoringPath);
        return profilePictureStoringPath;
    }

    public void delete(String profilePicturePath){
        profilePictureStorage.delete(profilePicturePath);
    }

    private String generateProfilePicturePathName(String originalFileName) {
        //get file extension
        int index = originalFileName.lastIndexOf(".");
        String fileExtension = originalFileName.substring(index + 1);

        //Generate and format timestamp
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String timestamp = dateFormatter.format(LocalDate.now());
        //Generate UUID
        String randomUUID = UUID.randomUUID().toString();

        //generate filename
        String storingDirectory = profilePictureStorage.getStoringDirectory();
        String filename = storingDirectory.concat("pfp_").concat(timestamp).concat("-").concat(randomUUID).concat(".").concat(fileExtension);
        return filename;
    }
}
