package com.sz.reservation.listingManagement.domain;

public class AddressInfo {
    private String id;
    private String country;
    private String streetAddress;
    private String apartmentNumber; // might be null
    private String postalCode;
    private String region; // mostly provinces/states, for example Buenos Aires
    private String locality; // its the town or city of the region


    public AddressInfo(String id,String apartmentNumber, String country, String locality, String postalCode, String region, String streetAddress) {
        this.id = id;
        this.apartmentNumber = apartmentNumber;
        this.country = country;
        this.locality = locality;
        this.postalCode = postalCode;
        this.region = region;
        this.streetAddress = streetAddress;
    }


    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public String getCountry() {
        return country;
    }

    public String getId() {
        return id;
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
