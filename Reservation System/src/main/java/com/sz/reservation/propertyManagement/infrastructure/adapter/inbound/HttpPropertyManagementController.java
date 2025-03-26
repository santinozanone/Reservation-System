package com.sz.reservation.propertyManagement.infrastructure.adapter.inbound;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1/account")
public class HttpPropertyManagementController {


    @PostMapping
    public ResponseEntity<String> listPropertyForReservation(){

        return new ResponseEntity<>("Property listed correctly", HttpStatus.OK);
    }

}
