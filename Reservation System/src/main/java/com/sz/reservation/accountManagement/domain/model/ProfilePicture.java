package com.sz.reservation.accountManagement.domain.model;

public class ProfilePicture {
    private String pictureId;
    private String accountId;
    private String imagePath;

    public ProfilePicture(String pictureId, String accountId, String imagePath) {
        this.pictureId = pictureId;
        this.accountId = accountId;
        this.imagePath = imagePath;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getPictureId() {
        return pictureId;
    }

    public String getImagePath() {
        return imagePath;
    }
}
