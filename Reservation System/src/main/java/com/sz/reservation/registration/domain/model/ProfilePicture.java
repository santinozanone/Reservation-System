package com.sz.reservation.registration.domain.model;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;


public class ProfilePicture {
    @NotNull(message = "file must not be null")
    private MultipartFile file;
    private Dimension photoDimensions;
    public ProfilePicture(MultipartFile file,Dimension photoDimensions ) {
        this.file = file;
        this.photoDimensions = photoDimensions;
    }
    public MultipartFile getFile() {
        return file;
    }

    public Dimension getPhotoDimensions() {
        return photoDimensions;
    }
}
