package com.sz.reservation.accountManagement.infrastructure.service;

import com.sz.reservation.globalConfiguration.exception.FileReadingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Testing AwtMultipartImageResizingService")
class ScaledInstanceMultipartImageResizingServiceTest {
    private static ScaledInstanceMultipartImageResizingService resizingService;
    private static final int WIDTH = 192;
    private static final int HEIGHT = 192;

    @BeforeAll
    public static void instantiatingService(){
        resizingService = new ScaledInstanceMultipartImageResizingService(WIDTH,HEIGHT);
    }




    @Test
    public void Should_ReturnResizedImage_When_PNGMultipartFile() throws IOException {
        //arrange
        String path = "src/test/resources/logo.png";
        InputStream inputStream = new FileInputStream(path);

        //act
        Image resizedImage = resizingService.resizeImage("logo.png",inputStream);

        //assert
        assertArrayEquals(new int[]{WIDTH,HEIGHT},new int[]{resizedImage.getWidth(null),resizedImage.getHeight(null)});
    }

    @Test
    public void Should_ReturnResizedImage_When_JPGMultipartFile() throws IOException {
        //arrange
        String path = "src/test/resources/bird.jpg";
        InputStream inputStream = new FileInputStream(path);

        //act
        Image resizedImage = resizingService.resizeImage("bird.jpg",inputStream);

        //assert
        assertArrayEquals(new int[]{WIDTH,HEIGHT},new int[]{resizedImage.getWidth(null),resizedImage.getHeight(null)});
    }

    @Test
    public void Should_ThrowFileReadingException_When_FailedToReadMultipartFile() throws IOException {
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("txtFile","photo.png",MediaType.TEXT_PLAIN_VALUE,"hello".getBytes());
        InputStream inputStream = multipartFile.getInputStream();
        //act and assert
        assertThrows(FileReadingException.class,() -> {
            resizingService.resizeImage("photo.png",inputStream);
        });

    }

    @Test
    public void Should_ThrowRuntimeException_When_NullMultipartFile() {
        assertThrows(RuntimeException.class,() -> {
            resizingService.resizeImage("",null);
        });
    }


    @Test
    public void Should_ThrowFileReadingException_When_EmptyMultipartFile() throws IOException {
        //arrange
        MockMultipartFile emptyFile = new MockMultipartFile("file", "empty.png", "image/png", new byte[0]);
        InputStream inputStream = emptyFile.getInputStream();
        //act and assert
        assertThrows(FileReadingException.class,() -> {
            resizingService.resizeImage("",inputStream);
        });
    }




}