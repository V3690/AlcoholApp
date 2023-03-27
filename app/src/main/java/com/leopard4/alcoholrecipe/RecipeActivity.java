package com.leopard4.alcoholrecipe;

import static android.view.View.VISIBLE;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


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

    ImageButton imgSearch;
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
        imgSearch=findViewById(R.id.imgSearch);
        editSearch=findViewById(R.id.editSearch);
        txtWarning=findViewById(R.id.txtWarning);
        toggleButton=findViewById(R.id.toggleButton);
        toggleButton2=findViewById(R.id.toggleButton2);
        spinnerRecipe=findViewById(R.id.spinnerRecipe);
        spinnerRecipe2=findViewById(R.id.spinnerRecipe2);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(RecipeActivity.this));

        toggleButton.setChecked(true);



        adapterPercent = ArrayAdapter.createFromResource(this,R.array.percent , android.R.layout.simple_spinner_dropdown_item);;
        adapterOrder = ArrayAdapter.createFromResource(this,R.array.order , android.R.layout.simple_spinner_dropdown_item);;

        spinnerRecipe.setOnItemSelectedListener(RecipeActivity.this);
        spinnerRecipe2.setOnItemSelectedListener(RecipeActivity.this);

        spinnerRecipe.setAdapter(adapterPercent);
        spinnerRecipe2.setAdapter(adapterOrder);


        // 들어가자마자 레시피보여주기
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
                isToggleButton1Checked = isChecked;
                if(isToggleButton1Checked != isToggleButton2Checked && isToggleButton2Checked==false){
                    toggleButton.setTextColor(Color.parseColor("#FFC107"));
                    toggleButton2.setTextColor(Color.WHITE);
                    recyclerView.setVisibility(View.VISIBLE);
                    percent=0;
                    order="cnt";
                    getNetworkData();
                    Log.i(TAG,"1번째 조건입니다 1번 "+isToggleButton1Checked+"2번 "+isToggleButton2Checked);

                    toggle1spinner();


                }else if(isToggleButton1Checked == isToggleButton2Checked && isToggleButton2Checked==true){
                    toggleButton.setTextColor(Color.parseColor("#FFC107"));
                    toggleButton2.setTextColor(Color.parseColor("#FFC107"));
                    recyclerView.setVisibility(View.VISIBLE);
                    percent=0;
                    order="cnt";
                    getNetworkData3();

                    Log.i(TAG,"3번째 조건입니다 1번 "+isToggleButton1Checked+"2번 "+isToggleButton2Checked);

                    toggle3spinner();

                }
                else{
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
                        getNetworkData2();
                        Log.i(TAG,"1-4번째 조건입니다 1번 "+isToggleButton1Checked+"2번 "+isToggleButton2Checked);
                    }
                }

            }
        });

        // 유저 눌렀을때
        toggleButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                isToggleButton2Checked = isChecked;

                if(isToggleButton1Checked != isToggleButton2Checked && isToggleButton2Checked==true ){
                    toggleButton.setTextColor(Color.WHITE);
                    toggleButton2.setTextColor(Color.parseColor("#FFC107"));
                    recyclerView.setVisibility(View.VISIBLE);
                    percent=0;
                    order="cnt";
                    getNetworkData2();
                    Log.i(TAG,"2번째 조건입니다 1번 "+isToggleButton1Checked+"2번 "+isToggleButton2Checked);


                    toggle2spinner();


                }else if(isToggleButton1Checked == isToggleButton2Checked && isToggleButton2Checked==true ){
                    toggleButton.setTextColor(Color.parseColor("#FFC107"));
                    toggleButton2.setTextColor(Color.parseColor("#FFC107"));
                    recyclerView.setVisibility(View.VISIBLE);
                    percent=0;
                    order="cnt";
                    getNetworkData3();

                    Log.i(TAG,"3번째 조건입니다"+isToggleButton2Checked+isToggleButton1Checked);

                    toggle3spinner();

                }
                else{
                    toggleButton.setTextColor(Color.WHITE);
                    toggleButton2.setTextColor(Color.WHITE);
                    recyclerView.setVisibility(View.GONE);
                    txtWarning.setVisibility(View.VISIBLE);
                    if(isToggleButton1Checked){
                        toggleButton.setTextColor(Color.parseColor("#FFC107"));
                        toggleButton2.setTextColor(Color.WHITE);
                        recyclerView.setVisibility(View.VISIBLE);
                        percent=0;
                        order="cnt";
                        getNetworkData();
                        Log.i(TAG,"2-3번째 조건입니다 1번 "+isToggleButton1Checked+"2번 "+isToggleButton2Checked);
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

    //키워드 검색
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


    public void switchColor() {
        if (toggleButton.isChecked()) {
            toggleButton.setTextColor(Color.YELLOW);
        }else {
            toggleButton.setTextColor(VISIBLE);
        }
        if (toggleButton2.isChecked()) {
            toggleButton2.setTextColor(Color.YELLOW);
        }else {
            toggleButton2.setTextColor(VISIBLE);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}