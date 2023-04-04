package com.leopard4.alcoholrecipe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.leopard4.alcoholrecipe.api.ClovaNetworkClient;
import com.leopard4.alcoholrecipe.api.ClovaVoiceService;
import com.leopard4.alcoholrecipe.api.GameApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.DiceRes;

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
import retrofit2.http.Tag;

public class GameDiceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner10,spinner11;

    TextView txtHuman , txtRule;
    RadioButton btnMan , btnWoman;
    Switch switchVoice;

    ImageView imgBack, imgBox1, imgBox2 ;
    Button btnSelect1,btnSelect2, btnSound ;

    int isswithcheck =0;
    private String text;
    private String speaker = "nwoosik";
    private int speed= 0;
    private int volume= 5;
    private int pitch= -3;
    private int emotionst= 0;
    private int emotion= 0;
    private int alpha= 0;
    private int end_pitch= 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_dice);

        spinner10=findViewById(R.id.spinner10);
        spinner11=findViewById(R.id.spinner11);
        btnSelect1 = findViewById(R.id.btnSelect1);
        btnSelect2 = findViewById(R.id.btnSelect2);
        imgBack = findViewById(R.id.imgBack);
        txtHuman = findViewById(R.id.txtHuman);
        txtRule = findViewById(R.id.txtRule);
        btnSound=findViewById(R.id.btnSound);
        imgBox1 = findViewById(R.id.imgBox1);
        imgBox2 = findViewById(R.id.imgBox2);
        btnMan=findViewById(R.id.btnMan);
        btnWoman=findViewById(R.id.btnWoman);
        switchVoice=findViewById(R.id.switchVoice);


        ArrayAdapter<CharSequence> adapterHuman = ArrayAdapter.createFromResource(this,R.array.spinner_item6 , R.layout.layout_spinner2);;
        ArrayAdapter<CharSequence> adapterRule = ArrayAdapter.createFromResource(this,R.array.spinner_item7 , R.layout.layout_spinner2);;

        spinner10.setOnItemSelectedListener(GameDiceActivity.this);
        spinner11.setOnItemSelectedListener(GameDiceActivity.this);

        spinner10.setAdapter(adapterHuman);
        spinner11.setAdapter(adapterRule);


        switchVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    isswithcheck=1;
                    btnSelect1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            YoYo.with(Techniques.RubberBand).duration(600)
                                    .repeat(0).playOn(imgBox1);

                            YoYo.with(Techniques.SlideInRight).duration(400)
                                    .repeat(0).playOn(txtHuman);

                            int subjectTypeId = spinner10.getSelectedItemPosition() + 1;
                            int penaltyTypeId = spinner11.getSelectedItemPosition() + 1;
                            Log.i(TAG,"서브젝트아이디"+ subjectTypeId);
                            Log.i(TAG,"패널티아이디"+ penaltyTypeId);

                            Retrofit retrofit = NetworkClient.getRetrofitClient(GameDiceActivity.this);
                            GameApi api = retrofit.create(GameApi.class);
                            SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                            String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

                            Call<DiceRes> call = api.diceRes(accessToken,subjectTypeId,penaltyTypeId);

                            call.enqueue(new Callback<DiceRes>() {
                                @Override
                                public void onResponse(Call<DiceRes> call, Response<DiceRes> response) {
                                    if(response.isSuccessful()){
                                        Log.i("객체확인",response.body().getSubject());
                                        txtHuman.setVisibility(View.VISIBLE);
                                        txtHuman.setText(response.body().getSubject());

                                        text=txtHuman.getText().toString();
                                        clova();

                                    }

                                }

                                @Override
                                public void onFailure(Call<DiceRes> call, Throwable t) {
                                    Log.d("TAG", "onFailure: " + t.getMessage());
                                }
                            });


                        }
                    });

                    btnSelect2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            YoYo.with(Techniques.RubberBand).duration(600)
                                    .repeat(0).playOn(imgBox2);

                            YoYo.with(Techniques.SlideInLeft).duration(400)
                                    .repeat(0).playOn(txtRule);

                            int subjectTypeId = spinner10.getSelectedItemPosition() + 1;
                            int penaltyTypeId = spinner11.getSelectedItemPosition() + 1;
                            Log.i(TAG,"서브젝트아이디"+ subjectTypeId);
                            Log.i(TAG,"패널티아이디"+ penaltyTypeId);

                            Retrofit retrofit = NetworkClient.getRetrofitClient(GameDiceActivity.this);
                            GameApi api = retrofit.create(GameApi.class);
                            SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                            String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");
                            Call<DiceRes> call = api.diceRes(accessToken,subjectTypeId,penaltyTypeId);
                            call.enqueue(new Callback<DiceRes>() {
                                @Override
                                public void onResponse(Call<DiceRes> call, Response<DiceRes> response) {
                                    Log.i("객체확인",response.body().getAction());
                                    txtRule.setVisibility(View.VISIBLE);
                                    txtRule.setText(response.body().getAction());
                                    text=txtRule.getText().toString();
                                    clova();
                                }

                                @Override
                                public void onFailure(Call<DiceRes> call, Throwable t) {

                                    Log.d("TAG", "onFailure: " + t.getMessage());
                                }
                            });


                        }
                    });


                }else {
                    isswithcheck=0;
                }
            }
        });



        btnSelect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                YoYo.with(Techniques.RubberBand).duration(600)
                        .repeat(0).playOn(imgBox1);

                YoYo.with(Techniques.SlideInRight).duration(400)
                        .repeat(0).playOn(txtHuman);

                int subjectTypeId = spinner10.getSelectedItemPosition() + 1;
                int penaltyTypeId = spinner11.getSelectedItemPosition() + 1;
                Log.i(TAG,"서브젝트아이디"+ subjectTypeId);
                Log.i(TAG,"패널티아이디"+ penaltyTypeId);

                Retrofit retrofit = NetworkClient.getRetrofitClient(GameDiceActivity.this);
                GameApi api = retrofit.create(GameApi.class);
                SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

                Call<DiceRes> call = api.diceRes(accessToken,subjectTypeId,penaltyTypeId);

                call.enqueue(new Callback<DiceRes>() {
                    @Override
                    public void onResponse(Call<DiceRes> call, Response<DiceRes> response) {
                        if(response.isSuccessful()){
                            Log.i("객체확인",response.body().getSubject());
                            txtHuman.setVisibility(View.VISIBLE);
                            txtHuman.setText(response.body().getSubject());


                    }

                }

                    @Override
                    public void onFailure(Call<DiceRes> call, Throwable t) {
                        Log.d("TAG", "onFailure: " + t.getMessage());
                    }
                });


            }
        });

        btnSelect2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                YoYo.with(Techniques.RubberBand).duration(600)
                        .repeat(0).playOn(imgBox2);

                YoYo.with(Techniques.SlideInLeft).duration(400)
                        .repeat(0).playOn(txtRule);

                int subjectTypeId = spinner10.getSelectedItemPosition() + 1;
                int penaltyTypeId = spinner11.getSelectedItemPosition() + 1;
                Log.i(TAG,"서브젝트아이디"+ subjectTypeId);
                Log.i(TAG,"패널티아이디"+ penaltyTypeId);

                Retrofit retrofit = NetworkClient.getRetrofitClient(GameDiceActivity.this);
                GameApi api = retrofit.create(GameApi.class);
                SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");
                Call<DiceRes> call = api.diceRes(accessToken,subjectTypeId,penaltyTypeId);
                call.enqueue(new Callback<DiceRes>() {
                    @Override
                    public void onResponse(Call<DiceRes> call, Response<DiceRes> response) {
                        Log.i("객체확인",response.body().getAction());
                        txtRule.setVisibility(View.VISIBLE);
                        txtRule.setText(response.body().getAction());
                    }

                    @Override
                    public void onFailure(Call<DiceRes> call, Throwable t) {

                        Log.d("TAG", "onFailure: " + t.getMessage());
                    }
                });


            }
        });


        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              text = txtHuman.getText().toString()+","+txtRule.getText().toString();
              clova();

            }
        });

        btnMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speaker = "nwoosik";
                speed = 0;
                volume = 5;
                pitch = -3;
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
                speaker = "nes_c_sohyun";
                speed = 0;
                volume = 5;
                pitch = 3;
                emotionst = 0;
                emotion = 0;
                alpha = 1;
                end_pitch = 0;
            }
        });




        imgBack.setOnClickListener(v -> {
            finish();
        });

    }

    public void clova() {

        Retrofit retrofit = ClovaNetworkClient.getRetrofitClient(GameDiceActivity.this);
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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private int getSpinnerIndex(Spinner spinner, String value) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                index = i;
                break;
            }
        }
        return index;
    }


}