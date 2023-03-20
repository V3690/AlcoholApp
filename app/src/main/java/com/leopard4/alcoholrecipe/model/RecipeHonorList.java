package com.leopard4.alcoholrecipe.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RecipeHonorList implements Serializable {
    private String result;
    @SerializedName("items")
    private List<RecipeHonor> items;

    private int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<RecipeHonor> getItems() {
        return items;
    }

    public void setItems(List<RecipeHonor> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
