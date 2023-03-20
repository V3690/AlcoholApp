package com.leopard4.alcoholrecipe.model;

import java.io.Serializable;
import java.util.ArrayList;

public class UserPreference implements Serializable {
    // 포스트맨 body 부분
//    {
//        "alcoholType": "1,2,3,4",
//        "withs": "1,2,3,4",
//        "percent": "50"
//    }
    private String alcoholType;
    private String withs;
    private int percent;

    public UserPreference() {}

    public UserPreference(String alcoholType, String withs, int percent) {
        this.alcoholType = alcoholType;
        this.withs = withs;
        this.percent = percent;
    }



}
