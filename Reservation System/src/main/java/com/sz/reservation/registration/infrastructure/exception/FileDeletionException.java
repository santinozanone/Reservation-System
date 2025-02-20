package com.sz.reservation.registration.infrastructure.exception;

public class FileDeletionException extends RuntimeException{
    public FileDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
