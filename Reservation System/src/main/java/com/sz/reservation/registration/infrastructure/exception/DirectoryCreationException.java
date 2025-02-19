package com.sz.reservation.registration.infrastructure.exception;

public class DirectoryCreationException extends RuntimeException{
    public DirectoryCreationException(String message,Throwable cause) {
        super(message,cause);
    }
}
