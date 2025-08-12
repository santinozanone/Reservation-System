package com.sz.reservation.listingManagement.domain.port.outbound;

public interface ListingReservationLockRepository {
    void lockByListingId(String listingId);
}
