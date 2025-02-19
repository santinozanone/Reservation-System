package com.sz.reservation.registration.infrastructure.exception;

public class FileWritingException extends RuntimeException{
    public FileWritingException(String message, Throwable cause) {
        super(message, cause);
    }
}
