package com.sz.reservation.registration.infrastructure.service;

import com.sz.reservation.registration.domain.service.MultipartImageResizingService;
import com.sz.reservation.registration.infrastructure.exception.FileReadingException;
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
public class AwtMultipartImageResizingService implements MultipartImageResizingService {
    private Logger logger = LogManager.getLogger(AwtMultipartImageResizingService.class);

    private int WIDTH;


    private int HEIGHT;

    public AwtMultipartImageResizingService(@Value("${pfp.width}") int WIDTH,  @Value("${pfp.height}") int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }

    @Override
    public Image resizeImage(MultipartFile imageToResize) {
        String originalImageName = imageToResize.getOriginalFilename();
        if (imageToResize == null) throw new RuntimeException("IOException, failed to read profile picture input stream " + originalImageName);
        logger.info("resizing image {} , to {} X {}",originalImageName,WIDTH,HEIGHT);
        BufferedImage bufferedProfilePicture = null;
        try (InputStream stream = imageToResize.getInputStream()){
            bufferedProfilePicture = ImageIO.read(stream);
        } catch (IOException e) {
            logger.error("failed to read profile picture input stream {}",originalImageName);
            throw new FileReadingException("IOException, failed to read profile picture input stream " + originalImageName, e);
        }
        if (bufferedProfilePicture == null){
            throw new FileReadingException("Failed to read profile picture with name, " + imageToResize.getOriginalFilename() +" No ImageReaders found for type "+imageToResize.getContentType());
        }
        logger.info("successful resizing for image {}",originalImageName);
        Image resized = bufferedProfilePicture.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
        bufferedProfilePicture.flush();
        bufferedProfilePicture = null;
        return resized;
    }
}
