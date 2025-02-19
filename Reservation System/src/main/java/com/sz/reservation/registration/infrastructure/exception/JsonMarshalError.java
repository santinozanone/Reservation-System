package com.sz.reservation.registration.infrastructure.exception;

public class JsonMarshalError extends RuntimeException{
    public JsonMarshalError(Throwable cause) {
        super("Error while processing json",cause);
    }
}
