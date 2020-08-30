package com.example.gardener.Model;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile {

    String name;
    String email;
    String user_id;
    String profile_image_id;
    float rating;
    String profile_desription;
    String iban;
    String phone_number;
    String city;
    String country;
    String address;
    String user_type;

    public Profile(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public Profile(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProfile_image_id() {
        return profile_image_id;
    }

    public void setProfile_image_id(String profile_image_id) {
        this.profile_image_id = profile_image_id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getProfile_desription() {
        return profile_desription;
    }

    public void setProfile_desription(String profile_desription) {
        this.profile_desription = profile_desription;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Profile(String name, String email, String user_id, String profile_image_id, float rating, String profile_desription, String iban, String phone_number, String city, String country, String address, String user_type) {
        this.name = name;
        this.email = email;
        this.user_id = user_id;
        this.profile_image_id = profile_image_id;
        this.rating = rating;
        this.profile_desription = profile_desription;
        this.iban = iban;
        this.phone_number = phone_number;
        this.city = city;
        this.country = country;
        this.address = address;
        this.user_type = user_type;
    }


}
