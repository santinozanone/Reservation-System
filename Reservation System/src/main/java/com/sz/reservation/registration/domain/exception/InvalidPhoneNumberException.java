package com.sz.reservation.registration.domain.exception;

public class InvalidPhoneNumberException extends RuntimeException{
    private String countryCode;
    private String phoneNumber;

    public InvalidPhoneNumberException(String countryCode, String phoneNumber) {
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
