package com.sz.reservation.accountManagement.infrastructure.service;

import com.sz.reservation.util.TikaFileValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing StandardProfilePictureValidator")
class TikaProfilePictureTypeValidatorTest {
    private static TikaProfilePictureTypeValidator validator;

    @BeforeAll
    private static void instantiatingValidator(){
        validator = new TikaProfilePictureTypeValidator(new TikaFileValidator());
    }


    @Test
    public void Should_ReturnTrue_When_ValidPngMultipart() throws IOException {
        //arrange
        String path = "src/test/resources/logo.png";
        byte[] imageLogo = Files.readAllBytes(Path.of(path));
        MockMultipartFile multipartFile = new MockMultipartFile("file","logo.png", MediaType.IMAGE_PNG_VALUE ,imageLogo);

        //act
        boolean isValid = validator.isValid(multipartFile);

        //assert
        assertTrue(isValid);
    }

    @Test
    public void Should_ReturnTrue_When_ValidJpgMultipart() throws IOException {
        //arrange
        String path = "src/test/resources/bird.jpg";
        byte[] imageLogo = Files.readAllBytes(Path.of(path));
        MockMultipartFile multipartFile = new MockMultipartFile("file","bird.jpg", MediaType.IMAGE_JPEG_VALUE ,imageLogo);

        //act
        boolean isValid = validator.isValid(multipartFile);

        //assert
        assertTrue(isValid);
    }

    @Test
    public void Should_ReturnFalse_When_EmptyMultipart() throws IOException {
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("file","bird.jpg", MediaType.IMAGE_JPEG_VALUE ,new byte[]{});

        //act
        boolean isValid = validator.isValid(multipartFile);

        //assert
        assertFalse(isValid);
    }

    @Test
    public void Should_ReturnFalse_When_WhitespaceOnlyContentMultipart() throws IOException {
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("file", "empty.jpg", MediaType.IMAGE_JPEG_VALUE, "   ".getBytes());

        //act
        boolean isValid = validator.isValid(multipartFile);

        //assert
        assertFalse(isValid);
    }
    @Test
    public void Should_ReturnFalse_When_FileHasMultipleExtensions() throws IOException {
        //arrange
        String path = "src/test/resources/logo.png";
        byte[] imageLogo = Files.readAllBytes(Path.of(path));
        MockMultipartFile multipartFile = new MockMultipartFile("file", "logo.png.exe", MediaType.IMAGE_PNG_VALUE, imageLogo);

        //act
        boolean isValid = validator.isValid(multipartFile);

        //assert
        assertFalse(isValid);
    }


    @Test
    public void Should_ReturnFalse_When_InvalidExtensionButValidContentMultipart() throws IOException {
        //arrange
        String path = "src/test/resources/bird.jpg";
        byte[] imageLogo = Files.readAllBytes(Path.of(path));
        MockMultipartFile multipartFile = new MockMultipartFile("file","bird.pdf", MediaType.IMAGE_JPEG_VALUE ,imageLogo);

        //act
        boolean isValid = validator.isValid(multipartFile);

        //assert
        assertFalse(isValid);
    }

    @Test
    public void Should_ReturnFalse_When_ValidExtensionButInvalidContentMultipart() throws IOException {
        //arrange
        String path = "src/test/resources/bird.jpg";
        byte[] imageLogo = Files.readAllBytes(Path.of(path));
        MockMultipartFile multipartFile = new MockMultipartFile("file","bird.jpg", MediaType.IMAGE_JPEG_VALUE ,"Invalid".getBytes());

        //act
        boolean isValid = validator.isValid(multipartFile);

        //assert
        assertFalse(isValid);
    }

    @Test
    public void Should_ReturnFalse_When_NoExtensionMultipart() throws IOException {
        //arrange
        String path = "src/test/resources/bird.jpg";
        byte[] imageLogo = Files.readAllBytes(Path.of(path));
        MockMultipartFile multipartFile = new MockMultipartFile("file","bird", MediaType.IMAGE_JPEG_VALUE ,imageLogo);

        //act
        boolean isValid = validator.isValid(multipartFile);

        //assert
        assertFalse(isValid);
    }

    @Test
    public void Should_ThrowIllegalArgumentException_When_nullMultipart() throws IOException {
        //arrange act and assert
        assertThrows(IllegalArgumentException.class,() -> {
            validator.isValid(null);
        });
    }

}