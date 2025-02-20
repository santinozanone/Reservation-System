package com.sz.reservation.registration.infrastructure.service;

import com.sz.reservation.registration.domain.service.PhoneNumberValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Testing libPhoneNumberValidator")
class libPhoneNumberValidatorTest {
    private PhoneNumberValidator phoneNumberValidator = new LibPhoneNumberValidator();

    private Logger logger = LogManager.getLogger(libPhoneNumberValidatorTest.class);


    @Test
    public void Should_ReturnTrue_When_ValidPhoneNumberProvided() {
        //Arrange
        String countryCode = "+54";
        String phoneNumber = "1111448899";

        //Act
        boolean isPhoneNumberValid = phoneNumberValidator.isValid(countryCode,phoneNumber);

        //Assert
        Assertions.assertTrue(isPhoneNumberValid);
    }

    @Test
    public void Should_ReturnFalse_When_InvalidNumber(){
        //arrange
        String countryCode = "+54";
        String phoneNumber = "545454";

        //Act
        boolean isPhoneNumberValid = phoneNumberValidator.isValid(countryCode,phoneNumber);

        //Assert
        Assertions.assertFalse(isPhoneNumberValid);
    }

    @Test
    public void Should_ReturnFalse_When_NumberWithMoreCharactersThanValid(){
        //arrange
        String countryCode = "+54";
        String phoneNumber = "11123456789";

        //Act
        boolean isPhoneNumberValid = phoneNumberValidator.isValid(countryCode,phoneNumber);

        //Assert
        Assertions.assertFalse(isPhoneNumberValid);
    }

    @Test
    public void Should_ReturnFalse_When_NumberWithLessCharactersThanValid(){
        //arrange
        String countryCode = "+54";
        String phoneNumber = "111234567";

        //Act
        boolean isPhoneNumberValid = phoneNumberValidator.isValid(countryCode,phoneNumber);

        //Assert
        Assertions.assertFalse(isPhoneNumberValid);
    }

    @Test
    public void Should_ThrowIllegalArgumentException_When_NullCountryCodeProvided() {
        //Arrange
        String phoneNumber = "1111448899";

        //Act and assert
        Assertions.assertThrows(IllegalArgumentException.class,() ->{
            phoneNumberValidator.isValid(null,phoneNumber);
        });
    }

    @Test
    public void Should_ThrowIllegalArgumentException_When_NullPhoneProvided() {
        logger.info("testing valid phone  number ");
        //Arrange
        String countryCode = "+54";
        //Act and assert
        Assertions.assertThrows(IllegalArgumentException.class,() ->{
            phoneNumberValidator.isValid(countryCode,null);
        });
    }
}