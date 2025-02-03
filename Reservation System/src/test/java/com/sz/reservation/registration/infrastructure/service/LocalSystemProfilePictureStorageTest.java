package com.sz.reservation.registration.infrastructure.service;

import com.sz.reservation.registration.domain.model.ProfilePicture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Local System Storage of Profile Picture images test")
class LocalSystemProfilePictureStorageTest {

    @Test
    public void Should_StoreFileCorrectly_When_ValidFile() throws IOException {
        //Arrange
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss");
        String timestamp = dateFormat.format(new Date());
        String filename = "pfp_".concat(timestamp).concat(".jpg");

        Dimension dimension = new Dimension(50,50);

        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, 0);


        ProfilePicture profilePicture = new ProfilePicture(filename,image,dimension);

        LocalSystemProfilePictureStorage profilePictureStorage = new LocalSystemProfilePictureStorage();
        profilePictureStorage.store(profilePicture);

        Assertions.assertTrue(Files.exists(Path.of(filename)));
    }

}