package com.sz.reservation.accountManagement.infrastructure.adapter.inbound;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("api/v1")
public class HttpEmailVerificationController {


    @PostMapping("/account/verify")
    public ResponseEntity<String> verifyActivationToken(@RequestParam("token") String token){
        // call to app service
        return new ResponseEntity<>("email verified successfully  "+ token, HttpStatus.OK);

    }

}
