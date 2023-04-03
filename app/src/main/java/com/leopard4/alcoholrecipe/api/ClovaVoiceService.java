package com.leopard4.alcoholrecipe.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public  interface ClovaVoiceService {

        @POST("tts")
        @Headers({
                "Content-Type: application/x-www-form-urlencoded",
                "X-NCP-APIGW-API-KEY-ID: wsui4v2eg0",
                "X-NCP-APIGW-API-KEY: xZEg70tLIgmJyZi0QJMUegM7IRBTOWAXVjf8PNRn"
        })
        @FormUrlEncoded
        Call<ResponseBody> synthesize(@Field("speaker") String speaker, //목소리(이름선택)
                                      @Field("speed") int speed,    //속도(-로 갈수록 빨라짐)
                                      @Field("volume") int volume, //소리 크기(클수록커짐)
                                      @Field("pitch") int pitch, //음역대? -로갈수록 외계인소리
                                      @Field("emotion-strength") int emotionst, //감정강도
                                      @Field("emotion") int emotion, //감정표현
                                      @Field("alpha") int alpha, //음색 높을수록 하이톤
                                      @Field("end-pitch") int enpitch, //끝음처리 -로갈수록 끝음내리고 +일수록 올림
                                      @Field("text") String text);

    }


