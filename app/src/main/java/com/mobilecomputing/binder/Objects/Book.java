package com.mobilecomputing.binder.Objects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mikael on 2017-10-06.
 */

public class Book {
    private  String key;
    private String title, author, genre, imageUrl;
    private String description = "Tom Sawyer is a boy of about 12 years of age, who resides in the fictional town of St. Petersburg, Missouri, in about the year 1845. Tom Sawyer's best friends include Joe Harper and Huckleberry Finn. In The Adventures of Tom Sawyer, Tom's infatuation with classmate Becky Thatcher is apparent as he tries to intrigue her with his strength, boldness, and handsome looks.";

    public Book(String jsonString) {
        JSONObject json;

        try {
            json = new JSONObject(jsonString);

            if(json != null) {
                JSONObject authorJson = (JSONObject) json.getJSONArray("authors").get(0);
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
}
