package com.sz.reservation.accountManagement.infrastructure.adapter.inbound;

import com.github.f4b6a3.uuid.util.UuidValidator;
import com.sz.reservation.accountManagement.application.useCase.AccountVerificationUseCase;
import com.sz.reservation.accountManagement.domain.exception.InvalidTokenException;
import com.sz.reservation.accountManagement.infrastructure.dto.annotation.NotNullNotWhitespace;
import jakarta.validation.constraints.Size;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1")
@Validated
public class HttpEmailVerificationController {
    private Logger logger = LogManager.getLogger(HttpEmailVerificationController.class);

    private final int UUID_VERSION = 7;
    private AccountVerificationUseCase accountVerificationUseCase;

    @Autowired
    public HttpEmailVerificationController(AccountVerificationUseCase accountVerificationUseCase) {
        this.accountVerificationUseCase = accountVerificationUseCase;
    }

    @PostMapping("/account/verify")
    public ResponseEntity<String> verifyActivationToken(@RequestParam("token") @Size(min = 36,max = 36,message = "token must be 36 characters") @NotNullNotWhitespace String token){
        logger.info("received token with :{} characters",token.length());
        validateToken(token);
        logger.debug("validation correct");
        accountVerificationUseCase.verifyAccount(token);
        return new ResponseEntity<>("email verified successfully ", HttpStatus.OK);
    }

    @GetMapping("/account/verify2")
    public ResponseEntity<String> getmapping(@RequestParam("token") @Size(min = 36,max = 36,message = "token must be 36 characters") @NotNullNotWhitespace String token){
        logger.info("received token with :{} characters",token.length());
        return new ResponseEntity<>("email verified successfully ", HttpStatus.OK);
    }

    private void validateToken(String token){
        if (!UuidValidator.isValid(token,UUID_VERSION)) throw new InvalidTokenException(token);
    }

}
