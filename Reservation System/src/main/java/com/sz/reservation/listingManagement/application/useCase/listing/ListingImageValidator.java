package com.sz.reservation.listingManagement.application.useCase.listing;

import java.io.BufferedInputStream;

public interface ListingImageValidator {
    boolean isListingImageValid(String originalFileName,BufferedInputStream ImageBufferedStream);
}
