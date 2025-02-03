package com.sz.reservation.registration.application.useCase;

import com.sz.reservation.registration.domain.exception.MediaNotSupportedException;
import com.sz.reservation.registration.domain.model.PhoneNumber;
import com.sz.reservation.registration.domain.model.ProfilePicture;
import com.sz.reservation.registration.domain.model.User;
import com.sz.reservation.registration.domain.port.outbound.ProfilePictureStorage;
import com.sz.reservation.registration.domain.port.outbound.UserRegistrationDb;
import com.sz.reservation.registration.infrastructure.dto.UserRegistrationRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RegistrationUseCase {
    private UserRegistrationDb userRegistrationDb;
    private HashingService hashingService;
    private ProfilePictureValidator profilePictureValidator;
    private PhoneNumberValidator phoneNumberValidator;

    private ImageResizingService imageResizingService;

    private ProfilePictureStorage profilePictureStorage;

    public RegistrationUseCase(UserRegistrationDb userRegistrationDb,ProfilePictureStorage profilePictureStorage,ImageResizingService imageResizingService ,HashingService hashingService, ProfilePictureValidator profilePictureValidator, PhoneNumberValidator phoneNumberValidator) {
        this.userRegistrationDb = userRegistrationDb;
        this.imageResizingService = imageResizingService;
        this.profilePictureStorage = profilePictureStorage;
        this.hashingService = hashingService;
        this.profilePictureValidator = profilePictureValidator;
        this.phoneNumberValidator = phoneNumberValidator;
    }

    public void registerNotEnabledUser(UserRegistrationRequest userRegistrationRequest) throws IOException {
        validatePicture(userRegistrationRequest.getProfilePicture());
        validatePhoneNumber(userRegistrationRequest.getCountryCode(), userRegistrationRequest.getPhoneNumber());

        PhoneNumber phoneNumber = new PhoneNumber(userRegistrationRequest.getCountryCode() ,userRegistrationRequest.getPhoneNumber());
        String hashedPassword = hashingService.hash(userRegistrationRequest.getPassword());


        Image profilePicturedResized = resizeImage(userRegistrationRequest.getProfilePicture());


        ProfilePicture profilePicture = new ProfilePicture(generateProfilePictureName(userRegistrationRequest.getProfilePicture().getOriginalFilename()),profilePicturedResized,new Dimension(50, 50));

        User notRegisteredUser = new User(userRegistrationRequest.getUsername(), userRegistrationRequest.getName(), userRegistrationRequest.getSurname(),
                                        userRegistrationRequest.getEmail(), phoneNumber,userRegistrationRequest.getBirthDate() ,
                                        userRegistrationRequest.getNationality(),profilePicture,hashedPassword , false);

        profilePictureStorage.store(profilePicture);
        userRegistrationDb.registerNotEnabledUser(notRegisteredUser);

        //TODO: Set a filename length limit. Restrict the allowed characters if possible
        //TODO: Protect the file upload from CSRF attacks

    }

    private String generateProfilePictureName(String originalFileName){
        int index = originalFileName.lastIndexOf(".");
        String fileExtension = originalFileName.substring(index+1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String timestamp = dateFormat.format(new Date());
        return "pfp_".concat(timestamp).concat(".").concat(fileExtension);
    }

    private Image resizeImage(MultipartFile profilePicture) throws IOException {
        BufferedImage bufferedProfilePicture = ImageIO.read(profilePicture.getInputStream());
        return imageResizingService.resizeImage(50, 50,bufferedProfilePicture);
    }

    private void validatePicture(MultipartFile profilePicture){
        if (!profilePictureValidator.isValid(profilePicture)) {
            throw new MediaNotSupportedException();
        }
    }
    private void validatePhoneNumber(String countryCode, String phoneNumber){
        if (!phoneNumberValidator.isValid(countryCode,phoneNumber)) {
            throw new RuntimeException("invalid phone number");
        }
    }


}