package com.leopard4.alcoholrecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leopard4.alcoholrecipe.adapter.AlcoholAdapter;
import com.leopard4.alcoholrecipe.adapter.IngredientAdapter;
import com.leopard4.alcoholrecipe.api.CreatingApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.alcohol.Alcohol;
import com.leopard4.alcoholrecipe.model.alcohol.AlcoholList;
import com.leopard4.alcoholrecipe.model.ingredient.Ingredient;
import com.leopard4.alcoholrecipe.model.ingredient.IngredientList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyRecipeWriteSecondActivity extends AppCompatActivity {

    // 알콜 부분 //
    // xml 파일에서는 현재 textview 로 되어있기 때문에
    // 코드 작성할 때 xml 을 recyclerView 로 수정하고 작성해야함
    RecyclerView alcoholListRecyclerView;
    TextView txtAlcoholCount;
    EditText editSearchAlcohol;
    ImageButton imgSearchAlcohol;

    // 리사이클러뷰 관련
    RecyclerView alcoholRecyclerView;
    AlcoholAdapter alcoholAdapter;
    ArrayList<Alcohol> alcoholList = new ArrayList<>();



    // 부재료 부분 //
    TextView txtIngreList;
    TextView txtIngreCount;
    EditText editSearchIngre;
    ImageButton imgSearchIngre;

    // 리사이클러뷰 관련
    RecyclerView ingreRecyclerView;
    IngredientAdapter ingredientAdapter;
    ArrayList<Ingredient> ingredientList = new ArrayList<>();


    // 체크박스
    CheckBox checkBox;

    // 저장 버튼
    Button btnSave;



    // 페이징 처리를 위한 변수
    int count = 0;
    int offset = 0;
    int limit = 5;

    private Alcohol selectedAlcohol;

    String alcoholKeyword;
    String ingreKeyword;

    private boolean isloading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe_write_second);

        // 알콜 부분
