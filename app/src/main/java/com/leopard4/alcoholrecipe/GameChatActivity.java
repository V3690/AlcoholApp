package com.leopard4.alcoholrecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.leopard4.alcoholrecipe.api.ClovaNetworkClient;
import com.leopard4.alcoholrecipe.api.ClovaVoiceService;
import com.leopard4.alcoholrecipe.api.GameApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.AlcoholFriendRes;
import com.leopard4.alcoholrecipe.model.QuestionMent;
import com.leopard4.alcoholrecipe.model.cheers.CheersMentRes;
import com.leopard4.alcoholrecipe.model.cheers.Ment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GameChatActivity extends AppCompatActivity {

    TextView txtAnswer, txtQuestion;
    EditText editUserInput;
    ImageView imgBtn, imgBack , btnSpeaker;

    RadioButton btnMan , btnWoman;
    Switch switchVoice;

    String question;
    int isswithcheck =0;
    private String text;
    private String speaker = "nyuna";
    private int speed= 0;
    private int volume= 5;
    private int pitch= -1;
    private int emotionst= 2;
    private int emotion= 1;
    private int alpha= 0;
    private int end_pitch= 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_chat);

        txtAnswer = findViewById(R.id.txtAnswer);
        txtQuestion = findViewById(R.id.txtQuestion);
        editUserInput = findViewById(R.id.editUserInput);
        imgBtn = findViewById(R.id.imgBtn);
        imgBack = findViewById(R.id.imgBack);

        btnMan=findViewById(R.id.btnMan);
        btnWoman=findViewById(R.id.btnWoman);
        switchVoice=findViewById(R.id.switchVoice);
        btnSpeaker=findViewById(R.id.btnSpeaker);


        switchVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    isswithcheck=1;

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
                                        text = txtAnswer.getText().toString();
                                        clova();


                                    }else{
                                        txtAnswer.setText("무슨 말인지 이해하지 못했어요\n다른 문장을 입력해주세요");
                                        txtAnswer.setText(response.body().getAnswer());
                                        text = txtAnswer.getText().toString();
                                        clova();
                                    }
                                }


                                @Override
                                public void onFailure(Call<AlcoholFriendRes> call, Throwable t) {
                                    Log.d("TAG", "onFailure: " + t.getMessage());
                                }
                            });



                        }
                    });



                }else{
                    isswithcheck=0;


                }
            }
        });

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

        btnSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = txtAnswer.getText().toString();
                clova();
            }
        });

        btnMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speaker = "nkyuwon";
                speed = 0;
                volume = 5;
                pitch = 2;
                emotionst = 0;
                emotion = 0;
                alpha = 0;
                end_pitch = -1;


            }
        });

        //여자목소리리
        btnWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speaker = "nyuna";
                speed = 0;
                volume = 5;
                pitch = -1;
                emotionst = 0;
                emotion = 2;
                alpha = 0;
                end_pitch = 0;
            }
        });



        imgBack.setOnClickListener(v -> {
            finish();
        });

    }


    public void clova() {

        Retrofit retrofit = ClovaNetworkClient.getRetrofitClient(GameChatActivity.this);
        ClovaVoiceService api = retrofit.create(ClovaVoiceService.class);

        Call<ResponseBody> call = api.synthesize(speaker, speed,volume ,pitch,emotionst,emotion,alpha,end_pitch,text);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        File tempMp3 = File.createTempFile("temp","mp3",getCacheDir());
                        tempMp3.deleteOnExit();;

                        InputStream inputStream = response.body().byteStream();
                        FileOutputStream fos = new FileOutputStream(tempMp3);
                        byte[] buffer = new byte[1024];
                        int read;
                        while ((read = inputStream.read(buffer))!=-1){
                            fos.write(buffer,0,read);
                        }
                        fos.flush();
                        fos.close();
                        inputStream.close();

                        MediaPlayer mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(tempMp3.getPath());
                        mediaPlayer.prepare();
                        mediaPlayer.start();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });

    }


}