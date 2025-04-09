package com.sz.reservation.propertyManagement.infrastructure;

import com.sz.reservation.propertyManagement.domain.AmenitiesType;
import com.sz.reservation.propertyManagement.domain.HousingType;
import com.sz.reservation.propertyManagement.domain.PropertyType;
import com.sz.reservation.propertyManagement.domain.ReservationType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ListingRequestDto test")
class ListingRequestDtoTest {
    private List<AmenitiesType> amenitiesTypeList;
    private AddressInfoRequestDto addressInfoRequestDto;

    private List<MultipartFile> listingPhotos;

    private String listingDescription;

    private String listingTitle;

    private int numberOfGuestAllowed;
    private int numberOfBeds;
    private int numberOfBedrooms;
    private int numberOfBathroom;
    private int pricePerNight;

    private PropertyType propertyType;

    private HousingType housingType;

    private ReservationType reservationType;

    @BeforeEach
    public void initialingVariables() throws IOException {
        amenitiesTypeList = new ArrayList<>();
        amenitiesTypeList.add(AmenitiesType.GYM);

        addressInfoRequestDto = new AddressInfoRequestDto("Argentina",
                "9 de Julio", "1111", "Buenos Aires", "CABA");

        listingPhotos = new ArrayList<>();
        for (int i = 0; i<5; i++){
            String path = "src/test/resources/logo.png";
            byte[] imageLogo = Files.readAllBytes(Path.of(path));
            MockMultipartFile multipartFile = new MockMultipartFile("file","logo.png", MediaType.IMAGE_PNG_VALUE ,imageLogo);
            listingPhotos.add(multipartFile);
        }

        listingDescription = "New beautiful house near the beach";
        listingTitle = "listing title";
        numberOfGuestAllowed = 4;
        numberOfBeds = 4;
        numberOfBedrooms = 4;
        numberOfBathroom = 4;
        pricePerNight = 20;
        propertyType = PropertyType.HOUSE;
        housingType = HousingType.ENTIRE;
        reservationType = ReservationType.OWNER_APPROVAL;
    }

    private final ValidatorFactory validatorFactory = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory();

    private final Validator validator = validatorFactory.usingContext()
            .messageInterpolator(new ParameterMessageInterpolator())
            .getValidator();


    // LISTING TITLE TESTS ---------------

