package com.mobilecomputing.binder.Utils;

/**
 * Created by mikael on 2017-10-13.
 */

public class Review {
    private User reviewUser;
    private String reviewText;

    public Review(User reviewUser, String reviewText) {
        this.reviewUser = reviewUser;
        this.reviewText = reviewText;
    }

    public User getReviewUser() {
        return reviewUser;
    }

    public String getReviewText() {
        return reviewText;
    }
}
