package com.leopard4.alcoholrecipe.api;


import com.leopard4.alcoholrecipe.model.recipe.RecipeHonorList;
import com.leopard4.alcoholrecipe.model.recipe.RecipeLabList;
import com.leopard4.alcoholrecipe.model.recipe.RecipeMasterList;
import com.leopard4.alcoholrecipe.model.recipeOne.RecipeOneResponse;
import com.leopard4.alcoholrecipe.model.Res;
import com.leopard4.alcoholrecipe.model.myRecipe.MyRecipeRes;

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

    // 마이페이지에서 유저가 작성한 레시피 목록 가져오기
    @GET("/recipe/me/list")
    Call<MyRecipeRes> getMyRecipeList(@Header("Authorization") String token,
                                      @Query("offset") int offset,
                                      @Query("limit") int limit);

    // 마이페이지에서 유저가 작성한 레시피 도수로 필터링해서 가져오기
    @GET("/recipe/me")
    Call<MyRecipeRes> getMyRecipeOrderList(@Header("Authorization") String token,
                                           @Query("order") int order,
                                           @Query("offset") int offset,
                                           @Query("limit") int limit);

    // 검색어로 유저가 작성한 레시피 리스트 가져오기
    @GET("/recipe/me/search")
    Call<MyRecipeRes> getMyRecipeKeyword(@Header("Authorization") String token,
                                         @Query("keyword") String keyword,
                                         @Query("offset") int offset,
                                         @Query("limit") int limit);


    // 주인장 레시피 필터링해서 가저오기
    @GET("/recipe/Masterall")
    Call<RecipeLabList> getRecipeMasterList(@Header("Authorization") String token,
                                            @Query("order") String order,
                                            @Query("offset") int offset,
                                            @Query("limit") int limit,
                                            @Query("percent") int percent);

    // 유저창작 레시피 필터링해서 가저오기
    @GET("/recipe/user")
    Call<RecipeLabList> getRecipeuserList(@Header("Authorization") String token,
                                          @Query("order") String order,
                                          @Query("offset") int offset,
                                          @Query("limit") int limit,
                                          @Query("percent") int percent);


    // 주인장 유저 다가저오기
    @GET("/recipe/all")
    Call<RecipeLabList> getRecipeAllList(@Header("Authorization") String token,
                                         @Query("order") String order,
                                         @Query("offset") int offset,
                                         @Query("limit") int limit,
                                         @Query("percent") int percent);


    // 검색어로 모든 레시피 리스트 가저오기
    @GET("/recipe/search")
    Call<RecipeLabList> getRecipeKeyword(@Header("Authorization") String token,
                                         @Query("keyword") String keyword,
                                         @Query("offset") int offset,
                                         @Query("limit") int limit,
                                         @Query("type") int type);



}
