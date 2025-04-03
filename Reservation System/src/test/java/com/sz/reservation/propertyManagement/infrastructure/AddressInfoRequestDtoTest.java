package com.sz.reservation.propertyManagement.infrastructure;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AddressInfoRequestDto test")
class AddressInfoRequestDtoTest {

    private String country;

    private String streetAddress;

    private String apartmentNumber; // might be empty

    private String postalCode;

    private String region; // mostly provinces/states, for example Buenos Aires

    private String locality;


    @BeforeEach
    public void initializeVariables(){
        country = "argentina";
        streetAddress = "9 de julio";
        apartmentNumber = "1111";
        postalCode = "9999";
        region = "Buenos aires";
        locality = "caba";
    }


    private ValidatorFactory validatorFactory = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory();

    private Validator validator = validatorFactory.usingContext()
            .messageInterpolator(new ParameterMessageInterpolator())
            .getValidator();


    @Test
    public void Should_CreateObjectCorrectly_When_ValidCountry(){
        //arrange
        AddressInfoRequestDto addressInfoRequestDto = new AddressInfoRequestDto(country,streetAddress,apartmentNumber,postalCode,region,locality);
        //act
        Set<ConstraintViolation<AddressInfoRequestDto>> violations = validator.validate(addressInfoRequestDto );
        //assert
        assertEquals(0, violations.size());
    }


    //country test
    @Test
    public void Should_ThrowException_When_BlankCountry(){
        //arrange
        country = "";
        AddressInfoRequestDto addressInfoRequestDto = new AddressInfoRequestDto(country,streetAddress,apartmentNumber,postalCode,region,locality);

        //act
        Set<ConstraintViolation<AddressInfoRequestDto>> violations = validator.validate(addressInfoRequestDto );

        //assert
        assertEquals(2, violations.size());
    }

    @Test
    public void Should_ThrowException_When_NullCountry(){
        //arrange
        country = null;
        AddressInfoRequestDto addressInfoRequestDto = new AddressInfoRequestDto(country,streetAddress,apartmentNumber,postalCode,region,locality);

        //act
        Set<ConstraintViolation<AddressInfoRequestDto>> violations = validator.validate(addressInfoRequestDto );

        //assert
        assertEquals(1, violations.size());
    }

    @Test
    public void Should_ThrowException_When_CountryLengthLessThanMin(){
        //arrange
        country = "AR";
        AddressInfoRequestDto addressInfoRequestDto = new AddressInfoRequestDto(country,streetAddress,apartmentNumber,postalCode,region,locality);

        //act
        Set<ConstraintViolation<AddressInfoRequestDto>> violations = validator.validate(addressInfoRequestDto );

        //assert
        assertEquals(1, violations.size());
    }


    //streetAddress tests
    @Test
    public void Should_ThrowException_When_BlankStreetAddress(){
        //arrange
        streetAddress = "";
        AddressInfoRequestDto addressInfoRequestDto = new AddressInfoRequestDto(country,streetAddress,apartmentNumber,postalCode,region,locality);

        //act
        Set<ConstraintViolation<AddressInfoRequestDto>> violations = validator.validate(addressInfoRequestDto );

        //assert
        assertEquals(1, violations.size());
    }

    @Test
    public void Should_ThrowException_When_NullStreetAddress(){
        //arrange
        streetAddress = null;
        AddressInfoRequestDto addressInfoRequestDto = new AddressInfoRequestDto(country,streetAddress,apartmentNumber,postalCode,region,locality);

        //act
        Set<ConstraintViolation<AddressInfoRequestDto>> violations = validator.validate(addressInfoRequestDto );

        //assert
        assertEquals(1, violations.size());
    }

    //apartmentNumber tests
    @Test
    public void Should_ThrowException_When_NullApartmentNumber(){
        //arrange
        apartmentNumber = null;
        AddressInfoRequestDto addressInfoRequestDto = new AddressInfoRequestDto(country,streetAddress,apartmentNumber,postalCode,region,locality);

        //act
        Set<ConstraintViolation<AddressInfoRequestDto>> violations = validator.validate(addressInfoRequestDto );

        //assert
        assertEquals(1, violations.size());
    }

    //postalCode tests
    @Test
    public void Should_ThrowException_When_BlankPostalCode(){
        //arrange
        postalCode = "";
        AddressInfoRequestDto addressInfoRequestDto = new AddressInfoRequestDto(country,streetAddress,apartmentNumber,postalCode,region,locality);

        //act
        Set<ConstraintViolation<AddressInfoRequestDto>> violations = validator.validate(addressInfoRequestDto );

        //assert
        assertEquals(1, violations.size());
    }

    @Test
    public void Should_ThrowException_When_NullPostalCode(){
        //arrange
        postalCode = null;
        AddressInfoRequestDto addressInfoRequestDto = new AddressInfoRequestDto(country,streetAddress,apartmentNumber,postalCode,region,locality);

        //act
        Set<ConstraintViolation<AddressInfoRequestDto>> violations = validator.validate(addressInfoRequestDto );

        //assert
        assertEquals(1, violations.size());
    }

    //postalCode tests
    @Test
    public void Should_ThrowException_When_BlankRegion(){
        //arrange
        region = "";
        AddressInfoRequestDto addressInfoRequestDto = new AddressInfoRequestDto(country,streetAddress,apartmentNumber,postalCode,region,locality);

        //act
        Set<ConstraintViolation<AddressInfoRequestDto>> violations = validator.validate(addressInfoRequestDto );

        //assert
        assertEquals(1, violations.size());
    }

    @Test
    public void Should_ThrowException_When_NullRegion(){
        //arrange
        region = null;
        AddressInfoRequestDto addressInfoRequestDto = new AddressInfoRequestDto(country,streetAddress,apartmentNumber,postalCode,region,locality);

        //act
        Set<ConstraintViolation<AddressInfoRequestDto>> violations = validator.validate(addressInfoRequestDto );

        //assert
        assertEquals(1, violations.size());
    }

    //postalCode tests
    @Test
    public void Should_ThrowException_When_BlankLocality(){
        //arrange
        locality = "";
        AddressInfoRequestDto addressInfoRequestDto = new AddressInfoRequestDto(country,streetAddress,apartmentNumber,postalCode,region,locality);

        //act
        Set<ConstraintViolation<AddressInfoRequestDto>> violations = validator.validate(addressInfoRequestDto );

        //assert
        assertEquals(1, violations.size());
    }

    @Test
    public void Should_ThrowException_When_NullLocality(){
        //arrange
        locality = null;
        AddressInfoRequestDto addressInfoRequestDto = new AddressInfoRequestDto(country,streetAddress,apartmentNumber,postalCode,region,locality);

        //act
        Set<ConstraintViolation<AddressInfoRequestDto>> violations = validator.validate(addressInfoRequestDto );

        //assert
        assertEquals(1, violations.size());
    }


}