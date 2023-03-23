package com.leopard4.alcoholrecipe.model.recipe;

import java.io.Serializable;

public class RecipeLab  implements Serializable {

//     "id": 17,
////            "title": "홍초주",
////            "percent": 1,
////            "cnt": 2

    private int id;
    private String title;
    private int cnt;
    private int percent;

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

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
