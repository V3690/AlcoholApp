package com.leopard4.alcoholrecipe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.leopard4.alcoholrecipe.adapter.RecipeLabAdapter;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.api.RecipeApi;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.recipe.RecipeLab;
import com.leopard4.alcoholrecipe.model.recipe.RecipeLabList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RecipeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    FloatingActionButton fab;
    ImageButton imgSearch;
    ImageView imgBack;
    EditText editSearch;
    TextView txtWarning;
    ToggleButton toggleButton , toggleButton2;
    Spinner spinnerRecipe , spinnerRecipe2;
    RecyclerView recyclerView;
    RecipeLabAdapter adapter;
    ArrayList<RecipeLab> RecipeLabList = new ArrayList<>();
    private boolean isloading = false;
    ArrayAdapter<CharSequence> adapterOrder = null;
    ArrayAdapter<CharSequence> adapterPercent = null;

    // 페이징 처리를 위한 변수
    int count = 0;
    int offset = 0;
    int limit = 30;

    int percent;
    String order;

    String keyword;
    int spinner2Id;


    boolean isToggleButton1Checked = true;
    boolean isToggleButton2Checked =false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        fab = findViewById(R.id.fab);

        imgBack=findViewById(R.id.imgBack);
        imgSearch=findViewById(R.id.imgSearch);
        editSearch=findViewById(R.id.editSearch);
        txtWarning=findViewById(R.id.txtWarning);
        toggleButton=findViewById(R.id.toggleButton);
        toggleButton2=findViewById(R.id.toggleButton2);
        spinnerRecipe=findViewById(R.id.spinner);
        spinnerRecipe2=findViewById(R.id.spinnerRecipe2);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(RecipeActivity.this));

        toggleButton.setChecked(true);



        adapterPercent = ArrayAdapter.createFromResource(this,R.array.percent , R.layout.layout_spinner);;
        adapterOrder = ArrayAdapter.createFromResource(this,R.array.order , R.layout.layout_spinner);;

        spinnerRecipe.setOnItemSelectedListener(RecipeActivity.this);
        spinnerRecipe2.setOnItemSelectedListener(RecipeActivity.this);

        spinnerRecipe.setAdapter(adapterPercent);
        spinnerRecipe2.setAdapter(adapterOrder);


        // 뒤로가기 버튼
        imgBack.setOnClickListener(v -> {
            finish();
        });

        // 레시피 게시글 작성 페이지로 이동
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeActivity.this, MyRecipeWriteFirstActivity.class);
                startActivity(intent);
            }
        });


        // 시간 남으면 하자....
        // todo : 토글 버튼 색깔 작업
        //  버튼 OFF : #806A6A6A
        //  버튼 ON : #8C404040


        //0번째 바로 주인장레시피
        //1번째 주인장레시피
        //2번째 유저레시피
        //3번째 모두레시피
        // n~번 업는경우: 스피너 no조작 n-1번: 스피너 도수조작 n-2 : 스피너 목록 조작

        // 들어가자마자 레시피보여주는 조건
        if(isToggleButton1Checked){
            recyclerView.setVisibility(View.VISIBLE);
            toggleButton.setTextColor(Color.parseColor("#FFC107"));
            toggleButton2.setTextColor(Color.WHITE);
            percent=0;
            order="cnt";
            getNetworkData();
            Log.i(TAG,"0번째 조건입니다 1번 "+isToggleButton1Checked+" 2번 "+isToggleButton2Checked);

            toggle1spinner();
        }


        //이후 주인장레시피 눌렀을때
       toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // 주인장이 눌린 상황에서
                isToggleButton1Checked = isChecked;
                //주인장 하고 유저가 같은상태가 아니며 유저는 꺼진경우(주인장만 켜진경우)
                if(isToggleButton1Checked != isToggleButton2Checked && isToggleButton2Checked==false){

                    toggleButton.setTextColor(Color.parseColor("#FFC107"));
                    toggleButton2.setTextColor(Color.WHITE);
                    recyclerView.setVisibility(View.VISIBLE);
                    percent=0;
                    order="cnt";

                    //주인장 레시피 api
                    getNetworkData();
                    Log.i(TAG,"1번째 조건입니다 1번 "+isToggleButton1Checked+"2번 "+isToggleButton2Checked);
                    //주인장만 눌렸을때 스피너조작
                    toggle1spinner();

                //주인장하고 유저가 같은 상태이며 유저는 켜져있는경우(둘다켜진경우)
                }else if(isToggleButton1Checked == isToggleButton2Checked && isToggleButton2Checked==true){
                    toggleButton.setTextColor(Color.parseColor("#FFC107"));
                    toggleButton2.setTextColor(Color.parseColor("#FFC107"));
                    recyclerView.setVisibility(View.VISIBLE);
                    percent=0;
                    order="cnt";
                    //모든 레시피 불러오는 api
                    getNetworkData3();

                    Log.i(TAG,"3번째 조건입니다 1번 "+isToggleButton1Checked+"2번 "+isToggleButton2Checked);

                    //모든 레시피 상태에서 스피너조작
                    toggle3spinner();

                }
                //유저만 켜진상황
                else if(isToggleButton1Checked != isToggleButton2Checked && isToggleButton2Checked==true){
                    toggleButton.setTextColor(Color.WHITE);
                    toggleButton2.setTextColor(Color.WHITE);
                    recyclerView.setVisibility(View.GONE);
                    txtWarning.setVisibility(View.VISIBLE);
                    if(isToggleButton2Checked){
                        toggleButton.setTextColor(Color.WHITE);
                        toggleButton2.setTextColor(Color.parseColor("#FFC107"));
                        recyclerView.setVisibility(View.VISIBLE);
                        percent=0;
                        order="cnt";
                        //유저 목록만 가저오는 api
                        getNetworkData2();
                        Log.i(TAG,"1-3번째 조건입니다 1번 "+isToggleButton1Checked+"2번 "+isToggleButton2Checked);
                        // 유저 목록 켜진 상황에서 가저오는 스피너
                        toggle2spinner();

                    }
                }

            }
        });

        // 유저 눌렀을때
        toggleButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                //유저 버튼이 켜짐
                isToggleButton2Checked = isChecked;
                //주인장과 유저버튼이 같은상태가 아니며 유저버튼이 켜진경우(유저만)
                if(isToggleButton1Checked != isToggleButton2Checked && isToggleButton2Checked==true ){
                    toggleButton.setTextColor(Color.WHITE);
                    toggleButton2.setTextColor(Color.parseColor("#FFC107"));
                    recyclerView.setVisibility(View.VISIBLE);
                    percent=0;
                    order="cnt";
                    //유저레시피 가저오는api
                    getNetworkData2();
                    Log.i(TAG,"2번째 조건입니다 1번 "+isToggleButton1Checked+"2번 "+isToggleButton2Checked);

                    //유저상황에서 스피너
                    toggle2spinner();

                    //주인장과 유저버튼이 같은상황이며 유저는 켜진경우(둘다켜짐)
                }else if(isToggleButton1Checked == isToggleButton2Checked && isToggleButton2Checked==true ){
                    toggleButton.setTextColor(Color.parseColor("#FFC107"));
                    toggleButton2.setTextColor(Color.parseColor("#FFC107"));
                    recyclerView.setVisibility(View.VISIBLE);
                    percent=0;
                    order="cnt";
                    //둘다켜진 api
                    getNetworkData3();

                    Log.i(TAG,"3번째 조건입니다"+isToggleButton2Checked+isToggleButton1Checked);
                    //둘다켜졌을때 스피너
                    toggle3spinner();

                }//주인장과 유저가 같지않으며 유저는 켜저있지않음(주인장만)
                else if(isToggleButton1Checked != isToggleButton2Checked && isToggleButton2Checked==false){
                    toggleButton.setTextColor(Color.parseColor("#FFC107"));
                    toggleButton2.setTextColor(Color.WHITE);
                    recyclerView.setVisibility(View.GONE);
                    txtWarning.setVisibility(View.VISIBLE);
                    if(isToggleButton1Checked){
                        toggleButton.setTextColor(Color.parseColor("#FFC107"));
                        recyclerView.setVisibility(View.VISIBLE);
                        percent=0;
                        order="cnt";
                        //
                        //주인장api
                        getNetworkData();
                        Log.i(TAG,"2-3번째 조건입니다 1번 "+isToggleButton1Checked+"2번 "+isToggleButton2Checked);
                        //주인장 스피너
                        toggle1spinner();
                    }
                }

            }
        });


        //검색
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword = editSearch.getText().toString().trim();

                if (keyword.isEmpty()){
                    Toast.makeText(RecipeActivity.this, "검색어를 입력해 주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                searchKeyword();

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                        int totalCount = recyclerView.getAdapter().getItemCount();
                        if (lastPosition + 1 == totalCount && !isloading) {
                            // 네트워크 통해서 데이터를 받아오고, 화면에 표시!
                            isloading = true;
                            addSearchKeyword();


                        }

                    }
                });


            }
        });


    }

    //유저가눌린상태에서 스피너
    private void toggle2spinner() {
        spinnerRecipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                percent = spinnerRecipe.getSelectedItemPosition();
                Log.i("SPINNERTEST", percent + "");

                getNetworkData2();
                Log.i(TAG,"2-1번째 조건입니다 1번 "+isToggleButton1Checked+"2번 "+isToggleButton2Checked);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinnerRecipe2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner2Id = spinnerRecipe2.getSelectedItemPosition();
                Log.i("SPINNERTEST", percent + "");

                if (spinner2Id == 0){
                    order = "cnt";
                } else if (spinner2Id == 1) {
                    order = "cnt";
                } else if (spinner2Id == 2) {
                    order = "createdAt";
                } else if (spinner2Id == 3) {
                    order = "title";
                }
                getNetworkData2();
                Log.i(TAG,"2-2번째 조건입니다 1번 "+isToggleButton1Checked+"2번 "+isToggleButton2Checked);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //주인장&유저가눌린상태에서 스피너
    private void toggle3spinner() {
        spinnerRecipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                percent = spinnerRecipe.getSelectedItemPosition();
                Log.i("SPINNERTEST", percent + "");

                getNetworkData3();
                Log.i(TAG,"3-1번째 조건입니다 1번 "+isToggleButton1Checked+"2번 "+isToggleButton2Checked);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinnerRecipe2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner2Id = spinnerRecipe2.getSelectedItemPosition();
                Log.i("SPINNERTEST", percent + "");

                if (spinner2Id == 0){
                    order = "cnt";
                } else if (spinner2Id == 1) {
                    order = "cnt";
                } else if (spinner2Id == 2) {
                    order = "createdAt";
                } else if (spinner2Id == 3) {
                    order = "title";
                }
                getNetworkData3();
                Log.i(TAG,"3-2번째 조건입니다 1번 "+isToggleButton1Checked+"2번 "+isToggleButton2Checked);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    //주인장만 눌린 스피너
    private void toggle1spinner() {
        spinnerRecipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                percent = spinnerRecipe.getSelectedItemPosition();
                Log.i("SPINNERTEST", percent + "");

                getNetworkData();
                Log.i(TAG,"1-1번째 조건입니다 1번 "+isToggleButton1Checked+"2번 "+isToggleButton2Checked);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerRecipe2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner2Id = spinnerRecipe2.getSelectedItemPosition();
                Log.i("SPINNERTEST", percent + "");

                if (spinner2Id == 0){
                    order = "cnt";
                } else if (spinner2Id == 1) {
                    order = "cnt";
                } else if (spinner2Id == 2) {
                    order = "createdAt";
                } else if (spinner2Id == 3) {
                    order = "title";
                }
                getNetworkData();
                Log.i(TAG,"1-2번째 조건입니다 1번 "+isToggleButton1Checked+"2번 "+isToggleButton2Checked);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    //키워드 검색api
    private void searchKeyword() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(RecipeActivity.this);
        RecipeApi api = retrofit.create(RecipeApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;
        count = 0;

        Call<RecipeLabList> call = api.getRecipeKeyword(accessToken, keyword, offset, limit);
        call.enqueue(new Callback<RecipeLabList>() {
            @Override
            public void onResponse(Call<RecipeLabList> call, Response<RecipeLabList> response) {

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.
                RecipeLabList.clear();


                if (response.isSuccessful()) {

                    count = response.body().getCount();

                    offset = offset + count;

                    RecipeLabList.addAll(response.body().getItems());
                    if(response.body().getCount() < 1){
                        Toast.makeText(RecipeActivity.this, "없는 레시피 입니다.", Toast.LENGTH_SHORT).show();
                    }

                    adapter = new RecipeLabAdapter(RecipeActivity.this,RecipeLabList );
                    recyclerView.setAdapter(adapter);

                }

            }

            @Override
            public void onFailure(Call<RecipeLabList> call, Throwable t) {

            }
        });


    }


    // 레시피 목록 검색api
    private void addSearchKeyword() {Retrofit retrofit = NetworkClient.getRetrofitClient(RecipeActivity.this);

        RecipeApi api = retrofit.create(RecipeApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;
        count = 0;

        Call<RecipeLabList> call = api.getRecipeKeyword(accessToken, keyword, offset, limit);
        call.enqueue(new Callback<RecipeLabList>() {
            @Override
            public void onResponse(Call<RecipeLabList> call, Response<RecipeLabList> response) {

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.
                RecipeLabList.clear();


                if (response.isSuccessful()) {

                    count = response.body().getCount();

                    offset = offset + count;

                    RecipeLabList.addAll(response.body().getItems());

                    adapter = new RecipeLabAdapter(RecipeActivity.this,RecipeLabList );
                    recyclerView.setAdapter(adapter);

                }

            }

            @Override
            public void onFailure(Call<RecipeLabList> call, Throwable t) {

            }
        });
    }


    //주인장 레시피 가저오는 api
    void getNetworkData(){

                Retrofit retrofit = NetworkClient.getRetrofitClient(RecipeActivity.this);

                RecipeApi api = retrofit.create(RecipeApi.class);

                SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

                offset = 0;
                count = 0;

                Call<RecipeLabList> call = api.getRecipeMasterList(accessToken,order,offset,limit,percent);
                call.enqueue(new Callback<RecipeLabList>() {
                    @Override
                    public void onResponse(Call<RecipeLabList> call, Response<RecipeLabList> response) {
                        RecipeLabList.clear();

                        if(response.isSuccessful()){
                            Log.i(TAG,"가지고옴"+response);
                            count = response.body().getCount();

                            offset = offset + count;

                            RecipeLabList.addAll(response.body().getItems());
                            Log.i(TAG,"가저옴"+RecipeLabList.addAll(response.body().getItems()));
                            adapter = new RecipeLabAdapter(RecipeActivity.this,RecipeLabList );
                            recyclerView.setAdapter(adapter);



                        }else{
                            Log.i(TAG,"하나도 못가저옴");
                        }
                    }

                    @Override
            public void onFailure(Call<RecipeLabList> call, Throwable t) {

            }
        });
    }

    //유저 레시피 가저오는 api
    void getNetworkData2(){

        Retrofit retrofit = NetworkClient.getRetrofitClient(RecipeActivity.this);

        RecipeApi api = retrofit.create(RecipeApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;
        count = 0;

        Call<RecipeLabList> call = api.getRecipeuserList(accessToken,order,offset,limit,percent);
        call.enqueue(new Callback<RecipeLabList>() {
            @Override
            public void onResponse(Call<RecipeLabList> call, Response<RecipeLabList> response) {
                RecipeLabList.clear();

                if(response.isSuccessful()){
                    Log.i(TAG,"가지고옴"+response);

                    count = response.body().getCount();

                    offset = offset + count;

                    RecipeLabList.addAll(response.body().getItems());
                    Log.i(TAG,"가저옴"+RecipeLabList.addAll(response.body().getItems()));

                    adapter = new RecipeLabAdapter(RecipeActivity.this,RecipeLabList );
                    recyclerView.setAdapter(adapter);



                }
            }

            @Override
            public void onFailure(Call<RecipeLabList> call, Throwable t) {

            }
        });
    }

    //주인장 + 유저 모두가저오는 api
    void getNetworkData3(){

        Retrofit retrofit = NetworkClient.getRetrofitClient(RecipeActivity.this);

        RecipeApi api = retrofit.create(RecipeApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;
        count = 0;

        Call<RecipeLabList> call = api.getRecipeAllList(accessToken,order,offset,limit,percent);
        call.enqueue(new Callback<RecipeLabList>() {
            @Override
            public void onResponse(Call<RecipeLabList> call, Response<RecipeLabList> response) {
                RecipeLabList.clear();

                if(response.isSuccessful()){
                    Log.i(TAG,"가지고옴"+response);

                    count = response.body().getCount();

                    offset = offset + count;

                    RecipeLabList.addAll(response.body().getItems());
                    Log.i(TAG,"가저옴"+RecipeLabList.addAll(response.body().getItems()));

                    adapter = new RecipeLabAdapter(RecipeActivity.this,RecipeLabList );
                    recyclerView.setAdapter(adapter);



                }
            }

            @Override
            public void onFailure(Call<RecipeLabList> call, Throwable t) {

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}