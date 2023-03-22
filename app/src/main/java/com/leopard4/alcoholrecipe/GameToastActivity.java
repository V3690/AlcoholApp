package com.leopard4.alcoholrecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.leopard4.alcoholrecipe.api.GameApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.CheersMentRes;
import com.leopard4.alcoholrecipe.model.Ment;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GameToastActivity extends AppCompatActivity {

    Button btnMyToast;
    TextView txtFirst,txtLast;
    ImageButton imgBack;
    ImageView imgSpeaker;
    private TextToSpeech tts;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_toast);

        imgSpeaker = findViewById(R.id.imgSpeaker);
        imgBack=findViewById(R.id.imgBack);
        btnMyToast = findViewById(R.id.btnPassEdit);
        txtFirst=findViewById(R.id.txtFirst);
        txtLast=findViewById(R.id.txtLast);

        getCheersMentResNetworkData();


        btnMyToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameToastActivity.this, GameToastFactoryActivity.class);
                startActivity(intent);
                finish();
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
                CharSequence ment = txtFirst.getText();
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








    private void getCheersMentResNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(GameToastActivity.this);
        GameApi api = retrofit.create(GameApi.class);

        Ment ment = new Ment(); // 이 객체는 이 함수가 끝나면 사라진다.
        // 랜덤으로 창조말 가져오기
        ment.LandomMent();
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<CheersMentRes> call = api.getCheers(accessToken,2, ment ); // 2는 선창 후창임

        call.enqueue(new Callback<CheersMentRes>() {
            @Override
            public void onResponse(Call<CheersMentRes> call, Response<CheersMentRes> response) {

                if(response.isSuccessful()){
                    txtFirst.setText( response.body().getItem().getFirst() );
                    txtLast.setText( response.body().getItem().getLast() );


                }else{
                    // response가 에러를 가져왔다면 여기서 처리
                    txtFirst.setText("이미 집에 가기는");
                    txtLast.setText("너무 늦었어요");

                }
            }

            @Override
            public void onFailure(Call<CheersMentRes> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());

            }
        });


    }
}