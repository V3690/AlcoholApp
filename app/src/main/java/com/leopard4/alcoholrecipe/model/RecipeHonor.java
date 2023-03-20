package com.leopard4.alcoholrecipe.model;

import java.io.Serializable;

public class RecipeHonor implements Serializable {
    //    "items": [
//    {
//        "id": 10,
//            "imgUrl": "https://i.imgur.com/1ZQ3Z9M.png",
//            "likeCount": 8
//    }
    private int id;
    private String imgUrl;
    private int likeCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
