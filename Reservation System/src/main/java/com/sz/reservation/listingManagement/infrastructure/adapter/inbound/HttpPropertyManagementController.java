package com.sz.reservation.listingManagement.infrastructure.adapter.inbound;

import com.github.f4b6a3.uuid.util.UuidValidator;
import com.sz.reservation.accountManagement.domain.event.AccountCreatedEvent;
import com.sz.reservation.accountManagement.domain.port.outbound.AccountRepository;
import com.sz.reservation.globalConfiguration.exception.InvalidRequestTypeException;
import com.sz.reservation.globalConfiguration.security.userDetails.CustomUserDetails;
import com.sz.reservation.listingManagement.application.useCase.listing.ListingImageState;
import com.sz.reservation.listingManagement.application.exception.*;
import com.sz.reservation.listingManagement.application.useCase.listing.ListingPropertyUseCase;
import com.sz.reservation.listingManagement.infrastructure.ListingRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.commons.fileupload2.core.FileItemInput;
import org.apache.commons.fileupload2.core.FileItemInputIterator;
import org.apache.commons.fileupload2.jakarta.servlet5.JakartaServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/listing")
public class HttpPropertyManagementController {
    private static final int UUID_VERSION = 7;
    private static final String LISTING_ID = "listingId";
    private static final String FILE_FIELD_NAME = "file";
    private Logger logger = LogManager.getLogger(HttpPropertyManagementController.class);
    private ListingPropertyUseCase listingPropertyUseCase;

    @Autowired
    public HttpPropertyManagementController(ListingPropertyUseCase listingPropertyUseCase) {
        this.listingPropertyUseCase = listingPropertyUseCase;
    }

    @PostMapping("/images-upload")
    public ResponseEntity uploadListingImages(HttpServletRequest request) throws IOException {
        List<ListingImageUploadResult> listingImageUploadResults = new ArrayList<>();
        boolean oneFileSucceeded = false;
        boolean isMultipart = JakartaServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            throw new InvalidRequestTypeException("The request is not multipart");
        }

        JakartaServletFileUpload upload = new JakartaServletFileUpload();
        FileItemInputIterator fileItemInputIterator = upload.getItemIterator(request);

        if (!fileItemInputIterator.hasNext()) {
            throw new IncompleteUploadRequestException("Incomplete request");
        }

        String listingId = validateRequest(fileItemInputIterator);

        while (fileItemInputIterator.hasNext()) {
            FileItemInput item = fileItemInputIterator.next();
            if (!isFileItemInputValid(item, listingImageUploadResults)) continue;

            String fileName = FilenameUtils.getName(item.getName());
            ListingImageState state = processInputStream(fileName, listingId, item);
            if (state == ListingImageState.CORRECT) {
                oneFileSucceeded = true;
                listingImageUploadResults.add(new ListingImageUploadResult(fileName, ListingImageState.CORRECT));
            } else {
                listingImageUploadResults.add(new ListingImageUploadResult(fileName, ListingImageState.DISCARDED));
            }
        }
        return generateResponse(listingId, listingImageUploadResults, oneFileSucceeded);
    }


    @PostMapping
    public ResponseEntity<String> listPropertyForReservation(@Valid @RequestBody ListingRequestDto listingRequestDto) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String hostId = userDetails.getId();
        String listingId = listingPropertyUseCase.listProperty(hostId, listingRequestDto);
        return new ResponseEntity<String>("Property listed correctly, listingId: " + listingId, HttpStatus.OK);
    }


    private String validateRequest(FileItemInputIterator fileItemInputIterator) throws IOException {
        String listingId;
        FileItemInput formItem = fileItemInputIterator.next();
        if (!formItem.isFormField()) throw new IncompleteUploadRequestException();
        String name = formItem.getFieldName();
        if (!name.equals(LISTING_ID)) {
            throw new InvalidFormFieldException(name);
        }
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(formItem.getInputStream()))) {
            listingId = bufferedReader.readLine();
        }
        if (listingId == null || listingId.isEmpty() || !isListingIdValid(listingId)) {
            throw new InvalidListingIdException("Empty or invalid listing id");
        }

        if (!fileItemInputIterator.hasNext()) {
            throw new IncompleteUploadRequestException("Incomplete request");
        }
        return listingId;
    }

    private boolean isFileItemInputValid(FileItemInput item, List<ListingImageUploadResult> listingImageUploadResults) throws IOException {
        if (!item.getFieldName().equals(FILE_FIELD_NAME)) {
            return false;
        }

        String fileName = item.getName();
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        fileName = FilenameUtils.getName(fileName);

        if (item.isFormField()) {
            listingImageUploadResults.add(new ListingImageUploadResult(fileName, ListingImageState.DISCARDED));
            return false;
        }
        return true;
    }

    private ListingImageState processInputStream(String fileName, String listingId, FileItemInput item) throws IOException {
        ListingImageState state;
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String hostId = userDetails.getId();
        try (BufferedInputStream inputStream = new BufferedInputStream(item.getInputStream())) {
            state = listingPropertyUseCase.uploadListingImages(hostId, listingId, fileName, inputStream);
        }
        return state;
    }


    private ResponseEntity generateResponse(String listingId, List<ListingImageUploadResult> listingImageUploadResults, boolean oneFileSucceeded) {
        HttpStatus httpStatus;
        ListingImageUploadResponseStatus listingImageUploadResponseStatus;

        if (listingImageUploadResults.isEmpty()) {
            listingImageUploadResponseStatus = ListingImageUploadResponseStatus.NO_FILES_DETECTED;
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (!oneFileSucceeded) {
            listingImageUploadResponseStatus = ListingImageUploadResponseStatus.ALL_DISCARDED;
            httpStatus = HttpStatus.BAD_REQUEST;
        } else {
            listingImageUploadResponseStatus = ListingImageUploadResponseStatus.CORRECT;
            httpStatus = HttpStatus.OK;
        }
        ListingImageUploadResponse listingImageUploadResponse = new ListingImageUploadResponse(listingId, listingImageUploadResponseStatus, listingImageUploadResults);
        return new ResponseEntity<>(listingImageUploadResponse, httpStatus);
    }






    private boolean isListingIdValid(String listingId) {
        if (!UuidValidator.isValid(listingId, UUID_VERSION)) return false;
        return true;
    }

}
