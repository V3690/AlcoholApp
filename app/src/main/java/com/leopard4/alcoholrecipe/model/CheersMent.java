package com.leopard4.alcoholrecipe.model;

import java.io.Serializable;

public class CheersMent implements Serializable {
//     "item": {
//                "title": "지화자",
//                "first": "지금부터 화끈한 자리를",
//                "last": "위하여!"
//              }
    // CheersMentRes로 부터 받은 item이라는 키값으로 오는 response가 CheersMent 클래스로 매핑됩니다.
    private String title;
    private String first;
    private String last;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }
}
