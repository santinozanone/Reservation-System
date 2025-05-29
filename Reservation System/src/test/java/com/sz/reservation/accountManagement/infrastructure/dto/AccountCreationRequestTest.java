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

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Account creation request Dto test")
class AccountCreationServiceRequestTest {

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
    private MockMultipartFile multipartFile;
    private String password;

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
        multipartFile = new MockMultipartFile("fileName","originalFilename", MediaType.IMAGE_PNG_VALUE,new byte[0]);

        password = "eightcharacterlong";
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
        AccountCreationRequest request = creationRequest();

        //act and assert
        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_UsernameBlank(){
        //arrange
        username = "";
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_UsernameBiggerThanMax(){
        //arrange
        username = "a".repeat(56);
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_UsernameHasWhitespaces(){
        //arrange
        username = " ".repeat(5);
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }


    @Test
    public void Should_ThrowException_When_NameIsBlank(){
        //arrange
        name = "";
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(2,violations.size()); //Non blank and min size exception
    }

    @Test
    public void Should_ThrowException_When_NameIsNull(){
        //arrange
        name = null;
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }
    @Test
    public void Should_ThrowException_When_NameIsBiggerThanMax(){
        //arrange
        name = "a".repeat(56);
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }



    @Test
    public void Should_ThrowException_When_SurnameIsBlank(){
        //arrange
        surname = "";
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(2,violations.size()); //Non blank and min size exception
    }

    @Test
    public void Should_ThrowException_When_SurnameIsNull(){
        //arrange
        surname = null;
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }
    @Test
    public void Should_ThrowException_When_SurnameIsBiggerThanMax(){
        //arrange
        surname = "a".repeat(56);
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }



    @Test
    public void Should_ThrowException_When_EmailIsIncorrect(){
        //arrange
        email = ".notRealEmail@not.gmail";
        AccountCreationRequest request = creationRequest();


        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }
    @Test
    public void Should_ThrowException_When_EmailNotContainDomain(){
        //arrange
        email = "invalid.gmail";
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_EmailIsNull(){
        //arrange
        email = null;
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        System.out.println("violation - " + violations.iterator().next().getMessage());
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_EmailContainWhitespaces(){
        //arrange
        email = "notRealEmail @not.gmail";
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_WhenCountryCodeIsBlank(){
        //arrange
        countryCode = "";
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(2,violations.size());
    }
    @Test
    public void Should_ThrowException_WhenCountryCodeContainsWhitespace(){
        //arrange
        countryCode = " ";
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(2,violations.size());
    }
    @Test
    public void Should_ThrowException_WhenCountryCodeContainsNonNumericChar(){
        //arrange
        countryCode = "+54";
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_WhenCountryCodeIsBiggerThanMax(){
        //arrange
        countryCode = "1111";
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_WhenPhoneNumberIsBlank(){
        //arrange
        phoneNumber = "";
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(2,violations.size());
    }

    @Test
    public void Should_ThrowException_When_PhoneNumberSizeIsSmallerThanMin(){
        //arrange
        phoneNumber = "114";
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_PhoneNumberSizeIsBiggerThanMax(){
        //arrange
        phoneNumber = "1".repeat(14);
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_BirthdateIsNull(){
        //arrange
        birthdate = null;
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }


    @Test
    public void Should_ThrowException_When_BirthdateIsInTheFuture(){
        //arrange
        birthdate = LocalDate.now().plusDays(2);
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_BirthdateIsNotIsoDate(){
        //arrange
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        birthdate =  LocalDate.parse("04-04-2026",dateTimeFormatter);
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_NationalityIsBlank(){
        //arrange
        nationality = "";
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(2,violations.size());
    }


    @Test
    public void Should_ThrowException_When_NationalityIsSmallerThanMin(){
        //arrange
        nationality = "AR";
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_NationalityIsBiggerThanMax(){
        //arrange
        nationality = "ARGENTINA".repeat(10);
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }


    @Test
    public void Should_ThrowException_When_PasswordIsNull(){
        //arrange
        password = null;
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_PasswordIsBlank(){
        //arrange
        password = "";
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(2,violations.size());
    }
    @Test
    public void Should_ThrowException_When_PasswordIsSmallerThanMin(){
        //arrange
        password = "min";
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_PasswordIsBiggerThanMax(){
        //arrange
        password = "hello".repeat(200);
        AccountCreationRequest request = creationRequest();

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    private AccountCreationRequest creationRequest(){
        return new AccountCreationRequest(
                username,
                name,
                surname,
                email,
                countryCode,
                phoneNumber,
                birthdate,
                nationality,
                password);
    }
}