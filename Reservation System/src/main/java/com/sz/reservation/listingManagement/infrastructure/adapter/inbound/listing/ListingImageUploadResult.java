package com.sz.reservation.listingManagement.infrastructure.adapter.inbound.listing;

import com.sz.reservation.listingManagement.application.useCase.listing.ListingImageState;

public class ListingImageUploadResult {
    private String filename;
    private ListingImageState state;



    public ListingImageUploadResult(String filename, ListingImageState state) {
        this.filename = filename;
        this.state = state;
    }

    public String getFilename() {
        return filename;
    }

    public ListingImageState getState() {
        return state;
    }


}
