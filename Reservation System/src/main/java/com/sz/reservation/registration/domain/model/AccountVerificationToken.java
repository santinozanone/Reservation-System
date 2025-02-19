package com.sz.reservation.registration.domain.model;

import java.time.LocalDate;

public class AccountVerificationToken {

    private String verificationToken;
    private LocalDate expiresAt;

    public AccountVerificationToken(String verificationToken, LocalDate expiresAt) {
        this.verificationToken = verificationToken;
        this.expiresAt = expiresAt;
    }

    public LocalDate getExpirationDate() {
        return expiresAt;
    }

    public String getToken() {
        return verificationToken;
    }

    public boolean matchesToken(String token){
        return verificationToken.equals(token);
    }

    public boolean isExpired(){
        return LocalDate.now().isAfter(expiresAt);
    }
}
