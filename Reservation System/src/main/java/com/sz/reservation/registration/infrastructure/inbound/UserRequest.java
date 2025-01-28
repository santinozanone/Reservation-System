package com.sz.reservation.registration.infrastructure.inbound;

import com.sz.reservation.registration.domain.model.PhoneNumber;
import com.sz.reservation.registration.domain.model.ProfilePicture;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class UserRequest {
    @NotBlank(message = "username cannot be blank")
    @Max(value = 55,message = "Username must be maximum 55 characters long")
    @Min(value = 5,message = "username must be minimum 5 character long ")
    private String username;

    @NotBlank(message = "name cannot be blank")
    @Max(value = 55,message = "name must be maximum 55 characters long")
    @Min(value = 1,message = "name must be minimum 1 character long ")
    private String name;

    @NotBlank(message = "surname cannot be blank")
    @Max(value = 55,message = "surname must be maximum 55 characters long")
    @Min(value = 1,message = "surname must be minimum 1 character long ")
    private String surname;

    //should propagate validation
    private PhoneNumber phoneNumber;

    @NotNull
    @Past(message = "birthDate must be in the past ")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date birthDate;

    @NotBlank(message = "nationality cannot be blank")
    @Max(value = 55,message = "nationality must be maximum 55 characters long")
    private String nationality; // should be enum

    @NotNull(message = "profile picture cannot be null")
    private MultipartFile profilePicture;

    @NotBlank
    @Min(value = 8,message = "password must be minimum 8 characters long")
    @Max(value = 255,message = "password must be maximum 255 characters long")
    private String password;


    public UserRequest(String username, String name, String surname, PhoneNumber phoneNumber, Date birthDate, String nationality, MultipartFile profilePicture, String password) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.profilePicture = profilePicture;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getNationality() {
        return nationality;
    }

    public MultipartFile getProfilePicture() {
        return profilePicture;
    }

    public String getPassword() {
        return password;
    }
}
