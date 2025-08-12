package com.sz.reservation.listingManagement.infrastructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AddressInfoRequestDto {
    @NotBlank
    @Size(min = 4,max = 60,message = "country must contain betweeen 4 and 60 characters")
    private String country;

    @NotBlank
    @Size(min = 4,max = 200,message = "street address must contain between 4 and 200 characters")
    private String streetAddress;

    @NotNull
    private String apartmentNumber; // might be empty

    @NotBlank
    @Size(min = 2,max = 40,message = "postal code must contain between 2 and 40 characters")
    private String postalCode;

    @NotBlank
    @Size(min = 4,max = 40,message = "region must contain between 4 and 40 characters")
    private String region; // mostly provinces/states, for example "Buenos Aires"
    @NotBlank
    @Size(min = 4,max = 40,message = "locality must contain between 4 and 40 characters")
    private String locality; // its the town or city of the region, for example "Puerto Madero" in buenos aires



  //  @JsonCreator
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

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public String getCountry() {
        return country;
    }

    public String getLocality() {
        return locality;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getRegion() {
        return region;
    }

    public String getStreetAddress() {
        return streetAddress;
    }
}
