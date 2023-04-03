package com.leopard4.alcoholrecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.leopard4.alcoholrecipe.api.ClovaNetworkClient;
import com.leopard4.alcoholrecipe.api.ClovaVoiceService;
import com.leopard4.alcoholrecipe.api.GameApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.cheers.CheersMentRes;
import com.leopard4.alcoholrecipe.model.cheers.Ment;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class GameToastActivity extends AppCompatActivity {

    Button btnStart;
    TextView txtFirst,txtLast , textView3;
    ImageView imgBack, imgSpeaker;
    private TextToSpeech tts;

    private String speaker = "nsunhee";
    private int speed= -1;
    private int volume= 5;
    private int pitch= -4;
    private int emotionst= 0;
    private int emotion= 0;
    private int alpha= 5;
    private int end_pitch= 0;
    private String ment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_toast);

        imgSpeaker = findViewById(R.id.imgSpeaker);
        imgBack=findViewById(R.id.imgBack);
        btnStart = findViewById(R.id.btnStart);
        txtFirst=findViewById(R.id.txtOutputFirst);
        txtLast=findViewById(R.id.txtOutputLast);
        textView3=findViewById(R.id.textView3);



        getCheersMentResNetworkData();


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameToastActivity.this, GameToastFactoryActivity.class);
                startActivity(intent);
                finish();
            }
        });


        imgSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ment = String.valueOf(txtFirst.getText());

                clova();

            }
        });

        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speaker = "nraewon";
                speed = -1;
                volume = 5;
                pitch = 1;
                emotionst = 0;
                emotion = 0;
                alpha = 0;
                end_pitch = 0;

                textView3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        speaker = "vdain";
                        speed = -1;
                        volume = 5;
                        pitch = 1;
                        emotionst = 2;
                        emotion = 2;
                        alpha = 0;
                        end_pitch = 0;
                    }
                });

            }
        });
//구글tts
//        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if (status == TextToSpeech.SUCCESS) {
//                    int result = tts.setLanguage(Locale.KOREA);
//
//                    if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
//                        Log.e("TTS", "Language not supported.");
//                    }
//                } else {
//                    Log.e("TTS", "Initialization failed.");
//                }
//            }
//        });
// 구글tts
//        imgSpeaker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CharSequence ment = txtFirst.getText();
//                tts.setPitch((float)0.5); // 톤조절
//                tts.setSpeechRate((float)0.8); // 속도
//
//                tts.speak(ment, TextToSpeech.QUEUE_FLUSH, null, "uid");
//                // 첫 번째 매개변수: 음성 출력을 할 텍스트
//                // 두 번째 매개변수: 1. TextToSpeech.QUEUE_FLUSH - 진행중인 음성 출력을 끊고 이번 TTS의 음성 출력
//                //                 2. TextToSpeech.QUEUE_ADD - 진행중인 음성 출력이 끝난 후에 이번 TTS의 음성 출력
//            }
//        });

        imgBack.setOnClickListener(v -> {
            finish();
        });

    }


//구글tts 종료
//    @Override
//    protected void onDestroy() {
//        if (tts != null) {
//            tts.stop();
//            // Interrupts the current utterance (whether played or rendered to file) and
//            // discards other utterances in the queue.
//            tts.shutdown();
//            // Releases the resources used by the TextToSpeech engine.
//        }
//        super.onDestroy();
//    }



    public void clova() {

        Retrofit retrofit = ClovaNetworkClient.getRetrofitClient(GameToastActivity.this);
        ClovaVoiceService api = retrofit.create(ClovaVoiceService.class);


        Call<ResponseBody> call = api.synthesize(speaker, speed,volume ,pitch,emotionst,emotion,alpha,end_pitch,ment);
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