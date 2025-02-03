package com.sz.reservation.registration.domain.model;

import java.util.Date;

public class User {
	private String username;
	private String name;
	private String surname;

	private String email;
	private PhoneNumber phoneNumber;
	private Date birthDate; // check if should be java.util or java.sql
	private String nationality; // should be enum
	private ProfilePicture profilePicture;
	private String password; //will have to hash it

	private boolean isEnabled;
	// Constructor

	public User( String username, String name, String surname, String email, PhoneNumber phoneNumber, Date birthDate, String nationality, ProfilePicture profilePicture, String password, boolean isEnabled) {

		this.username = username;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.birthDate = birthDate;
		this.nationality = nationality;
		this.profilePicture = profilePicture;
		this.password = password;
		this.isEnabled = isEnabled;
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

	public Date getBirthDate() {
		return birthDate;
	}

	public String getNationality() {
		return nationality;
	}

	public ProfilePicture getProfilePicture() {
		return profilePicture;
	}

	public String getPassword() {
		return password;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

//Some core functionality like change phone number or username
	
}
