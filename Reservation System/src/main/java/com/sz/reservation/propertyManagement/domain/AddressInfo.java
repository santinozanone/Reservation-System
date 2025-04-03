package com.sz.reservation.propertyManagement.domain;

public class AddressInfo {
    private String country;
    private String streetAddress;
    private String apartmentNumber; // might be null
    private String postalCode;
    private String region; // mostly provinces/states, for example Buenos Aires
    private String locality; // its the town or city of the region
}
