package com.sz.reservation.accountManagement.infrastructure.exception;

public class NetworkErrorException extends RuntimeException{
    public NetworkErrorException(String message,Throwable cause) {
        super(message,cause);
    }
}
