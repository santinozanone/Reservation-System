package com.sz.reservation.accountManagement.domain.exception;

public class AccountNotExistentException extends RuntimeException {
    private String email;

    public AccountNotExistentException(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
