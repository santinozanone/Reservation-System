package com.sz.reservation.registration.infrastructure.adapter.outbound;

import com.sz.reservation.configuration.RootConfig;
import com.sz.reservation.configuration.ServletConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Email sending service test")
@SpringJUnitWebConfig(classes = {RootConfig.class, ServletConfig.class})
@Disabled
class SendGridVerificationAccountValidationTokenEmailSenderTest {

    @Autowired
    private SendGridVerificationTokenEmailSender emailSender;

    @Test
    public void Should_SendEmailCorrectlyNotThrowError_When_Used() {
        //Arrange
        String email = "fakeemail@gmail.com";
        String username = "tomas";
        String token = "12345";

        //Act
        emailSender.sendEmailTo(email,username,token);


    }
}