//        alcoholListRecyclerView = findViewById(R.id.alcoholListRecyclerView);
        txtAlcoholCount = findViewById(R.id.txtAlcoholCount);
        editSearchAlcohol = findViewById(R.id.editSearchAlcohol);
        imgSearchAlcohol = findViewById(R.id.imgSearchAlcohol);

        alcoholRecyclerView = findViewById(R.id.alcoholRecyclerView);
        alcoholRecyclerView.setHasFixedSize(true);
        alcoholRecyclerView.setLayoutManager(new LinearLayoutManager(MyRecipeWriteSecondActivity.this));


        alcoholRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 맨 마지막 데이터가 화면에 보이면!!
                // 네트워크 통해서 데이터를 추가로 받아와라!!
                int lastPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                // 스크롤을 데이터 맨 끝까지 한 상태
                if(lastPosition + 1 == totalCount && !isloading ){
                    // 네트워크 통해서 데이터를 받아오고, 화면에 표시!
                    isloading = true;
                    addAlcoholNetworkData();



                }


            }
        });

        // 재료부분
        txtIngreList = findViewById(R.id.txtIngreList);
        txtIngreCount = findViewById(R.id.txtIngreCount);
        editSearchIngre = findViewById(R.id.editSearchIngre);
        imgSearchIngre = findViewById(R.id.imgSearchIngre);

        ingreRecyclerView = findViewById(R.id.ingreRecyclerView);
        ingreRecyclerView.setHasFixedSize(true);
        ingreRecyclerView.setLayoutManager(new LinearLayoutManager(MyRecipeWriteSecondActivity.this));


        // 체크박스
        checkBox = findViewById(R.id.checkBox);

        // 저장
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyRecipeWriteSecondActivity.this, RecipeInfoActivity.class);
                startActivity(intent);
            }
        });



        imgSearchAlcohol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alcoholKeyword = editSearchAlcohol.getText().toString().trim();

                if (alcoholKeyword.isEmpty()){
                    return;
                }

                getAlcoholNetworkData();

            }
        });

        imgSearchIngre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ingreKeyword = editSearchIngre.getText().toString().trim();

                if (alcoholKeyword.isEmpty()){
                    return;
                }

                getIngreNetworkData();
            }
        });



    }

    private void addAlcoholNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(MyRecipeWriteSecondActivity.this);

        CreatingApi api = retrofit.create(CreatingApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");



        Call<AlcoholList> call = api.getAlcoholList(accessToken, alcoholKeyword ,offset, limit);
        call.enqueue(new Callback<AlcoholList>() {
            @Override
            public void onResponse(Call<AlcoholList> call, Response<AlcoholList> response) {

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.
//                alcoholList.clear();


                if (response.isSuccessful()) {
                    offset = offset + count;
//                    alcoholList.clear();

//                    count = response.body().getCount();
//
//                    offset = offset + count;

                    alcoholList.addAll(response.body().getResult());

//                    alcoholAdapter = new AlcoholAdapter(MyRecipeWriteSecondActivity.this, alcoholList);
//                    alcoholRecyclerView.setAdapter(alcoholAdapter);
                    alcoholAdapter.notifyDataSetChanged();
                    isloading = false;


                }
            }

            @Override
            public void onFailure(Call<AlcoholList> call, Throwable t) {

            }
        });


    }

    private void addIngreNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(MyRecipeWriteSecondActivity.this);

        CreatingApi api = retrofit.create(CreatingApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");



        Call<IngredientList> call = api.getIngrelList(accessToken, ingreKeyword ,offset, limit);
        call.enqueue(new Callback<IngredientList>() {
            @Override
            public void onResponse(Call<IngredientList> call, Response<IngredientList> response) {

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.
//                alcoholList.clear();


                if (response.isSuccessful()) {
                    offset = offset + count;
//                    alcoholList.clear();

//                    count = response.body().getCount();
//
//                    offset = offset + count;

                    ingredientList.addAll(response.body().getResult());

//                    alcoholAdapter = new AlcoholAdapter(MyRecipeWriteSecondActivity.this, alcoholList);
//                    alcoholRecyclerView.setAdapter(alcoholAdapter);
                    ingredientAdapter.notifyDataSetChanged();
                    isloading = false;


                }
            }

            @Override
            public void onFailure(Call<IngredientList> call, Throwable t) {

            }
        });


    }



    @Override
    protected void onResume() {
        super.onResume();
        getAlcoholNetworkData();
        getIngreNetworkData();
    }

    void getAlcoholNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(MyRecipeWriteSecondActivity.this);

        CreatingApi api = retrofit.create(CreatingApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;
        count = 0;

        Call<AlcoholList> call = api.getAlcoholList(accessToken, alcoholKeyword ,offset, limit);
        call.enqueue(new Callback<AlcoholList>() {
            @Override
            public void onResponse(Call<AlcoholList> call, Response<AlcoholList> response) {

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.
                alcoholList.clear();


                if (response.isSuccessful()){

//                    alcoholList.clear();

                    count = response.body().getCount();

                    offset = offset + count;

                    alcoholList.addAll(response.body().getResult());

                    alcoholAdapter = new AlcoholAdapter(MyRecipeWriteSecondActivity.this, alcoholList);
                    alcoholRecyclerView.setAdapter(alcoholAdapter);

//                    offset = offset + count;

                }else {

                }

            }

            @Override
            public void onFailure(Call<AlcoholList> call, Throwable t) {

            }
        });

    }

    void getIngreNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(MyRecipeWriteSecondActivity.this);

        CreatingApi api = retrofit.create(CreatingApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;
        count = 0;

        Call<IngredientList> call = api.getIngrelList(accessToken, ingreKeyword ,offset, limit);
        call.enqueue(new Callback<IngredientList>() {
            @Override
            public void onResponse(Call<IngredientList> call, Response<IngredientList> response) {

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.
//                alcoholList.clear();


                if (response.isSuccessful()){

//                    alcoholList.clear();

                    count = response.body().getCount();

                    offset = offset + count;

                    ingredientList.addAll(response.body().getResult());

                    ingredientAdapter = new IngredientAdapter(MyRecipeWriteSecondActivity.this, ingredientList);
                    ingreRecyclerView.setAdapter(ingredientAdapter);

                }else {

                }

            }

            @Override
            public void onFailure(Call<IngredientList> call, Throwable t) {

            }


        });

    }
}