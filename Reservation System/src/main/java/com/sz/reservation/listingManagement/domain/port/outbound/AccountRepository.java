package com.sz.reservation.listingManagement.domain.port.outbound;

import com.sz.reservation.listingManagement.domain.Account;

import java.util.Optional;

public interface AccountRepository {
    void save(Account account);
    void update(Account account);
    Optional<Account> findByEmail(String email);
    Optional<Account> findById(String id);

}
