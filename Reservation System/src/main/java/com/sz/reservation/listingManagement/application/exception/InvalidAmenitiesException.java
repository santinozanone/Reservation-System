package com.sz.reservation.listingManagement.application.exception;

public class InvalidAmenitiesException extends RuntimeException{
    public InvalidAmenitiesException() {
    }

    public InvalidAmenitiesException(String message) {
        super(message);
    }
}
