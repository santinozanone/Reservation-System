package com.sz.reservation.accountManagement.domain.port.outbound;

import com.sz.reservation.accountManagement.domain.model.Account;
import com.sz.reservation.accountManagement.domain.model.ProfilePicture;

import java.util.Optional;

public interface AccountRepository {

	void updateAccount(Account account);
	void createAccount(Account account);
	void createProfilePicture(ProfilePicture profilePicture);
	Optional<Account> findAccountByUserId(String userId);
	Optional<Account> findAccountByEmail(String email);


}
