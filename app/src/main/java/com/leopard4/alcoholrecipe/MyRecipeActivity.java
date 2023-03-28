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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.leopard4.alcoholrecipe.adapter.DogamAdapter;
import com.leopard4.alcoholrecipe.adapter.MyRecipeAdapter;
import com.leopard4.alcoholrecipe.api.DogamApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.api.RecipeApi;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.dogam.Dogam;
import com.leopard4.alcoholrecipe.model.dogam.DogamList;
import com.leopard4.alcoholrecipe.model.myRecipe.MyRecipe;
import com.leopard4.alcoholrecipe.model.myRecipe.MyRecipeRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyRecipeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FloatingActionButton fab;

    EditText editSearch;
    ImageView imgSearch;

    RecyclerView recyclerView;
    MyRecipeAdapter myRecipeAdapter;
    ArrayList<MyRecipe> myRecipeList = new ArrayList<>();

    // 스피너 관련
    Spinner spinner;
    ArrayAdapter<CharSequence> adapterSpinner = null;


    // 페이징 처리를 위한 변수
    int count = 0;
    int offset = 0;
    int limit = 25;

    private boolean isloading = false;

    int spinnerSelected = -1;

    String keyword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);

        fab = findViewById(R.id.fab);

        editSearch = findViewById(R.id.editSearch);
        imgSearch = findViewById(R.id.imgSearch);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyRecipeActivity.this));

        spinner = findViewById(R.id.spinner);

        // 스피너 처리 관련 코드
        adapterSpinner = ArrayAdapter.createFromResource(this, R.array.myRecipe, R.layout.layout_spinner);
        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapterSpinner);

        spinner.setOnItemSelectedListener(MyRecipeActivity.this);

        // 레시피 검색
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword = editSearch.getText().toString().trim();

                if (keyword.isEmpty()){
                    return;
                }

                getKeywordNetworkData();

            }
        });



        // 레시피 게시글 작성 페이지로 이동
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyRecipeActivity.this, MyRecipeWriteFirstActivity.class);
                startActivity(intent);
            }
        });


    }

    void getNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(MyRecipeActivity.this);

        RecipeApi api = retrofit.create(RecipeApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;
        count = 0;

        Call<MyRecipeRes> call = api.getMyRecipeList(accessToken, offset, limit);
        call.enqueue(new Callback<MyRecipeRes>() {
            @Override
            public void onResponse(Call<MyRecipeRes> call, Response<MyRecipeRes> response) {

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.
                myRecipeList.clear();


                if (response.isSuccessful()) {

                    count = response.body().getCount();

                    offset = offset + count;

                    myRecipeList.addAll(response.body().getItems());

                    myRecipeAdapter = new MyRecipeAdapter(MyRecipeActivity.this, myRecipeList);
                    recyclerView.setAdapter(myRecipeAdapter);

                    // 스크롤 내렸을 때 데이터 추가로 가져오는 코드
                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            // 맨 마지막 데이터가 화면에 보이면!!
                            // 네트워크 통해서 데이터를 추가로 받아와라!!
                            int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                            int totalCount = recyclerView.getAdapter().getItemCount();

                            // 스크롤을 데이터 맨 끝까지 한 상태
                            if (lastPosition + 1 == totalCount && !isloading) {
                                // 네트워크 통해서 데이터를 받아오고, 화면에 표시!
                                isloading = true;
                                addNetworkData();
                            }
                        }
                    });


                } else {

                }

            }

            @Override
            public void onFailure(Call<MyRecipeRes> call, Throwable t) {

            }
        });
    }


    private void addNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(MyRecipeActivity.this);

        RecipeApi api = retrofit.create(RecipeApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        Call<MyRecipeRes> call = api.getMyRecipeList(accessToken, offset, limit);
        call.enqueue(new Callback<MyRecipeRes>() {
            @Override
            public void onResponse(Call<MyRecipeRes> call, Response<MyRecipeRes> response) {


                if (response.isSuccessful()) {
                    offset = offset + count;


                    myRecipeList.addAll(response.body().getItems());

//                    alcoholAdapter = new AlcoholAdapter(MyRecipeWriteSecondActivity.this, alcoholList);
//                    alcoholRecyclerView.setAdapter(alcoholAdapter);
                    myRecipeAdapter.notifyDataSetChanged();
                    isloading = false;


                }
            }

            @Override
            public void onFailure(Call<MyRecipeRes> call, Throwable t) {

            }
        });


    }


    void getPercentNetworkData(){

        Retrofit retrofit = NetworkClient.getRetrofitClient(MyRecipeActivity.this);

        RecipeApi api = retrofit.create(RecipeApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;
        count = 0;

        Call<MyRecipeRes> call = api.getMyRecipeOrderList(accessToken,spinnerSelected ,offset, limit);
        call.enqueue(new Callback<MyRecipeRes>() {
            @Override
            public void onResponse(Call<MyRecipeRes> call, Response<MyRecipeRes> response) {

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.
                myRecipeList.clear();


                if (response.isSuccessful()) {

                    count = response.body().getCount();

                    offset = offset + count;

                    myRecipeList.addAll(response.body().getItems());

                    myRecipeAdapter = new MyRecipeAdapter(MyRecipeActivity.this, myRecipeList);
                    recyclerView.setAdapter(myRecipeAdapter);

                    // 스크롤 내렸을 때 데이터 추가로 가져오는 코드
                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            // 맨 마지막 데이터가 화면에 보이면!!
                            // 네트워크 통해서 데이터를 추가로 받아와라!!
                            int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                            int totalCount = recyclerView.getAdapter().getItemCount();

                            // 스크롤을 데이터 맨 끝까지 한 상태
                            if (lastPosition + 1 == totalCount && !isloading) {
                                // 네트워크 통해서 데이터를 받아오고, 화면에 표시!
                                isloading = true;
                                addPercentNetworkData();
                            }
                        }
                    });


                } else {

                }

            }

            @Override
            public void onFailure(Call<MyRecipeRes> call, Throwable t) {

            }
        });
    }


    private void addPercentNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(MyRecipeActivity.this);

        RecipeApi api = retrofit.create(RecipeApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<MyRecipeRes> call = api.getMyRecipeOrderList(accessToken,spinnerSelected ,offset, limit);
        call.enqueue(new Callback<MyRecipeRes>() {
            @Override
            public void onResponse(Call<MyRecipeRes> call, Response<MyRecipeRes> response) {


                if (response.isSuccessful()) {
                    offset = offset + count;
                    myRecipeList.addAll(response.body().getItems());
                    myRecipeAdapter.notifyDataSetChanged();
                    isloading = false;


                }
            }

            @Override
            public void onFailure(Call<MyRecipeRes> call, Throwable t) {

            }
        });


    }


    void getKeywordNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(MyRecipeActivity.this);

        RecipeApi api = retrofit.create(RecipeApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;
        count = 0;

        Call<MyRecipeRes> call = api.getMyRecipeKeyword(accessToken, keyword, offset, limit);
        call.enqueue(new Callback<MyRecipeRes>() {
            @Override
            public void onResponse(Call<MyRecipeRes> call, Response<MyRecipeRes> response) {

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.
                myRecipeList.clear();


                if (response.isSuccessful()) {

                    count = response.body().getCount();

                    offset = offset + count;

                    myRecipeList.addAll(response.body().getItems());

                    myRecipeAdapter = new MyRecipeAdapter(MyRecipeActivity.this, myRecipeList);
                    recyclerView.setAdapter(myRecipeAdapter);

                    // 스크롤 내렸을 때 데이터 추가로 가져오는 코드
                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            // 맨 마지막 데이터가 화면에 보이면!!
                            // 네트워크 통해서 데이터를 추가로 받아와라!!
                            int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                            int totalCount = recyclerView.getAdapter().getItemCount();

                            // 스크롤을 데이터 맨 끝까지 한 상태
                            if (lastPosition + 1 == totalCount && !isloading) {
                                // 네트워크 통해서 데이터를 받아오고, 화면에 표시!
                                isloading = true;
                                addKeywordNetworkData();
                            }
                        }
                    });


                } else {

                }

            }

            @Override
            public void onFailure(Call<MyRecipeRes> call, Throwable t) {

            }
        });
    }

    private void addKeywordNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(MyRecipeActivity.this);

        RecipeApi api = retrofit.create(RecipeApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        Call<MyRecipeRes> call = api.getMyRecipeKeyword(accessToken,keyword ,offset, limit);
        call.enqueue(new Callback<MyRecipeRes>() {
            @Override
            public void onResponse(Call<MyRecipeRes> call, Response<MyRecipeRes> response) {


                if (response.isSuccessful()) {
                    offset = offset + count;


                    myRecipeList.addAll(response.body().getItems());

//                    alcoholAdapter = new AlcoholAdapter(MyRecipeWriteSecondActivity.this, alcoholList);
//                    alcoholRecyclerView.setAdapter(alcoholAdapter);
                    myRecipeAdapter.notifyDataSetChanged();
                    isloading = false;


                }
            }

            @Override
            public void onFailure(Call<MyRecipeRes> call, Throwable t) {

            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        spinnerSelected = -1;

        if (i == 0) {
            spinnerSelected = 0;
            getNetworkData();
        } else if (i==1) {
            spinnerSelected = 1;
            getPercentNetworkData();

        } else if (i==2) {
            spinnerSelected = 2;
            getPercentNetworkData();

        } else if (i==3) {
            spinnerSelected = 3;
            getPercentNetworkData();

        } else if (i==4) {
            spinnerSelected = 4;
            getPercentNetworkData();

        }
        Log.i("spinnerSelected", i+" " + spinnerSelected);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    // onResume
    @Override
    protected void onResume() {
        super.onResume();

    }

}