    @Test
    public void Should_CreateObjectCorrectly_When_ValidListingTitle() {
        //arrange
        ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(0, violations.size());
    }
    @Test
    public void Should_ThrowException_When_emptyListingTitle() {
        //arrange
        listingTitle = "";
         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

    @Test
    public void Should_ThrowException_When_nullListingTitle() {
        //arrange
        listingTitle = null;
         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

    @Test
    public void Should_ThrowException_When_ListingTitleSize_LessThanMin() {
        //arrange
        listingTitle = "miau";
         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

    @Test
    public void Should_ThrowException_When_ListingTitleSize_BiggerThanMax() {
        //arrange
        listingTitle = "miau".repeat(10); // 40 char word

         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }


    // LISTING DESCRIPTION TESTS
    @Test
    public void Should_CreateObjectCorrectly_When_ValidListingDescription() {
        //arrange
        listingDescription = "words".repeat(100); // 5 characters * 100 = 500 char

         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(0, violations.size());
    }

    @Test
    public void Should_ThrowException_When_ListingDescriptionSize_BiggerThanMax() {
        //arrange
        listingDescription = "words".repeat(101); // 5 characters * 101 = 505 char

         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }


    @Test
    public void Should_ThrowException_When_ListingDescriptionSize_LowerThanMin() {
        //arrange
        listingDescription = "word"; // 4 characters

         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

    @Test
    public void Should_ThrowException_When_ListingDescription_Null() {
        //arrange
        listingDescription = null;

         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

    @Test
    public void Should_ThrowException_When_ListingDescription_Blank() {
        //arrange
        listingDescription = "";

         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

    //AddressInfoRequestDto test
    @Test
    public void Should_ThrowException_When_AddressInfoRequestCountry_IsNotValid() {
        //arrange
        String country = "c"; //1 char country is not valid
        AddressInfoRequestDto addressInfoRequestDto = new AddressInfoRequestDto(country,"addrress","1111","caba","Puerto Madero");

         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }
    @Test
    public void Should_ThrowException_When_AddressInfoRequest_IsNull() {
        //arrange
        addressInfoRequestDto = null;
         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

    //listingPhotos tests

    @Test
    public void Should_CreateListingCorrectly_When_ListingPhotos_IsValid() throws IOException {
        //arrange
         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(0, violations.size());
    }

    /*
    @Test
    public void Should_ThrowException_When_NullListingPhotos() throws IOException {
        //arrange
        listingPhotos = null;
         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

     */

  /*  @Test
    public void Should_ThrowException_When_EmptyListingPhotos(){
        //arrange
        listingPhotos = new ArrayList<>();
         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(2, violations.size());
    }*/

    /*
    @Test
    public void Should_ThrowException_When_ListingPhotos_LessThanMin() throws IOException {
        //arrange
        listingPhotos = new ArrayList<>();
        for (int i = 0; i<4; i++){
            String path = "src/test/resources/logo.png";
            byte[] imageLogo = Files.readAllBytes(Path.of(path));
            MockMultipartFile multipartFile = new MockMultipartFile("file","logo.png", MediaType.IMAGE_PNG_VALUE ,imageLogo);
            listingPhotos.add(multipartFile);
        }
         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

     */

    /*
    @Test
    public void Should_ThrowException_When_ListingPhotos_MoreThanMax() throws IOException {
        //arrange
        listingPhotos = new ArrayList<>();
        for (int i = 0; i<60; i++){
            String path = "src/test/resources/logo.png";
            byte[] imageLogo = Files.readAllBytes(Path.of(path));
            MockMultipartFile multipartFile = new MockMultipartFile("file","logo.png", MediaType.IMAGE_PNG_VALUE ,imageLogo);
            listingPhotos.add(multipartFile);
        }
         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

     */

    //numberOfGuestAllowed test
    @Test
    public void Should_CreateObjectCorrectly_When_ValidNumberOfGuestAllowed() {
        //arrange
        numberOfGuestAllowed = 10;

         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);


    }
    @Test
    public void Should_ThrowException_When_numberOfGuestAllowed_IsLowerThanMin() {
        //arrange
        numberOfGuestAllowed = 0;
         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

    @Test
    public void Should_ThrowException_When_numberOfGuestAllowed_IsBiggerThanMax() {
        //arrange
        numberOfGuestAllowed = 17;
         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

    // NumberOfBeds tests
    @Test
    public void Should_CreateObjectCorrectly_When_ValidNumberOfBeds() {
        //arrange
        numberOfBeds = 10;

         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(0, violations.size());
    }


    @Test
    public void Should_ThrowException_When_numberOfBeds_LowerThanMin() {
        //arrange
        numberOfBeds = 0;

         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

    @Test
    public void Should_ThrowException_When_numberOfBeds_BiggerThanMax() {
        //arrange
        numberOfBeds = 51;

         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

    // numberOfBedroom tests
    @Test
    public void Should_CreateObjectCorrectly_When_ValidNumberOfBedrooms() {
        //arrange
        numberOfBedrooms = 10;

         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(0, violations.size());
    }


    @Test
    public void Should_ThrowException_When_numberOfBedrooms_LowerThanMin() {
        //arrange
        numberOfBedrooms = -1;

         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

    @Test
    public void Should_ThrowException_When_numberOfBedrooms_BiggerThanMax() {
        //arrange
        numberOfBedrooms = 51;

         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

    //numberOfBathroom tests
    @Test
    public void Should_CreateObjectCorrectly_When_ValidNumberOfBathroom() {
        //arrange
        numberOfBathroom = 10;

         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(0, violations.size());
    }


    @Test
    public void Should_ThrowException_When_numberOfBathroom_LowerThanMin() {
        //arrange
        numberOfBathroom = 0;

         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

    @Test
    public void Should_ThrowException_When_numberOfBathroom_BiggerThanMax() {
        //arrange
        numberOfBathroom = 51;

         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

    // pricePerNight tests
    @Test
    public void Should_CreateObjectCorrectly_When_ValidpricePerNight() {
        //arrange
        pricePerNight = 11;

         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(0, violations.size());
    }


    @Test
    public void Should_ThrowException_When_pricePerNight_LowerThanMin() {
        //arrange
        pricePerNight = 9;

         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }


    //PropertyType tests
    @Test
    public void Should_ThrowException_When_propertyType_IsNull() {
        //arrange
        propertyType = null;
         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }


    //housing type tests

    @Test
    public void Should_ThrowException_When_housingType_IsNull() {
        //arrange
        housingType = null;
         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

    //reservationType tests

    @Test
    public void Should_ThrowException_When_reservationType_IsNull() {
        //arrange
        reservationType = null;
         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

    //amenitiesList tests
    @Test
    public void Should_ThrowException_When_amenitiesList_IsNull() {
        //arrange
        amenitiesTypeList = null;
         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

    @Test
    public void Should_ThrowException_When_amenitiesList_IsEmpty() {
        //arrange
        amenitiesTypeList = new ArrayList<>();
         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(1, violations.size());
    }

    @Test
    public void Should_CreateListingCorrectly_When_amenitiesList_ContainsAmenities() {
        //arrange
         ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        Set<ConstraintViolation<ListingRequestDto>> violations = validator.validate(listingRequestDto);

        //assert
        assertEquals(0, violations.size());
    }
}