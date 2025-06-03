package com.sz.reservation.accountManagement.domain.event;

public class AccountCreatedEvent {
    private String accountId;
    private String username;
    private String name;
    private String surname;
    private String email;
    private boolean enabled;

    public AccountCreatedEvent(String accountId, String username, String name, String surname, String email, boolean enabled) {
        this.accountId = accountId;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.enabled = enabled;
    }
}
