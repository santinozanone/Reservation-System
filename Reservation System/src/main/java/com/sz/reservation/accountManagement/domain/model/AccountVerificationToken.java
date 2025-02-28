package com.sz.reservation.accountManagement.domain.model;

import java.time.LocalDate;

public class AccountVerificationToken {

    private String userId;
    private String verificationToken;
    private LocalDate expiresAt;

    public AccountVerificationToken(String userId,String verificationToken, LocalDate expiresAt) {
        this.userId = userId;
        this.verificationToken = verificationToken;
        this.expiresAt = expiresAt;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDate getExpirationDate() {
        return expiresAt;
    }

    public String getToken() {
        return verificationToken;
    }

    public boolean isValid(String token){
        if (matchesToken(token) && !isExpired()) return true;
        return false;
    }

    private boolean matchesToken(String token){
        return verificationToken.equals(token);
    }

    private boolean isExpired(){
        return LocalDate.now().isAfter(expiresAt);
    }
}
