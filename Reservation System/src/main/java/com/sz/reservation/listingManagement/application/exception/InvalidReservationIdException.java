package com.sz.reservation.listingManagement.application.exception;

public class InvalidReservationIdException extends RuntimeException{
    public InvalidReservationIdException(String message) {
        super(message);
    }
}
