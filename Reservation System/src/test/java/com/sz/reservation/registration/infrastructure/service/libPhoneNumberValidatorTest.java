package com.sz.reservation.registration.infrastructure.service;

import com.sz.reservation.registration.domain.service.PhoneNumberValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class libPhoneNumberValidatorTest {
    private PhoneNumberValidator phoneNumberValidator = new LibPhoneNumberValidator();

    private Logger logger = LogManager.getLogger(libPhoneNumberValidatorTest.class);


    @Test
    public void Should_ReturnTrue_When_ValidPhoneNumberProvided() {
        logger.info("testing valid phone  number ");
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
        String countryCode = "+54";
        String phoneNumber = "545454";


        //Act
        boolean isPhoneNumberValid = phoneNumberValidator.isValid(countryCode,phoneNumber);

        //Assert
        Assertions.assertFalse(isPhoneNumberValid);
    }

    @Test
    public void Should_ReturnFalse_When_NumberWithMoreCharactersThanValid(){
        String countryCode = "+54";
        String phoneNumber = "11123456789";


        //Act
        boolean isPhoneNumberValid = phoneNumberValidator.isValid(countryCode,phoneNumber);

        //Assert
        Assertions.assertFalse(isPhoneNumberValid);
    }

    @Test
    public void Should_ReturnFalse_When_NumberWithLessCharactersThanValid(){
        String countryCode = "+54";
        String phoneNumber = "111234567";


        //Act
        boolean isPhoneNumberValid = phoneNumberValidator.isValid(countryCode,phoneNumber);

        //Assert
        Assertions.assertFalse(isPhoneNumberValid);
    }
}