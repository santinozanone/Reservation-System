package com.sz.reservation.registration.infrastructure.service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.sz.reservation.registration.application.useCase.PhoneNumberValidator;

public class libPhoneNumberValidator implements PhoneNumberValidator {
    //E.164 Standard phone number validator
    @Override
    public boolean isValid(String countryCode,String phoneNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            System.out.println(phoneNumber);
            String phoneNumberWithCountryCode = countryCode.concat(phoneNumber);
            Phonenumber.PhoneNumber parsedPhone = phoneNumberUtil.parse(phoneNumberWithCountryCode, null);
            return phoneNumberUtil.isValidNumber(parsedPhone);
        } catch (NumberParseException e) {
            throw new RuntimeException(e); // throw specialized exception instead of runtime
        }
    }


}
