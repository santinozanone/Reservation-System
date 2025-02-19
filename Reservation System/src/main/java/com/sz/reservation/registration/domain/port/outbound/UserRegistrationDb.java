package com.sz.reservation.registration.domain.port.outbound;

import com.sz.reservation.registration.application.useCase.AccountCreationData;

import java.util.Optional;

public interface UserRegistrationDb {
	void registerNotEnabledUser(AccountCreationData user);
}
