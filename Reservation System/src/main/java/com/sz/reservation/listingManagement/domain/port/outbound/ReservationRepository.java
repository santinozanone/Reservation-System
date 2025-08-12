package com.sz.reservation.listingManagement.domain.port.outbound;

import com.sz.reservation.listingManagement.domain.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    List<Reservation> findConflictingReservations(String listingId, LocalDate checkIn, LocalDate checkOut);
    void save (Reservation reservation);
    void update (Reservation reservation);
    Optional<Reservation> findById(String reservationId);
}
