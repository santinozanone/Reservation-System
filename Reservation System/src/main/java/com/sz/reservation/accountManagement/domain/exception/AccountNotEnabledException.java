package com.sz.reservation.accountManagement.domain.exception;

public class AccountNotEnabledException extends RuntimeException{
    private String email;
    public AccountNotEnabledException(String email) {
        super();
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
