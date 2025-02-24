package com.sz.reservation.registration.domain.port.outbound;

import com.sz.reservation.registration.application.useCase.AccountCreationData;
import com.sz.reservation.registration.domain.model.Account;

import java.util.Optional;

public interface AccountRepository {
	void registerNotEnabledNotVerifiedUser(AccountCreationData user);

	Optional<Account> findAccountByUsername(String username);

	Optional<Account> findAccountByEmail(String email);


}
