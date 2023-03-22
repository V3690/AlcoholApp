package com.leopard4.alcoholrecipe.api;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LikeApi {
    // 레시피 좋아요
    @POST("/recipe/{recipeId}/like")
    Call<Void> postLike(@Header("Authorization") String token, @Path("recipeId") int recipeId);
    // 레시피 좋아요 취소
    @DELETE("/recipe/{recipeId}/like")
    Call<Void> deleteLike(@Header("Authorization") String token, @Path("recipeId") int recipeId);
    // todo 술 좋아요, 취소는 미구현상태
//    @POST("/alcohol/{alcoholId}/like")
//    Call<Void> postAlcoholLike(@Header("Authorization") String token, @Path("alcoholId") int alcoholId);
//    // 술 좋아요 취소
//    @DELETE("/alcohol/{alcoholId}/like")
//    Call<Void> deleteAlcoholLike(@Header("Authorization") String token, @Path("alcoholId") int alcoholId);

}
