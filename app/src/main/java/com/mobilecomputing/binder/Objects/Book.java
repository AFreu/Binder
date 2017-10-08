package com.mobilecomputing.binder.Objects;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mikael on 2017-10-06.
 */

public class Book {
    private String title, author, genre, imageUrl;

    public Book(String jsonString) {
        JSONObject json;

        Log.d("CardFragment", "got: " + jsonString);

        try {
            json = new JSONObject(jsonString);

            if(json != null) {
                JSONObject authorJson = (JSONObject) json.getJSONArray("authors").get(0);
                author = authorJson.get("name").toString();

                Log.d("CardFragment", author);

                title = json.get("title").toString();

                Log.d("CardFragment", title);

                imageUrl = "http://covers.openlibrary.org/b/ID/" + json.get("cover_id") + "-L.jpg";

                Log.d("CardFragment", imageUrl);

            } else {
                Log.d("CardFragment", "No json found");
            }

        } catch (JSONException e) { e.printStackTrace(); }
    }

    public Book(String title, String author, String genre, String imageUrl) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.imageUrl = imageUrl;
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
}
