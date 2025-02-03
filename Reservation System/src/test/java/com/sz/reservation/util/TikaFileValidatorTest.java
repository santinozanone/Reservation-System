package com.sz.reservation.util;



import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@DisplayName("Tika Validator Tests")
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TikaFileValidatorTest {
    private File outputFile;

    @BeforeEach
    public void createPngFile() throws IOException {
        //Arrange
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, 0);

        //Act
        outputFile = new File("PngFile.png");

        //Assert
        boolean isSuccessful = ImageIO.write(image, "png", outputFile);
        Assertions.assertTrue(isSuccessful);
    }

    @AfterEach
    public void removePngFile(){
        //Act
        boolean isSuccessful = outputFile.delete();

        //Assert
        Assertions.assertTrue(isSuccessful);
    }

    @Test
    @Order(1)
    public void Should_ReturnRealMediaType_When_ValidInputStream() throws IOException {
        //Arrange
        Path tempPath = Path.of(outputFile.getAbsolutePath());
        FileTypeValidator fileValidator = new TikaFileValidator();
        MediaType realMediaType = MediaType.IMAGE_PNG;

        //Act
        MediaType type = fileValidator.getRealFileType(Files.newInputStream(tempPath, StandardOpenOption.READ));

        //Assert
        Assertions.assertEquals(realMediaType, type);
    }

    @Test
    @Order(2)
    public void Should_RenameFileCorrectly_When_FilenameIsChanged(){
        //Arrange
        File newFile = new File("PngFileExtensionChanged.jpeg"); // create file with new name

        //Act
        boolean IsSuccessful = outputFile.renameTo(newFile); // rename PNG file to ".jpeg"

        //Assert
        Assertions.assertTrue(IsSuccessful);
        outputFile = newFile;
    }

    @Test
    @Order(3)
    public void Should_ReturnRealMediaType_When_ExtensionOfFileIsChanged() throws IOException {
        //Arrange
        FileTypeValidator fileValidator = new TikaFileValidator();
        Path filePath = Path.of(outputFile.getAbsolutePath());
        InputStream fileInputStream = Files.newInputStream(filePath, StandardOpenOption.READ);
        MediaType realMediaType = MediaType.IMAGE_PNG;

        //Act
        MediaType type  = fileValidator.getRealFileType(fileInputStream);

        //Assert
        Assertions.assertEquals(realMediaType, type);
    }

    @Test
    public void Should_ReturnOctetStream_When_CannotDetectFileType() throws IOException {
        //Arrange
        File fileWithInventedExtension = CreateNewFileWithInventedExtension();
        Path filePath = Path.of(fileWithInventedExtension.getAbsolutePath());
        InputStream fileInputStream = Files.newInputStream(filePath, StandardOpenOption.READ);
        FileTypeValidator fileValidator = new TikaFileValidator();
        MediaType realMediaType = MediaType.APPLICATION_OCTET_STREAM;

        //Act
        MediaType type = fileValidator.getRealFileType(fileInputStream);

        //Assert
        Assertions.assertEquals(realMediaType, type);

        //Cleaning resources
        DeleteFileWithInventedExtension(fileWithInventedExtension);
    }

    @Test
    public void Should_ReturnRealMediaType_When_MultipartFileIsSupplied() throws IOException {
        //Arrange
        MockMultipartFile multipartFile = new MockMultipartFile("file","photo.txt",MediaType.TEXT_PLAIN_VALUE,"helloo".getBytes());
        FileTypeValidator validator = new TikaFileValidator();
        MediaType realMediaType = MediaType.TEXT_PLAIN;

        //Act
        MediaType type = validator.getRealFileType(multipartFile.getInputStream());

        //Assert
        Assertions.assertEquals(realMediaType, type);

    }


    private File CreateNewFileWithInventedExtension() throws IOException {
        //Arrange
        File fileWithInventedExtension = new File("inventedExtension.erty");

        //Act
        boolean isSuccessful = fileWithInventedExtension.createNewFile();

        //Assert
        Assertions.assertTrue(isSuccessful);

        return fileWithInventedExtension;
    }

    private void DeleteFileWithInventedExtension(File file) throws IOException {
        //Act
        boolean isSuccessful = file.delete();

        //Assert
        Assertions.assertTrue(isSuccessful);
    }


}