package com.sz.reservation.listingManagement.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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

    private BigDecimal pricePerNight;

    private PropertyType propertyType;

    private HousingType housingType;

    private ReservationType reservationType;

    private List<AmenitiesType> amenities;

    private boolean enabled;
    public ListingProperty(String id, String hostId, String listingTitle, String listingDescription, AddressInfo addressInfo, int numberOfGuestAllowed,
                           int numberOfBeds, int numberOfBedrooms, int numberOfBathroom, BigDecimal pricePerNight, PropertyType propertyType,
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

    public BigDecimal getPricePerNight() {
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

    public void enable(){
        this.enabled = true;
    }
    public void disable(){
        this.enabled = false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ListingProperty that = (ListingProperty) o;
        return getNumberOfGuestAllowed() == that.getNumberOfGuestAllowed() &&
                getNumberOfBeds() == that.getNumberOfBeds() && getNumberOfBedrooms() == that.getNumberOfBedrooms() &&
                getNumberOfBathroom() == that.getNumberOfBathroom() && isEnabled() == that.isEnabled() &&
                Objects.equals(getId(), that.getId()) && Objects.equals(getHostId(), that.getHostId()) &&
                Objects.equals(getListingTitle(), that.getListingTitle()) &&
                Objects.equals(getListingDescription(), that.getListingDescription()) &&
                Objects.equals(getAddressInfo(), that.getAddressInfo()) &&
               (getPricePerNight().compareTo(that.getPricePerNight() ) == 0) &&
                getPropertyType() == that.getPropertyType() &&
                getHousingType() == that.getHousingType() &&
                getReservationType() == that.getReservationType() && Objects.equals(getAmenities(), that.getAmenities());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getId());
        result = 31 * result + Objects.hashCode(getHostId());
        result = 31 * result + Objects.hashCode(getListingTitle());
        result = 31 * result + Objects.hashCode(getListingDescription());
        result = 31 * result + Objects.hashCode(getAddressInfo());
        result = 31 * result + getNumberOfGuestAllowed();
        result = 31 * result + getNumberOfBeds();
        result = 31 * result + getNumberOfBedrooms();
        result = 31 * result + getNumberOfBathroom();
        result = 31 * result + Objects.hashCode(getPricePerNight());
        result = 31 * result + Objects.hashCode(getPropertyType());
        result = 31 * result + Objects.hashCode(getHousingType());
        result = 31 * result + Objects.hashCode(getReservationType());
        result = 31 * result + Objects.hashCode(getAmenities());
        result = 31 * result + Boolean.hashCode(isEnabled());
        return result;
    }
}
