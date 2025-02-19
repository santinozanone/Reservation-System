package com.sz.reservation.registration.domain.port.outbound;

import java.io.IOException;

public interface VerificationTokenEmailSender {
    void sendEmailTo(String email,String username,String token);
}
