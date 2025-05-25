package com.sz.reservation.accountManagement.infrastructure;

import com.sz.reservation.globalConfiguration.exception.DirectoryCreationException;
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

    private Logger logger = LogManager.getLogger(StartupApplicationListener.class);
    public StartupApplicationListener(@Value("${account.localpfpstorage.location}")String profilePictureDirectory) {
        this.profilePictureDirectory = profilePictureDirectory;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
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



}
