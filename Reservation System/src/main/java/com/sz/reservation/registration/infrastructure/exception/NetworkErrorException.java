package com.sz.reservation.registration.infrastructure.exception;

public class NetworkErrorException extends RuntimeException{
    public NetworkErrorException(String message,Throwable cause) {
        super(message,cause);
    }
}
