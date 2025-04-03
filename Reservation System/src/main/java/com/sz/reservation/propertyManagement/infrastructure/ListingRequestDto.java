package com.sz.reservation.propertyManagement.infrastructure;

import com.sz.reservation.propertyManagement.domain.AmenitiesType;
import com.sz.reservation.propertyManagement.domain.HousingType;
import com.sz.reservation.propertyManagement.domain.PropertyType;
import com.sz.reservation.propertyManagement.domain.ReservationType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ListingRequestDto {
    @Size(min = 5,max = 32)
    @NotNull
    private String listingTitle;

    @Size(min = 5,max = 500)
    @NotNull
    private String listingDescription;

    @Valid
    private AddressInfoRequestDto addressInfoDto;

    @NotEmpty
    @Size(min = 5,max = 50)
    private List<MultipartFile> listingPhotos;

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


    @NotEmpty
    private List<AmenitiesType> amenities;

    public ListingRequestDto(String listingTitle, String listingDescription, AddressInfoRequestDto addressInfoDto, List<MultipartFile> listingPhotos, int numberOfGuestAllowed, int numberOfBeds, int numberOfBedrooms, int numberOfBathroom, double pricePerNight, PropertyType propertyType, HousingType housingType, ReservationType reservationType, List<AmenitiesType> amenities) {
        this.listingTitle = listingTitle;
        this.listingDescription = listingDescription;
        this.addressInfoDto = addressInfoDto;
        this.listingPhotos = listingPhotos;
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

    public String getListingTitle() {
        return listingTitle;
    }

    public String getListingDescription() {
        return listingDescription;
    }

    public AddressInfoRequestDto getAddressInfoDto() {
        return addressInfoDto;
    }

    public List<MultipartFile> getListingPhotos() {
        return listingPhotos;
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
