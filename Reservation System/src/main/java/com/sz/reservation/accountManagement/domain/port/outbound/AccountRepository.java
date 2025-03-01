package com.sz.reservation.accountManagement.domain.port.outbound;

import com.sz.reservation.accountManagement.application.dto.AccountCreationData;
import com.sz.reservation.accountManagement.domain.model.Account;

import java.util.Optional;

public interface AccountRepository {

	void updateAccount(Account account);
	void registerNotEnabledNotVerifiedUser(AccountCreationData user);
	Optional<Account> findAccountByUserId(String userId);

	Optional<Account> findAccountByUsername(String username);

	Optional<Account> findAccountByEmail(String email);


}
