package com.sz.reservation.listingManagement.infrastructure.adapter.inbound;

import java.util.List;

public class ListingImageUploadResponse {
    private String listingId;
    private ListingImageUploadResponseStatus listingImageUploadResponseStatus;
    private List<ListingImageUploadResult> listingImageUploadResults;


    public ListingImageUploadResponse(String listingId, ListingImageUploadResponseStatus listingImageUploadResponseStatus, List<ListingImageUploadResult> listingImageUploadResults) {
        this.listingId = listingId;
        this.listingImageUploadResponseStatus = listingImageUploadResponseStatus;
        this.listingImageUploadResults = listingImageUploadResults;
    }

    public String getListingId() {
        return listingId;
    }

    public ListingImageUploadResponseStatus getUploadResponseStatus() {
        return listingImageUploadResponseStatus;
    }

    public List<ListingImageUploadResult> getListingUploadResults() {
        return listingImageUploadResults;
    }
}
