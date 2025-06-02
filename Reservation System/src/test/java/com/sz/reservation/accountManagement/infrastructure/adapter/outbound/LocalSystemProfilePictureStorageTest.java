package com.sz.reservation.accountManagement.infrastructure.adapter.outbound;

import com.sz.reservation.globalConfiguration.exception.FileWritingException;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@DisplayName("Testing LocalSystemProfilePictureStorage ")
class LocalSystemProfilePictureStorageTest {
    private static String filename ;
    private static LocalSystemProfilePictureStorage profilePictureStorage;

    private static String localPfpDirectory = System.getenv("RS_LOCAL_PFP_STORAGE_LOCATION_TEST");
    private static int WIDTH = 50;
    private static int HEIGHT = 50;

    @BeforeAll
    public static void instantiatingStorage() throws IOException {
        profilePictureStorage = new LocalSystemProfilePictureStorage(localPfpDirectory,WIDTH,HEIGHT);
        Path storage = Path.of(localPfpDirectory);
        if ( !Files.exists(storage)) {
            Files.createDirectories(storage);
        }
    }

    @AfterAll
    public static void deleteStorage() throws IOException {
        Path storage = Path.of(localPfpDirectory);
        if (Files.exists(storage)) {
            Files.delete(storage);
        }
    }



    @BeforeEach
    public void instantiatingFilename(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss");
        String timestamp = dateFormat.format(new Date());
        String randomUUID = UUID.randomUUID().toString();
        filename = localPfpDirectory.concat("pfp_").concat(timestamp).concat("-").concat(randomUUID).concat(".jpg");
    }

    private void deleteFilename() throws IOException {
        //arrange and act
        boolean isDeleted = Files.deleteIfExists(Path.of(filename));

        //assert
        Assertions.assertTrue(isDeleted);
    }



    @Test
    public void Should_StoreFileCorrectly_When_ValidFile() throws IOException {
        //Arrange
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, 0);

        //act
        profilePictureStorage.store(image,filename);

        //assert
        Assertions.assertTrue(Files.exists(Path.of(filename)));

        deleteFilename();
    }

    @Test
    public void Should_ThrowFileWritingException_When_FileWriteFails() {
        String invalidPath = "D:\\NotExistent\\pfp_test.jpg";
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, 0);

        Assertions.assertThrows(FileWritingException.class, () -> profilePictureStorage.store(image, invalidPath));
    }
    @Test
    public void Should_ThrowIllegalArgumentException_When_StoringFileIsNull() {
        //Arrange
        Image profilePicture = null;
        String path = null;

        //Act and Assert
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            profilePictureStorage.store(profilePicture,path);
        });
    }


    @Test
    public void Should_DeleteFileCorrectly_When_ValidFile() throws IOException {
        //arrange
        Path path = Files.createTempFile("tempFile", ".log");

        //act
        profilePictureStorage.delete(path.toString());

        //assert
        boolean exists = Files.exists(path);
        Assertions.assertFalse(exists);

    }

    @Test
    public void Should_ThrowIllegalArgumentException_When_DeletingEmptyFilenamePath(){
        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            profilePictureStorage.delete("");
        });
    }

    @Test
    public void Should_ThrowInvalidPathException_When_DeletingInvalidFilenamePath(){
        Assertions.assertThrows(InvalidPathException.class,() -> {
            profilePictureStorage.delete("/invalidpath:))))");
        });
    }




}