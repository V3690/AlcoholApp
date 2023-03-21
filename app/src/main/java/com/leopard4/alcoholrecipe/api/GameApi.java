package com.leopard4.alcoholrecipe.api;

import com.leopard4.alcoholrecipe.model.cheers.CheersMentRes;
import com.leopard4.alcoholrecipe.model.EmotionRes;
import com.leopard4.alcoholrecipe.model.cheers.Ment;

import java.io.Serializable;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface GameApi extends Serializable {
    // 건배사 가져오는 api
    @POST("/game/cheers")
    Call<CheersMentRes> getCheers(@Header("Authorization") String token, @Query("type") int type, @Body Ment ment);

    //얼굴 가저오는 api
    @Multipart
    @POST("/game/emotion")
    Call<EmotionRes> faceRes(@Header("Authorization")String token,
                            @Part MultipartBody.Part photoFile);


}
