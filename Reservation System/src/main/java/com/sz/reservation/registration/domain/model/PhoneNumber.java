package com.sz.reservation.registration.domain.model;

public class PhoneNumber {
	private Integer id;
	private String countryCode;
	private String areaCode;
	private String phoneNumber;
	
	public PhoneNumber(String countryCode, String areaCode, String phoneNumber) {
		this.countryCode = countryCode;
		this.areaCode = areaCode;
		this.phoneNumber = phoneNumber;
	}

	public Integer getId() {
		return id;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	
	
}
