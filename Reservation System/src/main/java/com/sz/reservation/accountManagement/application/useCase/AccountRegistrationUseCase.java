package com.sz.reservation.accountManagement.application.useCase;

import com.github.f4b6a3.uuid.UuidCreator;
import com.sz.reservation.accountManagement.application.service.AccountCreationService;
import com.sz.reservation.accountManagement.application.service.ProfilePictureService;
import com.sz.reservation.accountManagement.domain.exception.EmailAlreadyRegisteredException;
import com.sz.reservation.accountManagement.domain.exception.UsernameAlreadyRegisteredException;
import com.sz.reservation.accountManagement.domain.model.Account;
import com.sz.reservation.accountManagement.domain.model.AccountVerificationToken;
import com.sz.reservation.accountManagement.domain.model.ProfilePicture;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountVerificationTokenRepository;
import com.sz.reservation.accountManagement.domain.port.outbound.VerificationTokenEmailSender;
import com.sz.reservation.accountManagement.infrastructure.dto.AccountCreationRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Optional;


public class AccountRegistrationUseCase {
    private Logger logger = LogManager.getLogger(AccountRegistrationUseCase.class);
    private AccountRepository accountRepository;
    private AccountVerificationTokenRepository verificationTokenRepository;

    private ProfilePictureService profilePictureService;
    private VerificationTokenEmailSender emailSender;
    private AccountCreationService accountCreationService;


    public AccountRegistrationUseCase(AccountRepository accountRepository, AccountVerificationTokenRepository verificationTokenRepository,
                                      VerificationTokenEmailSender emailSender, ProfilePictureService profilePictureService,
                                      AccountCreationService accountCreationService) {
        this.accountRepository = accountRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailSender = emailSender;
        this.profilePictureService = profilePictureService;
        this.accountCreationService = accountCreationService;
    }


    public void registerNotEnabledUser(AccountCreationRequest accountCreationRequest) {
        logger.info("starting registration for email: {}", accountCreationRequest.getEmail());
        validateAccount(accountCreationRequest);

        //Account creation
        Account account = accountCreationService.create(accountCreationRequest);

        AccountVerificationToken accountVerificationToken = createAccountVerificationToken(account.getUniqueEmail(), account.getId());

        //Outbound adapters
        logger.info("registering user in database for email: {}", account.getUniqueEmail());
        registerNotEnabledUserInDb(account, accountVerificationToken); // once image is saved correctly, store in db

        logger.info("sending verification email to: {}", account.getUniqueEmail());
        emailSender.sendEmailTo(account.getUniqueEmail(), account.getUniqueUsername(), accountVerificationToken.getToken()); // then send email
    }

    public void uploadProfilePicture(String accountId, String profilePictureOriginalName, InputStream profilePictureStream) {
        int bufferSize = 32 * 1024; //32 Kilobyte
        logger.info("starting pfp upload for accoundID: {} ", accountId);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(profilePictureStream, bufferSize);

        profilePictureService.validate(profilePictureOriginalName, bufferedInputStream);

        Image resizedImage = profilePictureService.resize(profilePictureOriginalName, bufferedInputStream);

        // pfp storage
        //store in disk
        String profilePictureStoringPath = profilePictureService.store(profilePictureOriginalName, resizedImage);

        //store in db
        String profilePictureId = UuidCreator.getTimeOrderedEpoch().toString();
        ProfilePicture profilePicture = new ProfilePicture(profilePictureId, accountId, profilePictureStoringPath);

        createProfilePictureMetadata(accountId, profilePicture, profilePictureStoringPath);
        logger.info("Successfully uploaded pfp for accoundID: {} ", accountId);

    }

    @Transactional
    private void createProfilePictureMetadata(String accountId, ProfilePicture profilePicture, String profilePictureStoringPath) {
        try {
            accountRepository.createProfilePictureMetadata(profilePicture);
        } catch (DataAccessException e) {
            logger.info("FAILED pfp upload for accoundID: {}  ", accountId);
            profilePictureService.delete(profilePictureStoringPath);
        }
    }

    @Transactional
    private void registerNotEnabledUserInDb(Account account, AccountVerificationToken accountVerificationToken) {
        accountRepository.createAccount(account); // store user
        verificationTokenRepository.save(accountVerificationToken); // store verification token
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

    private AccountVerificationToken createAccountVerificationToken(String email, String id) {
        logger.debug("creating account verification token user with for email: {}", email);
        String verificationToken = UuidCreator.getTimeOrderedEpoch().toString();
        //Token date expiration
        LocalDate expirationDate = LocalDate.now().plusDays(7);
        //Object creation
        logger.debug("creating account verification token with, token:{} , expirationDate:{} , for user with email: {}",
                verificationToken, expirationDate, email);
        return new AccountVerificationToken(id, verificationToken, expirationDate);
    }
}