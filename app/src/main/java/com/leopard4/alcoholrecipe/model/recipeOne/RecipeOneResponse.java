package com.leopard4.alcoholrecipe.model.recipeOne;

import com.google.gson.annotations.SerializedName;
import com.leopard4.alcoholrecipe.model.recipeOne.RecipeOne;

public class RecipeOneResponse {
    @SerializedName("recipeOne") // JSON 응답의 키 이름입니다. // SerializeName 을 사용하면 JSON 응답의 키 이름과 변수 이름을 다르게 사용할 수 있습니다.
    private RecipeOne recipeOne;

    public RecipeOne getRecipeOne() {
        return recipeOne;
    }
}