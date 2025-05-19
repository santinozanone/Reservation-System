package com.sz.reservation.listingManagement.application.exception;

public class IncompleteUploadRequestException extends RuntimeException{
    public IncompleteUploadRequestException(String message) {
        super(message);
    }

    public IncompleteUploadRequestException() {
    }
}
