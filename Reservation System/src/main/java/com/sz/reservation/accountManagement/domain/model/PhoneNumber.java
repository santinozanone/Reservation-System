package com.sz.reservation.accountManagement.domain.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PhoneNumber {
	private Logger logger = LogManager.getLogger(PhoneNumber.class);
	private String id;
	private String countryCode;
	private String phoneNumber;
	private final String ARGENTINIAN_COUNTRY_CODE = "+54";
	private final String ARGENTINIAN_SPECIAL_CHARACTER = "9";
	private final String INTERNATIONAL_PREFIX = "+";


	public PhoneNumber(String id,String countryCode, String phoneNumber) {
		this.id = id;
		this.countryCode = formatCountryCode(countryCode);
		this.phoneNumber = formatNumber(countryCode,phoneNumber);
	}

	public String getId() {
		return id;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	private String formatCountryCode(String countryCode){
		if (!countryCode.startsWith(INTERNATIONAL_PREFIX)){
			return INTERNATIONAL_PREFIX.concat(countryCode);
		}
		return countryCode;
	}
	
	private String formatNumber(String countryCode,String phoneNumber){
		if (countryCode.equals(ARGENTINIAN_COUNTRY_CODE) && !phoneNumber.startsWith(ARGENTINIAN_SPECIAL_CHARACTER)){
			logger.info("formatting number:{} , to Argentinian standard",phoneNumber);
			return ARGENTINIAN_SPECIAL_CHARACTER.concat(phoneNumber);
		}
		return phoneNumber;
	}
	
}
