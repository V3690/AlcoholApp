package com.leopard4.alcoholrecipe.model;

public class RecipeFavorite {
//    {
//        "id": 2,
//        "title": "블루레몬에이드밀키스주",
//        "percent": 1
//    }
    private int id;
    private String title;
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

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
