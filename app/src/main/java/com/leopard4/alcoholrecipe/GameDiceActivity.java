package com.leopard4.alcoholrecipe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.leopard4.alcoholrecipe.api.GameApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.DiceRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Tag;

public class GameDiceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner10;
    Spinner spinner11;

    TextView txtHuman , txtRule;



    Button btnSelect1,btnSelect2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_dice);

        spinner10=findViewById(R.id.spinner10);
        spinner11=findViewById(R.id.spinner11);
        btnSelect1 = findViewById(R.id.btnSelect1);
        btnSelect2 = findViewById(R.id.btnSelect2);

        txtHuman = findViewById(R.id.txtHuman);
        txtRule = findViewById(R.id.txtRule);


        ArrayAdapter<CharSequence> adapterHuman = ArrayAdapter.createFromResource(this,R.array.spinner_item6 , android.R.layout.simple_spinner_dropdown_item);;
        ArrayAdapter<CharSequence> adapterRule = ArrayAdapter.createFromResource(this,R.array.spinner_item7 , android.R.layout.simple_spinner_dropdown_item);;

        spinner10.setOnItemSelectedListener(GameDiceActivity.this);
        spinner11.setOnItemSelectedListener(GameDiceActivity.this);

        spinner10.setAdapter(adapterHuman);
        spinner11.setAdapter(adapterRule);








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