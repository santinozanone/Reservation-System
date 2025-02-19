package com.sz.reservation.registration.infrastructure.exception;

public class SendGridApiException extends RuntimeException{
    public SendGridApiException(String message) {
        super(message);
    }
}
