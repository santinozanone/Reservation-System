package com.sz.reservation.listingManagement.configuration.exceptionHandler;

import com.sz.reservation.listingManagement.application.exception.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PropertyExceptionHandler {
    private Logger logger = LogManager.getLogger(PropertyExceptionHandler.class);

    @ExceptionHandler(value = InvalidRequestTypeException.class)
    public ProblemDetail handleInvalidRequestTypeException(InvalidRequestTypeException exception){
        ProblemDetail problemDetail =  ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Invalid Request");
        problemDetail.setDetail("The request type is not allowed");
        return problemDetail;
    }

    @ExceptionHandler(value = InvalidListingIdException.class)
    public ProblemDetail handleInvalidListingIdException(InvalidListingIdException exception){
        ProblemDetail problemDetail =  ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Invalid Request");
        problemDetail.setDetail("The listingId provided is not valid");
        return problemDetail;
    }

    @ExceptionHandler(value = InvalidFormFieldException.class)
    public ProblemDetail handleInvalidFormFieldException(InvalidFormFieldException exception){
        ProblemDetail problemDetail =  ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Invalid Request");
        problemDetail.setDetail("The form field: "+exception.getField() + " is not valid");
        return problemDetail;
    }

    @ExceptionHandler(value = IncompleteUploadRequestException.class)
    public ProblemDetail handleIncompleteUploadRequestException(IncompleteUploadRequestException exception){
        ProblemDetail problemDetail =  ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Invalid Request");
        problemDetail.setDetail("The request is not complete");
        return problemDetail;
    }

    @ExceptionHandler(value = InvalidAmenitiesException.class)
    public ProblemDetail handleInvalidAmenitiesException(InvalidAmenitiesException exception){
        ProblemDetail problemDetail =  ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Invalid Request");
        problemDetail.setDetail("The request contains invalid or null amenities");
        return problemDetail;
    }



}

