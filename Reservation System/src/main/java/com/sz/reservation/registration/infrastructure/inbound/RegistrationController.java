package com.sz.reservation.registration.infrastructure.inbound;

import com.sz.reservation.util.FileValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.sz.reservation.registration.domain.model.User;
import com.sz.reservation.registration.application.useCase.RegistrationUseCase;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/api/v1")
public class RegistrationController {

	private RegistrationUseCase registrationUseCase;
	@Autowired
	public RegistrationController(RegistrationUseCase registrationUseCase) {
		this.registrationUseCase = registrationUseCase;
	}
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@ModelAttribute @Valid UserRequest request){
		registrationUseCase.registerNotEnabledUser(request);
		return new ResponseEntity<>("User Registered Successfully, Pending validation",HttpStatus.CREATED);
	}


}

