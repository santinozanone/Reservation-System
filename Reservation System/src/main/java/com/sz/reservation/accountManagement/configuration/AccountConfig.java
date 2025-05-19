package com.sz.reservation.accountManagement.configuration;

import com.sz.reservation.accountManagement.application.service.AccountCreation;
import com.sz.reservation.accountManagement.application.service.ProfilePictureService;
import com.sz.reservation.accountManagement.application.useCase.AccountRegistrationUseCase;
import com.sz.reservation.accountManagement.application.useCase.AccountVerificationUseCase;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountVerificationTokenRepository;
import com.sz.reservation.accountManagement.domain.port.outbound.ProfilePictureStorage;
import com.sz.reservation.accountManagement.domain.port.outbound.VerificationTokenEmailSender;
import com.sz.reservation.accountManagement.domain.service.HashingService;
import com.sz.reservation.accountManagement.domain.service.MultipartImageResizingService;
import com.sz.reservation.accountManagement.domain.service.PhoneNumberValidator;
import com.sz.reservation.accountManagement.domain.service.ProfilePictureTypeValidator;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@ComponentScan(basePackages = "com.sz.reservation.accountManagement")
@PropertySource("classpath:application.properties")
@EnableWebMvc
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@EnableLoadTimeWeaving
public class AccountConfig {

    @Bean
    public StandardServletMultipartResolver multipartResolver(){
        return new StandardServletMultipartResolver();
    }

    @Bean
    public ProfilePictureService profilePictureService(ProfilePictureStorage profilePictureStorage, ProfilePictureTypeValidator profilePictureTypeValidator,
                                                       MultipartImageResizingService multipartImageResizingService){
        return new ProfilePictureService(profilePictureStorage,profilePictureTypeValidator,multipartImageResizingService);
    }
    @Bean
    public AccountVerificationUseCase accountVerificationUseCase(AccountRepository accountRepository, AccountVerificationTokenRepository verificationTokenRepository,
                                                                 VerificationTokenEmailSender verificationTokenEmailSender){
        return new AccountVerificationUseCase(accountRepository,verificationTokenRepository,verificationTokenEmailSender);
    }

    @Bean
    public AccountCreation accountCreation(PhoneNumberValidator phoneNumberValidator, HashingService hashingService){
        return new AccountCreation(phoneNumberValidator, hashingService);
    }

    @Bean
    public AccountRegistrationUseCase registrationUseCase(AccountRepository accountRepository, AccountVerificationTokenRepository verificationTokenRepository,
                                                          ProfilePictureService profilePictureService, VerificationTokenEmailSender verificationTokenEmailSender,
                                                          AccountCreation accountCreation){
        return new AccountRegistrationUseCase(accountRepository,verificationTokenRepository,verificationTokenEmailSender,profilePictureService,accountCreation);
    }
}
