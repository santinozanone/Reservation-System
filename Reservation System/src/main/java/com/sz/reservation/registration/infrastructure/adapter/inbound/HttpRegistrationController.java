package com.sz.reservation.registration.infrastructure.adapter.inbound;

import com.sz.reservation.registration.infrastructure.dto.UserRegistrationRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.sz.reservation.registration.application.useCase.RegistrationUseCase;

import java.io.IOException;

@Controller
@RequestMapping("/api/v1")
public class HttpRegistrationController {

	private RegistrationUseCase registrationUseCase;
	@Autowired
	public HttpRegistrationController(RegistrationUseCase registrationUseCase) {
		this.registrationUseCase = registrationUseCase;
	}
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@ModelAttribute @Valid UserRegistrationRequest request) throws IOException {
		registrationUseCase.registerNotEnabledUser(request);
		return new ResponseEntity<>("User Registered Successfully, Pending validation",HttpStatus.CREATED);
	}


}

