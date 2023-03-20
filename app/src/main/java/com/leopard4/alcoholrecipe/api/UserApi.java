package com.leopard4.alcoholrecipe.api;

import com.leopard4.alcoholrecipe.model.Res;
import com.leopard4.alcoholrecipe.model.User;
import com.leopard4.alcoholrecipe.model.UserRes;

import java.io.Serializable;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserApi extends Serializable {

    // 회원가입 API 함수 작성
    // 바디 객체 User와 리스폰객체 UserRes를 클래스로 만든다.
    @POST("/user/register")
    Call<UserRes> register(@Body User user); // UserRes에 리턴하라 // Body에는 user 객체가 들어간다
    // 로그인 api
    @POST("/user/login")
    Call<UserRes> login(@Body User user);
    // 로그아웃 api
    @POST("/user/logout")
    Call<Res> logout(@Header("Authorization") String token);
    // 비밀번호 변경 api
    // @Body의 키값은 password이다.
    @PUT("/user/edit/password")
    Call<Res> editPassword(@Header("Authorization") String token, @Body Map<String, String> passwordMap);
    // 닉네임 변경 api
    // @Body의 키값은 nickname이다.
    @PUT("/user/edit/nickname")
    Call<Res> editNickname(@Header("Authorization") String token, @Body Map<String, String> nicknameMap);
    // 회원탈퇴 api
    @DELETE("/user/detail")
    Call<Void> delete(@Header("Authorization") String token);  //  만약 Void를 쓰면 리턴값이 없다는 뜻
    // 유저 추가 정보 api
    @POST("/user/preference")
    Call<Res> registerInfo(@Header("Authorization") String token,
                           @Body UserPreference userPreference);
}