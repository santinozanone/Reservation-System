package com.sz.reservation.accountManagement.domain.event;

public class AccountUpdatedEvent {
    private String accountId;
    private String username;
    private String name;
    private String surname;

    public AccountUpdatedEvent(String accountId, String username, String name, String surname) {
        this.accountId = accountId;
        this.username = username;
        this.name = name;
        this.surname = surname;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
