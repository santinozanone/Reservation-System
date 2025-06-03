package com.sz.reservation.accountManagement.domain.event;

public class AccountDeletedEvent {
    private String accountId;

    public AccountDeletedEvent(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}
