package com.leopard4.alcoholrecipe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.api.UserApi;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.User;
import com.leopard4.alcoholrecipe.model.UserRes;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail;
    EditText editPassword;
    Button btnLogin;
    Button btnRegister;
    Button btnKakao;
    ProgressDialog dialog;
    Context context;

    private String kakaoEmail;
    private  String kakanickname;
    private String password;

    private String kakotoken;
    private int accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnOk);
        btnRegister = findViewById(R.id.btnRegister);
        btnKakao = findViewById(R.id.btnKakao);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString().trim();
                Pattern pattern = Patterns.EMAIL_ADDRESS;
                accountType=0;

                if(pattern.matcher(email).matches() == false){
                    Toast.makeText(LoginActivity.this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String password = editPassword.getText().toString().trim();
                // 우리 기획에는 비번길이가 4~12 만 허용
                if(password.length() < 4 || password.length() > 12){
                    Toast.makeText(LoginActivity.this, "비번 길이를 확인하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                showProgress("로그인 중입니다.");

                Retrofit retrofit = NetworkClient.getRetrofitClient(LoginActivity.this);
                UserApi api = retrofit.create(UserApi.class);

                User user = new User(email, password,accountType);

                Call<UserRes> call = api.login(user);
                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        dismissProgress();

                        if(response.isSuccessful()){


                            UserRes res = response.body();

                            SharedPreferences sp =
                                    getApplication().getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString(Config.NICKNAME, res.getNickname() );
                            editor.putString(Config.ACCESS_TOKEN, res.getAccess_token() );
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else if (response.code() == 400) {
                            Toast.makeText(LoginActivity.this, "회원가입이 되어있지 않거나 비번이 틀렸습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Toast.makeText(LoginActivity.this, "정상적으로 처리되지 않았습니다.", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {
                        dismissProgress();
                        Toast.makeText(LoginActivity.this, "정상적으로 처리되지 않았습니다.", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();

            }
        });

        //카카오로그인
        btnKakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)){
                   //어플이 있는경우
                    login();
                }
                else{
                    //어플이 없는경우
                    accountLogin();
                }
            }
        });

    }
  /// 어플없을경우 웹으로 연결
    private void accountLogin() {

        String TAG = "accountLogin()";
        UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, (oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());


                kakotoken = oAuthToken.getAccessToken();
                getUserInfo();

//                SharedPreferences prefs = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putString(Config.NICKNAME, kakanickname);
//                editor.putString(Config.ACCESS_TOKEN, oAuthToken.getAccessToken());
//                editor.apply();
//
//
//                // MainActivity로 화면 전환
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//                Log.d(TAG,"토큰확인후 이동"+kakotoken);
//                finish();
            }
            return null;
        });


    }

    //카카오 정보
    private void getUserInfo() {

        String TAG = "getUserInfo()";
        UserApiClient.getInstance().me((user, meError) -> {
            if (meError != null) {
                Log.e(TAG, "사용자 정보 요청 실패", meError);
            } else {

                kakaoEmail = user.getKakaoAccount().getEmail();
                kakanickname = user.getKakaoAccount().getProfile().getNickname();

                System.out.println("로그인 완료");
                Log.i(TAG, user.toString());
                {
                    Log.i(TAG, "사용자 정보 요청 성공" +
                            "\n회원번호: "+user.getId() +
                            "\n이메일: "+user.getKakaoAccount().getEmail());
                }
                Account user1 = user.getKakaoAccount();
                System.out.println("사용자 계정" + user1);

                // 카카오 로그인이 처음인지 확인
                kakaoregistercheck();

            }
            return null;
        });

    }

    //카카오 로그인 처음인지 확인
    private void kakaoregistercheck() {

        password = null;
        accountType=1;
        Retrofit retrofit = NetworkClient.getRetrofitClient(LoginActivity.this);
        UserApi api = retrofit.create(UserApi.class);


        Log.i(TAG,"이메일잘왔나?"+kakaoEmail);
        User user = new User(kakaoEmail, password,accountType);
        Call<UserRes> call = api.login(user);
        call.enqueue(new Callback<UserRes>() {
            @Override
            public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                if(response.isSuccessful()){

                    UserRes res = response.body();

                    SharedPreferences sp =
                            getApplication().getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(Config.NICKNAME, res.getNickname() );
                    editor.putString(Config.ACCESS_TOKEN, res.getAccess_token() );
                    editor.apply();

                    //만약 이전에 로그인한적 있으면 바로 메인으로 이동
//                 MainActivity로 화면 전환
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                Log.d(TAG,"토큰확인후 이동");
//                return;

                //우리 데이터베이스에 이메일,닉네임이 없다(처음방문이란 뜻)
                }else if (response.code() == 400){
                    //카카오 계정 회원가입
                    kakaoregister();
                }

            }

            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {

            }
        });


    }

    private void login() {
        String TAG = "login()";
        UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this,(oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
                getUserInfo();
            }
            return null;
        });

    }

    void showProgress(String message){
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);
        dialog.show();
    }

    // 로직처리가 끝나면 화면에서 사라지는 함수
    void dismissProgress(){
        dialog.dismiss();
    }
    
    // 첫 카카오로그인시 회원가입
    void kakaoregister(){

        Log.i(TAG,"카카오회갑"+kakaoEmail+kakanickname);


        accountType = 1;
        

        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        UserApi api = retrofit.create(UserApi.class); // 레트로핏으로 서버에 요청할 객체 생성
        

        User user = new User(kakaoEmail,null,kakanickname,accountType ); // User 객체 생성
        Call<UserRes> call = api.register(user); // 서버에 요청

        call.enqueue(new Callback<UserRes>() { // 비동기로 서버에 요청
            @Override
            public void onResponse(@NonNull Call<UserRes> call, @NonNull Response<UserRes> response) {


                if(response.isSuccessful()) {
                    UserRes res = response.body();

                      // 응답받은 데이터 == UserRes 객체

//                            res.getAccess_token(); // 토큰값
                    SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(Config.ACCESS_TOKEN, res.getAccess_token());
                    editor.putString(Config.NICKNAME, kakanickname);
                    editor.apply(); // 저장

                    Intent intent = new Intent(LoginActivity.this, RegisterInfoActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(LoginActivity.this, "아이디 또는 닉네임이 이미 사용 중입니다.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserRes> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
        
        
    }
    
    
}