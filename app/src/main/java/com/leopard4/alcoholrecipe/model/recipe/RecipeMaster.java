package com.leopard4.alcoholrecipe.model.recipe;

import java.io.Serializable;

public class RecipeMaster implements Serializable {
//    "items": [
//    {
//        "id": 13,
//            "title": "메로나주",
//            "imgUrl": "https://i.imgur.com/1ZQ3Z9M.png"
//    }
    private int id;
    private String title;
    private String imgUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
