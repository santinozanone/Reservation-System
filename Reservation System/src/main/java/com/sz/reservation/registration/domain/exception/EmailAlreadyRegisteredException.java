package com.sz.reservation.registration.domain.exception;

public class EmailAlreadyRegisteredException extends RuntimeException{
    public EmailAlreadyRegisteredException(Throwable cause) {
        super(cause);
    }
}
