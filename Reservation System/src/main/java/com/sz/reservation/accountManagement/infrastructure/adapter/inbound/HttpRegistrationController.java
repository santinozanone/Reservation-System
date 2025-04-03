package com.sz.reservation.accountManagement.infrastructure.adapter.inbound;

import com.sz.reservation.accountManagement.infrastructure.dto.AccountCreationRequest;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.sz.reservation.accountManagement.application.useCase.AccountRegistrationUseCase;

import java.io.IOException;

@Controller
@RequestMapping("/api/v1/account")
public class HttpRegistrationController {
	private Logger logger = LogManager.getLogger(HttpRegistrationController.class);

	private AccountRegistrationUseCase accountRegistrationUseCase;
	@Autowired
	public HttpRegistrationController(AccountRegistrationUseCase accountRegistrationUseCase) {
		this.accountRegistrationUseCase = accountRegistrationUseCase;
	}
	@PostMapping("/registration")
	public ResponseEntity<String> registerUser(@ModelAttribute @Valid AccountCreationRequest request) throws IOException {

		logger.info("User registration request with email {}, arrived",request.getEmail());
		accountRegistrationUseCase.registerNotEnabledUser(request);
		logger.info("User with email {} registered succesfully",request.getEmail());
		return new ResponseEntity<>("User Registered Successfully, Pending validation",HttpStatus.CREATED);

	}


}

