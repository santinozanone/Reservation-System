package com.sz.reservation.accountManagement.domain.port.outbound;

import java.awt.*;

public interface ProfilePictureStorage {
    String getStoringDirectory();
    void store(Image profilePicture, String profilePicturePath) ;

    void delete(String profilePicturePath);
}
