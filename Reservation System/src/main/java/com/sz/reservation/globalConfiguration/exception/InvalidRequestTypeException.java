package com.sz.reservation.globalConfiguration.exception;

public class InvalidRequestTypeException extends RuntimeException{
    public InvalidRequestTypeException() {
    }

    public InvalidRequestTypeException(String message) {
        super(message);
    }
}
