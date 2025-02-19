package com.sz.reservation.registration.domain.service;

import com.sz.reservation.registration.domain.model.PhoneNumber;

public interface PhoneNumberValidator {
    boolean isValid(String countryCode,String phoneNumber);
}
