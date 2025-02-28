package com.sz.reservation.accountManagement.application.useCase;

import com.sz.reservation.accountManagement.application.dto.AccountCreationData;
import com.sz.reservation.accountManagement.application.service.AccountCreation;
import com.sz.reservation.accountManagement.domain.exception.InvalidPhoneNumberException;
import com.sz.reservation.accountManagement.domain.model.ProfilePicture;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

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


    @BeforeAll
    private static void initializingAccountCreation(){
        PhoneNumberValidator phoneNumberValidator = new LibPhoneNumberValidator();
        HashingService service = new BCryptPasswordHashingService();
        accountCreation = new AccountCreation(phoneNumberValidator, service);
    }

    @Test
    public void Should_ReturnAccountCreationData_When_ValidRequest() throws IOException {
        //arrange
        String path = "src/test/resources/bird.jpg";
        ProfilePicture profile = new ProfilePicture(path);
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "Mike17",
                "mike",
                "kawasaki",
                "notRealEmail@not.gmail",
                "+54",
                "1111111111",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "eightcharacterlong");
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
        String path = "src/test/resources/bird.jpg";
        ProfilePicture profile = new ProfilePicture(path);
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "Mike17",
                "mike",
                "kawasaki",
                "notRealEmail@not.gmail",
                "+54",
                "000",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "eightcharacterlong");

        //act and assert
        assertThrows(InvalidPhoneNumberException.class,() -> {
            accountCreation.accountCreationData(request, profile);
        });
    }


    @Test
    public void ShouldThrowIllegalArgumentException_WhenNullAccountRequest(){
        //arrange
        String path = "src/test/resources/bird.jpg";
        ProfilePicture profile = new ProfilePicture(path);

        //act and assert
        assertThrows(IllegalArgumentException.class,() -> {
            accountCreation.accountCreationData(null, profile);
        });
    }

    @Test
    public void ShouldThrowIllegalArgumentException_WhenNullProfile(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "Mike17",
                "mike",
                "kawasaki",
                "notRealEmail@not.gmail",
                "+54",
                "000",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "eightcharacterlong");

        //act and assert
        assertThrows(IllegalArgumentException.class,() -> {
            accountCreation.accountCreationData(request, null);
        });
    }

}