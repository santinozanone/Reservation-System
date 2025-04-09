package com.sz.reservation.accountManagement.configuration;

import com.sz.reservation.accountManagement.application.service.AccountCreation;
import com.sz.reservation.accountManagement.application.service.ProfilePictureService;
import com.sz.reservation.accountManagement.application.useCase.AccountRegistrationUseCase;
import com.sz.reservation.accountManagement.application.useCase.AccountVerificationUseCase;
import com.sz.reservation.accountManagement.domain.exception.AccountAlreadyVerifiedException;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountVerificationTokenRepository;
import com.sz.reservation.accountManagement.domain.port.outbound.ProfilePictureStorage;
import com.sz.reservation.accountManagement.domain.port.outbound.VerificationTokenEmailSender;
import com.sz.reservation.accountManagement.domain.service.HashingService;
import com.sz.reservation.accountManagement.domain.service.MultipartImageResizingService;
import com.sz.reservation.accountManagement.domain.service.PhoneNumberValidator;
import com.sz.reservation.accountManagement.domain.service.ProfilePictureTypeValidator;
import com.sz.reservation.accountManagement.infrastructure.adapter.outbound.SendGridVerificationTokenEmailSender;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@ComponentScan(basePackages = "com.sz.reservation.accountManagement")
@PropertySource("classpath:application.properties")
@EnableWebMvc
//@EnableWebSecurity(debug = true)
public class AccountConfig {
  /*  @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        mvc.servletPath("/api/v1");
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(mvc.pattern("/account/*")).permitAll()
                        .requestMatchers("/error/**").permitAll()

                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //  .exceptionHandling(handlingConfigurer ->
                //        handlingConfigurer.accessDeniedHandler(accessDeniedHandler))
                //.httpBasic(Customizer.withDefaults()
                //       basicConfigurer.authenticationEntryPoint(authEntryPoint)
                //)
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        return http.build();
    }

   */

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
   // @Profile("prod")
    public VerificationTokenEmailSender verificationTokenEmailSender(){
        return new SendGridVerificationTokenEmailSender();
    }

    @Bean
    public AccountRegistrationUseCase registrationUseCase(AccountRepository accountRepository, AccountVerificationTokenRepository verificationTokenRepository,
                                                          ProfilePictureService profilePictureService, VerificationTokenEmailSender verificationTokenEmailSender,
                                                          AccountCreation accountCreation){
        return new AccountRegistrationUseCase(accountRepository,verificationTokenRepository,verificationTokenEmailSender,profilePictureService,accountCreation);
    }
}
