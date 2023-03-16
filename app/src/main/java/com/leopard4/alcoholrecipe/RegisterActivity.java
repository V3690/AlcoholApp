package com.leopard4.alcoholrecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class RegisterActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    EditText editEmail, editPassword, editPassword2, editNickName;
    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editPassword2 = findViewById(R.id.editPassword2);
        editNickName = findViewById(R.id.editNickName);
        btnOk = findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이메일 가져와서 형식 체크
                String email = editEmail.getText().toString().trim();

                Pattern pattern = Patterns.EMAIL_ADDRESS;
                if(!pattern.matcher(email).matches()){
                    Toast.makeText(RegisterActivity.this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 비밀번호 체크
                String password = editPassword.getText().toString().trim();
                String password2 = editPassword2.getText().toString().trim();
                // 우리 기획에는 비번길이가 4~12 만 허용
                if(password.length() < 4 || password.length() > 12){
                    Toast.makeText(RegisterActivity.this, "비번 길이를 확인하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 비밀번호 확인과 같은지 체크
                if (!password.equals(password2)) {
                    Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 닉네임 체크
                String nickname = editNickName.getText().toString().trim();
                if(nickname.length() < 2 || nickname.length() > 12){
                    Toast.makeText(RegisterActivity.this, "닉네임 길이를 확인하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                //todo : 닉네임 중복 체크
                // 서버에 요청해서 중복되는 닉네임이 있는지 확인
                // 중복되는 닉네임이 있으면 Toast로 알려주고
                // 없으면 회원가입 진행

                // 1. 다이얼로그를 화면에 보여준다.
                showProgress("회원가입 중입니다...");

                // 2-1. 레트로핏 변수 생성
                Retrofit retrofit =
                        NetworkClient.getRetrofitClient(RegisterActivity.this);
                UserApi api = retrofit.create(UserApi.class); // 레트로핏으로 서버에 요청할 객체 생성


                User user = new User(email, password, nickname); // User 객체 생성
                Call<UserRes> call = api.register(user); // 서버에 요청

                call.enqueue(new Callback<UserRes>() { // 비동기로 서버에 요청
                    @Override
                    public void onResponse(@NonNull Call<UserRes> call, @NonNull Response<UserRes> response) {
                        dismissProgress(); // 다이얼로그 사라짐

                        if(response.isSuccessful()) {

                            UserRes res = response.body(); // 응답받은 데이터 == UserRes 객체

//                            res.getAccess_token(); // 토큰값
                            SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString(Config.ACCESS_TOKEN, res.getAccess_token());
                            editor.apply(); // 저장

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }else{
                            Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {
                        dismissProgress(); // 다이얼로그 사라짐
                    }
                });




            }
        });

//        // 로그인 화면으로 이동
//        txtLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

    }
    // 로직처리가 시작되면 화면에 보여지는 함수
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
}