package com.leopard4.alcoholrecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leopard4.alcoholrecipe.adapter.RecipeFavoriteAdapter;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.api.RecipeApi;
import com.leopard4.alcoholrecipe.api.RecipeFavoriteApi;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.Recipe;
import com.leopard4.alcoholrecipe.model.RecipeFavoriteList;
import com.leopard4.alcoholrecipe.model.RecipeOne;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecipeInfoActivity extends AppCompatActivity {

    ImageView imgUrl;
    TextView txtLikeCnt, txtPercent, txtAlcoholType, txtUserId, txtEngTitle, txtIntro, txtContent, txtIngredient;
    private String accessToken;
    private int recipeId;
    private ArrayList<Recipe> recipeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);

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


    }
    private void getNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        RecipeApi api = retrofit.create(RecipeApi.class);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴

        Call<RecipeOne> call = api.getRecipeOne(accessToken, recipeId);

        call.enqueue(new Callback<RecipeOne>() {
            @Override
            public void onResponse(Call<RecipeOne> call, Response<RecipeOne> response) {
                if (response.isSuccessful()) {
                    recipeList.addAll(response.body().getRecipeOne());

                }
            }


            @Override
            public void onFailure(Call<RecipeOne> call, Throwable t) {
                Log.i("레시피 정보", t.getMessage());
            }
        });

    }

}