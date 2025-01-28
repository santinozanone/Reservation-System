package com.sz.reservation.configuration;

import com.sz.reservation.registration.application.port.outbound.UserRegistrationDb;
import com.sz.reservation.registration.application.useCase.RegistrationUseCase;
import com.sz.reservation.util.FileValidator;
import com.sz.reservation.util.TikaFileValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
    public FileValidator tikaFileValidator(){
        return new TikaFileValidator();
    }

    @Bean
    public RegistrationUseCase registrationUseCase(UserRegistrationDb userRegistrationDb, FileValidator fileValidator){
        return new RegistrationUseCase(userRegistrationDb,fileValidator);
    }
}
