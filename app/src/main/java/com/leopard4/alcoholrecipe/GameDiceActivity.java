package com.leopard4.alcoholrecipe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.leopard4.alcoholrecipe.api.GameApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.DiceRes;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Tag;

public class GameDiceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner10,spinner11;

    TextView txtHuman , txtRule;

    ImageView imgBack ;
    Button btnSelect1,btnSelect2, btnSound ;
    private TextToSpeech tts;

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

        ArrayAdapter<CharSequence> adapterHuman = ArrayAdapter.createFromResource(this,R.array.spinner_item6 , R.layout.layout_spinner);;
        ArrayAdapter<CharSequence> adapterRule = ArrayAdapter.createFromResource(this,R.array.spinner_item7 , R.layout.layout_spinner);;

        spinner10.setOnItemSelectedListener(GameDiceActivity.this);
        spinner11.setOnItemSelectedListener(GameDiceActivity.this);

        spinner10.setAdapter(adapterHuman);
        spinner11.setAdapter(adapterRule);


        //tts
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





        btnSelect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                CharSequence ment = txtHuman.getText().toString()+","+txtRule.getText().toString();
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