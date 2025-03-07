package com.sz.reservation.accountManagement.infrastructure.security;

import com.sz.reservation.accountManagement.domain.model.Account;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;


public class CustomUserDetailsService implements UserDetailsService {
    private AccountRepository accountRepository;

    @Autowired
    public CustomUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findAccountByEmail(email);
        if (optionalAccount.isEmpty()) throw new UsernameNotFoundException("the user with email "+ email + " does not exists");
        Account account = optionalAccount.get();
        CustomUserDetails customUserDetails = new CustomUserDetails(account.getUniqueUsername(),account.getPassword()
        ,account.isEnabled(),account.getUniqueEmail());
        return customUserDetails;
    }
}
