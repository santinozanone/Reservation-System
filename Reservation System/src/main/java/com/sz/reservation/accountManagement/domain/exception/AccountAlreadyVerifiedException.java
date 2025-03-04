package com.sz.reservation.accountManagement.domain.exception;

public class AccountAlreadyVerifiedException extends RuntimeException{
    private String userId;

    public AccountAlreadyVerifiedException(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
