package com.mobilecomputing.binder.Objects;

import java.net.URL;

/**
 * Created by Lovis on 04/10/17.
 */

public class Match {
    public String name;
    public Integer age;
    public String city;
    public String presentation;
    public URL picture;
    public Integer percent;

    public Match (String name, Integer age, String city, String presentation, URL picture, Integer percent){
        this.name = name;
        this.age = age;
        this.city = city;
        this.presentation = presentation;
        this.picture = picture;
        this.percent = percent;
    }


}
