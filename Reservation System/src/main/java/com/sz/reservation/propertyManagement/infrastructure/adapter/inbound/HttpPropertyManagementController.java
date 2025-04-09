package com.sz.reservation.propertyManagement.infrastructure.adapter.inbound;

import com.sz.reservation.accountManagement.domain.exception.AccountAlreadyVerifiedException;
import com.sz.reservation.globalConfiguration.security.userDetails.CustomUserDetails;
import com.sz.reservation.propertyManagement.infrastructure.ListingRequestDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Controller
@RequestMapping("/listings")
public class HttpPropertyManagementController {
    private Logger logger = LogManager.getLogger(HttpPropertyManagementController.class);


    @PostMapping("/images-upload")
    public ResponseEntity<String> uploadListingImages(){
        return new ResponseEntity<>("sasa",HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> listPropertyForReservation(@RequestBody ListingRequestDto listingRequestDto){
      //  CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new ResponseEntity<>("Property listed correctly for user ", HttpStatus.OK);
    }

}
