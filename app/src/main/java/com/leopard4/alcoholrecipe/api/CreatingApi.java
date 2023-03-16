package com.leopard4.alcoholrecipe.api;

import com.leopard4.alcoholrecipe.model.Res;

import java.io.Serializable;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface CreatingApi extends Serializable {

    // 레시피 포스팅 생성 API
    // title, engTitle, intro, percent, content, img
    @Multipart
    @POST("/creating/recipe")
    Call<Res> addRecipe(@Header("Authorization") String token,
                         @Part("title") RequestBody title,
                         @Part("engTitle")RequestBody engTitle,
                         @Part("intro")RequestBody intro,
                         @Part("percent")RequestBody percent,
                         @Part("content")RequestBody content,
                         @Part MultipartBody.Part img);

}
