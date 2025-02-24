package com.sz.reservation.registration.infrastructure;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing startup application listener")
class StartupApplicationListenerTest {

    private final static String pfpDirectory = "C:\\Users\\losmelli\\Music\\directory";
    private final static String tempPfpDirectory = "C:\\Users\\losmelli\\Music\\tempDirectory";
    private static StartupApplicationListener startupApplicationListener;

    @BeforeAll
    private static void instantiatingListener(){
        startupApplicationListener = new StartupApplicationListener(pfpDirectory, tempPfpDirectory);
    }

    private void deleteDirectories() throws IOException {
        Files.deleteIfExists(Path.of(pfpDirectory));
        Files.deleteIfExists(Path.of(tempPfpDirectory));
    }

    @Test
    public void Should_CreateDirectories_When_Startup() throws IOException {
        //act
        startupApplicationListener.onApplicationEvent(null); //should create directories

        //assert
        boolean existsMainDirectory = Files.isDirectory(Path.of(pfpDirectory));
        boolean existsTempDirectory = Files.isDirectory(Path.of(tempPfpDirectory));

        assertTrue(existsMainDirectory);
        assertTrue(existsTempDirectory);

        deleteDirectories();
    }

}