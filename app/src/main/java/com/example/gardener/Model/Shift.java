package com.example.gardener.Model;

import java.util.Comparator;

public class Shift {

    private String post_id;
    private String user_id;
    private String image;
    private String title;
    private String description;
    private String price;
    private String country;
    private String state_province;
    private String city;
    private String email;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Shift(String post_id, String user_id, String image, String title, String description,
                 String price, String country, String state_province, String city, String email, String date) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.image = image;
        this.title = title;
        this.description = description;
        this.price = price;
        this.country = country;
        this.state_province = state_province;
        this.city = city;
        this.email = email;
        this.date = date;
    }

    public Shift() {

    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState_province() {
        return state_province;
    }

    public void setState_province(String state_province) {
        this.state_province = state_province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static Comparator<Shift> shiftDate = new Comparator<Shift>() {
        @Override
        public int compare(Shift e1, Shift e2) {
            return e1.getDate().compareTo(e2.getDate());
        }
    };

    @Override
    public String toString() {
        return "Post{" +
                "post_id='" + post_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", country='" + country + '\'' +
                ", state_province='" + state_province + '\'' +
                ", city='" + city + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}