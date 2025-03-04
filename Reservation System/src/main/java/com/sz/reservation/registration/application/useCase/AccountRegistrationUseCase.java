package com.sz.reservation.registration.application.useCase;

import com.sz.reservation.registration.domain.exception.EmailAlreadyRegisteredException;
import com.sz.reservation.registration.domain.exception.MediaNotSupportedException;
import com.sz.reservation.registration.domain.exception.UsernameAlreadyRegisteredException;
import com.sz.reservation.registration.domain.model.ProfilePicture;
import com.sz.reservation.registration.domain.port.outbound.ProfilePictureStorage;
import com.sz.reservation.registration.domain.port.outbound.AccountRepository;
import com.sz.reservation.registration.domain.port.outbound.VerificationTokenEmailSender;
import com.sz.reservation.registration.domain.service.*;
import com.sz.reservation.registration.infrastructure.dto.AccountCreationRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


public class AccountRegistrationUseCase {
    private Logger logger = LogManager.getLogger(AccountRegistrationUseCase.class);
    private AccountRepository accountRepository;
    private ProfilePictureStorage profilePictureStorage;
    private MultipartImageResizingService multipartImageResizingService;
    private ProfilePictureTypeValidator profilePictureTypeValidator;
    private VerificationTokenEmailSender emailSender;
    private AccountCreation accountCreation;


    public AccountRegistrationUseCase(AccountRepository accountRepository, ProfilePictureStorage profilePictureStorage,
                                      MultipartImageResizingService multipartImageResizingService,
                                      ProfilePictureTypeValidator profilePictureTypeValidator, VerificationTokenEmailSender emailSender,
                                      AccountCreation accountCreation) {
        this.accountRepository = accountRepository;
        this.profilePictureStorage = profilePictureStorage;
        this.multipartImageResizingService = multipartImageResizingService;
        this.profilePictureTypeValidator = profilePictureTypeValidator;
        this.emailSender = emailSender;
        this.accountCreation = accountCreation;
    }


    public void registerNotEnabledUser(AccountCreationRequest accountCreationRequest) {
        //Profile picture validation
        logger.info("starting registration for email: {}", accountCreationRequest.getEmail());
        if (accountRepository.findAccountByEmail(accountCreationRequest.getEmail()).isPresent()){
            throw new EmailAlreadyRegisteredException(accountCreationRequest.getEmail());
        }
        if (accountRepository.findAccountByUsername(accountCreationRequest.getUsername()).isPresent()){
            throw new UsernameAlreadyRegisteredException(accountCreationRequest.getUsername());
        }

        MultipartFile profileImageMultipart = accountCreationRequest.getProfilePicture();
        if (!profilePictureTypeValidator.isValid(accountCreationRequest.getProfilePicture())) {
            throw new MediaNotSupportedException("FAILED profile picture validation for email "+accountCreationRequest.getEmail() +" , profile picture extension/content is not valid");
        }
        String profilePictureStoringPath = generateProfilePicturePathName(profileImageMultipart.getOriginalFilename());
        Image resizedImage = multipartImageResizingService.resizeImage(accountCreationRequest.getProfilePicture());
        ProfilePicture profilePicture = new ProfilePicture(profilePictureStoringPath);


        //Account creation data
        AccountCreationData accountCreationData = accountCreation.accountCreationData(accountCreationRequest, profilePicture);

        //Outbound adapters
        logger.info("storing profile picture at: {}", profilePictureStoringPath);
        profilePictureStorage.store(resizedImage, profilePictureStoringPath); // first store image on disk

        logger.info("registering user in database for email: {}", accountCreationData.getEmail());
        try {
            registerNotEnabledUserInDb(accountCreationData); // once image is saved correctly, store in db
        } catch (EmailAlreadyRegisteredException | UsernameAlreadyRegisteredException ex) {
            logger.info("email or username already in use, deleting profile picture:{}",profilePictureStoringPath);
            profilePictureStorage.delete(profilePictureStoringPath);
            throw ex;
        }
        catch (Exception ex) {
            logger.error("Failed to register user. Deleting profile picture: {}", profilePictureStoringPath);
            profilePictureStorage.delete(profilePictureStoringPath);
            throw ex; // Re-throw the exception to propagate it
        }
        logger.info("sending verification email to: {}", accountCreationData.getEmail());

       // emailSender.sendEmailTo(accountCreationData.getEmail(), accountCreationData.getUsername(), accountCreationData.getVerificationToken().getToken()); // then send email

        //TODO: Protect the file upload from CSRF attacks
        //TODO: MAKE EMAIL VALIDATION BETTER, SENDGRID DETECS INVALID EMAILS
    }

    @Transactional
    private void registerNotEnabledUserInDb(AccountCreationData accountCreationData) {
        accountRepository.registerNotEnabledNotVerifiedUser(accountCreationData);
    }

    private String generateProfilePicturePathName(String originalFileName) {
        //get file extension
        int index = originalFileName.lastIndexOf(".");
        String fileExtension = originalFileName.substring(index + 1);

        //Generate and format timestamp
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String timestamp = dateFormatter.format(LocalDate.now());
        //Generate UUID
        String randomUUID = UUID.randomUUID().toString();

        //generate filename
        String storingDirectory = profilePictureStorage.getStoringDirectory();
        String filename = storingDirectory.concat("pfp_").concat(timestamp).concat("-").concat(randomUUID).concat(".").concat(fileExtension);
        return filename;
    }


}