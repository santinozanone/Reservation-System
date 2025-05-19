package com.sz.reservation.listingManagement.domain;

public enum HousingType {
    ENTIRE(1),
    ROOM(2),
    SHARED_ROOM(3);

    private final int id;

    HousingType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
