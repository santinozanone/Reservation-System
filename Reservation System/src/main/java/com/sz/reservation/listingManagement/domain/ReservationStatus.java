package com.sz.reservation.listingManagement.domain;

public enum ReservationStatus {
    CONFIRMED(1),
    PENDING_APPROVAL(2),
    CANCELED(3),
    DISAPPROVED(4),
    COMPLETED(5);

    private final int id;

    ReservationStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
