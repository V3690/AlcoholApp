package com.leopard4.alcoholrecipe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.leopard4.alcoholrecipe.api.GameApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.cheers.CheersMentRes;
import com.leopard4.alcoholrecipe.model.cheers.Ment;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Tag;

public class GameToastFactoryActivity extends AppCompatActivity {

    TextView txtOutputFirst,txtOutputLast,txtOutput;
    ImageButton  imgButtonTwo,imgButtonOne;
    EditText editUserInput1,editUserInput2;

    ImageView imgSpeaker , imgSpeaker2 , imgBack ;
    private TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_toast_factory);

        txtOutputFirst=findViewById(R.id.txtOutputFirst);
        txtOutputLast=findViewById(R.id.txtOutputLast);
        txtOutput=findViewById(R.id.txtOutput);
        imgButtonTwo=findViewById(R.id.imgButtonTwo);
        imgButtonOne=findViewById(R.id.imgButtonOne);
        editUserInput1=findViewById(R.id.editUserInput1);
        editUserInput2=findViewById(R.id.editUserInput2);
        imgBack=findViewById(R.id.imgBack);
        imgSpeaker=findViewById(R.id.imgSpeaker);
        imgSpeaker2=findViewById(R.id.imgSpeaker2);

        imgButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String editMent = editUserInput1.getText().toString().trim();
                Ment ment = new Ment();
                ment.setMent(editMent);


                Retrofit retrofit = NetworkClient.getRetrofitClient(GameToastFactoryActivity.this);
                GameApi api = retrofit.create(GameApi.class);
                SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

                Call<CheersMentRes> call = api.getCheers(accessToken,2,ment);
                call.enqueue(new Callback<CheersMentRes>() {

                    @Override
                    public void onResponse(Call<CheersMentRes> call, Response<CheersMentRes> response) {
                        if (response.isSuccessful()){

                            Log.i(TAG,"키워드는"+editMent);
                            Log.i(TAG,"정상 쓸내용"+response.body().getItem().getFirst());
                            txtOutputFirst.setText( response.body().getItem().getFirst() );
                            txtOutputLast.setText( response.body().getItem().getLast() );
                        }else{

                            txtOutputFirst.setText("다른 단어 또는 문장을 입력해주세요");
                        }
                    }

                    @Override
                    public void onFailure(Call<CheersMentRes> call, Throwable t) {
                        Log.d("TAG", "onFailure: " + t.getMessage());
                    }
                });

            }
        });

        imgButtonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editMent = editUserInput2.getText().toString().trim();
                Ment ment = new Ment();
                ment.setMent(editMent);

                Retrofit retrofit = NetworkClient.getRetrofitClient(GameToastFactoryActivity.this);
                GameApi api = retrofit.create(GameApi.class);
                SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

                Call<CheersMentRes> call = api.getCheers(accessToken,1,ment);
                call.enqueue(new Callback<CheersMentRes>() {
                    @Override
                    public void onResponse(Call<CheersMentRes> call, Response<CheersMentRes> response) {
                        if(response.isSuccessful()){
                            txtOutput.setText(response.body().getItem().getFirst());
                        }else{
                            txtOutput.setText("다른 단어 또는 문장을 입력해주세요");

                        }

                    }

                    @Override
                    public void onFailure(Call<CheersMentRes> call, Throwable t) {
                        Log.d("TAG", "onFailure: " + t.getMessage());
                    }
                });



            }
        });

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.KOREA);

                    if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                        Log.e("TTS", "Language not supported.");
                    }
                } else {
                    Log.e("TTS", "Initialization failed.");
                }
            }
        });

        imgSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence ment = txtOutputFirst.getText().toString();
                tts.setPitch((float)0.5); // 톤조절
                tts.setSpeechRate((float)0.8); // 속도

                tts.speak(ment, TextToSpeech.QUEUE_FLUSH, null, "uid");
                // 첫 번째 매개변수: 음성 출력을 할 텍스트
                // 두 번째 매개변수: 1. TextToSpeech.QUEUE_FLUSH - 진행중인 음성 출력을 끊고 이번 TTS의 음성 출력
                //                 2. TextToSpeech.QUEUE_ADD - 진행중인 음성 출력이 끝난 후에 이번 TTS의 음성 출력
            }
        });

        imgSpeaker2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence ment = txtOutput.getText().toString();
                tts.setPitch((float)0.5); // 톤조절
                tts.setSpeechRate((float)0.8); // 속도

                tts.speak(ment, TextToSpeech.QUEUE_FLUSH, null, "uid");
                // 첫 번째 매개변수: 음성 출력을 할 텍스트
                // 두 번째 매개변수: 1. TextToSpeech.QUEUE_FLUSH - 진행중인 음성 출력을 끊고 이번 TTS의 음성 출력
                //                 2. TextToSpeech.QUEUE_ADD - 진행중인 음성 출력이 끝난 후에 이번 TTS의 음성 출력

            }
        });



        imgBack.setOnClickListener(v -> {
            finish();
        });

    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            // Interrupts the current utterance (whether played or rendered to file) and
            // discards other utterances in the queue.
            tts.shutdown();
            // Releases the resources used by the TextToSpeech engine.
        }
        super.onDestroy();
    }


}