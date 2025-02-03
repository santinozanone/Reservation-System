package com.sz.reservation.registration.domain.model;

public class PhoneNumber {
	private Integer id;
	private String countryCode;
	private String phoneNumber;

	private final String ARGENTINIAN_COUNTRY_CODE = "+54";
	private final String ARGENTINIAN_SPECIAL_CHARACTER = "9";


	public PhoneNumber(String countryCode, String phoneNumber) {
		this.id = id;
		this.countryCode = countryCode;
		this.phoneNumber = formatNumber(countryCode,phoneNumber);
	}

	public Integer getId() {
		return id;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	private String formatNumber(String countryCode,String phoneNumber){
		if (countryCode.equals(ARGENTINIAN_COUNTRY_CODE) && !phoneNumber.startsWith(ARGENTINIAN_SPECIAL_CHARACTER)){
			return ARGENTINIAN_SPECIAL_CHARACTER.concat(phoneNumber);
		}
		return phoneNumber;
	}
	
}
