package com.leopard4.alcoholrecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leopard4.alcoholrecipe.api.DogamApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.api.RecipeApi;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.RecipeOne;
import com.leopard4.alcoholrecipe.model.RecipeOneResponse;
import com.leopard4.alcoholrecipe.model.dogam.Dogam;
import com.leopard4.alcoholrecipe.model.dogam.DogamInfo;
import com.leopard4.alcoholrecipe.model.dogam.DogamInfoResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DogamInfoActivity extends AppCompatActivity {

    ImageButton imgBack;
    TextView txtName;
    Button btnEdit;
    ImageView imgAlcohol;
    ImageButton imgLike;
    TextView txtLike, txtPercent, txtType, txtCategory, txtProduce, txtSupply;
    private String accessToken;
    int alcoholId;
    DogamInfo dogamInfo = new DogamInfo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogam_info);

        imgBack = findViewById(R.id.imgBack);
        txtName = findViewById(R.id.txtName);
        btnEdit = findViewById(R.id.btnEdit);
        imgAlcohol = findViewById(R.id.imgAlcohol);
        imgLike = findViewById(R.id.imgLike);
        txtLike = findViewById(R.id.txtLike);
        txtPercent = findViewById(R.id.txtPercent);
        txtType = findViewById(R.id.txtType);
        txtCategory = findViewById(R.id.txtCategory);
        txtProduce = findViewById(R.id.txtProduce);
        txtSupply = findViewById(R.id.txtSupply);

        // 네트워크 호출 함수
        getNetworkData();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    private void getNetworkData() {

        // 선택한 알콜 아이디 뽑아내는 코드
        alcoholId = getIntent().getIntExtra("alcoholId", 0);

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DogamApi api = retrofit.create(DogamApi.class);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴

        Call<DogamInfoResponse> call = api.getDogamInfo(accessToken, alcoholId);

        call.enqueue(new Callback<DogamInfoResponse>() {

            @Override
            public void onResponse(Call<DogamInfoResponse> call, Response<DogamInfoResponse> response) {
                if (response.isSuccessful()) {

                    dogamInfo = response.body().getDogamInfo();

                   txtName.setText(dogamInfo.getName());

                    // glide로 이미지 뿌려주기
                    Glide.with(DogamInfoActivity.this)
                            .load(dogamInfo.getImgUrl())
                            .into(imgAlcohol);

                    txtLike.setText(dogamInfo.getLikeCnt()+"");
                    txtPercent.setText(dogamInfo.getPercent()+"");
                    txtType.setText(dogamInfo.getAlcoholType());
                    txtCategory.setText(dogamInfo.getCategory());
                    txtProduce.setText(dogamInfo.getProduce());
                    txtSupply.setText(dogamInfo.getSupply());

                }
            }


            @Override
            public void onFailure(Call<DogamInfoResponse> call, Throwable t) {

            }
        });

    }


    }
