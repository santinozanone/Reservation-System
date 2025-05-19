package com.sz.reservation.globalConfiguration.security.userDetails;

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
    private boolean verified;

    private String email;
    private String id;

    public CustomUserDetails(String id,String username, String password, boolean enabled,boolean verified ,String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.verified = verified;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (enabled && verified) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ENABLED_VERIFIED_USER");
            ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(simpleGrantedAuthority);
            return authorities;
        }
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
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
