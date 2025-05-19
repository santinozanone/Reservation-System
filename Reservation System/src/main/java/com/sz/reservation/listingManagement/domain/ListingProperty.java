package com.sz.reservation.listingManagement.domain;

import java.util.List;

public class ListingProperty {

    private String id;

    private String hostId;

    private String listingTitle;

    private String listingDescription;

    private AddressInfo addressInfo;

    private int numberOfGuestAllowed;

    private int numberOfBeds;

    private int numberOfBedrooms;

    private int numberOfBathroom;

    private double pricePerNight; // check if double is correct for this type of use

    private PropertyType propertyType;

    private HousingType housingType;

    private ReservationType reservationType;

    private List<AmenitiesType> amenities;

    private boolean enabled;
    public ListingProperty(String id, String hostId, String listingTitle, String listingDescription, AddressInfo addressInfo, int numberOfGuestAllowed,
                           int numberOfBeds, int numberOfBedrooms, int numberOfBathroom, double pricePerNight, PropertyType propertyType,
                           HousingType housingType, ReservationType reservationType, List<AmenitiesType> amenities, boolean enabled) {
        this.id = id;
        this.hostId = hostId;
        this.listingTitle = listingTitle;
        this.listingDescription = listingDescription;
        this.addressInfo = addressInfo;
        this.numberOfGuestAllowed = numberOfGuestAllowed;
        this.numberOfBeds = numberOfBeds;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathroom = numberOfBathroom;
        this.pricePerNight = pricePerNight;
        this.propertyType = propertyType;
        this.housingType = housingType;
        this.reservationType = reservationType;
        this.amenities = amenities;
        this.enabled = enabled;
    }


    public String getId() {
        return id;
    }

    public String getHostId() {
        return hostId;
    }

    public String getListingTitle() {
        return listingTitle;
    }

    public String getListingDescription() {
        return listingDescription;
    }

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public int getNumberOfGuestAllowed() {
        return numberOfGuestAllowed;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public int getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public int getNumberOfBathroom() {
        return numberOfBathroom;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public HousingType getHousingType() {
        return housingType;
    }

    public ReservationType getReservationType() {
        return reservationType;
    }

    public List<AmenitiesType> getAmenities() {
        return amenities;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
