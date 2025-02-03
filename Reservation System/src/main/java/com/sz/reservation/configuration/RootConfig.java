package com.sz.reservation.configuration;

import com.sz.reservation.registration.application.useCase.*;
import com.sz.reservation.registration.domain.port.outbound.ProfilePictureStorage;
import com.sz.reservation.registration.domain.port.outbound.UserRegistrationDb;
import com.sz.reservation.registration.infrastructure.service.*;
import com.sz.reservation.util.FileTypeValidator;
import com.sz.reservation.util.TikaFileValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.parameters.P;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan("com.sz.reservation")
@EnableWebMvc 
public class RootConfig {

    @Bean
    public StandardServletMultipartResolver multipartResolver(){
        return new StandardServletMultipartResolver();
    }


    @Bean
    public FileTypeValidator tikaFileValidator(){
        return new TikaFileValidator();
    }

    @Bean
    public StandardProfilePictureValidator profilePictureValidator(FileTypeValidator fileTypeValidator){
        return new StandardProfilePictureValidator(fileTypeValidator);
    }
    @Bean
    public ProfilePictureStorage profilePictureStorage(){
        return new LocalSystemProfilePictureStorage();
    }

    @Bean
    public HashingService hashingService(){
        return new BCryptHashingService();
    }

    @Bean
    public PhoneNumberValidator phoneNumberValidator(){
        return new libPhoneNumberValidator();
    }

    @Bean
    public ImageResizingService imageResizingService(){
        return new AwtImageResizingService();
    }


    @Bean
    public RegistrationUseCase registrationUseCase(UserRegistrationDb userRegistrationDb,HashingService hashingService,
                                                   ProfilePictureValidator profilePictureValidator,PhoneNumberValidator phoneNumberValidator,
                                                   ImageResizingService imageResizingService, ProfilePictureStorage profilePictureStorage){
        return new RegistrationUseCase(userRegistrationDb, profilePictureStorage, imageResizingService, hashingService,
                   profilePictureValidator, phoneNumberValidator);
    }
}
