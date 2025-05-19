package com.sz.reservation.accountManagement.application.useCase;

import com.github.f4b6a3.uuid.UuidCreator;
import com.sz.reservation.accountManagement.application.dto.AccountCreationData;
import com.sz.reservation.accountManagement.application.service.AccountCreation;
import com.sz.reservation.accountManagement.domain.exception.InvalidPhoneNumberException;
import com.sz.reservation.accountManagement.domain.model.ProfilePicture;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.accountManagement.domain.service.HashingService;
import com.sz.reservation.accountManagement.domain.service.PhoneNumberValidator;
import com.sz.reservation.accountManagement.infrastructure.dto.AccountCreationRequest;
import com.sz.reservation.accountManagement.infrastructure.service.BCryptPasswordHashingService;
import com.sz.reservation.accountManagement.infrastructure.service.LibPhoneNumberValidator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Account creation test")
class AccountCreationTest {
    private static AccountCreation accountCreation;
    private final ValidatorFactory validatorFactory = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory();
    private final Validator  validator = validatorFactory.usingContext()
            .messageInterpolator(new ParameterMessageInterpolator())
            .getValidator();

    private String username;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String countryCode;

    private LocalDate birthDate;
    private String nationality;

    private MultipartFile multipartFile;
    private String password;

    private ProfilePicture profile;
    @BeforeEach
    public void initializeVariables(){
        username = "wolfofwallstreet";
        name = "jordan";
        surname = "belfort";
        email = "inventedEmail@miau.com";
        countryCode = "54";
        phoneNumber = "1111448899";
        birthDate = LocalDate.now().minusDays(10);
        nationality = "Argentina";
        password ="ultrasafepassword";

        String path = "src/test/resources/bird.jpg";
        profile = new ProfilePicture(path);
        multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
    }

    @BeforeAll
    public static void initializingAccountCreation(){
        PhoneNumberValidator phoneNumberValidator = new LibPhoneNumberValidator();
        HashingService service = new BCryptPasswordHashingService();
        accountCreation = new AccountCreation(phoneNumberValidator, service);
    }

    @Test
    public void Should_ReturnAccountCreationData_When_ValidRequest() throws IOException {
        //arrange
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthDate,
                nationality,
                multipartFile,
                password);
        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) throw new IllegalArgumentException("Illegal arguments supplied "+ violations.toString());

        //act
        AccountCreationData data = accountCreation.accountCreationData(request,profile);

        //assert
        Assertions.assertNotNull(data);

    }


    @Test
    public void ShouldThrowInvalidPhoneNumberException_WhenInvalidPhoneNumber(){
        //arrange
        phoneNumber = "000";
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthDate,
                nationality,
                multipartFile,
                password);

        //act and assert
        assertThrows(InvalidPhoneNumberException.class,() -> {
            accountCreation.accountCreationData(request, profile);
        });
    }


    @Test
    public void ShouldThrowIllegalArgumentException_WhenNullAccountRequest(){
        //act and assert
        assertThrows(IllegalArgumentException.class,() -> {
            accountCreation.accountCreationData(null, profile);
        });
    }

    @Test
    public void ShouldThrowIllegalArgumentException_WhenNullProfile(){
        //arrange
        profile = null;
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthDate,
                nationality,
                multipartFile,
                password);

        //act and assert
        assertThrows(IllegalArgumentException.class,() -> {
            accountCreation.accountCreationData(request, profile);
        });
    }

}