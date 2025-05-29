package com.sz.reservation.accountManagement.infrastructure.adapter.inbound;

import com.sz.reservation.accountManagement.infrastructure.dto.AccountCreationRequest;
import com.sz.reservation.globalConfiguration.exception.InvalidRequestException;
import com.sz.reservation.globalConfiguration.exception.InvalidRequestTypeException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.commons.fileupload2.core.FileItemInput;
import org.apache.commons.fileupload2.core.FileItemInputIterator;
import org.apache.commons.fileupload2.jakarta.servlet5.JakartaServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.sz.reservation.accountManagement.application.useCase.AccountRegistrationUseCase;
import java.io.*;

@Controller
@RequestMapping("/account")
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
	@PostMapping("/registration/profile-picture")
	public ResponseEntity<String> submitProfilePicture(HttpServletRequest request) throws IOException {
		boolean isMultipart = JakartaServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			throw new InvalidRequestTypeException("The request is not multipart");
		}
		JakartaServletFileUpload upload = new JakartaServletFileUpload();
		FileItemInputIterator fileItemInputIterator = upload.getItemIterator(request);

		if (!fileItemInputIterator.hasNext()){
			throw new InvalidRequestException("Empty request");
		}

		FileItemInput item = fileItemInputIterator.next();

		if (item.isFormField()){
			throw new InvalidRequestException("Request must contain a file");
		}

		if (!item.getFieldName().equals("PFP")){
			throw new InvalidRequestException("Incorrect field name");
		}

		BufferedInputStream inputStream = new BufferedInputStream(item.getInputStream());

		return null;
	}

}

