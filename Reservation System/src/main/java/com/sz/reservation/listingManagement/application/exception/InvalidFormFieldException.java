package com.sz.reservation.listingManagement.application.exception;

public class InvalidFormFieldException extends RuntimeException{
    private String field;
    public InvalidFormFieldException() {
    }

    public InvalidFormFieldException(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
