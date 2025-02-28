package com.sz.reservation.accountManagement.infrastructure.exception;

public class SendGridApiException extends RuntimeException{
    public SendGridApiException(String message) {
        super(message);
    }
}
