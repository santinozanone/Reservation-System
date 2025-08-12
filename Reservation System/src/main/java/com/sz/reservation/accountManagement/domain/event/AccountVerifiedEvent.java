package com.sz.reservation.accountManagement.domain.event;

public class AccountVerifiedEvent {
    private String accountId;
    private String username;
    private String name;
    private String surname;
    private String email;
    private boolean enabled;

    public AccountVerifiedEvent(String accountId, String username, String name, String surname, String email, boolean enabled) {
        this.accountId = accountId;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.enabled = enabled;
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

    public String getEmail() {
        return email;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
