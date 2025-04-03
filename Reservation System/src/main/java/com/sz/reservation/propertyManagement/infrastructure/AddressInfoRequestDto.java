package com.sz.reservation.propertyManagement.infrastructure;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AddressInfoRequestDto {
    @NotBlank
    @Size(min = 4)
    private String country;

    @NotBlank
    private String streetAddress;

    @NotNull
    private String apartmentNumber; // might be empty

    @NotBlank
    private String postalCode;

    @NotBlank
    private String region; // mostly provinces/states, for example Buenos Aires
    @NotBlank
    private String locality; // its the town or city of the region

    public AddressInfoRequestDto(String country, String streetAddress, String apartmentNumber, String postalCode, String region, String locality) {
        this.country = country;
        this.streetAddress = streetAddress;
        this.apartmentNumber = apartmentNumber;
        this.postalCode = postalCode;
        this.region = region;
        this.locality = locality;
    }

    public AddressInfoRequestDto(String country, String streetAddress, String postalCode, String region, String locality) {
        this.country = country;
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.region = region;
        this.locality = locality;
        this.apartmentNumber = "";
    }
}
