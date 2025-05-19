package com.sz.reservation.listingManagement.application.useCase.listing;

public class ListingImageMetadata {
    private String imageId;
    private String listingId;
    private String pathName;

    public ListingImageMetadata(String imageId,String listingId, String pathName) {
        this.imageId = imageId;
        this.listingId = listingId;
        this.pathName = pathName;
    }

    public String getImageId() {
        return imageId;
    }

    public String getListingId() {
        return listingId;
    }

    public String getPathName() {
        return pathName;
    }
}
