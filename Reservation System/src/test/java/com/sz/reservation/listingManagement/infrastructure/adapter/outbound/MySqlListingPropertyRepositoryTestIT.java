package com.sz.reservation.listingManagement.infrastructure.adapter.outbound;

import com.github.f4b6a3.uuid.UuidCreator;

import com.sz.reservation.globalConfiguration.RootConfig;
import com.sz.reservation.listingManagement.configuration.ListingConfig;
import com.sz.reservation.listingManagement.domain.ListingProperty;
import com.sz.reservation.listingManagement.domain.*;
import com.sz.reservation.listingManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.listingManagement.infrastructure.adapter.outbound.listing.MySqlListingPropertyRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {RootConfig.class, ListingConfig.class})
@ActiveProfiles(value = {"test","default"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Integration testing MySqlListingPropertyRepositoryTest")
class MySqlListingPropertyRepositoryTestIT {

    @Autowired
    @Qualifier("listing.jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private MySqlListingPropertyRepository mySqlListingPropertyRepository;
    private AccountRepository accountRepositoryMySql;

    private String propertyId;
    private String hostId;
    private String listingDescription;
    private String listingTitle;
    private AddressInfo addressInfo;
    private int numberOfGuestAllowed;
    private int numberOfBeds;
    private int numberOfBedrooms;
    private int numberOfBathroom;
    private int pricePerNight;
    private PropertyType propertyType;
    private HousingType housingType;
    private ReservationType reservationType;
    private List<AmenitiesType> amenitiesTypeList;
    private boolean enabled;


    @BeforeAll
    public void initVariables(){
        mySqlListingPropertyRepository = new MySqlListingPropertyRepository(jdbcTemplate);
        accountRepositoryMySql = new AccountMySqlRepository(jdbcTemplate);

        propertyId = UuidCreator.getTimeOrderedEpoch().toString();
        hostId = UuidCreator.getTimeOrderedEpoch().toString();
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
        enabled = true;

        amenitiesTypeList = new ArrayList<>();
        amenitiesTypeList.add(AmenitiesType.GYM);
        amenitiesTypeList.add(AmenitiesType.TV);
        addressInfo = new AddressInfo(UuidCreator.getTimeOrderedEpoch().toString(),"A",
                "Argentina","CABA", "1111", "Buenos Aires", "9 de Julio");
    }

    @Test
    @Transactional(transactionManager = "listing.transactionManager")

    public void Should_InsertListingCorrectly_When_ValidInput(){
        //arrange
        ListingProperty listingProperty = new ListingProperty(propertyId,hostId,listingTitle,
                listingDescription, addressInfo,
                numberOfGuestAllowed, numberOfBeds, numberOfBedrooms, numberOfBathroom, BigDecimal.valueOf(pricePerNight),
                propertyType, housingType, reservationType, amenitiesTypeList,enabled);
        String hostId = createVerifiedAccount();

        //act
        mySqlListingPropertyRepository.create(listingProperty);

        //assert
        Optional<ListingProperty> optionalListingProperty = mySqlListingPropertyRepository.findById(propertyId);
        assertTrue(optionalListingProperty.isPresent());
        assertListingPropertiesEqual(listingProperty,optionalListingProperty.get());


    }




    private void assertListingPropertiesEqual(ListingProperty expected, ListingProperty actual) {
        assertNotNull(expected, "Expected listing is null");
        assertNotNull(actual, "Actual listing is null");

        assertEquals(expected.getId(), actual.getId(), "Mismatch in ID");
        assertEquals(expected.getListingTitle(), actual.getListingTitle(), "Mismatch in Title");
        assertEquals(expected.getListingDescription(), actual.getListingDescription(), "Mismatch in Description");

        assertNotNull(expected.getAddressInfo(), "Expected address is null");
        assertNotNull(actual.getAddressInfo(), "Actual address is null");

        assertEquals(expected.getAddressInfo().getId(),actual.getAddressInfo().getId(),"Mismatch in address ID");
        assertEquals(expected.getAddressInfo().getCountry(), actual.getAddressInfo().getCountry(), "Mismatch in Country");
        assertEquals(expected.getAddressInfo().getStreetAddress(), actual.getAddressInfo().getStreetAddress(), "Mismatch in Street Address");
        assertEquals(expected.getAddressInfo().getApartmentNumber(), actual.getAddressInfo().getApartmentNumber(), "Mismatch in Apartment Number");
        assertEquals(expected.getAddressInfo().getPostalCode(), actual.getAddressInfo().getPostalCode(), "Mismatch in Postal Code");
        assertEquals(expected.getAddressInfo().getRegion(), actual.getAddressInfo().getRegion(), "Mismatch in Region");
        assertEquals(expected.getAddressInfo().getLocality(), actual.getAddressInfo().getLocality(), "Mismatch in Locality");

        assertEquals(expected.getNumberOfGuestAllowed(), actual.getNumberOfGuestAllowed(), "Mismatch in Number of Guests Allowed");
        assertEquals(expected.getNumberOfBeds(), actual.getNumberOfBeds(), "Mismatch in Number of Beds");
        assertEquals(expected.getNumberOfBedrooms(), actual.getNumberOfBedrooms(), "Mismatch in Number of Bedrooms");
        assertEquals(expected.getNumberOfBathroom(), actual.getNumberOfBathroom(), "Mismatch in Number of Bathrooms");
        assertEquals(expected.getPricePerNight(), actual.getPricePerNight(), "Mismatch in Price Per Night");


        assertEquals(expected.getPropertyType(), actual.getPropertyType(), "Mismatch in Property Type");
        assertEquals(expected.getReservationType(), actual.getReservationType(), "Mismatch in Reservation Type");
        assertEquals(expected.getHousingType(), actual.getHousingType(), "Mismatch in Housing Type");
        assertEquals(expected.isEnabled(), actual.isEnabled(), "Mismatch in Enabled status");

        for (AmenitiesType expectedAmenity : expected.getAmenities()) {
            assertTrue(actual.getAmenities().contains(expectedAmenity));
        }
    }






    private String createVerifiedAccount(){
        //arrange
        String userId = UuidCreator.getTimeOrderedEpoch().toString();
        Account accountCreationData = new Account(
                userId,
                "username",
                "name",
                "surname",
                "inventedEmail@miau.com",
                true);

        //act
        accountRepositoryMySql.save(accountCreationData);

        //assert
        Optional<Account> optionalAccount = accountRepositoryMySql.findByEmail("inventedEmail@miau.com");
        Assertions.assertTrue(optionalAccount.isPresent());
        Account account = optionalAccount.get();

        //verify account
        Assertions.assertTrue(account.isEnabled());

        return userId;
    }





}