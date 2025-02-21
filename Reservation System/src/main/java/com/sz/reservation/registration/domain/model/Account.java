package com.sz.reservation.registration.domain.model;


import com.sz.reservation.registration.domain.exception.AccountNotEnabledException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Account {
    private String id;
    private String uniqueUsername;
    private String name;
    private String surname;

    private String uniqueEmail;
    private PhoneNumber phoneNumber;

    private ProfilePicture profilePicture;
    private String password;
    private boolean enabled;

    public Account(String id,String uniqueUsername, String name, String surname, String uniqueEmail, PhoneNumber phoneNumber,  ProfilePicture profilePicture, String password,boolean enabled) {
        this.id = validateId(id);
        this.uniqueUsername = validateUsername(uniqueUsername);
        this.name = validateName(name);
        this.surname = validateSurname(surname);
        this.uniqueEmail = validateEmail(uniqueEmail);
        this.phoneNumber = validatePhoneNumber(phoneNumber);
        this.profilePicture = validateProfilePicture(profilePicture);
        this.password = validatePassword(password);
        this.enabled = enabled;
    }

    public void changeUsername(String uniqueUsername){
        if (!enabled) throw new AccountNotEnabledException(uniqueEmail);
        this.uniqueUsername = validateUsername(uniqueUsername);
    }

    public void changeName(String name){
        if (!enabled) throw new AccountNotEnabledException(uniqueEmail);
        this.name = validateName(name);
    }

    public void changeSurname(String surname){
        if (!enabled) throw new AccountNotEnabledException(uniqueEmail);
        this.surname = validateSurname(surname);
    }

    public void changePhoneNumber(PhoneNumber phoneNumber){
        if (!enabled) throw new AccountNotEnabledException(uniqueEmail);
        this.phoneNumber = validatePhoneNumber(phoneNumber);
    }

    public void changeProfilePicture(ProfilePicture profilePicture){
        if (!enabled) throw new AccountNotEnabledException(uniqueEmail);
        this.profilePicture = validateProfilePicture(profilePicture);
    }

    public void changePassword(String hashedPassword){
        if (!enabled) throw new AccountNotEnabledException(uniqueEmail);
        this.password = validatePassword(hashedPassword);
    }

    public void enableAccount(){
        this.enabled = true;
    }

    public void disableAccount(){
        this.enabled = false;
    }



    private String validateId(String id){
        if (id == null || id.isEmpty() || id.contains(" ")){
            throw new IllegalArgumentException("invalid id format");
        }
        return id;
    }

    private String validateUsername(String uniqueUsername){
        if (uniqueUsername == null || uniqueUsername.contains(" ") ||
            uniqueUsername.length() > 55 || uniqueUsername.length() < 5){
            throw new IllegalArgumentException("invalid username format");
        }
        return uniqueUsername;
    }
    private String validateName(String name){
        if (name == null || name.length() > 55 || name.length() < 1){
            throw new IllegalArgumentException("invalid name format");
        }

        return name;
    }
    private String validateSurname(String surname){
        if (surname == null || surname.length() > 55 || surname.length() < 1){
            throw new IllegalArgumentException("invalid surname format");
        }
        return surname;
    }
    private String validateEmail(String uniqueEmail){
       Pattern pattern = Pattern.compile("^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@"
                + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$");
        Matcher matcher = pattern.matcher(uniqueEmail);
        if ( !matcher.find() ){
            throw new IllegalArgumentException("invalid email format");
        }
        return uniqueEmail;
    }

    private PhoneNumber validatePhoneNumber(PhoneNumber phoneNumber){
        if (phoneNumber == null ){
            throw new IllegalArgumentException("invalid phone number, cannot be null");
        }
        return phoneNumber;
    }

    private ProfilePicture validateProfilePicture(ProfilePicture profilePicture){
        if (profilePicture == null){
            throw new IllegalArgumentException("invalid profile picture,cannot be null");
        }
        return profilePicture;
    }
    private String validatePassword(String password){
       if(password == null || password.isEmpty() || password.contains(" ")){
           throw new IllegalArgumentException("invalid password format");
       }
       return password;
    }


    public String getId() {
        return id;
    }

    public String getUniqueUsername() {
        return uniqueUsername;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUniqueEmail() {
        return uniqueEmail;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public ProfilePicture getProfilePicture() {
        return profilePicture;
    }

    public String getPassword() {
        return password;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
