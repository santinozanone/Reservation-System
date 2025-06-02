package com.sz.reservation.listingManagement.application.useCase;

import com.github.f4b6a3.uuid.UuidCreator;
import com.sz.reservation.accountManagement.domain.model.Account;
import com.sz.reservation.accountManagement.domain.model.PhoneNumber;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.globalConfiguration.RootConfig;
import com.sz.reservation.listingManagement.application.useCase.listing.ListingImageState;
import com.sz.reservation.listingManagement.application.useCase.listing.ListingPropertyUseCase;
import com.sz.reservation.listingManagement.configuration.ListingConfig;
import com.sz.reservation.listingManagement.domain.*;
import com.sz.reservation.listingManagement.domain.exception.InvalidListingIdException;
import com.sz.reservation.listingManagement.domain.port.outbound.ListingImageMetadataRepository;
import com.sz.reservation.listingManagement.domain.port.outbound.ListingPropertyRepository;
import com.sz.reservation.listingManagement.infrastructure.AddressInfoRequestDto;
import com.sz.reservation.listingManagement.infrastructure.ListingRequestDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@ActiveProfiles(profiles = {"test","default"})
@ContextConfiguration(classes = {RootConfig.class, ListingConfig.class})
@ExtendWith(SpringExtension.class)
@DisplayName("ListingPropertyUseCase Integration test")
@Disabled
class ListingPropertyUseCaseTestIT {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AccountRepository accountRepositoryMySql;

    @Autowired
    private ListingPropertyRepository listingPropertyRepository;

    @Autowired
    private ListingImageMetadataRepository listingImageMetadataRepository;

    @Autowired
    private ListingPropertyUseCase listingPropertyUseCase;

    private MockMvc mvc;


    private String listingTitle;
    private String listingDescription;
    private AddressInfoRequestDto addressInfoRequestDto;
    private int numberOfGuestAllowed;
    private int numberOfBeds;
    private int numberOfBedrooms;
    private int numberOfBathroom;
    private int pricePerNight;
    private PropertyType propertyType;
    private HousingType housingType;
    private ReservationType reservationType;
    private List<AmenitiesType> amenitiesTypeList;

    @BeforeEach
    public void setup(){
        mvc = MockMvcBuilders.standaloneSetup(context).build();
        listingTitle = "listing title";
        listingDescription = "New beautiful house near the beach";
        addressInfoRequestDto = new AddressInfoRequestDto("Argentina",
                "9 de Julio","A", "1111", "Buenos Aires", "CABA");
        numberOfGuestAllowed = 4;
        numberOfBeds = 4;
        numberOfBedrooms = 4;
        numberOfBathroom = 4;
        pricePerNight = 20;
        propertyType = PropertyType.HOUSE;
        housingType = HousingType.ENTIRE;
        reservationType = ReservationType.OWNER_APPROVAL;
        amenitiesTypeList = new ArrayList<>();
        amenitiesTypeList.add(AmenitiesType.GYM);
    }

    //Listing property tests
    @Test
    @Transactional
    public void Should_CreateListingInRepository_Correctly(){
        //arrange
        String hostId = createNotEnabledNotVerifiedAccount();
        ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //act
        String listingId = listingPropertyUseCase.listProperty(hostId,listingRequestDto);

        //assert
        Optional<ListingProperty> optionalListingProperty = listingPropertyRepository.findById(listingId);
        assertTrue(optionalListingProperty.isPresent());
    }


