package com.sz.reservation.registration.infrastructure.outbound;

import com.sz.reservation.registration.domain.model.User;
import com.sz.reservation.registration.application.port.outbound.UserRegistrationDb;

public class UserRegistrationMySqlDb implements UserRegistrationDb{

	@Override
	public User getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerNotEnabledUser(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enableRegisteredUserByEmail(String email) {
		// TODO Auto-generated method stub
		
	}

}
