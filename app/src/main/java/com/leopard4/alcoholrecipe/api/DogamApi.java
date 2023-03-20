package com.leopard4.alcoholrecipe.api;

import com.leopard4.alcoholrecipe.model.dogam.DogamInfoResponse;
import com.leopard4.alcoholrecipe.model.dogam.DogamList;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DogamApi extends Serializable {


    // 도감 리스트 가져오는 API
    @GET("/alcohol")
    Call<DogamList> getDogamlList(@Header("Authorization") String token,
                                  @Query("order") String Keyword,
                                  @Query("offset") int offset,
                                  @Query("limit") int limit);

    // 도감 하나 가져오는 API
    @GET("/alcohol/{alcoholId}")
    Call<DogamInfoResponse> getDogamInfo(@Header("Authorization") String token,
                                         @Path("alcoholId") int alcoholId);



}
