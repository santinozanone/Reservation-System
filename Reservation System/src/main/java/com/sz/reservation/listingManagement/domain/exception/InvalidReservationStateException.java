package com.sz.reservation.listingManagement.domain.exception;

public class InvalidReservationStateException extends RuntimeException{
    public InvalidReservationStateException(String message) {
        super(message);
    }
}
