package com.mobilecomputing.binder.Objects;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Lovis on 04/10/17.
 */

public class Match implements Serializable, Comparator<Match> {
    public String name;
    public Integer age;
    public String city;
    public int id;
    public String pictureUrl;
    public Integer percent;
    public List<Book> books = new ArrayList<>();

    public Match(){}

    public Match (String name, Integer age, String city, int id, String picture, Integer percent){
        this.name = name;
        this.age = age;
        this.city = city;
        this.id = id;
        this.pictureUrl = picture;
        this.percent = percent;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<Book> getBooks() {
        return books;
    }

    public int compare(Match left, Match right) {
        return right.percent.compareTo(left.percent);
    }
}
