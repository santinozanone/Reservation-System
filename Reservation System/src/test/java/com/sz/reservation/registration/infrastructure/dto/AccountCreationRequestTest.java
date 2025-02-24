package com.sz.reservation.registration.infrastructure.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeAll;
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

    @BeforeAll
    private static void instantiatingValidator(){
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
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                null,
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
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_UsernameBlank(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "",
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
       assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_UsernameBiggerThanMax(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "saaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "saaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaassaaaaaa" +
                        "saaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
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
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_UsernameHasWhitespaces(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "           ",
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
        assertEquals(1,violations.size());
    }


    @Test
    public void Should_ThrowException_When_NameIsBlank(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "",
                "kawasaki",
                "notRealEmail@not.gmail",
                "+54",
                "1111111111",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(2,violations.size()); //Non blank and min size exception
    }

    @Test
    public void Should_ThrowException_When_NameIsNull(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                null,
                "kawasaki",
                "notRealEmail@not.gmail",
                "+54",
                "1111111111",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }
    @Test
    public void Should_ThrowException_When_NameIsBiggerThanMax(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "saaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "kawasaki",
                "notRealEmail@not.gmail",
                "+54",
                "1111111111",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }



    @Test
    public void Should_ThrowException_When_SurnameIsBlank(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "sasasa",
                "",
                "notRealEmail@not.gmail",
                "+54",
                "1111111111",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(2,violations.size()); //Non blank and min size exception
    }

    @Test
    public void Should_ThrowException_When_SurnameIsNull(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "sasasas",
                null,
                "notRealEmail@not.gmail",
                "+54",
                "1111111111",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }
    @Test
    public void Should_ThrowException_When_SurnameIsBiggerThanMax(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "sasasa",
                "saaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                        "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "notRealEmail@not.gmail",
                "+54",
                "1111111111",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }



    @Test
    public void Should_ThrowException_When_EmailIsIncorrect(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "sasa",
                "kawasaki",
                ".notRealEmail@not.gmail",
                "+54",
                "1111111111",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }
    @Test
    public void Should_ThrowException_When_EmailNotContainDomain(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "sasa",
                "kawasaki",
                "invalid.gmail",
                "+54",
                "1111111111",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_EmailIsNull(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "sasa",
                "kawasaki",
                null,
                "+54",
                "1111111111",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        System.out.println("violation - " + violations.iterator().next().getMessage());
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_EmailContainWhitespaces(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "sasa",
                "kawasaki",
                "newEmail @gmail.com",
                "+54",
                "1111111111",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_WhenCountryCodeIsBlank(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "sasa",
                "kawasaki",
                "newEmail@gmail.com",
                "",
                "1111111111",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_WhenCountryCodeIsBiggerThanMax(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "sasa",
                "kawasaki",
                "newEmail@gmail.com",
                "1111",
                "1111111111",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_WhenPhoneNumberIsBlank(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "sasa",
                "kawasaki",
                "newEmail@gmail.com",
                "111",
                "",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(2,violations.size());
    }

    @Test
    public void Should_ThrowException_When_PhoneNumberSizeIsSmallerThanMin(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "sasa",
                "kawasaki",
                "newEmail@gmail.com",
                "111",
                "114",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_PhoneNumberSizeIsBiggerThanMin(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "sasa",
                "kawasaki",
                "newEmail@gmail.com",
                "111",
                "11223344556677",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_BirthdateIsNull(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "sasa",
                "kawasaki",
                "newEmail@gmail.com",
                "111",
                "11001124",
                null,
                "argentina",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }


    @Test
    public void Should_ThrowException_When_BirthdateIsInTheFuture(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "sasa",
                "kawasaki",
                "newEmail@gmail.com",
                "111",
                "11001124",
                LocalDate.of(2026,4,4),
                "argentina",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_BirthdateIsNotIsoDate(){
        //arrange
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date =  LocalDate.parse("04-04-2026",dateTimeFormatter);


        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "sasa",
                "kawasaki",
                "newEmail@gmail.com",
                "111",
                "11001124",
                date,
                "argentina",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_NationalityIsBlank(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "sasa",
                "kawasaki",
                "newEmail@gmail.com",
                "111",
                "11001124",
                LocalDate.of(2020,4,4),
                "",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(2,violations.size());
    }


    @Test
    public void Should_ThrowException_When_NationalityIsSmallerThanMin(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "sasa",
                "kawasaki",
                "newEmail@gmail.com",
                "111",
                "11001124",
                LocalDate.of(2020,4,4),
                "AR",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_NationalityIsBiggerThanMax(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "sasa",
                "kawasaki",
                "newEmail@gmail.com",
                "111",
                "11001124",
                LocalDate.of(2020,4,4),
                "ARGENTINA" +"CHILE" +"EEUU" +"PERU" +"BOLIVIA " +
                        "Mexico" + "Elsavldator " + "CANADA " + "GERMANY",
                multipartFile,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }


    @Test
    public void Should_ThrowException_When_ProfilePictureIsNull(){
        //arrange
        AccountCreationRequest request = new AccountCreationRequest(
                "mike18181",
                "sasa",
                "kawasaki",
                "newEmail@gmail.com",
                "111",
                "11001124",
                LocalDate.of(2020,4,4),
                "ARGENTINA" ,
                null,
                "eightcharacterlong");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_PasswordIsNull(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "sasasa",
                "mike",
                "kawasaki",
                "notRealEmail@not.gmail",
                "+54",
                "1111111111",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                null);

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_PasswordIsBlank(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "sasasa",
                "mike",
                "kawasaki",
                "notRealEmail@not.gmail",
                "+54",
                "1111111111",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(2,violations.size());
    }
    @Test
    public void Should_ThrowException_When_PasswordIsSmallerThanMin(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "sasasa",
                "mike",
                "kawasaki",
                "notRealEmail@not.gmail",
                "+54",
                "1111111111",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "megapas");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }

    @Test
    public void Should_ThrowException_When_PasswordIsBiggerThanMax(){
        //arrange
        MockMultipartFile multipartFile = new MockMultipartFile("fake","fake.png", MediaType.IMAGE_PNG_VALUE,new byte[0]);
        AccountCreationRequest request = new AccountCreationRequest(
                "sasasa",
                "mike",
                "kawasaki",
                "notRealEmail@not.gmail",
                "+54",
                "1111111111",
                LocalDate.of(2014,4,15),
                "argentina",
                multipartFile,
                "dsssssssssssssssssssssssssssssssssssss xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxkkkkkkkkkkkkkkkkkkkkkkxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

        Set<ConstraintViolation<AccountCreationRequest>> violations = validator.validate(request);
        assertEquals(1,violations.size());
    }
}