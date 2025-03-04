package com.sz.reservation.accountManagement.infrastructure.adapter.outbound;

import com.sz.reservation.configuration.RootConfig;
import com.sz.reservation.configuration.ServletConfig;
import com.sz.reservation.accountManagement.domain.port.outbound.VerificationTokenEmailSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Email sending service test")
@SpringJUnitWebConfig(classes = {RootConfig.class, ServletConfig.class})
@WebAppConfiguration
@ActiveProfiles(value = {"test","default"})
class VerificationAccountValidationTokenEmailSenderTest {

    @Autowired
    private VerificationTokenEmailSender emailSender;

    @Test
    public void Should_SendEmailCorrectlyNotThrowError_When_Used() {
        //Arrange
        String email = "zanone.santinoet36@gmail.com";
        String username = "tomas4";
        String token = "01854f09-742d-7d86-a3da-b0127c8facc4";

        //Act and assert
        assertDoesNotThrow(() ->{
            emailSender.sendEmailTo(email,username,token);
        });
    }
}