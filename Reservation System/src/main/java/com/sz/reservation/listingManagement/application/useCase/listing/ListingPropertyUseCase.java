package com.sz.reservation.listingManagement.application.useCase.listing;

import com.github.f4b6a3.uuid.UuidCreator;
import com.sz.reservation.listingManagement.domain.ListingProperty;
import com.sz.reservation.listingManagement.domain.AddressInfo;
import com.sz.reservation.listingManagement.domain.exception.InvalidListingIdException;
import com.sz.reservation.listingManagement.domain.port.outbound.ListingImageMetadataRepository;
import com.sz.reservation.listingManagement.domain.port.outbound.ListingImageStorage;
import com.sz.reservation.listingManagement.domain.port.outbound.ListingPropertyRepository;
import com.sz.reservation.listingManagement.infrastructure.AddressInfoRequestDto;
import com.sz.reservation.listingManagement.infrastructure.ListingRequestDto;
import com.sz.reservation.listingManagement.infrastructure.service.TikaListingImageValidator;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Optional;


public class ListingPropertyUseCase {
    private ListingPropertyRepository listingPropertyRepository;
    private TikaListingImageValidator listingImageValidator;
    private ListingImageStorage listingImageStorage;
    private ListingImageMetadataRepository listingImageMetadataRepository;

    public ListingPropertyUseCase(ListingPropertyRepository listingPropertyRepository, TikaListingImageValidator listingImageValidator,
                                  ListingImageStorage listingImageStorage, ListingImageMetadataRepository listingImageMetadataRepository) {
        this.listingPropertyRepository = listingPropertyRepository;
        this.listingImageValidator = listingImageValidator;
        this.listingImageStorage = listingImageStorage;
        this.listingImageMetadataRepository = listingImageMetadataRepository;
    }

    @Transactional
    public String listProperty(String hostId,ListingRequestDto listingRequestDto){
        boolean enabledListing = true;
        AddressInfoRequestDto addressInfoRequestDto = listingRequestDto.getAddressInfoDto();
        String listingID = UuidCreator.getTimeOrderedEpoch().toString();
        String addressId = UuidCreator.getTimeOrderedEpoch().toString();

        AddressInfo addressInfo = new AddressInfo(addressId,addressInfoRequestDto.getApartmentNumber(),
                addressInfoRequestDto.getCountry(),addressInfoRequestDto.getLocality(),
                addressInfoRequestDto.getPostalCode(),addressInfoRequestDto.getRegion(),
                addressInfoRequestDto.getStreetAddress());

        ListingProperty listingProperty = new ListingProperty(listingID,hostId,
                listingRequestDto.getListingTitle(),listingRequestDto.getListingDescription(),
                addressInfo,listingRequestDto.getNumberOfGuestAllowed(),
                listingRequestDto.getNumberOfBeds(),listingRequestDto.getNumberOfBedrooms(),
                listingRequestDto.getNumberOfBathroom(),listingRequestDto.getPricePerNight(),
                listingRequestDto.getPropertyType(),listingRequestDto.getHousingType(),
                listingRequestDto.getReservationType(),listingRequestDto.getAmenities(),enabledListing);

        listingPropertyRepository.create(hostId,listingProperty);
        return listingID;
    }

    @Transactional
    public ListingImageState uploadListingImages(String hostId,String listingId,String originalImageName ,BufferedInputStream imageBufferedInputStream) throws IOException {
        Optional<ListingProperty> optionalListingProperty = listingPropertyRepository.findById(listingId);
        if (optionalListingProperty.isEmpty()) {
            throw new InvalidListingIdException("Invalid listing id, " + listingId +  ", it doesnt exists in repository" );
        }
        if (!optionalListingProperty.get().getHostId().equals(hostId)){
            throw new InvalidListingIdException("Invalid listing id, " + listingId);
        }
        if (!optionalListingProperty.get().isEnabled()){
            throw new InvalidListingIdException("Invalid listing id, " + listingId);
        }

        if (!listingImageValidator.isListingImageValid(originalImageName,imageBufferedInputStream)){
            return ListingImageState.DISCARDED;
        }

        String photoId = UuidCreator.getTimeOrderedEpoch().toString();

        StorageImageProcessingState listingImageState = listingImageStorage.store(originalImageName,hostId,listingId,photoId,imageBufferedInputStream);
        if (listingImageState.getImageState() == ListingImageState.DISCARDED){
            listingImageStorage.delete(listingImageState.getImageFilepath());
            return listingImageState.getImageState();
        }
        ListingImageMetadata listingImageMetadata = new ListingImageMetadata(photoId,listingId,listingImageState.getImageFilepath());
        try {
            listingImageMetadataRepository.create(listingImageMetadata);
        }catch (DataAccessException e){
            listingImageStorage.delete(listingImageState.getImageFilepath());
            throw e;
        }
        return listingImageState.getImageState();
    }



}
