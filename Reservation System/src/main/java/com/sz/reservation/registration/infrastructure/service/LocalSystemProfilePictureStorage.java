package com.sz.reservation.registration.infrastructure.service;

import com.sz.reservation.registration.domain.model.ProfilePicture;
import com.sz.reservation.registration.domain.port.outbound.ProfilePictureStorage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LocalSystemProfilePictureStorage implements ProfilePictureStorage {
    @Override
    public void store(ProfilePicture profilePicture) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(profilePicture.getPhotoDimensions().width, profilePicture.getPhotoDimensions().height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(profilePicture.getProfileImage(),0,0,null);
        g2.dispose();

        String fullPath = "C:\\Users\\Pictures".concat("\\").concat(profilePicture.getFileName());
        File outputImage = new File(fullPath);
        ImageIO.write(bufferedImage, fullPath, outputImage);
    }
}
