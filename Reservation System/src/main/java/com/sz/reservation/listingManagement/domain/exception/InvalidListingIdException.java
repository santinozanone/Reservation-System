package com.sz.reservation.listingManagement.domain.exception;

public class InvalidListingIdException extends RuntimeException{
    public InvalidListingIdException(String message) {
        super(message);
    }
}
