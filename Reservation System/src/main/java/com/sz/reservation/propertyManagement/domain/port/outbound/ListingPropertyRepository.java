package com.sz.reservation.propertyManagement.domain.port.outbound;

import com.sz.reservation.propertyManagement.application.ListingProperty;

public interface ListingPropertyRepository {
    void create(ListingProperty listingProperty);
}
