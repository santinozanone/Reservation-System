package com.sz.reservation.accountManagement.infrastructure;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing startup application listener")
class StartupApplicationListenerTest {

    private final static String pfpDirectory = "C:\\Users\\losmelli\\Music\\directory";
    private static StartupApplicationListener startupApplicationListener;

    @BeforeAll
    private static void instantiatingListener(){
        startupApplicationListener = new StartupApplicationListener(pfpDirectory);
    }

    private void deleteDirectories() throws IOException {
        Files.deleteIfExists(Path.of(pfpDirectory));
    }

    @Test
    public void Should_CreateDirectories_When_Startup() throws IOException {
        //act
        startupApplicationListener.onApplicationEvent(null); //should create directories

        //assert
        boolean existsMainDirectory = Files.isDirectory(Path.of(pfpDirectory));
        assertTrue(existsMainDirectory);

        deleteDirectories();
    }

}