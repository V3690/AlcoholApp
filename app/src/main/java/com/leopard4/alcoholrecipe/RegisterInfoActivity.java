package com.leopard4.alcoholrecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.api.UserApi;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.Res;
import com.leopard4.alcoholrecipe.model.User;
import com.leopard4.alcoholrecipe.model.UserPreference;
import com.leopard4.alcoholrecipe.model.UserRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterInfoActivity extends AppCompatActivity {

    private ProgressDialog dialog;

    TextView txtPercent;
    SeekBar seekBar;
    CheckBox checkBox1_1, checkBox1_2, checkBox1_3, checkBox1_4, checkBox1_5, checkBox1_6;
    CheckBox checkBox3_1, checkBox3_2, checkBox3_3, checkBox3_4, checkBox3_5, checkBox3_6;

    Button btnPass, btnOk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);

        // 체크박스 : 1.맥주 2.소주 3.막걸리 4.와인 5.양주 6.기타
        checkBox1_1 = findViewById(R.id.checkBox1_1);
        checkBox1_2 = findViewById(R.id.checkBox1_2);
        checkBox1_3 = findViewById(R.id.checkBox1_3);
        checkBox1_4 = findViewById(R.id.checkBox1_4);
        checkBox1_5 = findViewById(R.id.checkBox1_5);
        checkBox1_6 = findViewById(R.id.checkBox1_6);

        // 체크박스 : 1.가족 2.친구 3.혼자 4.직장 5.지인 6.기타
        checkBox3_1 = findViewById(R.id.checkBox3_1);
        checkBox3_2 = findViewById(R.id.checkBox3_2);
        checkBox3_3 = findViewById(R.id.checkBox3_3);
        checkBox3_4 = findViewById(R.id.checkBox3_4);
        checkBox3_5 = findViewById(R.id.checkBox3_5);
        checkBox3_6 = findViewById(R.id.checkBox3_6);

        // 시크바 설정
        txtPercent = findViewById(R.id.txtPercent);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtPercent.setText(String.format("%d", seekBar.getProgress())+" %");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnPass = findViewById(R.id.btnPass);
        btnOk = findViewById(R.id.btnOk);



        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> alcoholTypeList = new ArrayList<>();
                if(checkBox1_1.isChecked()){
                    alcoholTypeList.add("1");
                }
                if(checkBox1_2.isChecked()){
                    alcoholTypeList.add("2");
                }
                if(checkBox1_3.isChecked()){
                    alcoholTypeList.add("3");
                }
                if(checkBox1_4.isChecked()){
                    alcoholTypeList.add("4");
                }
                if(checkBox1_5.isChecked()){
                    alcoholTypeList.add("5");
                }
                if(checkBox1_6.isChecked()){
                    alcoholTypeList.add("6");
                }
                String alcoholType = String.join(",",alcoholTypeList);
//                Log.i("alcoholType", alcoholType);

                ArrayList<String> withsList = new ArrayList<>();
                if(checkBox3_1.isChecked()){
                    withsList.add("1");
                }
                if(checkBox3_2.isChecked()){
                    withsList.add("2");
                }
                if(checkBox3_3.isChecked()){
                    withsList.add("3");
                }
                if(checkBox3_4.isChecked()){
                    withsList.add("4");
                }
                if(checkBox3_5.isChecked()){
                    withsList.add("5");
                }
                if(checkBox3_6.isChecked()){
                    withsList.add("6");
                }
                String withs = String.join(",",withsList);
//                Log.i("withs", withs);

                int percent = seekBar.getProgress();


                // 1. 다이얼로그를 화면에 보여준다.
                showProgress("정보 등록 중...");

                // 2-1. 레트로핏 변수 생성
                Retrofit retrofit = NetworkClient.getRetrofitClient(RegisterInfoActivity.this);
                UserApi api = retrofit.create(UserApi.class); // 레트로핏으로 서버에 요청할 객체 생성

                // 2-2. 토큰 가져오기
                SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

                // 3. 저장
                UserPreference userPreference = new UserPreference(alcoholType,withs,percent); // UserPreference 객체 생성
                Call<Res> call = api.registerInfo(accessToken, userPreference); // 서버에 요청
                call.enqueue(new Callback<Res>() {
                    @Override
                    public void onResponse(@NonNull Call<Res> call, @NonNull Response<Res> response) {
                        dismissProgress(); // 다이얼로그 사라짐

                        if(response.isSuccessful()) {

                            Intent intent = new Intent(RegisterInfoActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(RegisterInfoActivity.this, "내용을 모두 입력해 주세요.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Res> call, Throwable t) {
                        dismissProgress(); // 다이얼로그 사라짐
                    }
                });

            }
        });

        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterInfoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
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