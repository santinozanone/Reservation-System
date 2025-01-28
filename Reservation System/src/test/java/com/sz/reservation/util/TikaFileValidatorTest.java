package com.sz.reservation.util;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@ExtendWith(MockitoExtension.class)
class TikaFileValidatorTest {
    private File outputFile;

    @BeforeEach
    public void createPngFile(){
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, 0);
        outputFile = new File("saved.png");
        try {
            Assertions.assertTrue(ImageIO.write(image, "png", outputFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void removePngFile(){
        Assertions.assertTrue(outputFile.delete());
    }

    @Test
    public void Should_ReturnRealMediaType_When_ValidInputStream() throws IOException {
        Path tempPath = Path.of(outputFile.getAbsolutePath());
        FileValidator fileValidator = new TikaFileValidator();
        MediaType type = fileValidator.getRealFileType(Files.newInputStream(tempPath, StandardOpenOption.READ));
        Assertions.assertEquals(MediaType.IMAGE_PNG, type);
    }

    @Test
    public void Should_ReturnRealMediaType_When_ExtensionOfFileIsChanged() throws IOException {

        File newFile = new File("saved2.jpeg");
        Assertions.assertTrue(outputFile.renameTo(newFile));
        Path tempPath = Path.of(newFile.getAbsolutePath());
        FileValidator fileValidator = new TikaFileValidator();
        MediaType type  = fileValidator.getRealFileType(Files.newInputStream(tempPath, StandardOpenOption.READ));
        Assertions.assertEquals(MediaType.IMAGE_PNG, type);
        outputFile = newFile;

    }
}