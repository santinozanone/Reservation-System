package com.sz.reservation.accountManagement.application.useCase;

import com.sz.reservation.accountManagement.application.dto.AccountCreationData;
import com.sz.reservation.accountManagement.application.service.AccountCreation;
import com.sz.reservation.accountManagement.application.service.ProfilePictureService;
import com.sz.reservation.accountManagement.domain.exception.EmailAlreadyRegisteredException;
import com.sz.reservation.accountManagement.domain.exception.UsernameAlreadyRegisteredException;
import com.sz.reservation.accountManagement.domain.model.Account;
import com.sz.reservation.accountManagement.domain.model.ProfilePicture;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountVerificationTokenRepository;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.accountManagement.domain.port.outbound.VerificationTokenEmailSender;
import com.sz.reservation.accountManagement.infrastructure.dto.AccountCreationRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.Optional;


public class AccountRegistrationUseCase {
    private Logger logger = LogManager.getLogger(AccountRegistrationUseCase.class);
    private AccountRepository accountRepository;
    private AccountVerificationTokenRepository verificationTokenRepository;

    private ProfilePictureService profilePictureService;
    private VerificationTokenEmailSender emailSender;
    private AccountCreation accountCreation;


    public AccountRegistrationUseCase(AccountRepository accountRepository, AccountVerificationTokenRepository verificationTokenRepository,
                                      VerificationTokenEmailSender emailSender, ProfilePictureService profilePictureService,
                                      AccountCreation accountCreation) {
        this.accountRepository = accountRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailSender = emailSender;
        this.profilePictureService = profilePictureService;
        this.accountCreation = accountCreation;
    }


    public void registerNotEnabledUser(AccountCreationRequest accountCreationRequest) {
        //Profile picture validation
        logger.info("starting registration for email: {}", accountCreationRequest.getEmail());
        validateAccount(accountCreationRequest);

        profilePictureService.validate(accountCreationRequest);
        Image resizedImage = profilePictureService.resize(accountCreationRequest.getProfilePicture());
        String profilePictureStoringPath = profilePictureService.store(accountCreationRequest.getProfilePicture().getOriginalFilename(), resizedImage);

        //Account creation data
        AccountCreationData accountCreationData = accountCreation.accountCreationData(accountCreationRequest, new ProfilePicture(profilePictureStoringPath));

        //Outbound adapters
        logger.info("registering user in database for email: {}", accountCreationData.getEmail());
        try {
            registerNotEnabledUserInDb(accountCreationData); // once image is saved correctly, store in db
        } catch (EmailAlreadyRegisteredException | UsernameAlreadyRegisteredException ex) {
            logger.info("email or username already in use, deleting profile picture:{}", profilePictureStoringPath);
            profilePictureService.delete(profilePictureStoringPath);
            throw ex;
        } catch (Exception ex) {
            logger.error("Failed to register user. Deleting profile picture: {}", profilePictureStoringPath);
            profilePictureService.delete(profilePictureStoringPath);
            throw ex; // Re-throw the exception to propagate it
        }
        logger.info("sending verification email to: {}", accountCreationData.getEmail());
        emailSender.sendEmailTo(accountCreationData.getEmail(), accountCreationData.getUsername(), accountCreationData.getVerificationToken().getToken()); // then send email
    }

    @Transactional
    private void registerNotEnabledUserInDb(AccountCreationData accountCreationData) {
        accountRepository.registerNotEnabledNotVerifiedUser(accountCreationData); // store user
        verificationTokenRepository.save(accountCreationData.getVerificationToken()); // store verification token
    }

    private void validateAccount(AccountCreationRequest accountCreationRequest) {
        Optional<Account> optionalAccount = accountRepository.findAccountByEmail(accountCreationRequest.getEmail());
        if (optionalAccount.isEmpty()) return;
        if (optionalAccount.get().getUniqueEmail().equals(accountCreationRequest.getEmail())) {
            throw new EmailAlreadyRegisteredException(accountCreationRequest.getEmail());
        }
        if (optionalAccount.get().getUniqueUsername().equals(accountCreationRequest.getUsername())) {
            throw new UsernameAlreadyRegisteredException(accountCreationRequest.getUsername());
        }
    }
}