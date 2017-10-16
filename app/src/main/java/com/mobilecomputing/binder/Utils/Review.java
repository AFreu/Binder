package com.mobilecomputing.binder.Utils;

import com.mobilecomputing.binder.Objects.User;

import java.io.Serializable;

/**
 * Created by mikael on 2017-10-13.
 */

public class Review implements Serializable {
    private User reviewUser;
    private String reviewText;

    public Review(User reviewUser, String reviewText) {
        this.reviewUser = reviewUser;
        this.reviewText = reviewText;
    }

    public User getReviewUser() {
        return this.reviewUser;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
