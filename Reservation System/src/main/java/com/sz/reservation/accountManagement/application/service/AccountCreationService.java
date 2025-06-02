package com.sz.reservation.accountManagement.application.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.sz.reservation.accountManagement.domain.exception.InvalidPhoneNumberException;
import com.sz.reservation.accountManagement.domain.model.Account;
import com.sz.reservation.accountManagement.domain.model.AccountVerificationToken;
import com.sz.reservation.accountManagement.domain.model.PhoneNumber;
import com.sz.reservation.accountManagement.domain.model.ProfilePicture;
import com.sz.reservation.accountManagement.domain.service.HashingService;
import com.sz.reservation.accountManagement.domain.service.PhoneNumberValidator;
import com.sz.reservation.accountManagement.infrastructure.dto.AccountCreationRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;


public class AccountCreationService {
    private PhoneNumberValidator phoneNumberValidator;
    private HashingService hashingService;
    private final String INTERNATIONAL_PREFIX = "+";
    private Logger logger = LogManager.getLogger(AccountCreationService.class);

    public AccountCreationService(PhoneNumberValidator phoneNumberValidator, HashingService hashingService){
        this.phoneNumberValidator = phoneNumberValidator;
        this.hashingService = hashingService;
    }

    public Account create(AccountCreationRequest accountCreationRequest){
        if (accountCreationRequest == null )throw new IllegalArgumentException("Account creation request cannot be null");

        String registrationEmail = accountCreationRequest.getEmail();
        logger.info("creating account creation data for user with email: {}",registrationEmail);

        //Phone number validation
        logger.debug("creating phone number with, countryCode:{}, phoneNumber:{} for user with email: {}",
                accountCreationRequest.getCountryCode(), accountCreationRequest.getPhoneNumber(),registrationEmail);
        PhoneNumber phoneNumber = createPhoneNumber(accountCreationRequest.getCountryCode(),accountCreationRequest.getPhoneNumber());
        validatePhoneNumber(phoneNumber);

        //Account UUID creation
        logger.debug("creating uuid for user with email: {}",registrationEmail);
        String userId = UuidCreator.getTimeOrderedEpoch().toString();

        //Hashing password
        String hashedPassword = hashingService.hash(accountCreationRequest.getPassword());

        logger.info("account creation data created successfully for user with email:{}",registrationEmail);
        return new Account(userId,
                accountCreationRequest.getUsername(),
                accountCreationRequest.getName(),
                accountCreationRequest.getSurname(),
                accountCreationRequest.getEmail(),
                phoneNumber,
                accountCreationRequest.getBirthDate(),
                hashedPassword,
                false,false);
    }

    private void validatePhoneNumber(PhoneNumber phoneNumber) {
//        String formattedCountryCode = INTERNATIONAL_PREFIX.concat(countryCode);
        if (!phoneNumberValidator.isValid(phoneNumber.getCountryCode(), phoneNumber.getPhoneNumber())) {
            throw new InvalidPhoneNumberException(phoneNumber.getCountryCode(), phoneNumber.getPhoneNumber());
        }
    }

    private PhoneNumber createPhoneNumber(String countryCode,String phoneNumber){
        String phoneNumberId =  UuidCreator.getTimeOrderedEpoch().toString();
        return new PhoneNumber(phoneNumberId,countryCode, phoneNumber);

    }

}
