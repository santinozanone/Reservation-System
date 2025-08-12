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
import com.sz.reservation.listingManagement.infrastructure.UpdateListingRequestDto;
import com.sz.reservation.listingManagement.infrastructure.service.TikaListingImageValidator;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.math.BigDecimal;
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

    @Transactional(transactionManager = "listing.transactionManager")
    public String listProperty(String hostId, ListingRequestDto listingRequestDto) {
        boolean enabledListing = true;
        AddressInfoRequestDto addressInfoRequestDto = listingRequestDto.getAddressInfoDto();
        String listingID = UuidCreator.getTimeOrderedEpoch().toString();
        String addressId = UuidCreator.getTimeOrderedEpoch().toString();

        AddressInfo addressInfo = new AddressInfo(addressId, addressInfoRequestDto.getApartmentNumber(),
                addressInfoRequestDto.getCountry(), addressInfoRequestDto.getLocality(),
                addressInfoRequestDto.getPostalCode(), addressInfoRequestDto.getRegion(),
                addressInfoRequestDto.getStreetAddress());

        ListingProperty listingProperty = new ListingProperty(listingID, hostId,
                listingRequestDto.getListingTitle(), listingRequestDto.getListingDescription(),
                addressInfo, listingRequestDto.getNumberOfGuestAllowed(),
                listingRequestDto.getNumberOfBeds(), listingRequestDto.getNumberOfBedrooms(),
                listingRequestDto.getNumberOfBathroom(), BigDecimal.valueOf(listingRequestDto.getPricePerNight()),
                listingRequestDto.getPropertyType(), listingRequestDto.getHousingType(),
                listingRequestDto.getReservationType(), listingRequestDto.getAmenities(), enabledListing);

        listingPropertyRepository.create(listingProperty);
        return listingID;
    }

    @Transactional(transactionManager = "listing.transactionManager")
    public void updateProperty(String hostId, UpdateListingRequestDto updateListingRequestDto) {
        Optional<ListingProperty> optionalListingProperty = listingPropertyRepository.findById(updateListingRequestDto.getListingId());
        validateListingProperty(optionalListingProperty, hostId, updateListingRequestDto.getListingId());

        ListingProperty listingToUpdate = buildUpdatedListingProperty(hostId,optionalListingProperty.get(),updateListingRequestDto);
        listingPropertyRepository.update(listingToUpdate);
    }



    public ListingImageState uploadListingImages(String hostId, String listingId, String originalImageName, BufferedInputStream imageBufferedInputStream) throws IOException {
        Optional<ListingProperty> optionalListingProperty = listingPropertyRepository.findById(listingId);
        validateListingProperty(optionalListingProperty, hostId, listingId);

        if (!listingImageValidator.isListingImageValid(originalImageName, imageBufferedInputStream)) {
            return ListingImageState.DISCARDED;
        }
        //generate uuid
        String photoId = UuidCreator.getTimeOrderedEpoch().toString();

        StorageImageProcessingState listingImageState = listingImageStorage.store(originalImageName, hostId, listingId, photoId, imageBufferedInputStream);
        if (listingImageState.getImageState() == ListingImageState.DISCARDED) {
            listingImageStorage.delete(listingImageState.getImageFilepath());
            return listingImageState.getImageState();
        }

        //store in db
        ListingImageMetadata listingImageMetadata = new ListingImageMetadata(photoId, listingId, listingImageState.getImageFilepath());
        try {
            storeMetadata(listingImageMetadata);
        } catch (DataAccessException e) {
            listingImageStorage.delete(listingImageMetadata.getPathName());
            throw e;
        }
        return listingImageState.getImageState();
    }

    @Transactional(transactionManager = "listing.transactionManager")
    private void storeMetadata(ListingImageMetadata listingImageMetadata) {
            listingImageMetadataRepository.create(listingImageMetadata);
    }

    private void validateListingProperty(Optional<ListingProperty> optionalListingProperty, String hostId, String listingId) {
        if (optionalListingProperty.isEmpty()) { // if listing does not exist
            throw new InvalidListingIdException("Invalid listing id, " + listingId + ", it doesnt exists in repository");
        }
        if (!optionalListingProperty.get().getHostId().equals(hostId)) { // if you are not the owner
            throw new InvalidListingIdException("Invalid listing id, " + listingId);
        }
        if (!optionalListingProperty.get().isEnabled()) { // if not enabled
            throw new InvalidListingIdException("Invalid listing id, " + listingId);
        }
    }

    private ListingProperty buildUpdatedListingProperty(String hostId, ListingProperty listingProperty, UpdateListingRequestDto updateListingRequestDto) {
        AddressInfo updatedAddressInfo = new AddressInfo(
                listingProperty.getAddressInfo().getId(),
                listingProperty.getAddressInfo().getApartmentNumber(),
                listingProperty.getAddressInfo().getCountry(),
                listingProperty.getAddressInfo().getLocality(),
                listingProperty.getAddressInfo().getPostalCode(),
                listingProperty.getAddressInfo().getRegion(),
                listingProperty.getAddressInfo().getStreetAddress()
        );
        ListingProperty listingToUpdate = new ListingProperty(
                updateListingRequestDto.getListingId(),
                hostId,
                updateListingRequestDto.getListingTitle(),
                updateListingRequestDto.getListingDescription(),
                updatedAddressInfo,
                updateListingRequestDto.getNumberOfGuestAllowed(),
                updateListingRequestDto.getNumberOfBeds(),
                updateListingRequestDto.getNumberOfBedrooms(),
                updateListingRequestDto.getNumberOfBathroom(),
                BigDecimal.valueOf(updateListingRequestDto.getPricePerNight()),
                updateListingRequestDto.getPropertyType(),
                updateListingRequestDto.getHousingType(),
                updateListingRequestDto.getReservationType(),
                updateListingRequestDto.getAmenities(),
                updateListingRequestDto.isEnabled()
        );
        return listingToUpdate;
    }


}
