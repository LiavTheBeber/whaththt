package com.example.whaththt.side_classes;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class User implements Serializable {

    private String username;
    private String email;
    private String phone;
    private Bitmap image;


    public User(String username, String email, String phone, Bitmap image) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.image = image;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}

