package com.sz.reservation.listingManagement.infrastructure;

import com.sz.reservation.util.annotation.NotNullNotWhitespace;
import com.sz.reservation.listingManagement.application.exception.InvalidAmenitiesException;
import com.sz.reservation.listingManagement.domain.AmenitiesType;
import com.sz.reservation.listingManagement.domain.HousingType;
import com.sz.reservation.listingManagement.domain.PropertyType;
import com.sz.reservation.listingManagement.domain.ReservationType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UpdateListingRequestDto {

    @NotNullNotWhitespace
    private String listingId;

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

    @NotNull
    private boolean enabled;



    public UpdateListingRequestDto(String listingId,String listingTitle, String listingDescription, AddressInfoRequestDto addressInfoDto, int numberOfGuestAllowed, int numberOfBeds, int numberOfBedrooms, int numberOfBathroom, double pricePerNight, PropertyType propertyType, HousingType housingType, ReservationType reservationType, List<AmenitiesType> amenities,boolean enabled) {
        this.listingId = listingId;
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
        this.enabled = enabled;
    }

    private List<AmenitiesType> validateAmenities(List<AmenitiesType> amenities){
        if (amenities == null || amenities.isEmpty()) throw new InvalidAmenitiesException();
        Set<AmenitiesType> amenitiesTypeSet = new HashSet<>();
        List<AmenitiesType> typeList = amenities.stream().filter(a -> amenitiesTypeSet.add(a) == false).toList();
        if (!typeList.isEmpty()) throw new InvalidAmenitiesException("Repeated amenities types");
        return amenities;
    }

    public String getListingId() {
        return listingId;
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

    public boolean isEnabled() {
        return enabled;
    }
}
