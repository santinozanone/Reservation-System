package com.sz.reservation.registration.domain.exception;

public class MediaNotSupportedException extends RuntimeException{
    public MediaNotSupportedException() {
        super("the submitted media format is not supported");
    }
}
