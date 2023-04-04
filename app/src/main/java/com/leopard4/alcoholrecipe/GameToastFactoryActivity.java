package com.leopard4.alcoholrecipe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.leopard4.alcoholrecipe.api.ClovaNetworkClient;
import com.leopard4.alcoholrecipe.api.ClovaVoiceService;
import com.leopard4.alcoholrecipe.api.GameApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.cheers.CheersMentRes;
import com.leopard4.alcoholrecipe.model.cheers.Ment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.Tag;

public class GameToastFactoryActivity extends AppCompatActivity {

    TextView txtOutputFirst,txtOutputLast,txtOutput;

    EditText editUserInput1,editUserInput2;

    ImageView  imgBack, btnStart1,btnStart2; ;
    Switch switchVoice;

    Button btnShare   ,btnSound1 ,btnSound2 ,btnSound3 ,btnSound ;
    RadioButton  btnMan , btnWoman;
    private TextToSpeech tts;
    private String text;
    private String speaker = "nminyoung";
    private int speed= -1;
    private int volume= 5;
    private int pitch= 1;
    private int emotionst= 0;
    private int emotion= 0;
    private int alpha= 0;
    private int end_pitch= 0;

    int isswithcheck =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_toast_factory);

        txtOutputFirst=findViewById(R.id.txtOutputFirst);
        txtOutputLast=findViewById(R.id.txtOutputLast);
        txtOutput=findViewById(R.id.txtOutput);
        btnStart1=findViewById(R.id.btnStart1);
        btnStart2=findViewById(R.id.btnStart2);
        editUserInput1=findViewById(R.id.editUserInput1);
        editUserInput2=findViewById(R.id.editUserInput2);
        imgBack=findViewById(R.id.imgBack);

        btnShare = findViewById(R.id.btnShare);


        btnMan=findViewById(R.id.btnMan);
        btnWoman=findViewById(R.id.btnWoman);
        switchVoice=findViewById(R.id.switchVoice);
        btnSound1=findViewById(R.id.btnSound1);
        btnSound2=findViewById(R.id.btnSound2);
        btnSound3=findViewById(R.id.btnSound3);
        btnSound=findViewById(R.id.btnSound);

        switchVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    isswithcheck = 1;
                    btnStart1.setOnClickListener(new View.OnClickListener() {
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

                                        btnShare.setVisibility(View.VISIBLE);

                                        text =txtOutputFirst.getText().toString() +" ," +txtOutputLast.getText().toString()+"!!!!";
                                        clova();
                                        btnShare.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                // 현재화면을 캡쳐해서 공유하기

                                                View view1 = getWindow().getDecorView(); // 현재 화면을 가져옴 // 현재 화면을 가져오는 방법은 여러가지가 있지만, 여기서는 가장 간단한 방법을 사용
                                                view1.setDrawingCacheEnabled(true); // 캐시를 사용하도록 설정 // 캐시를 사용하면, 화면이 바뀌어도 캐시를 사용하기 때문에 화면이 바뀌지 않는다.
                                                Bitmap captureView = view1.getDrawingCache(); // 캐시를 비트맵으로 변환 // 비트맵이란 픽셀로 이루어진 이미지를 의미
                                                String path = MediaStore.Images.Media.insertImage(getContentResolver(), captureView, "title", null); // 갤러리에 저장 // 갤러리에 저장하기 위해서는 Uri가 필요하다.
                                                Uri uri = Uri.parse(path); // 저장한 경로를 Uri로 변환 // Uri는 경로를 의미
                                                Intent intent = new Intent(Intent.ACTION_SEND); // 공유하기 위한 인텐트 생성
                                                intent.setType("image/*"); // 이미지를 공유하기 위해 타입을 이미지로 설정 // 이미지를 공유하기 위해 타입을 이미지로 설정
                                                intent.putExtra(Intent.EXTRA_STREAM, uri); // 공유할 이미지를 추가
                                                startActivity(Intent.createChooser(intent, "공유하기")); // 공유하기 창 띄우기
                                            }
                                        });
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

                    btnStart2.setOnClickListener(new View.OnClickListener() {
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

                                        text=txtOutput.getText().toString();
                                        clova();

                                        btnShare.setVisibility(View.VISIBLE);
                                        btnShare.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                // 현재화면을 캡쳐해서 공유하기

                                                View view1 = getWindow().getDecorView(); // 현재 화면을 가져옴 // 현재 화면을 가져오는 방법은 여러가지가 있지만, 여기서는 가장 간단한 방법을 사용
                                                view1.setDrawingCacheEnabled(true); // 캐시를 사용하도록 설정 // 캐시를 사용하면, 화면이 바뀌어도 캐시를 사용하기 때문에 화면이 바뀌지 않는다.
                                                Bitmap captureView = view1.getDrawingCache(); // 캐시를 비트맵으로 변환 // 비트맵이란 픽셀로 이루어진 이미지를 의미
                                                String path = MediaStore.Images.Media.insertImage(getContentResolver(), captureView, "title", null); // 갤러리에 저장 // 갤러리에 저장하기 위해서는 Uri가 필요하다.
                                                Uri uri = Uri.parse(path); // 저장한 경로를 Uri로 변환 // Uri는 경로를 의미
                                                Intent intent = new Intent(Intent.ACTION_SEND); // 공유하기 위한 인텐트 생성
                                                intent.setType("image/*"); // 이미지를 공유하기 위해 타입을 이미지로 설정 // 이미지를 공유하기 위해 타입을 이미지로 설정
                                                intent.putExtra(Intent.EXTRA_STREAM, uri); // 공유할 이미지를 추가
                                                startActivity(Intent.createChooser(intent, "공유하기")); // 공유하기 창 띄우기

                                            }
                                        });
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





                }else{
                    isswithcheck = 0;



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

                }
            }
        });


        btnStart1.setOnClickListener(new View.OnClickListener() {
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

                            btnShare.setVisibility(View.VISIBLE);
                            btnShare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // 현재화면을 캡쳐해서 공유하기

                                    View view1 = getWindow().getDecorView(); // 현재 화면을 가져옴 // 현재 화면을 가져오는 방법은 여러가지가 있지만, 여기서는 가장 간단한 방법을 사용
                                    view1.setDrawingCacheEnabled(true); // 캐시를 사용하도록 설정 // 캐시를 사용하면, 화면이 바뀌어도 캐시를 사용하기 때문에 화면이 바뀌지 않는다.
                                    Bitmap captureView = view1.getDrawingCache(); // 캐시를 비트맵으로 변환 // 비트맵이란 픽셀로 이루어진 이미지를 의미
                                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), captureView, "title", null); // 갤러리에 저장 // 갤러리에 저장하기 위해서는 Uri가 필요하다.
                                    Uri uri = Uri.parse(path); // 저장한 경로를 Uri로 변환 // Uri는 경로를 의미
                                    Intent intent = new Intent(Intent.ACTION_SEND); // 공유하기 위한 인텐트 생성
                                    intent.setType("image/*"); // 이미지를 공유하기 위해 타입을 이미지로 설정 // 이미지를 공유하기 위해 타입을 이미지로 설정
                                    intent.putExtra(Intent.EXTRA_STREAM, uri); // 공유할 이미지를 추가
                                    startActivity(Intent.createChooser(intent, "공유하기")); // 공유하기 창 띄우기

                                }
                            });


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

        btnStart2.setOnClickListener(new View.OnClickListener() {
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

                            btnShare.setVisibility(View.VISIBLE);
                            btnShare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // 현재화면을 캡쳐해서 공유하기

                                    View view1 = getWindow().getDecorView(); // 현재 화면을 가져옴 // 현재 화면을 가져오는 방법은 여러가지가 있지만, 여기서는 가장 간단한 방법을 사용
                                    view1.setDrawingCacheEnabled(true); // 캐시를 사용하도록 설정 // 캐시를 사용하면, 화면이 바뀌어도 캐시를 사용하기 때문에 화면이 바뀌지 않는다.
                                    Bitmap captureView = view1.getDrawingCache(); // 캐시를 비트맵으로 변환 // 비트맵이란 픽셀로 이루어진 이미지를 의미
                                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), captureView, "title", null); // 갤러리에 저장 // 갤러리에 저장하기 위해서는 Uri가 필요하다.
                                    Uri uri = Uri.parse(path); // 저장한 경로를 Uri로 변환 // Uri는 경로를 의미
                                    Intent intent = new Intent(Intent.ACTION_SEND); // 공유하기 위한 인텐트 생성
                                    intent.setType("image/*"); // 이미지를 공유하기 위해 타입을 이미지로 설정 // 이미지를 공유하기 위해 타입을 이미지로 설정
                                    intent.putExtra(Intent.EXTRA_STREAM, uri); // 공유할 이미지를 추가
                                    startActivity(Intent.createChooser(intent, "공유하기")); // 공유하기 창 띄우기

                                }
                            });


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


        //남자 목소리
        btnMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speaker = "neunwoo";
                speed = -1;
                volume = 5;
                pitch = 1;
                emotionst = 0;
                emotion = 0;
                alpha = 0;
                end_pitch = 0;


            }
        });

        //여자목소리리
        btnWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speaker = "nminyoung";
                speed = -1;
                volume = 5;
                pitch = 2;
                emotionst = 0;
                emotion = 0;
                alpha = 0;
                end_pitch = 0;
            }
        });



        //선창
        btnSound1 .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = txtOutputFirst.getText().toString();
                clova();
