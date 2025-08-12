package com.sz.reservation.listingManagement.domain;

public class Account {
    private String id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private boolean enabled;

    public Account(String id, String username, String name, String surname,String email, boolean enabled) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.enabled = enabled;
    }


    public String getId() {
        return id;
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

    public void disableAccount(){
        enabled = false;
    }

    public void enableAccount(){
        enabled = true;
    }

}
