package com.sz.reservation.registration.domain.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;


public class ProfilePicture {

    private String fileName;

    private MultipartFile file;

    private Dimension photoDimensions;

    public ProfilePicture(String fileName, MultipartFile file, Dimension photoDimensions) {
        this.fileName = fileName;
        this.file = file;
        this.photoDimensions = photoDimensions;
    }

    public String getFileName() {
        return fileName;
    }

    public MultipartFile getFile() {
        return file;
    }

    public Dimension getPhotoDimensions() {
        return photoDimensions;
    }
}
