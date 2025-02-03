package com.sz.reservation.registration.domain.port.outbound;

import com.sz.reservation.registration.domain.model.User;

import java.util.Optional;

public interface UserRegistrationDb {
	Optional<User> getUserByEmail(String email);
	void registerNotEnabledUser(User user);
	void enableRegisteredUserByEmail(String email);
}
