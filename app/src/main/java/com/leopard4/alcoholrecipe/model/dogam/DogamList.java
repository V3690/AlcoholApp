package com.leopard4.alcoholrecipe.model.dogam;

import com.leopard4.alcoholrecipe.model.ingredient.Ingredient;

import java.util.List;

public class DogamList {

    private String result;
    private List<Dogam> items;
    private int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Dogam> getItems() {
        return items;
    }

    public void setItems(List<Dogam> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
