package com.sz.reservation.registration.infrastructure.service;

import com.sz.reservation.configuration.RootConfig;
import com.sz.reservation.registration.domain.model.ProfilePicture;
import com.sz.reservation.registration.infrastructure.adapter.outbound.LocalSystemProfilePictureStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@DisplayName("Local System Storage of Profile Picture images test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RootConfig.class})
@WebAppConfiguration
@Disabled
class LocalSystemProfilePictureStorageTest {



    @Autowired
    LocalSystemProfilePictureStorage profilePictureStorage;
    @Test
    public void Should_StoreFileCorrectly_When_ValidFile() throws IOException {
        //Arrange
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss");
        String timestamp = dateFormat.format(new Date());
        String randomUUID = UUID.randomUUID().toString();
        String filename = "D:\\Reservation-System\\Profile-Picture\\".concat("pfp_").concat(timestamp).concat("-").concat(randomUUID).concat(".jpg");


        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, 0);


        profilePictureStorage.store(image,filename);
        Assertions.assertTrue(Files.exists(Path.of(filename)));
    }

    @Test
    public void Should_ThrowException_When_FileIsNull() {
        //Arrange
        Image profilePicture = null;
        String path = null;
        //Act and Assert
        Assertions.assertThrows(RuntimeException.class,()->{
            profilePictureStorage.store(profilePicture,path);
        });
    }

}