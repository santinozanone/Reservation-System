package com.sz.reservation.listingManagement.domain.port.outbound;

import com.sz.reservation.listingManagement.domain.ListingProperty;

import java.util.Optional;

public interface ListingPropertyRepository {
    void create(String hostId,ListingProperty listingProperty);
    Optional<ListingProperty>findById(String listingId);
    void delete (String listingId);

}
