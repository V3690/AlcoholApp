package com.leopard4.alcoholrecipe.model.myRecipe;

import com.leopard4.alcoholrecipe.model.alcohol.Alcohol;

import java.util.List;

public class MyRecipeRes {

    private String result;

    private List<MyRecipe> items;

    private int count;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<MyRecipe> getItems() {
        return items;
    }

    public void setItems(List<MyRecipe> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
