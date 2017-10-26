package com.mobilecomputing.binder.Objects;

import com.mobilecomputing.binder.Utils.Review;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by mikael on 2017-10-06.
 */

public class Book implements Serializable {
    private List<Book> bookList = new ArrayList<>();
    private String key = "";
    private String title = "", author = "", genre = "", imageUrl = "";
    private String description = "Tom Sawyer is a boy of about 12 years of age, who resides in the fictional town of St. Petersburg, Missouri, in about the year 1845. Tom Sawyer's best friends include Joe Harper and Huckleberry Finn. In The Adventures of Tom Sawyer, Tom's infatuation with classmate Becky Thatcher is apparent as he tries to intrigue her with his strength, boldness, and handsome looks.";

    private List<Review> reviews = new ArrayList<>();

    public Book() {}

    public Book(String jsonString) {
        JSONObject json;

        try {
            json = new JSONObject(jsonString);

            if(json != null) {
                JSONObject authorJson = (JSONObject) json.getJSONArray("authors").get(0);
                key = json.getString("key");
                author = authorJson.get("name").toString();
                title = json.get("title").toString();
                imageUrl = "http://covers.openlibrary.org/b/ID/" + json.get("cover_id") + "-L.jpg";

            }

        } catch (JSONException e) { e.printStackTrace(); }
    }

    public Book(String title, String author, String genre, String imageUrl) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.imageUrl = imageUrl;
    }

    public Book(String title, String author, String genre, String imageUrl, String key) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.imageUrl = imageUrl;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    /* To edit an already existing review */
    public void setReviewTextForUser(String text, User user){
        boolean userExisted = false;

        for(Review r : reviews){
            if(r.getReviewUser().equals(user)){
                userExisted = true;
                r.setReviewText(text);
            }
        }

        if(!userExisted) reviews.add(new Review(user, text));
    }

    @Override
    public boolean equals(Object otherBook) {
        return title.equals(((Book)otherBook).getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;

    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<Review> getReviewsByMe(User me){
        List<Review> temp = new ArrayList<>();
        for(Review r : reviews){
            if(r.getReviewUser().equals(me)) temp.add(r);
        }
        return temp;
    }

    public List<Review> getReviewsByOthers(User me){
        List<Review> temp = new ArrayList<>();
        for(Review r : reviews){
            if(!r.getReviewUser().equals(me)) temp.add(r);
        }
        return temp;
    }

    public void addReview(Review review){
        if(!review.getReviewText().isEmpty())
            reviews.add(review);
    }
}
