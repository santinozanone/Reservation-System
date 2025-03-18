package com.sz.reservation.accountManagement.domain.port.outbound;

import com.sz.reservation.accountManagement.domain.model.AccountVerificationToken;

import java.util.Optional;

public interface AccountVerificationTokenRepository {
    Optional<AccountVerificationToken> findByToken(String token);
    void save(AccountVerificationToken accountVerificationToken);

    void update(String oldToken, AccountVerificationToken newToken);
}
