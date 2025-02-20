package com.sz.reservation.registration.infrastructure.service;

import com.sz.reservation.configuration.RootConfig;
import com.sz.reservation.registration.infrastructure.exception.FileReadingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Testing AwtMultipartImageResizingService")
class AwtMultipartImageResizingServiceTest {
    private static AwtMultipartImageResizingService resizingService;
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    @BeforeAll
    private static void instantiatingService(){
        resizingService = new AwtMultipartImageResizingService(WIDTH,HEIGHT);
    }




    @Test
    public void Should_ReturnResizedImage_When_PNGMultipartFile() throws IOException {
        //arrange
        String path = "src/test/resources/logo.png";
        byte[] imageLogo = Files.readAllBytes(Path.of(path));
        MockMultipartFile multipartFile = new MockMultipartFile("file","logo.png",MediaType.IMAGE_PNG_VALUE ,imageLogo);

        //act
        Image resizedImage = resizingService.resizeImage(multipartFile);

        //assert
        assertArrayEquals(new int[]{WIDTH,HEIGHT},new int[]{resizedImage.getWidth(null),resizedImage.getHeight(null)});
    }

    @Test
    public void Should_ReturnResizedImage_When_JPGMultipartFile() throws IOException {
        //arrange
        String path = "src/test/resources/bird.jpg";
        byte[] imageLogo = Files.readAllBytes(Path.of(path));
        MockMultipartFile multipartFile = new MockMultipartFile("file","bird.jpg",MediaType.IMAGE_JPEG_VALUE ,imageLogo);

        //act
        Image resizedImage = resizingService.resizeImage(multipartFile);

        //assert
        assertArrayEquals(new int[]{WIDTH,HEIGHT},new int[]{resizedImage.getWidth(null),resizedImage.getHeight(null)});
    }

    @Test
    public void Should_ThrowFileReadingException_When_FailedToReadMultipartFile(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("txtFile","photo.png",MediaType.TEXT_PLAIN_VALUE,"hello".getBytes());

        //act and assert
        assertThrows(FileReadingException.class,() -> {
            resizingService.resizeImage(multipartFile);
        });

    }

    @Test
    public void Should_ThrowRuntimeException_When_NullMultipartFile() throws IOException {
        assertThrows(RuntimeException.class,() -> {
            resizingService.resizeImage(null);
        });
    }


    @Test
    public void Should_ThrowFileReadingException_When_EmptyMultipartFile(){
        //arrange
        MockMultipartFile emptyFile = new MockMultipartFile("file", "empty.png", "image/png", new byte[0]);

        //act and assert
        assertThrows(FileReadingException.class,() -> {
            resizingService.resizeImage(emptyFile);
        });
    }


}