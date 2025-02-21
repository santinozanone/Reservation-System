package com.sz.reservation.registration.infrastructure.dto;

import com.sz.reservation.registration.infrastructure.annotation.NotNullNotWhitespace;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class AccountCreationRequest {


    @Size(min = 5, message = "username must be minimum 5 character long ")
    @Size(max = 55, message = "Username must be maximum 55 characters long")
    @NotNullNotWhitespace(message = "username cannot contain whitespaces or be null")
    private String username;

    @NotBlank(message = "name cannot be blank")
    @Size(max = 55,message = "name must be maximum 55 characters long")
    @Size(min = 1,message = "name must be minimum 1 character long ")
    private String name;

    @NotBlank(message = "surname cannot be blank")
    @Size(max = 55,message = "surname must be maximum 55 characters long")
    @Size(min = 1,message = "surname must be minimum 1 character long ")
    private String surname;

    @NotNull
    @Pattern(regexp = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@"
            + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$",message = "invalid email")
    private String email;

    @NotBlank(message = "countryCode cannot be blank")
    @Size(max = 3,message = "country code must be maximum 3 characters")
    private String countryCode;// An example would be: "+54"


    @NotBlank(message = "phoneNumber cannot be blank")
    @Size(max = 12,message = "phone number must be maximum 12 characters")
    @Size(min = 4,message = "phone number must be minimum 4 character long ")
    private String phoneNumber; // includes area code

    @NotNull
    @Past(message = "birthDate must be in the past ")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    @NotBlank(message = "nationality cannot be blank")
    @Size(min = 4,message = "nationality must be minimum 4 characters")
    @Size(max = 55,message = "nationality must be maximum 55 characters long")
    private String nationality; // should be enum

    @NotNull(message = "profile picture cannot be null")
    private MultipartFile profilePicture;

    @NotBlank
    @Size(min = 8,message = "password must be minimum 8 characters long")
    @Size(max = 255,message = "password must be maximum 255 characters long")
    private String password;

    public AccountCreationRequest(String username, String name, String surname, String email, String countryCode , String phoneNumber, LocalDate birthDate, String nationality, MultipartFile profilePicture, String password) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.countryCode = countryCode;
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

    public String getEmail() {
        return email;
    }

    public String getCountryCode() {
        return countryCode;
    }



    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getBirthDate() {
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
