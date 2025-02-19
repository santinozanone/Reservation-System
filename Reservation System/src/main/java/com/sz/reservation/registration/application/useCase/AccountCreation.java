package com.sz.reservation.registration.application.useCase;

import com.github.f4b6a3.uuid.UuidCreator;
import com.sz.reservation.registration.domain.exception.InvalidPhoneNumberException;
import com.sz.reservation.registration.domain.model.AccountVerificationToken;
import com.sz.reservation.registration.domain.model.PhoneNumber;
import com.sz.reservation.registration.domain.model.ProfilePicture;
import com.sz.reservation.registration.domain.service.HashingService;
import com.sz.reservation.registration.domain.service.PhoneNumberValidator;
import com.sz.reservation.registration.infrastructure.dto.AccountCreationRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.time.LocalDate;


public class AccountCreation {
    private PhoneNumberValidator phoneNumberValidator;
    private HashingService hashingService;
    private Logger logger = LogManager.getLogger(AccountCreation.class);
    public AccountCreation(PhoneNumberValidator phoneNumberValidator,HashingService hashingService){
        this.phoneNumberValidator = phoneNumberValidator;
        this.hashingService = hashingService;
    }

    public AccountCreationData accountCreationData(AccountCreationRequest accountCreationRequest, ProfilePicture profilePicture){
        String registrationEmail = accountCreationRequest.getEmail();
        logger.info("creating account creation data for user with email: {}",registrationEmail);
        //Phone number validation
        if (!phoneNumberValidator.isValid(accountCreationRequest.getCountryCode(), accountCreationRequest.getPhoneNumber())) {
            logger.info("Invalid phone number for user with email: {} , countryCode:{} , and phoneNumber:{}",registrationEmail,accountCreationRequest.getCountryCode(), accountCreationRequest.getPhoneNumber());
            throw new InvalidPhoneNumberException("Invalid phone number with countryCode "+accountCreationRequest.getCountryCode() + " and PhoneNumber "+accountCreationRequest.getPhoneNumber());
        }

        //Hashing password
        String hashedPassword = hashingService.hash(accountCreationRequest.getPassword());

        //UUID creation
        logger.debug("creating uuid for user with email: {}",registrationEmail);
        String userId = UuidCreator.getTimeOrderedEpoch().toString();
        logger.debug("creating account verification token user with for email: {}",registrationEmail);
        String accountVerificationToken = UuidCreator.getTimeOrderedEpoch().toString();
        logger.debug("creating phone number uuid for user with email: {}",registrationEmail);
        String phoneNumberId =  UuidCreator.getTimeOrderedEpoch().toString();

        logger.debug("created : user uuid:{} , accountVerificationToken: {} , phoneNumberId: {}",userId,accountVerificationToken,phoneNumberId);

        //Token date expiration
        LocalDate expirationDate = LocalDate.now().plusDays(7);

        //Object creation
        logger.debug("creating account verification token with, token:{} , expirationDate:{} , for user with email: {}",accountVerificationToken,expirationDate,registrationEmail);
        AccountVerificationToken accountValidationToken = new AccountVerificationToken(accountVerificationToken,expirationDate);
        logger.debug("creating phone number with, phoneID:{}, countryCode:{}, phoneNumber:{} for user with email: {}",phoneNumberId,accountCreationRequest.getCountryCode(), accountCreationRequest.getPhoneNumber(),registrationEmail);
        PhoneNumber phoneNumber = new PhoneNumber(phoneNumberId,accountCreationRequest.getCountryCode(), accountCreationRequest.getPhoneNumber());

        logger.info("account creation data created successfully for user with email:{}",registrationEmail);
        return new AccountCreationData(
                userId,
                accountCreationRequest.getUsername(),
                accountCreationRequest.getName(),
                accountCreationRequest.getSurname(),
                accountCreationRequest.getEmail(),
                phoneNumber,
                accountCreationRequest.getBirthDate(),
                accountCreationRequest.getNationality(),
                hashedPassword,
                profilePicture,
                accountValidationToken);
    }

}
