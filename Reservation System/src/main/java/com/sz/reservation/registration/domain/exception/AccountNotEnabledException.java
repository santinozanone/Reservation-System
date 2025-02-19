package com.sz.reservation.registration.domain.exception;

public class AccountNotEnabledException extends RuntimeException{
    public AccountNotEnabledException() {
        super("Account is not enabled");
    }
}
