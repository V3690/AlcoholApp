package com.leopard4.alcoholrecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leopard4.alcoholrecipe.api.LikeApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.api.RecipeApi;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.recipeOne.RecipeOne;
import com.leopard4.alcoholrecipe.model.recipeOne.RecipeOneResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecipeInfoActivity extends AppCompatActivity {

    ImageView imgUrl,imgRecipeLike;
    TextView txtRecipeTitle, txtLikeCnt, txtPercent, txtAlcoholType, txtUserId, txtEngTitle, txtIntro, txtContent, txtIngredient;
    Button btnReturnRecipe, btnEdit;
    ImageView imgBack;
    private String accessToken;
    private int recipeId;
    private int userId;
    private int getAdapterUserId;
    private ArrayList<RecipeOne> recipeOneList = new ArrayList<>();
    // 상태가변화됨을 감지하는 변수
    public static int state = 0;
    int likeCnt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);

        imgBack = findViewById(R.id.imgBack);
        btnReturnRecipe = findViewById(R.id.btnReturnRecipe);
        txtRecipeTitle = findViewById(R.id.txtRecipeTitle);
        imgUrl = findViewById(R.id.imgUrl);
        txtLikeCnt = findViewById(R.id.txtLikeCnt);
        txtPercent = findViewById(R.id.txtPercent);
        txtAlcoholType = findViewById(R.id.txtAlcoholType);
        txtUserId = findViewById(R.id.txtUserId);
        txtEngTitle = findViewById(R.id.txtEngTitle);
        txtIntro = findViewById(R.id.txtIntro);
        txtContent = findViewById(R.id.txtContent);
        txtIngredient = findViewById(R.id.txtIngredient);

        imgRecipeLike = findViewById(R.id.imgRecipeLike);

        btnEdit = findViewById(R.id.btnEdit);



        recipeId = getIntent().getIntExtra("recipeId", 0);
        Log.i("레시피 아이디", Integer.toString(recipeId));
        Log.i("액티비티스탯",state+"");

        getNetworkData();


        //  하트의 상태를 변화시킨다.
        imgRecipeLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgRecipeLike.isSelected()) { // 하트가 채워져 있다면
                    imgRecipeLike.setSelected(false); // 하트가 비워진다.

                    imgRecipeLike.setImageResource(R.drawable.baseline_favorite_border_24_gray);
                    // 비동기로 네트워크 실행
                    Retrofit retrofit = NetworkClient.getRetrofitClient(RecipeInfoActivity.this);
                    LikeApi api = retrofit.create(LikeApi.class);
                    Call<Void> call = api.deleteLike(accessToken, recipeId);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                // 상태가변화됨을 감지하는 변수
                                state = 1;
                                // txtLikeCnt 에도 하트가 눌렸을 때의 숫자를 반영한다.
                                likeCnt = likeCnt - 1;
                                txtLikeCnt.setText(Integer.toString(likeCnt));
                                Log.i("하트", "하트가 눌렸습니다.");
                                Log.i("액티비티스탯3",state+"");
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.i("하트", "하트가 눌리지 않았습니다.");
                        }
                    });


                } else { // 하트가 비워져 있다면
                    imgRecipeLike.setSelected(true); // 하트가 채워진다
                    imgRecipeLike.setImageResource(R.drawable.baseline_favorite_24);
                    // 비동기로 네트워크 실행
                    Retrofit retrofit = NetworkClient.getRetrofitClient(RecipeInfoActivity.this);
                    LikeApi api = retrofit.create(LikeApi.class);
                    Call<Void> call = api.postLike(accessToken, recipeId);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                state = 1;
                                // txtLikeCnt 에도 하트가 눌렸을 때의 숫자를 반영한다.
                                likeCnt = likeCnt + 1;
                                txtLikeCnt.setText(Integer.toString(likeCnt));
                                Log.i("액티비티스탯2",state+"");
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



        // 뒤로가기 버튼
        imgBack.setOnClickListener(v -> {
            finish();
        });

        // 레시피 버튼을 눌렀을때 엑티비티 레시피로 이동~
        btnReturnRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeInfoActivity.this, RecipeActivity.class);
            startActivity(intent);
        });
    }
    private void getNetworkData() {


        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        RecipeApi api = retrofit.create(RecipeApi.class);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴

        Call<RecipeOneResponse> call = api.getRecipeOne(accessToken, recipeId);

        call.enqueue(new Callback<RecipeOneResponse>() {

            @Override
            public void onResponse(Call<RecipeOneResponse> call, Response<RecipeOneResponse> response) {
                if (response.isSuccessful()) {
                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.
                    try {
                        RecipeOne recipeOne = new RecipeOne();
    //                    recipeOneList.addAll(response.body().getRecipeOne());

                        recipeOne = response.body().getRecipeOne();
                        // glide로 이미지 뿌려주기
                        Glide.with(RecipeInfoActivity.this)
                                .load(recipeOne.getImgUrl())
                                .into(imgUrl);

                        // 레시피를 작성한 사람이라면 수정버튼을 보여주기 위해서
                        // myrecipeAdapter 에서 받은 userId
                        getAdapterUserId = getIntent().getIntExtra("userId", 0);
//                    Log.i("접속한유저ID", getAdapterUserId+"");
                        userId = recipeOne.getUserId();
//                    Log.i("유저ID테스트", userId+"");
                        if (userId == getAdapterUserId){
                            btnEdit.setVisibility(View.VISIBLE);
                        }


                        if (recipeOne.getIsLike() == 1) {
                            imgRecipeLike.setImageResource(R.drawable.baseline_favorite_24);
                        }

                        // recipeOnd에 값이 없는게 있을때 처리
                        if (recipeOne.getImgUrl() == null) {
                            recipeOne.setImgUrl("");
                        }
                        if (recipeOne.getAlcoholType() == null) {
                            recipeOne.setAlcoholType("");
                        }
                        if (recipeOne.getEngTitle() == null) {
                            recipeOne.setEngTitle("");
                        }
                        if (recipeOne.getIntro() == null) {
                            recipeOne.setIntro("");
                        }

                        // 닉네임 뿌려주기
                        if (recipeOne.getUserId() == 1) {
                            txtUserId.setText("주인장의시크릿");
                        } else {
                            txtUserId.setText(recipeOne.getNickname());
                        }
                        // 도수 뿌려주기
                        if (recipeOne.getPercent() == 1) {
                            txtPercent.setText("약");
                        } else if (recipeOne.getPercent() == 2) {
                            txtPercent.setText("중");
                        } else if (recipeOne.getPercent() == 3) {
                            txtPercent.setText("강");
                        } else {
                            txtPercent.setText("비밀");
                        }


                        txtRecipeTitle.setText(recipeOne.getTitle()+"");
                        txtLikeCnt.setText(recipeOne.getLikeCnt()+"");
                        txtAlcoholType.setText(recipeOne.getAlcoholType()+"");
                        txtEngTitle.setText(recipeOne.getEngTitle()+"");
                        txtIntro.setText(recipeOne.getIntro()+"");
                        txtContent.setText(recipeOne.getContent()+"");
                        txtIngredient.setText(recipeOne.getIngredient()+ "");

                        likeCnt = recipeOne.getLikeCnt();


                    }
                    catch (Exception e) {
                        e.printStackTrace();


                    }

                }
            }

            @Override
            public void onFailure(Call<RecipeOneResponse> call, Throwable t) {
                Log.i("레시피 정보", t.getMessage());
            }
        });

    }

}