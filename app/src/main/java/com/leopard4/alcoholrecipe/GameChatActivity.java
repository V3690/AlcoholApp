package com.leopard4.alcoholrecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.leopard4.alcoholrecipe.api.GameApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.AlcoholFriendRes;
import com.leopard4.alcoholrecipe.model.QuestionMent;
import com.leopard4.alcoholrecipe.model.cheers.CheersMentRes;
import com.leopard4.alcoholrecipe.model.cheers.Ment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GameChatActivity extends AppCompatActivity {

    TextView txtAnswer, txtQuestion;
    EditText editUserInput;
    ImageView imgBtn, imgBack;

    String question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_chat);

        txtAnswer = findViewById(R.id.txtAnswer);
        txtQuestion = findViewById(R.id.txtQuestion);
        editUserInput = findViewById(R.id.editUserInput);
        imgBtn = findViewById(R.id.imgBtn);
        imgBack = findViewById(R.id.imgBack);


        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                question = editUserInput.getText().toString().trim();

                if (question.isEmpty()){
                    return;
                }
                txtQuestion.setText(question);

                QuestionMent questionMent = new QuestionMent();

                questionMent.setQuestion(question+",답장은 20글자 이내로 해줘.");

                txtAnswer.setText("생각 중 입니다...");

                Retrofit retrofit = NetworkClient.getRetrofitClient(GameChatActivity.this);
                GameApi api = retrofit.create(GameApi.class);
                SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

                Call<AlcoholFriendRes> call = api.getAnswer(accessToken, questionMent);
                call.enqueue(new Callback<AlcoholFriendRes>() {
                    @Override
                    public void onResponse(Call<AlcoholFriendRes> call, Response<AlcoholFriendRes> response) {
                        if(response.isSuccessful()){

                            txtAnswer.setText(response.body().getAnswer());




                        }else{
                        txtAnswer.setText("무슨 말인지 이해하지 못했어요\n다른 문장을 입력해주세요");

                        }
                    }


                    @Override
                    public void onFailure(Call<AlcoholFriendRes> call, Throwable t) {
                        Log.d("TAG", "onFailure: " + t.getMessage());
                    }
                });



            }
        });




    }





}