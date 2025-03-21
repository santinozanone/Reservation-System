package com.sz.reservation.accountManagement.infrastructure.adapter.outbound;

import com.sz.reservation.accountManagement.domain.port.outbound.VerificationTokenEmailSender;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class MockEmailTokenVerificationSender implements VerificationTokenEmailSender {
    @Override
    public void sendEmailTo(String email, String username, String token) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
