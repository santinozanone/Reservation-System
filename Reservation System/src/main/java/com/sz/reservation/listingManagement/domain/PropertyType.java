package com.sz.reservation.listingManagement.domain;

public enum PropertyType {
    HOUSE(1),
    APARTMENT(2),
    BARN(3),
    BED_AND_BREAKFAST(4),
    BOAT(5),
    CABIN(6),
    CAMPER(7),
    CASTLE(8),
    CAVE(9),
    CONTAINER(10),
    FARM(11),
    HOTEL(12);

    private final int id;

    PropertyType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


}