    //listing image upload test
    @Test
    @Transactional
    public void Should_UploadImage_correctly() throws IOException {
        //arrange
        String hostId = createNotEnabledNotVerifiedAccount();
        ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //create listing
        String listingId = listingPropertyUseCase.listProperty(hostId,listingRequestDto);
        File file = new File("src/test/resources/bird.jpg");
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))){
            assertNotNull(bufferedInputStream);

            //act
            ListingImageState listingImageState = listingPropertyUseCase.uploadListingImages(hostId,listingId,"bird.jpg",bufferedInputStream);

            //assert
            assertEquals(ListingImageState.CORRECT,listingImageState);
        }
    }



    @Test
    @Transactional
    public void Should_ThrowInvalidListingIdException_When_ListingId_DoesntExists() throws IOException {
        //arrange
        String hostId = createNotEnabledNotVerifiedAccount();
        ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //create random uuid
        String listingId = UuidCreator.getTimeOrderedEpoch().toString();

         File file = new File("src/test/resources/bird.jpg");
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))){
            assertNotNull(bufferedInputStream);
            //act and assert
           assertThrows(InvalidListingIdException.class,() ->{
               listingPropertyUseCase.uploadListingImages(hostId,listingId,"bird.jpg",bufferedInputStream);
           });
        }
    }


    @Test
    @Transactional
    public void Should_ThrowInvalidListingIdException_When_ListingId_DoesntBelongToHost() throws IOException {
        //arrange
        String hostId = createNotEnabledNotVerifiedAccount();
        ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //create listing
       listingPropertyUseCase.listProperty(hostId,listingRequestDto);

        //create other listing to simulate other person listing
        String otherListing = listingPropertyUseCase.listProperty(createNotEnabledNotVerifiedAccount(),listingRequestDto);

         File file = new File("src/test/resources/bird.jpg");
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))){
            assertNotNull(bufferedInputStream);
            //act and assert
            assertThrows(InvalidListingIdException.class,() ->{
                listingPropertyUseCase.uploadListingImages(hostId,otherListing,"bird.jpg",bufferedInputStream);
            });
        }
    }

    @Test
    @Transactional
    public void Should_ThrowInvalidListingIdException_When_ListingId_Disabled() throws IOException {
        //arrange
        String hostId = createNotEnabledNotVerifiedAccount();
        ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //create listing property
        String listingId = listingPropertyUseCase.listProperty(hostId,listingRequestDto);
        //disable property
        listingPropertyRepository.delete(listingId);

         File file = new File("src/test/resources/bird.jpg");
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))){
            assertNotNull(bufferedInputStream);
            //act and assert
            assertThrows(InvalidListingIdException.class,() ->{
                listingPropertyUseCase.uploadListingImages(hostId,listingId,"bird.jpg",bufferedInputStream);
            });
        }
    }

    @Test
    @Transactional
    public void Should_ReturnDiscardedImageState_When_ImageHasInvalidExtension() throws IOException {
        //arrange
        String hostId = createNotEnabledNotVerifiedAccount();
        ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //create listing property
        String listingId = listingPropertyUseCase.listProperty(hostId,listingRequestDto);

        File file = new File("src/test/resources/badExtensionImage.jp");
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))){
            assertNotNull(bufferedInputStream);

            //act and assert
            ListingImageState state = listingPropertyUseCase.uploadListingImages(hostId,listingId,"badExtensionImage.jp",bufferedInputStream);
            assertEquals(ListingImageState.DISCARDED,state);
        }
    }

    @Test
    @Transactional
    public void Should_ReturnDiscardedImageState_When_ImageHas_DifferentExtensionThanReal() throws IOException {
        //arrange
        String hostId = createNotEnabledNotVerifiedAccount();
        ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //create listing property
        String listingId = listingPropertyUseCase.listProperty(hostId,listingRequestDto);
        File file = new File("src/test/resources/extensionChangedImage.jpg");
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))){
            assertNotNull(bufferedInputStream);

            //act and assert
            ListingImageState state = listingPropertyUseCase.uploadListingImages(hostId,listingId,"extensionChangedImage.jpg",bufferedInputStream);
            assertEquals(ListingImageState.DISCARDED,state);
        }
    }

    @Test
    @Transactional
    public void Should_ReturnDiscardedImageState_When_ImageHas_BiggerSizeThanMax() throws IOException {
        //arrange
        String hostId = createNotEnabledNotVerifiedAccount();
        ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //create listing property
        String listingId = listingPropertyUseCase.listProperty(hostId,listingRequestDto);

        File file = new File("src/test/resources/18MbImage.png");
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))){
            assertNotNull(bufferedInputStream);

            //act and assert
            ListingImageState state = listingPropertyUseCase.uploadListingImages(hostId,listingId,"18MbImage.png",bufferedInputStream);
            assertEquals(ListingImageState.DISCARDED,state);
        }
    }

    @Test
    @Transactional
    public void Should_StoreValidImage_When_Valid_And_InvalidImage_Supplied() throws IOException {
        //arrange
        String hostId = createNotEnabledNotVerifiedAccount();
        ListingRequestDto listingRequestDto = new ListingRequestDto(listingTitle,
                listingDescription, addressInfoRequestDto,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, pricePerNight,
                propertyType, housingType, reservationType, amenitiesTypeList);

        //create listing property
        String listingId = listingPropertyUseCase.listProperty(hostId,listingRequestDto);

        File invalidFile = new File("src/test/resources//18MbImage.png");
        File validFile = new File("src/test/resources/bird.jpg");

        try (BufferedInputStream invalidImageBuffer = new BufferedInputStream(new FileInputStream(invalidFile));
             BufferedInputStream validImageBuffer = new BufferedInputStream(new FileInputStream(validFile))){
            assertNotNull(invalidImageBuffer);

            //act and assert

            //INVALID IMAGE
            ListingImageState state = listingPropertyUseCase.uploadListingImages(hostId,listingId,"18MbImage.png",invalidImageBuffer);
            assertEquals(ListingImageState.DISCARDED,state);

            //VALID IMAGE
            ListingImageState listingImageState = listingPropertyUseCase.uploadListingImages(hostId,listingId,"bird.jpg",validImageBuffer);
            assertEquals(ListingImageState.CORRECT,listingImageState);
        }


    }


    private String createNotEnabledNotVerifiedAccount(){
        //arrange
        String userId = UuidCreator.getTimeOrderedEpoch().toString();
        double random = Math.random();
        String email = "email" + random + "@gmail.com";

        Account account = new Account(
                userId,
                "username"+random,
                "name",
                "surname",
                email,
                new PhoneNumber(UuidCreator.getTimeOrderedEpoch().toString(),"54","1121010000"),
                LocalDate.now().minusDays(10),
                "password",
                false,
                false);


        //act
        accountRepositoryMySql.createAccount(account);

        //assert
        Optional<Account> optionalAccount = accountRepositoryMySql.findAccountByEmail(email);
        Assertions.assertTrue(optionalAccount.isPresent());

        return userId;
    }

}