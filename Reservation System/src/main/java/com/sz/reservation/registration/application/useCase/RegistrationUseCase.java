package com.sz.reservation.registration.application.useCase;

import com.sz.reservation.registration.application.port.outbound.UserRegistrationDb;
import com.sz.reservation.registration.exception.NotSupportedMediaException;
import com.sz.reservation.registration.infrastructure.inbound.UserRequest;
import com.sz.reservation.util.FileValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


public class RegistrationUseCase {
	private UserRegistrationDb userRegistrationDb;
	private FileValidator fileValidator;

	@Autowired
	public RegistrationUseCase(UserRegistrationDb userRegistrationDb, FileValidator fileValidator) {
		this.userRegistrationDb = userRegistrationDb;
		this.fileValidator = fileValidator;
	}

	public void registerNotEnabledUser(UserRequest userRequest) {
		if (!isFileTypeImageCompatible(userRequest.getProfilePicture())) {
			throw new NotSupportedMediaException();
		}
		userRegistrationDb.registerNotEnabledUser(null);/*fill the details later*/
	}

	private boolean isFileTypeImageCompatible(MultipartFile file) {
		try (InputStream inputStream = file.getInputStream()) {
			MediaType mediaType = fileValidator.getRealFileType(inputStream);
			return mediaType.equals(MediaType.IMAGE_JPEG) || mediaType.equals(MediaType.IMAGE_PNG);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}