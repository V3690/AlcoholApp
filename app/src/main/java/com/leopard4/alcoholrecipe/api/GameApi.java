package com.leopard4.alcoholrecipe.api;

import com.leopard4.alcoholrecipe.model.CheersMentRes;
import com.leopard4.alcoholrecipe.model.Ment;
import com.leopard4.alcoholrecipe.model.Res;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GameApi extends Serializable {
    // 건배사 가져오는 api
    @POST("/game/cheers")
    Call<CheersMentRes> getCheers(@Header("Authorization") String token, @Query("type") int type, @Body Ment ment);
}
