package com.leopard4.alcoholrecipe.api;

import com.leopard4.alcoholrecipe.model.recipe.RecipeFavoriteList;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface RecipeFavoriteApi extends Serializable {
//    {{aws}}/recipe/favorite?order=1&type=1&strength=0&offset=0&limit=5
    @GET("/recipe/favorite")
    Call<RecipeFavoriteList> getRecipeFavorite(@Header("Authorization") String token,
                                               @Query("order") int order,
                                               @Query("type") int type,
                                               @Query("strength") int strength,
                                               @Query("offset") int offset,
                                               @Query ("limit") int limit);


    @GET("/recipe/favorite/search")
    Call<RecipeFavoriteList> getRecipeSearchFavorite(@Header("Authorization") String token,
                                               @Query("keyword") String keyword,
                                               @Query("offset") int offset,
                                               @Query ("limit") int limit);

}
