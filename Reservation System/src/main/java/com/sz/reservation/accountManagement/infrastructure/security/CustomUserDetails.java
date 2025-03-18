package com.sz.reservation.accountManagement.infrastructure.security;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails, CredentialsContainer {
    private String username;
    private String password;
    private boolean enabled;

    private String email;

    public CustomUserDetails(String username, String password, boolean enabled, String email) {
        this.username = username;
        this.password = password;
        this.enabled = true;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (!enabled) return Collections.emptyList();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ENABLED_USER");
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
