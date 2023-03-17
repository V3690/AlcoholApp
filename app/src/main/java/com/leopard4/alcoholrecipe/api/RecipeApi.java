package com.leopard4.alcoholrecipe.api;

import com.leopard4.alcoholrecipe.model.RecipeOne;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface RecipeApi extends Serializable {
    // 레시피1개 가져오는 api
    @GET("/recipe/{recipeId}")
    Call<RecipeOne> getRecipeOne(@Header("Authorization") String token, @Path("recipeId") int recipeId);
}
