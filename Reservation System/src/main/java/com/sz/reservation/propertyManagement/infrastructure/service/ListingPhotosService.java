package com.sz.reservation.propertyManagement.infrastructure.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.sz.reservation.propertyManagement.application.ListingPhoto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ListingPhotosService {

    private String listingPhotoDirectory;
    private final String PREFIX = "photo_";

    public ListingPhotosService(@Value("${local_listingPhotos_storage.location}")String listingPhotoDirectory) {
        this.listingPhotoDirectory = listingPhotoDirectory;
    }

    public List<ListingPhoto> createListingPhotoList(List<MultipartFile> multipartPhotos){
        List<ListingPhoto> listingPhotos = new ArrayList<>();
        for (MultipartFile multipartPhoto: multipartPhotos){
            String id = UuidCreator.getTimeOrderedEpoch().toString();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String timestamp = dateFormatter.format(LocalDate.now());

            int index = multipartPhoto.getOriginalFilename().lastIndexOf("."); // get index of the last point
            String fileExtension = multipartPhoto.getOriginalFilename().substring(index + 1); // get extension

            String pathName = listingPhotoDirectory.concat(PREFIX).concat(timestamp).concat(id).concat(".").concat(fileExtension);

            listingPhotos.add(new ListingPhoto(id,pathName));
        }
        return listingPhotos;
    }
}
