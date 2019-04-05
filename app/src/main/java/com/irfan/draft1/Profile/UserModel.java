package com.irfan.draft1.Profile;

/**
 * Created by irfan on 27/01/2018.
 */

public class UserModel {
    private String UID;
    private String name;
    private String image;
    private String email;

    public UserModel(String UID, String name, String image, String email) {
        this.UID = UID;
        this.name = name;
        this.image = image;
        this.email = email;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
