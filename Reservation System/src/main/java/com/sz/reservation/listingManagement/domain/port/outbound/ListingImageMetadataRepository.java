package com.sz.reservation.listingManagement.domain.port.outbound;

import com.sz.reservation.listingManagement.application.useCase.listing.ListingImageMetadata;

import java.util.Optional;

public interface ListingImageMetadataRepository {
    void create(ListingImageMetadata listingImageMetadata);
    Optional<ListingImageMetadata>findById(String imageId);
}
