package com.leopard4.alcoholrecipe.model.alcohol;

public class Alcohol {


    private int id;
    private String name;
    private String imgUrl;
    private String createdAt;
    private String updatedAt;

    private String selectedAlcohol;

    public String getSelectedAlcohol() {
        return selectedAlcohol;
    }

    public void setSelectedAlcohol(String selectedAlcohol) {
        this.selectedAlcohol = selectedAlcohol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
