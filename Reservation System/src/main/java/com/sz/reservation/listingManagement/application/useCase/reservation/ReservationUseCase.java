package com.sz.reservation.listingManagement.application.useCase.reservation;

import com.github.f4b6a3.uuid.UuidCreator;
import com.sz.reservation.listingManagement.application.exception.DateAlreadyBookedException;
import com.sz.reservation.listingManagement.application.exception.InvalidReservationIdException;
import com.sz.reservation.listingManagement.domain.ListingProperty;
import com.sz.reservation.listingManagement.domain.Reservation;
import com.sz.reservation.listingManagement.domain.ReservationStatus;
import com.sz.reservation.listingManagement.domain.ReservationType;
import com.sz.reservation.listingManagement.domain.exception.InvalidListingIdException;
import com.sz.reservation.listingManagement.domain.port.outbound.ListingPropertyRepository;
import com.sz.reservation.listingManagement.domain.port.outbound.ListingReservationLockRepository;
import com.sz.reservation.listingManagement.domain.port.outbound.ReservationRepository;
import com.sz.reservation.listingManagement.infrastructure.adapter.inbound.reservation.ApprovalReservationRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class ReservationUseCase {
    private ListingReservationLockRepository reservationLockRepository;
    private ListingPropertyRepository listingPropertyRepository;
    private ReservationRepository reservationRepository;

    @Autowired
    public ReservationUseCase(ListingReservationLockRepository reservationLockRepository, ListingPropertyRepository listingPropertyRepository,
                              ReservationRepository reservationRepository) {
        this.reservationLockRepository = reservationLockRepository;
        this.listingPropertyRepository = listingPropertyRepository;
        this.reservationRepository = reservationRepository;
    }

    @Retryable(retryFor = PessimisticLockingFailureException.class,maxAttempts = 3,backoff =  @Backoff(delay = 150))
    @Transactional(transactionManager = "listing.transactionManager",isolation = Isolation.READ_COMMITTED)
    public void makeReservation(String guestId,String listingId, LocalDate checkIn, LocalDate checkOut,int numOfGuests){
        ListingProperty listing = findListingPropertyById(listingId);

        // lock listing_reservation row
        reservationLockRepository.lockByListingId(listingId);
        List<Reservation> conflictingReservations = reservationRepository.findConflictingReservations(listingId,checkIn,checkOut);
        if (!conflictingReservations.isEmpty()) {
            throw new DateAlreadyBookedException("The requested date is not available");
        }
        Reservation reservation = createReservation(guestId,listing,checkIn,checkOut,numOfGuests);
        reservationRepository.save(reservation); // save reservation

        //TODO: notify host and guest about the new reservation
    }

    @Transactional(transactionManager = "listing.transactionManager")
    public void approvalReservation(String hostId, ApprovalReservationRequestDto approvalReservationRequestDto){
        String reservationId = approvalReservationRequestDto.getReservationId();
        Reservation reservation = validateReservation(hostId,reservationId);
        if (approvalReservationRequestDto.isApproved()) {
            reservation.confirmReservation();
        } else {
            reservation.disapproveReservation();
        }
        reservationRepository.update(reservation);

        //TODO: notify guest
    }

    //TODO: CANCEL RESERVATION





    private ListingProperty findListingPropertyById(String listingId){
        Optional<ListingProperty> optionalListingProperty = listingPropertyRepository.findById(listingId);
        if (optionalListingProperty.isEmpty()){
            throw new InvalidListingIdException("The listing id is not valid");
        }
        return optionalListingProperty.get();
    }

    private Reservation createReservation(String guestId,ListingProperty listing,LocalDate checkIn,LocalDate checkOut,int numOfGuests){
        ReservationStatus status = ReservationStatus.PENDING_APPROVAL;
        if (listing.getReservationType() == ReservationType.AUTOMATIC_APPROVAL){
            status = ReservationStatus.CONFIRMED;
        }
        Reservation reservation = Reservation.create(UuidCreator.getTimeOrderedEpoch().toString(),guestId,listing.getHostId(),
                listing.getId(),checkIn,checkOut,numOfGuests,calculatePrice(listing.getPricePerNight(),checkIn,checkOut),status);

       return reservation;
    }

    private BigDecimal calculatePrice(BigDecimal pricePerNight,LocalDate checkIn,LocalDate checkOut){
        BigDecimal totalPrice;
        BigDecimal numberOfDays = BigDecimal.valueOf(checkIn.until(checkOut).getDays());
        totalPrice = pricePerNight.multiply(numberOfDays);
        return totalPrice;
    }

    private Reservation validateReservation(String hostId,String reservationId){
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
        if (optionalReservation.isEmpty()){
            throw new InvalidReservationIdException("The reservationId: "+reservationId+" is not valid");
        }
        if (!optionalReservation.get().getHostId().equals(hostId)){
            throw new InvalidReservationIdException("The reservationId: "+reservationId+" is not valid");
        }
        return optionalReservation.get();
    }
}
