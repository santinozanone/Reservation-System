package com.sz.reservation.registration.domain.exception;

public class UsernameAlreadyRegisteredException extends RuntimeException{
    private String username;
    public UsernameAlreadyRegisteredException(String username,Throwable cause) {
        super(username,cause);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
