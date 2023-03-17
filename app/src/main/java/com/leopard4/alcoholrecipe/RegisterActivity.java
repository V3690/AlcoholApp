package com.leopard4.alcoholrecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import org.json.JSONObject;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    EditText editEmail, editNickName, editPassword, editPassword2;
    TextView txtViewCheckEmail, txtViewCheckName, txtViewCheckPassword1, txtViewCheckPassword2;
    Button btnOk;

    String email;
    String nickname;
    String password;
    String password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editEmail = findViewById(R.id.editEmail);
        editNickName = findViewById(R.id.editNickName);
        editPassword = findViewById(R.id.editPassword);
        editPassword2 = findViewById(R.id.editPassword2);
        btnOk = findViewById(R.id.btnOk);

        txtViewCheckEmail = findViewById(R.id.txtViewCheckId);
        txtViewCheckName = findViewById(R.id.txtViewCheckName);
        txtViewCheckPassword1 = findViewById(R.id.txtViewCheckPassword1);
        txtViewCheckPassword2 = findViewById(R.id.txtViewCheckPassword2);


        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 이메일 가져와서 형식 체크
                email = editEmail.getText().toString().trim();
                Pattern pattern = Patterns.EMAIL_ADDRESS;
                if(!pattern.matcher(email).matches()){
                    txtViewCheckEmail.setText("이메일 형식으로 입력해 주세요.");
                    txtViewCheckEmail.setVisibility(View.VISIBLE);
                    return;
                }else{
                    txtViewCheckEmail.setVisibility(View.INVISIBLE);
                }
            }
        });

        editNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 닉네임 체크
                nickname = editNickName.getText().toString().trim();
                if(nickname.length() < 2 || nickname.length() > 12){
                    txtViewCheckName.setText("2~12자 사이로 입력해 주세요.");
                    txtViewCheckName.setVisibility(View.VISIBLE);
                    return;
                }else{
                    txtViewCheckName.setVisibility(View.INVISIBLE);
                }
            }
        });

        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 비밀번호 체크
                password = editPassword.getText().toString().trim();
                // 우리 기획에는 비번길이가 4~12 만 허용
                if(password.length() < 4 || password.length() > 12){
                    txtViewCheckPassword1.setVisibility(View.VISIBLE);
                    return;
                }else{
                    txtViewCheckPassword1.setVisibility(View.INVISIBLE);
                }
            }
        });

        editPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 비밀번호 확인과 같은지 체크
                password2 = editPassword2.getText().toString().trim();
                if (!password2.equals(password)) {
                    txtViewCheckPassword2.setVisibility(View.VISIBLE);
                    return;
                }else{
                    txtViewCheckPassword2.setVisibility(View.INVISIBLE);
                }

            }
        });


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                            Intent intent = new Intent(RegisterActivity.this, RegisterInfoActivity.class);
                            startActivity(intent);
                            finish();

                        }else{
                            Toast.makeText(RegisterActivity.this, "아이디 또는 닉네임이 이미 사용 중입니다.", Toast.LENGTH_LONG).show();
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