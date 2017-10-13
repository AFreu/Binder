package com.mobilecomputing.binder.Utils;

import java.io.Serializable;

/**
 * Created by mikael on 2017-10-09.
 */

public class User implements Serializable {
    private String givenName, imageUrl;

    public User(String givenName, String imageUrl) {
        this.givenName= givenName;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }
}
