package com.leopard4.alcoholrecipe.api;

import com.leopard4.alcoholrecipe.model.Res;
import com.leopard4.alcoholrecipe.model.dogam.DogamInfoResponse;
import com.leopard4.alcoholrecipe.model.dogam.DogamList;

import java.io.Serializable;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DogamApi extends Serializable {


    // 도감 리스트 가져오는 API
    @GET("/alcohol")
    Call<DogamList> getDogamlList(@Header("Authorization") String token,
                                  @Query("percent") int percent,
                                  @Query("order") String order,
                                  @Query("offset") int offset,
                                  @Query("limit") int limit);

    // 도감 하나 가져오는 API
    @GET("/alcohol/{alcoholId}")
    Call<DogamInfoResponse> getDogamInfo(@Header("Authorization") String token,
                                         @Path("alcoholId") int alcoholId);

    // 도감에 없는 알콜 주인장에게 요청하는 API
    @Multipart
    @POST("/alcohol/request")
    Call<Res> requestDogam(@Header("Authorization") String token,
                           @Part MultipartBody.Part photo,
                           @Part("requestType") RequestBody requestType,
                           @Part("name")RequestBody name,
                           @Part("percent")RequestBody percent,
                           @Part("content")RequestBody content);

    // 키워드로 도감 데이터 검색하는 API
    @GET("/alcohol/search")
    Call<DogamList> getKeywordDogamList(@Header("Authorization") String token,
                                        @Query("keyword") String Keyword,
                                        @Query("offset") int offset,
                                        @Query("limit") int limit);


}
