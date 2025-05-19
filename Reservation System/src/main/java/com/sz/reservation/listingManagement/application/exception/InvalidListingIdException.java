package com.sz.reservation.listingManagement.application.exception;

public class InvalidListingIdException extends RuntimeException{
    public InvalidListingIdException() {
    }

    public InvalidListingIdException(String message) {
        super(message);
    }
}
