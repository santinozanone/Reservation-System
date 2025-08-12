package com.sz.reservation.listingManagement.application.exception;

public class DateAlreadyBookedException extends RuntimeException{
    public DateAlreadyBookedException(String message) {
        super(message);
    }
}
