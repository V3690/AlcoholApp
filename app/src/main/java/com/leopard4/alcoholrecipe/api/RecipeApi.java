package com.leopard4.alcoholrecipe.api;

import com.leopard4.alcoholrecipe.model.recipe.RecipeHonorList;
import com.leopard4.alcoholrecipe.model.recipe.RecipeMasterList;
import com.leopard4.alcoholrecipe.model.recipeOne.RecipeOneResponse;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecipeApi extends Serializable {
    // 레시피1개 가져오는 api
    @GET("/recipe/{recipeId}")
    Call<RecipeOneResponse> getRecipeOne(@Header("Authorization") String token, @Path("recipeId") int recipeId);

    // 메인페이지 주인장 레시피 가져오기
    @GET("/recipe/master")
    Call<RecipeMasterList> getRecipeMaster(@Header("Authorization") String token, @Query("offset") int offset, @Query("limit") int limit);
    // 메인페이지 명예 레시피 가져오기
    @GET("/recipe/honor")
    Call<RecipeHonorList> getRecipeHonor(@Header("Authorization") String token, @Query("offset") int offset, @Query("limit") int limit);
}
