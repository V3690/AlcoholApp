package com.leopard4.alcoholrecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.api.RecipeApi;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.RecipeOne;
import com.leopard4.alcoholrecipe.model.RecipeOneResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecipeInfoActivity extends AppCompatActivity {

    ImageView imgUrl;
    TextView txtTitle, txtLikeCnt, txtPercent, txtAlcoholType, txtUserId, txtEngTitle, txtIntro, txtContent, txtIngredient;
    Button btnReturnRecipe;
    ImageView imgBack;
    private String accessToken;
    private int recipeId;
    private ArrayList<RecipeOne> recipeOneList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);


        imgBack = findViewById(R.id.imgBack);
        btnReturnRecipe = findViewById(R.id.btnReturnRecipe);
        txtTitle = findViewById(R.id.txtTitle);
        imgUrl = findViewById(R.id.imgUrl);
        txtLikeCnt = findViewById(R.id.txtLikeCnt);
        txtPercent = findViewById(R.id.txtPercent);
        txtAlcoholType = findViewById(R.id.txtAlcoholType);
        txtUserId = findViewById(R.id.txtUserId);
        txtEngTitle = findViewById(R.id.txtEngTitle);
        txtIntro = findViewById(R.id.txtIntro);
        txtContent = findViewById(R.id.txtContent);
        txtIngredient = findViewById(R.id.txtIngredient);


        recipeId = getIntent().getIntExtra("recipeId", 0);
        Log.i("레시피 아이디", Integer.toString(recipeId));

        getNetworkData();

        // 뒤로가기 버튼
        imgBack.setOnClickListener(v -> {
            finish();
        });

        // 레시피 목록으로 돌아가기 레시피가 뭔지모르겟어 일단 피니쉬
        btnReturnRecipe.setOnClickListener(v -> {
            finish();
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
                    Log.i("레시피 정보", response.body().toString());
                    RecipeOne recipeOne = new RecipeOne();
//                    recipeOneList.addAll(response.body().getRecipeOne());

                    recipeOne = response.body().getRecipeOne();

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
                    if (recipeOne.getContent() == null) {
                        recipeOne.setContent("");
                    }
                    if (recipeOne.getIngredient() == null) {
                        recipeOne.setIngredient("");
                    }
                    // 해당 값들의 의미를 한글로 바꿔서 화면에 보여준다
                    if (recipeOne.getUserId() == 1) {
                        txtUserId.setText("주인장의시크릿");
                    } else {
                        recipeOne.setUserId("일반 사용자");
                    }

                    // todo: 나중에 지워야댐
                    Log.i("레시피 정보", recipeOne.toString()+"");
                    Log.i("레시피 정보", recipeOne.getImgUrl()+"");
                    Log.i("레시피 정보", recipeOne.getLikeCnt()+"");
                    Log.i("레시피 정보", recipeOne.getPercent()+"");
                    Log.i("레시피 정보", recipeOne.getAlcoholType()+"");
                    Log.i("레시피 정보", recipeOne.getUserId()+"");
                    Log.i("레시피 정보", recipeOne.getEngTitle()+"");
                    Log.i("레시피 정보", recipeOne.getIntro()+"");
                    Log.i("레시피 정보", recipeOne.getContent()+"");
                    Log.i("레시피 정보", recipeOne.getIngredient()+ "");

//                    imgUrl.setImageResource(Integer.parseInt(recipeOne.getImgUrl()));
                    txtTitle.setText(recipeOne.getTitle()+"");
                    txtLikeCnt.setText(recipeOne.getLikeCnt()+"");
                    txtPercent.setText(recipeOne.getPercent()+"");
                    txtAlcoholType.setText(recipeOne.getAlcoholType()+"");

                    txtEngTitle.setText(recipeOne.getEngTitle()+"");
                    txtIntro.setText(recipeOne.getIntro()+"");
                    txtContent.setText(recipeOne.getContent()+"");
                    txtIngredient.setText(recipeOne.getIngredient()+ "");

                }
            }


            @Override
            public void onFailure(Call<RecipeOneResponse> call, Throwable t) {
                Log.i("레시피 정보", t.getMessage());
            }
        });

    }

}