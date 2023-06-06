package com.example.whaththt.side_classes;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;


public class UserCompany implements Serializable{

    private String username;
    private String email;
    private String password;
    private String phone;
    private GeoPoint address;
    private Bitmap image;
    private float ratingBar;

    public UserCompany(String username, String email, String password, String phone, GeoPoint address, Bitmap image, float ratingBar) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.image = image;
        this.ratingBar = ratingBar;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public GeoPoint getAddress() {return address;}

    public void setAddress(GeoPoint address) {this.address = address;}
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
    public float getRatingBar() { return  ratingBar;}
    public void setRatingBar(float ratingBar){ this.ratingBar = ratingBar;}
}
