package com.sz.reservation.propertyManagement.application;

import com.sz.reservation.propertyManagement.domain.AmenitiesType;
import com.sz.reservation.propertyManagement.domain.HousingType;
import com.sz.reservation.propertyManagement.domain.PropertyType;
import com.sz.reservation.propertyManagement.domain.ReservationType;
import com.sz.reservation.propertyManagement.infrastructure.AddressInfoRequestDto;

import java.util.List;

public class ListingProperty {

    private String id;
    private String listingTitle;

    private String listingDescription;

    private AddressInfoRequestDto addressInfoDto;

    private List<ListingPhoto> listingPhotosID;

    private int numberOfGuestAllowed;

    private int numberOfBeds;

    private int numberOfBedrooms;

    private int numberOfBathroom;

    private double pricePerNight; // check if double is correct for this type of use

    private PropertyType propertyType;

    private HousingType housingType;

    private ReservationType reservationType;

    private List<AmenitiesType> amenities;

    public ListingProperty(String id,String listingTitle, String listingDescription, AddressInfoRequestDto addressInfoDto,
                           List<ListingPhoto> listingPhotosID, int numberOfGuestAllowed, int numberOfBeds, int numberOfBedrooms,
                           int numberOfBathroom, double pricePerNight, PropertyType propertyType, HousingType housingType,
                           ReservationType reservationType, List<AmenitiesType> amenities) {
        this.id = id;
        this.listingTitle = listingTitle;
        this.listingDescription = listingDescription;
        this.addressInfoDto = addressInfoDto;
        this.listingPhotosID = listingPhotosID;
        this.numberOfGuestAllowed = numberOfGuestAllowed;
        this.numberOfBeds = numberOfBeds;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathroom = numberOfBathroom;
        this.pricePerNight = pricePerNight;
        this.propertyType = propertyType;
        this.housingType = housingType;
        this.reservationType = reservationType;
        this.amenities = amenities;
    }

    public String getId() {
        return id;
    }

    public String getListingTitle() {
        return listingTitle;
    }

    public String getListingDescription() {
        return listingDescription;
    }

    public AddressInfoRequestDto getAddressInfoDto() {
        return addressInfoDto;
    }

    public List<ListingPhoto> getListingPhotosID() {
        return listingPhotosID;
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
}
