package com.sz.reservation.listingManagement.domain;

public enum ReservationType {
    OWNER_APPROVAL(1), // Reservation is done only if owner approves it
    AUTOMATIC_APPROVAL(2); // Reservation is done automatically without owner approval

    private final int id;

    ReservationType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
