package com.sz.reservation.listingManagement.infrastructure;

import com.sz.reservation.listingManagement.application.exception.InvalidAmenitiesException;
import com.sz.reservation.listingManagement.domain.AmenitiesType;
import com.sz.reservation.listingManagement.domain.HousingType;
import com.sz.reservation.listingManagement.domain.PropertyType;
import com.sz.reservation.listingManagement.domain.ReservationType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListingRequestDto {
    @Size(min = 5,max = 32)
    @NotNull
    private String listingTitle;

    @Size(min = 5,max = 500)
    @NotNull
    private String listingDescription;

    @Valid
    @NotNull
    private AddressInfoRequestDto addressInfoDto;

    @Min(1)
    @Max(16)
    private int numberOfGuestAllowed;

    @Min(1)
    @Max(50)
    private int numberOfBeds;

    @Min(0)
    @Max(50)
    private int numberOfBedrooms;

    @Min(1)
    @Max(50)
    private int numberOfBathroom;

    @Min(10)
    private double pricePerNight; // check if double is correct for this type of use

    @NotNull
    private PropertyType propertyType;

    @NotNull
    private HousingType housingType;

    @NotNull
    private ReservationType reservationType;



    private List<AmenitiesType> amenities;



    public ListingRequestDto(String listingTitle, String listingDescription, AddressInfoRequestDto addressInfoDto, int numberOfGuestAllowed, int numberOfBeds, int numberOfBedrooms, int numberOfBathroom, double pricePerNight, PropertyType propertyType, HousingType housingType, ReservationType reservationType, List<AmenitiesType> amenities) {
        this.listingTitle = listingTitle;
        this.listingDescription = listingDescription;
        this.addressInfoDto = addressInfoDto;
        this.numberOfGuestAllowed = numberOfGuestAllowed;
        this.numberOfBeds = numberOfBeds;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathroom = numberOfBathroom;
        this.pricePerNight = pricePerNight;
        this.propertyType = propertyType;
        this.housingType = housingType;
        this.reservationType = reservationType;
        this.amenities = validateAmenities(amenities);
    }

    private List<AmenitiesType> validateAmenities(List<AmenitiesType> amenities){
        if (amenities == null || amenities.isEmpty()) throw new InvalidAmenitiesException();
        Set<AmenitiesType> amenitiesTypeSet = new HashSet<>();
        List<AmenitiesType> typeList = amenities.stream().filter(a -> amenitiesTypeSet.add(a) == false).toList();
        if (!typeList.isEmpty()) throw new InvalidAmenitiesException("Repeated amenities types");
        return amenities;
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
