package com.leopard4.alcoholrecipe.api;


import com.leopard4.alcoholrecipe.model.AlcoholFriendRes;
import com.leopard4.alcoholrecipe.model.QuestionMent;
import com.leopard4.alcoholrecipe.model.alcohol.Alcohol;
import com.leopard4.alcoholrecipe.model.cheers.CheersMentRes;
import com.leopard4.alcoholrecipe.model.DiceRes;
import com.leopard4.alcoholrecipe.model.EmotionRes;
import com.leopard4.alcoholrecipe.model.cheers.Ment;


import java.io.Serializable;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
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

    @GET("/game/dice/{subjectTypeId}/{penaltyTypeId}")
    Call<DiceRes> diceRes(@Header("Authorization") String token,
                          @Path("subjectTypeId") int subjectTypeId , @Path("penaltyTypeId") int penaltyTypeId) ;

    // 비밀 술 친구
    @POST("/game/chatbot")
    Call<AlcoholFriendRes> getAnswer (@Header("Authorization") String token,
                                      @Body QuestionMent questionMent);
}
