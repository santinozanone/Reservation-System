package com.sz.reservation.accountManagement.domain.port.outbound;

public interface VerificationTokenEmailSender {
    void sendEmailTo(String email,String username,String token);
}
