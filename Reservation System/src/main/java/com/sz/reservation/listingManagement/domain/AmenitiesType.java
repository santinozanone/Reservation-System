package com.sz.reservation.listingManagement.domain;

public enum AmenitiesType {
    WIFI(1),
    TV(2),
    KITCHEN(3),
    WASHER(4),
    FREE_PARKING(5),
    AIR_CONDITIONING(6),
    DEDICATED_WORKSPACE(7),
    POOL(8),
    PATIO(9),
    BBQ_GRILL(10),
    LAKE_ACCESS(11),
    BEACH_ACCESS(12),
    GYM(13);

    private final int id;

    AmenitiesType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
