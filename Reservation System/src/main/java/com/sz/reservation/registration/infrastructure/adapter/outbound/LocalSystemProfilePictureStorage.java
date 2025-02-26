package com.sz.reservation.registration.infrastructure.adapter.outbound;

import com.sz.reservation.registration.domain.model.ProfilePicture;
import com.sz.reservation.registration.domain.port.outbound.ProfilePictureStorage;
import com.sz.reservation.registration.infrastructure.exception.DirectoryCreationException;
import com.sz.reservation.registration.infrastructure.exception.FileDeletionException;
import com.sz.reservation.registration.infrastructure.exception.FileWritingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class LocalSystemProfilePictureStorage implements ProfilePictureStorage {

    private Logger logger = LogManager.getLogger(LocalSystemProfilePictureStorage.class);

    private String profilePictureDirectory;

    private int WIDTH;

    private int HEIGHT;

    public LocalSystemProfilePictureStorage(@Value("${localpfpstorage.location}") String profilePictureDirectory,
                                            @Value("${pfp.width}") int WIDTH,
                                            @Value("${pfp.height}")int HEIGHT) {
        this.profilePictureDirectory = profilePictureDirectory;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }

    @Override
    public String getStoringDirectory() {
        return profilePictureDirectory;
    }

    @Override
    public void store(Image profilePicture,String profilePicturePath)  {
        logger.debug("starting image storage to path: {}",profilePicturePath);
        if (profilePicture == null || profilePicturePath.isEmpty()){
            logger.debug("profile picture or path is null");
            throw new IllegalArgumentException("profile picture or path cannot be null");
        }
        BufferedImage bufferedImage = drawImage(profilePicture,WIDTH, HEIGHT);
        writeFile(profilePicturePath, bufferedImage);
    }

    @Override
    public void delete(String profilePicturePath) {
        logger.debug("deleting profile picture with path: {}",profilePicturePath);
        if (profilePicturePath == null || profilePicturePath.isEmpty()){
            logger.debug("profile picture path is null");
            throw new IllegalArgumentException("profile picture path cannot be null");
        }
        try {
            Files.deleteIfExists(Path.of(profilePicturePath));
        } catch (IOException e) {
            throw new FileDeletionException("Failed to delete profile picture with path: "+ profilePicturePath,e);
        }
        logger.info("profile picture deleted correctly");
    }


    private void writeFile(String profilePicturePath,BufferedImage bufferedImage) {
        logger.debug("starting write file to: {}",profilePicturePath);
        String fullPath = profilePicturePath;
        Path directory = Path.of(fullPath).getParent();
        if (! Files.exists(directory )) throw new FileWritingException("failed to write file, directory "+directory + " not found");
        File outputImage = new File(fullPath);
        int index = fullPath.lastIndexOf("."); // get index of file extension
        String fileExtension = fullPath.substring(index + 1); // get file extension
        try {
            ImageIO.write(bufferedImage, fileExtension, outputImage);
            bufferedImage.flush();
        } catch (IOException e) {
            throw new FileWritingException("Failed to write file " + fullPath, e);
        }
        logger.info("profile picture stored successfully at : {}",fullPath);
    }
    private BufferedImage drawImage(Image profileImage,int width, int height){
        logger.debug("starting drawing image with WIDTH:{}, HEIGHT:{}",WIDTH,HEIGHT);
        BufferedImage bufferedImage = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(profileImage,0,0,null);
        g2.dispose();
        logger.debug("successfully drew image with WIDTH:{}, HEIGHT:{}",WIDTH,HEIGHT);
        return bufferedImage;
    }

}
