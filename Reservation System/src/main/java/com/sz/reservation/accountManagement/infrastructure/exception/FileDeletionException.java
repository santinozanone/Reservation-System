package com.sz.reservation.accountManagement.infrastructure.exception;

public class FileDeletionException extends RuntimeException{
    public FileDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
