package com.sz.reservation.accountManagement.infrastructure.exception;

public class DirectoryCreationException extends RuntimeException{
    public DirectoryCreationException(String message,Throwable cause) {
        super(message,cause);
    }
}
