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
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
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

        //Profile picture validation
        profilePictureService.validate(accountCreationRequest);
        Image resizedImage = profilePictureService.resize(accountCreationRequest.getProfilePicture());
        String profilePictureStoringPath = profilePictureService.store(accountCreationRequest.getProfilePicture().getOriginalFilename(),
                resizedImage);
        //Account creation data
        Account account = accountCreationService.create(accountCreationRequest, new ProfilePicture(profilePictureStoringPath));

        AccountVerificationToken accountVerificationToken = createAccountVerificationToken(account.getUniqueEmail(),account.getId());

        //Outbound adapters
        logger.info("registering user in database for email: {}", account.getUniqueEmail());
        registerNotEnabledUserInDb(account,accountVerificationToken,profilePictureStoringPath); // once image is saved correctly, store in db

        logger.info("sending verification email to: {}", account.getUniqueEmail());
        emailSender.sendEmailTo(account.getUniqueEmail(), account.getUniqueUsername(), accountVerificationToken.getToken()); // then send email
    }

    @Transactional
    private void registerNotEnabledUserInDb(Account account, AccountVerificationToken accountVerificationToken,
                                            String profilePictureStoringPath) {
        try {
            accountRepository.createAccount(account); // store user
        }catch (EmailAlreadyRegisteredException | UsernameAlreadyRegisteredException ex) {
            logger.info("email or username already in use, deleting profile picture:{}", profilePictureStoringPath);
            profilePictureService.delete(profilePictureStoringPath);
            throw ex;
        } catch (Exception ex) {
            logger.error("Failed to register user. Deleting profile picture: {}", profilePictureStoringPath);
            profilePictureService.delete(profilePictureStoringPath);
            throw ex; // Re-throw the exception to propagate it
        }
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

    private AccountVerificationToken createAccountVerificationToken(String email, String id){
        logger.debug("creating account verification token user with for email: {}",email);
        String verificationToken = UuidCreator.getTimeOrderedEpoch().toString();
        //Token date expiration
        LocalDate expirationDate = LocalDate.now().plusDays(7);
        //Object creation
        logger.debug("creating account verification token with, token:{} , expirationDate:{} , for user with email: {}",
                verificationToken,expirationDate,email);
        return new AccountVerificationToken(id,verificationToken,expirationDate);
    }
}