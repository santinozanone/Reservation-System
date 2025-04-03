package com.sz.reservation.propertyManagement.infrastructure.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ListingPhotosValidator {
    boolean isPhotoListValid(List<MultipartFile> photoList);
}
