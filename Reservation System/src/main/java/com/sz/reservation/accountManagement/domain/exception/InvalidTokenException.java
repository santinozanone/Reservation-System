package com.sz.reservation.accountManagement.domain.exception;

public class InvalidTokenException extends RuntimeException{
    private String token;

    public InvalidTokenException(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
