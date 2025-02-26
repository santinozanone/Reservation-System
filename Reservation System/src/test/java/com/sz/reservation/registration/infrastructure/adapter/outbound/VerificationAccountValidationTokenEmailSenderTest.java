package com.sz.reservation.registration.infrastructure.adapter.outbound;

import com.sz.reservation.configuration.RootConfig;
import com.sz.reservation.configuration.ServletConfig;
import com.sz.reservation.registration.domain.port.outbound.VerificationTokenEmailSender;
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
class VgiterificationAccountValidationTokenEmailSenderTest {

    @Autowired
    private VerificationTokenEmailSender emailSender;

    @Test
    public void Should_SendEmailCorrectlyNotThrowError_When_Used() {
        //Arrange
        String email = "fakeemail@gmail.com";
        String username = "tomas";
        String token = "12345";

        //Act and assert
        assertDoesNotThrow(() ->{
            emailSender.sendEmailTo(email,username,token);
        });
    }
}