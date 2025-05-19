package com.sz.reservation.listingManagement.domain.port.outbound;

import com.sz.reservation.listingManagement.application.useCase.listing.StorageImageProcessingState;

import java.io.BufferedInputStream;

public interface ListingImageStorage {

    StorageImageProcessingState store(String originalFilename,String hostId,String listingId,String photoId,BufferedInputStream bufferedInputStream);
    void delete (String filepath);
}
