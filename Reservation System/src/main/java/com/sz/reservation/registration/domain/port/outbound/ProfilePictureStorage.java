package com.sz.reservation.registration.domain.port.outbound;

import com.sz.reservation.registration.domain.model.ProfilePicture;

import java.awt.*;
import java.io.IOException;

public interface ProfilePictureStorage {
    String getStoringDirectory();
    void store(Image profilePicture, String profilePicturePath) ;

    void delete(String profilePicturePath);
}