//                googletts();

            }
        });

        //후창
        btnSound2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text=txtOutputLast.getText().toString()+"!!!!!";
                clova();
            }
        });

        //전체
        btnSound3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text=txtOutputFirst.getText().toString() +" ," +txtOutputLast.getText().toString();
                clova();
            }
        });

        //단독
        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = txtOutput.getText().toString()+"!!!!";
                clova();

//                googletts();

            }
        });



        imgBack.setOnClickListener(v -> {
            finish();
        });


    }




    public void clova() {

        Retrofit retrofit = ClovaNetworkClient.getRetrofitClient(GameToastFactoryActivity.this);
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



//구글 tts
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

    //구글tts실행시 사용설정
//    public void googletts() {
//        tts.setPitch((float)0.5); // 톤조절
//        tts.setSpeechRate((float)0.8); // 속도
//
//        tts.speak(ment, TextToSpeech.QUEUE_FLUSH, null, "uid");
//        // 첫 번째 매개변수: 음성 출력을 할 텍스트
//        // 두 번째 매개변수: 1. TextToSpeech.QUEUE_FLUSH - 진행중인 음성 출력을 끊고 이번 TTS의 음성 출력
//        //                 2. TextToSpeech.QUEUE_ADD - 진행중인 음성 출력이 끝난 후에 이번 TTS의 음성 출력
//    }
//


}