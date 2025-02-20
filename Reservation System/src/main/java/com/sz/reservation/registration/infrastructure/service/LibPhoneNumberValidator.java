package com.sz.reservation.registration.infrastructure.service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.sz.reservation.registration.domain.service.PhoneNumberValidator;
import com.sz.reservation.registration.infrastructure.exception.LibPhoneParserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class LibPhoneNumberValidator implements PhoneNumberValidator {
    private Logger logger = LogManager.getLogger(LibPhoneNumberValidator.class);
    //E.164 Standard phone number validator
    @Override
    public boolean isValid(String countryCode,String phoneNumber) {
        if (countryCode == null || phoneNumber == null) throw new IllegalArgumentException("country code or phone number cannot be null");
        if (countryCode.isEmpty() || phoneNumber.isEmpty()) throw new IllegalArgumentException("country code or phone number cannot be empty");
        logger.debug("starting phone number validator with countryCode:{} , and phoneNumber:{} ",countryCode,phoneNumber);
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            String phoneNumberWithCountryCode = countryCode.concat(phoneNumber);
            Phonenumber.PhoneNumber parsedPhone = phoneNumberUtil.parse(phoneNumberWithCountryCode, null);
            return phoneNumberUtil.isValidNumber(parsedPhone);
        } catch (NumberParseException e) {
            logger.error("NumberParseException when parsing phone number with countryCode:{} , and phoneNumber:{} ",countryCode,phoneNumber);
            throw new LibPhoneParserException("Exception while parsing phone number with countryCode: "+ countryCode + " and phoneNumber: "+phoneNumber,e); // throw specialized exception instead of runtime
        }
    }


}
