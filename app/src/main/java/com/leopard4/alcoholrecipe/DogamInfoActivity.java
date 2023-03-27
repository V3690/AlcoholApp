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
import com.leopard4.alcoholrecipe.api.LikeApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.api.RecipeApi;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.dogam.DogamInfo;
import com.leopard4.alcoholrecipe.model.dogam.DogamInfoResponse;
import com.leopard4.alcoholrecipe.model.recipeOne.RecipeOne;
import com.leopard4.alcoholrecipe.model.recipeOne.RecipeOneResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DogamInfoActivity extends AppCompatActivity {

    ImageView imgBack, imgAlcohol, imgLike;
    TextView txtName;
    Button btnEdit;
    TextView txtLike, txtPercent, txtType, txtCategory, txtProduce, txtSupply;
    private String accessToken;
    int alcoholId;
    DogamInfo dogamInfo = new DogamInfo();

    public static int state = 0;
    int likeCnt;


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

        // 선택한 알콜 아이디 뽑아내는 코드
        alcoholId = getIntent().getIntExtra("alcoholId", 0);

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


        //  하트의 상태를 변화시킨다.
        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgLike.isSelected()) { // 하트가 채워져 있다면
                    imgLike.setSelected(false); // 하트가 비워진다.

                    imgLike.setImageResource(R.drawable.baseline_favorite_border_24_gray);
                    // 비동기로 네트워크 실행
                    Retrofit retrofit = NetworkClient.getRetrofitClient(DogamInfoActivity.this);
                    LikeApi api = retrofit.create(LikeApi.class);
                    Call<Void> call = api.deleteAlcoholLike(accessToken, alcoholId);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                // 상태가변화됨을 감지하는 변수
                                state = 1;
                                // txtLikeCnt 에도 하트가 눌렸을 때의 숫자를 반영한다.
                                likeCnt = likeCnt - 1;
                                txtLike.setText(Integer.toString(likeCnt));
                                Log.i("하트", "하트가 눌렸습니다.");
                                Log.i("액티비티스탯3", state + "");
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.i("하트", "하트가 눌리지 않았습니다.");
                        }
                    });


                } else { // 하트가 비워져 있다면
                    imgLike.setSelected(true); // 하트가 채워진다
                    imgLike.setImageResource(R.drawable.baseline_favorite_24);
                    // 비동기로 네트워크 실행
                    Retrofit retrofit = NetworkClient.getRetrofitClient(DogamInfoActivity.this);
                    LikeApi api = retrofit.create(LikeApi.class);
                    Call<Void> call = api.postAlcoholLike(accessToken, alcoholId);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                state = 1;
                                // txtLikeCnt 에도 하트가 눌렸을 때의 숫자를 반영한다.
                                likeCnt = likeCnt + 1;
                                txtLike.setText(Integer.toString(likeCnt));
                                Log.i("액티비티스탯2", state + "");
                                Log.i("하트", "하트가 눌렸습니다.");
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.i("하트", "하트가 눌리지 않았습니다.");
                        }
                    });
                }
            }
        });


    }

    private void getNetworkData() {


        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        DogamApi api = retrofit.create(DogamApi.class);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴

        Call<DogamInfoResponse> call = api.getDogamInfo(accessToken, alcoholId);

        call.enqueue(new Callback<DogamInfoResponse>() {

            @Override
            public void onResponse(Call<DogamInfoResponse> call, Response<DogamInfoResponse> response) {
                if (response.isSuccessful()) {
                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.
                    try {

                        dogamInfo = response.body().getDogamInfo();

                        txtName.setText(dogamInfo.getName());

                        // glide로 이미지 뿌려주기
                        Glide.with(DogamInfoActivity.this)
                                .load(dogamInfo.getImgUrl())
                                .into(imgAlcohol);

                        if (dogamInfo.getIsLike() == 1) {
                            imgLike.setImageResource(R.drawable.baseline_favorite_24);
                        }

                        txtLike.setText(dogamInfo.getLikeCnt() + "");
                        txtPercent.setText(dogamInfo.getPercent() + "");
                        txtType.setText(dogamInfo.getAlcoholType());
                        txtCategory.setText(dogamInfo.getCategory());
                        txtProduce.setText(dogamInfo.getProduce());
                        txtSupply.setText(dogamInfo.getSupply());


                        likeCnt = dogamInfo.getLikeCnt();


                        // glide로 이미지 뿌려주기
                        Glide.with(DogamInfoActivity.this)
                                .load(dogamInfo.getImgUrl())
                                .into(imgAlcohol);


                    } catch (Exception e) {
                        e.printStackTrace();


                    }

                }
            }

            @Override
            public void onFailure(Call<DogamInfoResponse> call, Throwable t) {
                Log.i("레시피 정보", t.getMessage());
            }
        });



    }
}
