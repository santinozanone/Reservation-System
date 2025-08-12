package com.sz.reservation.listingManagement.infrastructure.adapter.outbound.reservation;

import com.github.f4b6a3.uuid.UuidCreator;
import com.sz.reservation.globalConfiguration.RootConfig;
import com.sz.reservation.listingManagement.domain.*;
import com.sz.reservation.listingManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.listingManagement.domain.port.outbound.ListingPropertyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(value = {"test", "default"})
@ContextConfiguration(classes = {RootConfig.class})
@DisplayName("Integration testing ListingReservationLockMysqlRepositoryTest")
@WebAppConfiguration
class ListingReservationLockMysqlRepositoryTestIT {

    @Autowired
    private ListingPropertyRepository listingPropertyRepository;

    @Autowired
    private ListingReservationLockMysqlRepository listingReservationLockMysqlRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @Transactional(transactionManager = "listing.transactionManager")
    public void should_acquireLock_correctly() {
        //arrange
        ListingProperty listing = createListingProperty(createAccount(), true);
        listingReservationLockMysqlRepository.lockByListingId(listing.getId());
        tryToLock(listing.getId());
        //act and assert
       /* ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> tryToLock(listing.getId()));
        try {
            future.get(3, TimeUnit.SECONDS);
            fail("Row was not locked");
        } catch (ExecutionException | InterruptedException | CancellationException | TimeoutException e) {
            executor.shutdown();
        }*/
    }

    @Transactional(transactionManager = "listing.transactionManager", propagation = Propagation.REQUIRES_NEW)
    private void tryToLock(String listingId) {
        listingReservationLockMysqlRepository.lockByListingId(listingId);
    }


    private String createAccount() {
        //arrange
        String userId = UuidCreator.getTimeOrderedEpoch().toString();
        String username = "username".concat(String.valueOf(Math.random()));
        String email = "inventedEmail@miau.com".concat(String.valueOf(Math.random()));
        Account accountCreationData = new Account(
                userId,
                username,
                "name",
                "surname",
                email,
                true);

        //act
        accountRepository.save(accountCreationData);

        //assert
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        Assertions.assertTrue(optionalAccount.isPresent());
        Account account = optionalAccount.get();

        //verify account
        Assertions.assertTrue(account.isEnabled());
        return userId;
    }

    private ListingProperty createListingProperty(String hostId, boolean automaticApproval) {
        String listingId = UuidCreator.getTimeOrderedEpoch().toString();
        ReservationType reservationType = ReservationType.OWNER_APPROVAL;
        if (automaticApproval) reservationType = ReservationType.AUTOMATIC_APPROVAL;
        List<AmenitiesType> amenitiesTypeList = new ArrayList<>();
        amenitiesTypeList.add(AmenitiesType.GYM);
        amenitiesTypeList.add(AmenitiesType.TV);

        ListingProperty listingProperty = new ListingProperty(
                listingId,
                hostId,
                "My house",
                "the best",
                new AddressInfo(UuidCreator.getTimeOrderedEpoch().toString(),
                        "A", "Argentina", "CABA", "1111", "Buenos Aires", "9 de Julio"),
                4,
                4,
                2,
                1,
                BigDecimal.valueOf(20),
                PropertyType.HOUSE,
                HousingType.ENTIRE,
                reservationType,
                amenitiesTypeList,
                true);
        listingPropertyRepository.create(listingProperty);

        Optional<ListingProperty> optionalListingProperty = listingPropertyRepository.findById(listingId);
        Assertions.assertTrue(optionalListingProperty.isPresent());
        assertEquals(listingProperty, optionalListingProperty.get());
        return listingProperty;
    }
}