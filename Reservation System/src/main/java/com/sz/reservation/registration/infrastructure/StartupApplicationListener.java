package com.sz.reservation.registration.infrastructure;

import com.sz.reservation.registration.infrastructure.exception.DirectoryCreationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class StartupApplicationListener  implements
        ApplicationListener<ContextRefreshedEvent> {

    private String profilePictureDirectory;
    private String tempDirectory;

    private Logger logger = LogManager.getLogger(StartupApplicationListener.class);
    public StartupApplicationListener(@Value("${localpfpstorage.location}")String profilePictureDirectory, @Value(("${tempStorage.location}")) String tempDirectory) {
        this.profilePictureDirectory = profilePictureDirectory;
        this.tempDirectory = tempDirectory;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createProfilePicTempDirectoryIfNotExists();
        createProfilePictureDirectoryIfNotExists();
    }

    private void createProfilePictureDirectoryIfNotExists()  {
        Path directoryPath = Path.of(profilePictureDirectory);
        if (!Files.exists(directoryPath)){
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                logger.error("failed to create profile picture directory: {}",profilePictureDirectory,e);
                throw new DirectoryCreationException("failed to create directory " + directoryPath,e);
            }
        }
    }

    private void createProfilePicTempDirectoryIfNotExists(){
        try {
            Files.createDirectories(Path.of(tempDirectory));
        }
        catch (IOException e) {
            logger.error("failed to create TEMP profile picture directory: {}",tempDirectory,e);
            throw new DirectoryCreationException("failed to create temp directory " +tempDirectory, e);
        }
    }

}
