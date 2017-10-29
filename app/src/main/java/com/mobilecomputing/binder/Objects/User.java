package com.mobilecomputing.binder.Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mikael on 2017-10-09.
 */

public class User implements Serializable {
    protected String givenName, imageUrl = "https://cdn.dribbble.com/users/113499/screenshots/3551730/dribbble.png", city;

    protected Integer age;

    protected List<Book> likedBooks = new ArrayList<>();
    protected List<Book> featuredBooks = new ArrayList<>();

    public User(){

    }

    public User(String givenName, String imageUrl) {
        this.givenName= givenName;
        this.imageUrl = imageUrl;
    }

    public User(String givenName, Integer age, String city, String imageUrl){
        this.givenName= givenName;
        this.imageUrl = imageUrl;
        this.age = age;
        this.city = city;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getCity() {
        return city;
    }

    public Integer getAge() {
        return age;
    }

    public List<Book> getBooks() {
        return likedBooks;
    }

    public List<Book> getFeaturedBooks() {
        return featuredBooks;
    }

    public void setLikedBooks(List<Book> likedBooks){
        this.likedBooks = likedBooks;
    }

    public void setFeaturedBooks(List<Book> featuredBooks) {this.featuredBooks = featuredBooks;}

}
