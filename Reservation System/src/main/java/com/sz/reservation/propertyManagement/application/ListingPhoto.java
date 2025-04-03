package com.sz.reservation.propertyManagement.application;

public class ListingPhoto {
    private String id;
    private String pathName;

    public ListingPhoto(String id, String pathName) {
        this.id = id;
        this.pathName = pathName;
    }

    public String getId() {
        return id;
    }

    public String getPathName() {
        return pathName;
    }
}
