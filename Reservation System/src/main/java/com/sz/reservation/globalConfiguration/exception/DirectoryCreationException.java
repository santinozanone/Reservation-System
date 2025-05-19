package com.sz.reservation.globalConfiguration.exception;

public class DirectoryCreationException extends RuntimeException{
    public DirectoryCreationException(String message,Throwable cause) {
        super(message,cause);
    }

    public DirectoryCreationException(String message) {
        super(message);
    }
}
