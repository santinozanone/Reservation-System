package com.sz.reservation.accountManagement.infrastructure.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Account creation request Dto test")
class AccountCreationRequestTest {

    private static  ValidatorFactory validatorFactory ;
    private static Validator  validator;

    private String username;
    private String name;
    private String surname;
    private String email;
    private String countryCode;
    private String phoneNumber;
    private LocalDate birthdate;
    private String nationality;
    private String password;

    private String originalFilename;
    private String fileName;

    @BeforeEach
    public void initializeVariables(){
        username = "mike01";
        name = "mike";
        surname = "kawasaki";
        email = "notRealEmail@not.gmail";
        countryCode = "54";
        phoneNumber = "1111111111";
        birthdate = LocalDate.of(2014, 4, 15);
        nationality = "argentina";
        password = "eightcharacterlong";
        originalFilename = "fake.png";
        fileName = "fake";
    }

    @BeforeAll
    public static void instantiatingValidator(){
        validatorFactory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        validator = validatorFactory.usingContext()
                .messageInterpolator(new ParameterMessageInterpolator())
                .getValidator();
    }

    @Test
    public void Should_ThrowException_When_UsernameNull(){
        //arrange
        username = null;
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        //act and assert
        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_UsernameBlank(){
        //arrange
        username = "";
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

       Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
       assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_UsernameBiggerThanMax(){
        //arrange
        username = "a".repeat(56);
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_UsernameHasWhitespaces(){
        //arrange
        username = " ".repeat(5);
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }


    @Test
    public void Should_ThrowException_When_NameIsBlank(){
        //arrange
        name = "";
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(2,violations.size()); //Non blank and min size exception
    }

    @Test
    public void Should_ThrowException_When_NameIsNull(){
        //arrange
        name = null;
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }
    @Test
    public void Should_ThrowException_When_NameIsBiggerThanMax(){
        //arrange
        name = "a".repeat(56);
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }



    @Test
    public void Should_ThrowException_When_SurnameIsBlank(){
        //arrange
        surname = "";
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(2,violations.size()); //Non blank and min size exception
    }

    @Test
    public void Should_ThrowException_When_SurnameIsNull(){
        //arrange
        surname = null;
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }
    @Test
    public void Should_ThrowException_When_SurnameIsBiggerThanMax(){
        //arrange
        surname = "a".repeat(56);
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }



    @Test
    public void Should_ThrowException_When_EmailIsIncorrect(){
        //arrange
        email = ".notRealEmail@not.gmail";
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);


        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }
    @Test
    public void Should_ThrowException_When_EmailNotContainDomain(){
        //arrange
        email = "invalid.gmail";
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_EmailIsNull(){
        //arrange
        email = null;
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        System.out.println("violation - " + violations.iterator().next().getMessage());
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_EmailContainWhitespaces(){
        //arrange
        email = "notRealEmail @not.gmail";
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_WhenCountryCodeIsBlank(){
        //arrange
        countryCode = "";
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(2,violations.size());
    }
    @Test
    public void Should_ThrowException_WhenCountryCodeContainsWhitespace(){
        //arrange
        countryCode = " ";
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(2,violations.size());
    }
    @Test
    public void Should_ThrowException_WhenCountryCodeContainsNonNumericChar(){
        //arrange
        countryCode = "+54";
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_WhenCountryCodeIsBiggerThanMax(){
        //arrange
        countryCode = "1111";
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_WhenPhoneNumberIsBlank(){
        //arrange
        phoneNumber = "";
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(2,violations.size());
    }

    @Test
    public void Should_ThrowException_When_PhoneNumberSizeIsSmallerThanMin(){
        //arrange
        phoneNumber = "114";
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_PhoneNumberSizeIsBiggerThanMax(){
        //arrange
        phoneNumber = "1".repeat(14);
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_BirthdateIsNull(){
        //arrange
        birthdate = null;
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }


    @Test
    public void Should_ThrowException_When_BirthdateIsInTheFuture(){
        //arrange
        birthdate = LocalDate.now().plusDays(2);
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_BirthdateIsNotIsoDate(){
        //arrange
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        birthdate =  LocalDate.parse("04-04-2026",dateTimeFormatter);

        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_NationalityIsBlank(){
        //arrange
        nationality = "";
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(2,violations.size());
    }


    @Test
    public void Should_ThrowException_When_NationalityIsSmallerThanMin(){
        //arrange
        nationality = "AR";
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);


        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_NationalityIsBiggerThanMax(){
        //arrange
        nationality = "ARGENTINA".repeat(10);
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);


        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }


    @Test
    public void Should_ThrowException_When_ProfilePictureIsNull(){
        //arrange
        MockMultipartFile multipartFile = null;
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);


        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_PasswordIsNull(){
        //arrange
        password = null;
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);


        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_PasswordIsBlank(){
        //arrange
        password = "";
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(2,violations.size());
    }
    @Test
    public void Should_ThrowException_When_PasswordIsSmallerThanMin(){
        //arrange
        password = "min";
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_PasswordIsBiggerThanMax(){
        //arrange
        password = "hello".repeat(200);
        MockMultipartFile multipartFile = new MockMultipartFile(fileName,originalFilename, MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                multipartFile,
                password);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }
}