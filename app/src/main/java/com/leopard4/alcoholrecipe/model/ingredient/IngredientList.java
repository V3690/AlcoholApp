package com.leopard4.alcoholrecipe.model.ingredient;

import com.leopard4.alcoholrecipe.model.alcohol.Alcohol;

import java.util.List;

public class IngredientList {

    private List<Ingredient> result;
    private int count;

    public List<Ingredient> getResult() {
        return result;
    }

    public void setResult(List<Ingredient> result) {
        this.result = result;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
