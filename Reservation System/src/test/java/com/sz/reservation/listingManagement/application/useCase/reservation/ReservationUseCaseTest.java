package com.sz.reservation.listingManagement.application.useCase.reservation;

import com.github.f4b6a3.uuid.UuidCreator;
import com.sz.reservation.listingManagement.application.exception.DateAlreadyBookedException;
import com.sz.reservation.listingManagement.domain.*;
import com.sz.reservation.listingManagement.domain.exception.InvalidListingIdException;
import com.sz.reservation.listingManagement.domain.port.outbound.ListingPropertyRepository;
import com.sz.reservation.listingManagement.domain.port.outbound.ListingReservationLockRepository;
import com.sz.reservation.listingManagement.domain.port.outbound.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit testing ReservationUseCaseTest")
class ReservationUseCaseTest {

    @Mock
    private ListingReservationLockRepository reservationLockRepository;

    @Mock
    private ListingPropertyRepository listingPropertyRepository;

    @Mock
    private ReservationRepository reservationRepository;

    private ReservationUseCase reservationUseCase;

    @BeforeEach
    public void init(){
        reservationUseCase = new ReservationUseCase(reservationLockRepository,listingPropertyRepository,
                reservationRepository);
    }


    @Test
    public void should_CreateReservation_whenNoConflict(){
        //arrange
        ListingProperty listingProperty = createListing();
        Mockito.when(listingPropertyRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(listingProperty));
        doNothing().when(reservationLockRepository).lockByListingId(ArgumentMatchers.any());
        Mockito.when(reservationRepository.findConflictingReservations(ArgumentMatchers.any(), ArgumentMatchers.any(),ArgumentMatchers.any())).thenReturn(new ArrayList<>());

        //act and assert
        reservationUseCase.makeReservation(UuidCreator.getTimeOrderedEpoch().toString(),
                UuidCreator.getTimeOrderedEpoch().toString(),
                LocalDate.now(),LocalDate.now().plusDays(4),2);
    }

    @Test
    public void should_ThrowInvalidListingId_whenInvalidListingId() {
        //arrange
        Mockito.when(listingPropertyRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());
        assertThrows(InvalidListingIdException.class, () -> {
            reservationUseCase.makeReservation(UuidCreator.getTimeOrderedEpoch().toString(),
                    UuidCreator.getTimeOrderedEpoch().toString(),
                    LocalDate.now(),LocalDate.now().plusDays(4),2);
        });
    }

    @Test
    public void should_ThrowDateAlreadyBookedException_When_DateIsBooked(){
        //arrange
        ArrayList<Reservation> conflictingReservations = new ArrayList<>();
        conflictingReservations.add(createReservation());
        ListingProperty listingProperty = createListing();
        Mockito.when(listingPropertyRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(listingProperty));
        doNothing().when(reservationLockRepository).lockByListingId(ArgumentMatchers.any());
        Mockito.when(reservationRepository.findConflictingReservations(ArgumentMatchers.any(), ArgumentMatchers.any(),ArgumentMatchers.any())).thenReturn(conflictingReservations);

        //act and assert
        assertThrows(DateAlreadyBookedException.class, () -> {
            reservationUseCase.makeReservation(UuidCreator.getTimeOrderedEpoch().toString(),
                    UuidCreator.getTimeOrderedEpoch().toString(),
                    LocalDate.now(), LocalDate.now().plusDays(4), 2);
        });
    }


    private Reservation createReservation(){
        Reservation reservation = Reservation.create(
                UuidCreator.getTimeOrderedEpoch().toString(),
                UuidCreator.getTimeOrderedEpoch().toString(),
                UuidCreator.getTimeOrderedEpoch().toString(),
                UuidCreator.getTimeOrderedEpoch().toString(),
                LocalDate.now(),
                LocalDate.now().plusDays(4),
                2,
                BigDecimal.valueOf(200),
                ReservationStatus.CONFIRMED);
        return reservation;
    }

    private ListingProperty createListing(){
        List<AmenitiesType> amenitiesTypeList = new ArrayList<>();
        amenitiesTypeList.add(AmenitiesType.GYM);
        amenitiesTypeList.add(AmenitiesType.TV);

        ListingProperty listingProperty = new ListingProperty(
                UuidCreator.getTimeOrderedEpoch().toString(),
                UuidCreator.getTimeOrderedEpoch().toString(),
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
                ReservationType.AUTOMATIC_APPROVAL,
                amenitiesTypeList,
                true);
        return listingProperty;
    }


}