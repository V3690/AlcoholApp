package com.leopard4.alcoholrecipe.api;


import com.leopard4.alcoholrecipe.model.CreateRecipeRes.RecipeRes;
import com.leopard4.alcoholrecipe.model.alcohol.AlcoholList;
import com.leopard4.alcoholrecipe.model.ingredient.IngredientList;
import com.leopard4.alcoholrecipe.model.recipeIngreAlcol.RecipeIngreAlcol;

import java.io.Serializable;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CreatingApi extends Serializable {

    // 레시피 포스팅 생성 API
    // title, engTitle, intro, percent, content, img
    @Multipart
    @POST("/creating/recipe")
    Call<RecipeRes> addRecipe(@Header("Authorization") String token,
                              @Part("title") RequestBody title,
                              @Part("engTitle")RequestBody engTitle,
                              @Part("intro")RequestBody intro,
                              @Part("percent")RequestBody percent,
                              @Part("content")RequestBody content,
                              @Part MultipartBody.Part img);


    // 알콜 리스트 가져오는 API
    @GET("/creating/search/alcohol")
    Call<AlcoholList> getAlcoholList(@Header("Authorization") String token,
                                 @Query("keyword") String Keyword,
                                 @Query("offset") int offset,
                                 @Query("limit") int limit);

    // 재료 리스트 가져오는 API
    @GET("/creating/search/ingredient")
    Call<IngredientList> getIngrelList(@Header("Authorization") String token,
                                        @Query("keyword") String Keyword,
                                        @Query("offset") int offset,
                                        @Query("limit") int limit);
    // 본인 레시피 수정 API
    // title, engTitle, intro, percent, content, img
    @Multipart
    @PUT("/creating/recipe/edit/{recipeId}")
    Call<RecipeRes> editRecipe(@Header("Authorization") String token,
                              @Part("title") RequestBody title,
                              @Part("engTitle")RequestBody engTitle,
                              @Part("intro")RequestBody intro,
                              @Part("percent")RequestBody percent,
                              @Part("content")RequestBody content,
                              @Part MultipartBody.Part img,
                              @Path("recipeId") String recipeId);

    // 본인 재료 수정 API
//    {
//            "alcoholId": "11,12,13,14,15,16",
//            "ingredientId": "11,12,13,14,15,16"
//    }
    @PUT("/creating/ingredient/edit/{ingredientId}")
    Call<Void> editAlcoholIngre(@Header("Authorization") String token,
                             @Path("ingredientId") int id,
                             @Body Map<String, String> map);


    // 레시피 술,재료 등록 api
    @POST("creating/ingredient")
    Call<Void> addAlcoholIngre(@Header("Authorization") String token,
                               @Body RecipeIngreAlcol  recipeIngreAlcol);



}
