package com.sz.reservation.propertyManagement.application;

import com.github.f4b6a3.uuid.UuidCreator;
import com.sz.reservation.globalConfiguration.exception.MediaNotSupportedException;
import com.sz.reservation.globalConfiguration.security.userDetails.CustomUserDetails;
import com.sz.reservation.propertyManagement.domain.port.outbound.ListingPropertyRepository;
import com.sz.reservation.propertyManagement.infrastructure.ListingRequestDto;
import com.sz.reservation.propertyManagement.infrastructure.service.ListingPhotosService;
import com.sz.reservation.propertyManagement.infrastructure.service.TikaListingPhotosValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public class ListingPropertyUseCase {
    private TikaListingPhotosValidator tikaListingPhotosValidator;
    private ListingPhotosService listingPhotosService;

    private ListingPropertyRepository listingPropertyRepository;
    private Logger logger = LogManager.getLogger(ListingPropertyUseCase.class);


    public ListingPropertyUseCase(TikaListingPhotosValidator tikaListingPhotosValidator,ListingPhotosService listingPhotosService,
                                  ListingPropertyRepository listingPropertyRepository) {
        this.tikaListingPhotosValidator = tikaListingPhotosValidator;
        this.listingPhotosService = listingPhotosService;
        this.listingPropertyRepository = listingPropertyRepository;
    }

    public void listProperty(ListingRequestDto listingRequestDto){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = userDetails.getEmail();
        logger.info("starting listing process for user:{} ",userEmail);

        List<MultipartFile> multipartPhotos = listingRequestDto.getListingPhotos();
        if (!tikaListingPhotosValidator.isPhotoListValid(multipartPhotos)) {
            throw new MediaNotSupportedException("invalid format for one or more listing photos");
        }
        List<ListingPhoto> listingPhotos = listingPhotosService.createListingPhotoList(multipartPhotos);

        String id = UuidCreator.getTimeOrderedEpoch().toString();
        ListingProperty listingProperty = new ListingProperty(id,listingRequestDto.getListingTitle(),
                listingRequestDto.getListingDescription(),listingRequestDto.getAddressInfoDto(),
                listingPhotos,listingRequestDto.getNumberOfGuestAllowed(),
                listingRequestDto.getNumberOfBeds(),listingRequestDto.getNumberOfBedrooms(),
                listingRequestDto.getNumberOfBathroom(),listingRequestDto.getPricePerNight(),
                listingRequestDto.getPropertyType(),listingRequestDto.getHousingType(),
                listingRequestDto.getReservationType(),listingRequestDto.getAmenities());

        //store listing in db
        logger.info("storing property listing for user:{}",userEmail);
        listingPropertyRepository.create(listingProperty);

        //store photos in file system

        logger.info("sending email to user :{}",userEmail);
        //send email

    }







}
