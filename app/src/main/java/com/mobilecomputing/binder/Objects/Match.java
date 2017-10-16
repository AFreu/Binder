package com.mobilecomputing.binder.Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Lovis on 04/10/17.
 */

public class Match extends User implements Serializable, Comparator<Match> {

    public int id;
    public Integer percent;
    public List<Book> matchingBooks = new ArrayList<>();


    public Match () {
        super();
    }

    public Match (String name, Integer age, String city, int id, String picture, Integer percent){
        super(name, age, city, picture);
        this.id = id;
        this.percent = percent;
    }

    public void setBooks(List<Book> matchBooks, List<Book> userBooks) {
        this.likedBooks = matchBooks;
        matchingBooks.clear();
        userBooks.stream().forEach(b -> {
            if(likedBooks.contains(b)) matchingBooks.add(b);

        });
    }

    public List<Book> getMatchingBooks() {
        return matchingBooks;
    }


    public int compare(Match left, Match right) {
        return right.percent.compareTo(left.percent);
    }
}
