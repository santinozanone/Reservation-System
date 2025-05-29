package com.sz.reservation.accountManagement.infrastructure.service;

import com.sz.reservation.accountManagement.domain.service.MultipartImageResizingService;
import com.sz.reservation.globalConfiguration.exception.FileReadingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Component
public class ScaledInstanceMultipartImageResizingService implements MultipartImageResizingService {
    private Logger logger = LogManager.getLogger(ScaledInstanceMultipartImageResizingService.class);

    private int WIDTH;


    private int HEIGHT;

    public ScaledInstanceMultipartImageResizingService(@Value("${account.pfp.width}") int WIDTH, @Value("${account.pfp.height}") int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }

    @Override
    public Image resizeImage(String profilePictureOriginalName,InputStream profilePictureToResize) {
        if (profilePictureToResize == null) throw new RuntimeException("IOException, failed to read profile picture input stream " + profilePictureOriginalName);
        logger.info("resizing image {} , to {} X {}",profilePictureOriginalName,WIDTH,HEIGHT);

        BufferedImage bufferedProfilePicture = null;
        try (InputStream stream = profilePictureToResize){
            bufferedProfilePicture = ImageIO.read(stream);
        } catch (IOException e) {
            logger.error("failed to read profile picture input stream {}",profilePictureOriginalName);
            throw new FileReadingException("IOException, failed to read profile picture input stream " + profilePictureOriginalName, e);
        }
        if (bufferedProfilePicture == null){
            throw new FileReadingException("Failed to read profile picture with name, " + profilePictureOriginalName);
        }
        logger.info("successful resizing for image {}",profilePictureOriginalName);
        Image resized = bufferedProfilePicture.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
        bufferedProfilePicture.flush();
        return resized;
    }
}
