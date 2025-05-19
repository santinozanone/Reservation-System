package com.sz.reservation.listingManagement.application.exception;

public class InvalidRequestTypeException extends RuntimeException{
    public InvalidRequestTypeException() {
    }

    public InvalidRequestTypeException(String message) {
        super(message);
    }
}
