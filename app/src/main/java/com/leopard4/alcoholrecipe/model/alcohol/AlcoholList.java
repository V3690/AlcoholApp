package com.leopard4.alcoholrecipe.model.alcohol;

import java.util.List;

public class AlcoholList {

    private String result;
    private List<Alcohol> items;
    private int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Alcohol> getItems() {
        return items;
    }

    public void setItems(List<Alcohol> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
