package com.sz.reservation.registration.application.useCase;

import com.sz.reservation.registration.domain.model.AccountVerificationToken;
import com.sz.reservation.registration.domain.model.PhoneNumber;
import com.sz.reservation.registration.domain.model.ProfilePicture;

import java.time.LocalDate;
import java.util.Date;

public class AccountCreationData {

	private String id;
	private String username;
	private String name;
	private String surname;
	private String email;
	private PhoneNumber phoneNumber;
	private LocalDate birthDate;
	private String nationality; // should be enum
	private String password;
	private ProfilePicture profilePicture;
	private AccountVerificationToken verificationToken;

	public AccountCreationData(String id,String username, String name, String surname, String email,PhoneNumber phoneNumber, LocalDate birthDate, String nationality,String password,ProfilePicture profilePicture,AccountVerificationToken verificationToken) {
		this.id = id;
		this.username = username;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.birthDate = birthDate;
		this.nationality = nationality;
		this.password = password;
		this.profilePicture = profilePicture;
		this.verificationToken = verificationToken;
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getEmail() {
		return email;
	}

	public PhoneNumber getPhoneNumber() {
		return phoneNumber;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public String getNationality() {
		return nationality;
	}

	public String getPassword() {
		return password;
	}

	public ProfilePicture getProfilePicture() {
		return profilePicture;
	}

	public AccountVerificationToken getVerificationToken() {
		return verificationToken;
	}
}
