package com.sz.reservation.accountManagement.domain.service;

public interface PhoneNumberValidator {
    boolean isValid(String countryCode,String phoneNumber);
}
