package com.sz.reservation.registration.domain.exception;

public class UsernameAlreadyRegisteredException extends RuntimeException{
    public UsernameAlreadyRegisteredException(Throwable cause) {
        super(cause);
    }
}
