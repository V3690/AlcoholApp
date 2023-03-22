package com.leopard4.alcoholrecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    TextView txtRecipeTitle, txtLikeCnt, txtPercent, txtAlcoholType, txtUserId, txtEngTitle, txtIntro, txtContent, txtIngredient;
    Button btnReturnRecipe, btnEdit;
    ImageView imgBack;
    private String accessToken;
    private int recipeId;
    private int userId;
    private int getAdapterUserId;
    private ArrayList<RecipeOne> recipeOneList = new ArrayList<>();

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
        btnEdit = findViewById(R.id.btnEdit);


        recipeId = getIntent().getIntExtra("recipeId", 0);
        Log.i("레시피 아이디", Integer.toString(recipeId));

        getNetworkData();


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

                    RecipeOne recipeOne = new RecipeOne();
//                    recipeOneList.addAll(response.body().getRecipeOne());

                    recipeOne = response.body().getRecipeOne();


                    // 레시피를 작성한 사람이라면 수정버튼을 보여주기 위해서
                    // myrecipeAdapter 에서 받은 userId
                    getAdapterUserId = getIntent().getIntExtra("userId", 0);
//                    Log.i("접속한유저ID", getAdapterUserId+"");
                    userId = recipeOne.getUserId();
//                    Log.i("유저ID테스트", userId+"");
                    if (userId == getAdapterUserId){
                        btnEdit.setVisibility(View.VISIBLE);
                    }


                    // glide로 이미지 뿌려주기
                    Glide.with(RecipeInfoActivity.this)
                            .load(recipeOne.getImgUrl())
                            .into(imgUrl);

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

                    // todo: 나중에 지워야댐 근데 레시피 db에는 주종이라는게 없는데 ??
                    // 답변 : 주종 제외시키기로 해놓고 기획 수정을 안했네요...
                    // 오븐에 화면 수정된거 반영했습니다. (3/21)
                    Log.i("레시피 정보1", response.body().toString());
                    Log.i("레시피 정보2", recipeOne.toString()+"");
                    Log.i("레시피 정보3", recipeOne.getImgUrl()+"");
                    Log.i("레시피 정보4", recipeOne.getTitle()+"");
                    Log.i("레시피 정보5", recipeOne.getLikeCnt()+"");
                    Log.i("레시피 정보6", recipeOne.getPercent()+"");
                    Log.i("레시피 정보7", recipeOne.getAlcoholType()+"");
                    Log.i("레시피 정보8", recipeOne.getUserId()+"");
                    Log.i("레시피 정보9", recipeOne.getEngTitle()+"");
                    Log.i("레시피 정보10", recipeOne.getIntro()+"");
                    Log.i("레시피 정보11", recipeOne.getContent()+"");
                    Log.i("레시피 정보12", recipeOne.getIngredient()+ "");
                    Log.i("레시피 정보13", recipeOne.getNickname()+ "");


//                    imgUrl.setImageResource(Integer.parseInt(recipeOne.getImgUrl()));
                    txtRecipeTitle.setText(recipeOne.getTitle()+"");
                    txtLikeCnt.setText(recipeOne.getLikeCnt()+"");
                    txtAlcoholType.setText(recipeOne.getAlcoholType()+"");

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