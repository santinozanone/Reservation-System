package com.sz.reservation.listingManagement.application.useCase.listing;

public class StorageImageProcessingState {
    private String imageFilepath;
    private ListingImageState listingImageState;

    public StorageImageProcessingState(String imageFilepath, ListingImageState listingImageState) {
        this.imageFilepath = imageFilepath;
        this.listingImageState = listingImageState;
    }

    public String getImageFilepath() {
        return imageFilepath;
    }

    public ListingImageState getImageState() {
        return listingImageState;
    }
}
