package com.sz.reservation.registration.application.useCase;

import com.sz.reservation.configuration.RootConfig;
import com.sz.reservation.registration.infrastructure.dto.AccountCreationRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RootConfig.class})
@WebAppConfiguration
@Disabled
class RegistrationUseCaseTest {

    @Autowired
    AccountRegistrationUseCase useCase;
    @Test
    public void tryingtoRegisterUser() throws IOException {
        String imagePath = "C:\\Users\\losmelli\\Pictures\\messi2.jpg";

        // Read the file into a MultipartFile
        FileInputStream inputStream = new FileInputStream(imagePath);
        MultipartFile profilePicture = new MockMultipartFile(
                "profilePicture",
                "messi2.jpg",
                "image/jpg",
                inputStream
        );

        AccountCreationRequest request = new AccountCreationRequest(
                "johnDoe123",          // username (5-55 chars, no whitespaces)
                "John",                // name (1-55 chars)
                "Doe",                 // surname (1-55 chars)
                "john.doe@example.com",// email (valid pattern)
                "+1",                   // countryCode (max 3 chars)
                "5056468976",          // phoneNumber (max 12 chars)
                LocalDate.of(1990, 1, 1), // birthDate (must be in the past)
                "American",            // nationality (max 55 chars)
                profilePicture,        // profilePicture (not null)
                "StrongPass123!"       // password (8-255 chars)
        );

        useCase.registerNotEnabledUser(request);
    }


}