package com.leopard4.alcoholrecipe.model.recipeIngreAlcol;

import java.util.ArrayList;

public class RecipeIngreAlcol {
//    {
//        "recipeId": 300,
//            "alcoholId": "1,2,3,4,5,6",
//            "ingredientId": "1,2,3,4,5,6"
//    }
    private int recipeId;
    private String alcoholId;
    private String ingredientId;

    public RecipeIngreAlcol(int recipeId, String alcoholId, String ingredientId) {
        this.recipeId = recipeId;
        this.alcoholId = alcoholId;
        this.ingredientId = ingredientId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getAlcoholId() {
        return alcoholId;
    }

    public void setAlcoholId(String alcoholId) {
        this.alcoholId = alcoholId;
    }

    public String getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(String ingredientId) {
        this.ingredientId = ingredientId;
    }
}
