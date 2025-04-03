package com.sz.reservation.propertyManagement.infrastructure.adapter.inbound;

import com.sz.reservation.globalConfiguration.security.userDetails.CustomUserDetails;
import com.sz.reservation.propertyManagement.infrastructure.ListingRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1/account/listing")
public class HttpPropertyManagementController {


    @PostMapping
    public ResponseEntity<String> listPropertyForReservation(@RequestBody ListingRequestDto listingRequestDto){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>("Property listed correctly for user "+ userDetails.getUsername() + " " + userDetails.getEmail(), HttpStatus.OK);
    }

}
