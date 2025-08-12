package com.sz.reservation.listingManagement.infrastructure.adapter.outbound.reservation;

import com.github.f4b6a3.uuid.UuidCreator;
import com.sz.reservation.globalConfiguration.RootConfig;
import com.sz.reservation.listingManagement.domain.*;
import com.sz.reservation.listingManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.listingManagement.domain.port.outbound.ListingPropertyRepository;
import com.sz.reservation.listingManagement.domain.port.outbound.ReservationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(value = {"test","default"})
@ContextConfiguration(classes = {RootConfig.class})
@DisplayName("Integration testing MySqlReservationRepositoryTest")
@WebAppConfiguration
class MySqlReservationRepositoryTestIT {


    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ListingPropertyRepository listingPropertyRepository;

    @Test
    @Transactional(transactionManager = "listing.transactionManager")
    public void should_createReservation_Correctly(){
        //arrange
        String reservationId = "0197ec01-6e28-7d66-b1d2-e70b8fe30ff2";
        String hostId = createAccount();
        String guestId = createAccount();
        ListingProperty listing = createListingProperty(hostId,true);
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(2);

        Reservation reservation = Reservation.create(reservationId,
                guestId,
                hostId,
                listing.getId(),
                checkIn,
                checkOut,
                2,
                calculatePrice(listing.getPricePerNight(),checkIn,checkOut),
                ReservationStatus.CONFIRMED);

        //act
        reservationRepository.save(reservation);
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);

        //assert
        assertTrue(optionalReservation.isPresent());
        assertEquals(reservation,optionalReservation.get());
    }

    @Test
    @Transactional(transactionManager = "listing.transactionManager")
    public void should_UpdateReservation_Correctly(){
        String reservationId = "0197ec01-6e28-7d66-b1d2-e70b8fe30ff2";
        String hostId = createAccount();
        String guestId = createAccount();
        ListingProperty listing = createListingProperty(hostId,true);
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(2);

        Reservation reservation = Reservation.create(reservationId,
                guestId,
                hostId,
                listing.getId(),
                checkIn,
                checkOut,
                2,
                calculatePrice(listing.getPricePerNight(),checkIn,checkOut),
                ReservationStatus.CONFIRMED);

        //act
        reservationRepository.save(reservation);
        reservation.cancelReservation();
        reservationRepository.update(reservation);

        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
        //assert
        assertTrue(optionalReservation.isPresent());
        assertEquals(reservation,optionalReservation.get());
    }


    @Test
    @Transactional(transactionManager = "listing.transactionManager")
    public void should_findConflictingReservations_when_checkOutOverlaps(){
        //arrange
        String reservationId = "0197ec01-6e28-7d66-b1d2-e70b8fe30ff2";
        String hostId = createAccount();
        String guestId = createAccount();
        ListingProperty listing = createListingProperty(hostId,true);
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(2);

        Reservation reservation = Reservation.create(reservationId,
                guestId,
                hostId,
                listing.getId(),
                checkIn,
                checkOut,
                2,
                calculatePrice(listing.getPricePerNight(),checkIn,checkOut),
                ReservationStatus.CONFIRMED);


        //act
        reservationRepository.save(reservation);

        // try to checkIn 2 days before and leave at the same day should be a conflict. because it overlaps
        List<Reservation> reservations = reservationRepository.findConflictingReservations(listing.getId(),checkIn.minusDays(2),checkOut);

        //assert
        assertEquals(1,reservations.size() );
        assertEquals(reservation,reservations.get(0));
    }

    @Test
    @Transactional(transactionManager = "listing.transactionManager")
    public void should_findConflictingReservations_when_checkInOverlaps(){
        //arrange
        String reservationId = "0197ec01-6e28-7d66-b1d2-e70b8fe30ff2";
        String hostId = createAccount();
        String guestId = createAccount();
        ListingProperty listing = createListingProperty(hostId,true);
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(2);

        Reservation reservation = Reservation.create(reservationId,
                guestId,
                hostId,
                listing.getId(),
                checkIn,
                checkOut,
                2,
                calculatePrice(listing.getPricePerNight(),checkIn,checkOut),
                ReservationStatus.CONFIRMED);


        //act
        reservationRepository.save(reservation);

        // try to checkIn 2 days before and leave at the same day should be a conflict. because it overlaps
        List<Reservation> reservations = reservationRepository.findConflictingReservations(listing.getId(),checkIn,checkOut.plusDays(1));

        //assert
        assertEquals(1,reservations.size() );
        assertEquals(reservation,reservations.get(0));
    }

    @Test
    @Transactional(transactionManager = "listing.transactionManager")
    public void should_findConflictingReservations_when_sameDate(){
        //arrange
        String reservationId = "0197ec01-6e28-7d66-b1d2-e70b8fe30ff2";
        String hostId = createAccount();
        String guestId = createAccount();
        ListingProperty listing = createListingProperty(hostId,true);
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(2);

        Reservation reservation = Reservation.create(reservationId,
                guestId,
                hostId,
                listing.getId(),
                checkIn,
                checkOut,
                2,
                calculatePrice(listing.getPricePerNight(),checkIn,checkOut),
                ReservationStatus.CONFIRMED);


        //act
        reservationRepository.save(reservation);

        // try to checkIn 2 days before and leave at the same day should be a conflict. because it overlaps
        List<Reservation> reservations = reservationRepository.findConflictingReservations(listing.getId(),checkIn,checkOut);

        //assert
        assertEquals(1,reservations.size() );
        assertEquals(reservation,reservations.get(0));
    }

    @Test
    @Transactional(transactionManager = "listing.transactionManager")
    public void should_findConflictingReservations_when_dateIsInMiddle(){
        //arrange
        String reservationId = "0197ec01-6e28-7d66-b1d2-e70b8fe30ff2";
        String hostId = createAccount();
        String guestId = createAccount();
        ListingProperty listing = createListingProperty(hostId,true);
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(3);

        Reservation reservation = Reservation.create(reservationId,
                guestId,
                hostId,
                listing.getId(),
                checkIn,
                checkOut,
                2,
                calculatePrice(listing.getPricePerNight(),checkIn,checkOut),
                ReservationStatus.CONFIRMED);


        //act
        reservationRepository.save(reservation);

        // try to checkIn 1 day after and leave 1 day before day should be a conflict. because its in the middle
        List<Reservation> reservations = reservationRepository.findConflictingReservations(listing.getId(),checkIn.plusDays(1),checkOut.minusDays(1));

        //assert
        assertEquals(1,reservations.size() );
        assertEquals(reservation,reservations.get(0));
    }


    private BigDecimal calculatePrice(BigDecimal pricePerNight,LocalDate checkIn,LocalDate checkOut){
        BigDecimal totalPrice;
        BigDecimal numberOfDays = BigDecimal.valueOf(checkIn.until(checkOut).getDays());
        totalPrice = pricePerNight.multiply(numberOfDays);
        return totalPrice;
    }
    private String createAccount(){
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

    private ListingProperty createListingProperty(String hostId,boolean automaticApproval){
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
                "A","Argentina","CABA","1111", "Buenos Aires", "9 de Julio"),
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
        assertEquals(listingProperty,optionalListingProperty.get());
        return listingProperty;
    }
}