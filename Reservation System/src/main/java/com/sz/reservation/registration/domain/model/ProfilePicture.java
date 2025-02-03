package com.sz.reservation.registration.domain.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;


public class ProfilePicture {

    private String fileName;

    private Image profileImage;

    private Dimension photoDimensions;

    public ProfilePicture(String fileName, Image profileImage, Dimension photoDimensions) {
        this.fileName = fileName;
        this.profileImage = profileImage;
        this.photoDimensions = photoDimensions; 
    }



    public String getFileName() {
        return fileName;
    }

    public Image getProfileImage() {
        return profileImage;
    }

    public Dimension getPhotoDimensions() {
        return photoDimensions;
    }
}
