package com.sz.reservation.accountManagement.domain.exception;

public class EmailAlreadyRegisteredException extends RuntimeException{
    private String email;

    public EmailAlreadyRegisteredException(String email) {
        this.email = email;
    }

    public EmailAlreadyRegisteredException(String email, Throwable cause) {
        super(cause);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
