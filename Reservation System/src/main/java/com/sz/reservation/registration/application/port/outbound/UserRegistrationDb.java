package com.sz.reservation.registration.application.port.outbound;

import com.sz.reservation.registration.domain.model.User;

public interface UserRegistrationDb {
	User getUserByEmail(String email);
	void registerNotEnabledUser(User user);
	void enableRegisteredUserByEmail(String email);
}
