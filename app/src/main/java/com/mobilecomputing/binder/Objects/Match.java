package com.mobilecomputing.binder.Objects;

import java.io.Serializable;
import java.net.URL;

/**
 * Created by Lovis on 04/10/17.
 */

public class Match implements Serializable {
    public String name;
    public Integer age;
    public String city;
    public String id;
    public URL picture;
    public Integer percent;

    public Match (String name, Integer age, String city, String id, URL picture, Integer percent){
        this.name = name;
        this.age = age;
        this.city = city;
        this.id = id;
        this.picture = picture;
        this.percent = percent;
    }


}
