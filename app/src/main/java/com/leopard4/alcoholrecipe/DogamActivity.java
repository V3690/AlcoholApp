package com.leopard4.alcoholrecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.leopard4.alcoholrecipe.adapter.AlcoholAdapter;
import com.leopard4.alcoholrecipe.adapter.DogamAdapter;
import com.leopard4.alcoholrecipe.api.CreatingApi;
import com.leopard4.alcoholrecipe.api.DogamApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.alcohol.Alcohol;
import com.leopard4.alcoholrecipe.model.alcohol.AlcoholList;
import com.leopard4.alcoholrecipe.model.dogam.Dogam;
import com.leopard4.alcoholrecipe.model.dogam.DogamList;

import java.util.ArrayList;
import java.util.function.ToDoubleBiFunction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DogamActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    EditText editSearch;
    ImageButton imgSearch;
    Button btnRequest;

    RecyclerView recyclerView;
    DogamAdapter dogamAdapter;
    ArrayList<Dogam> dogamList = new ArrayList<>();


    // 페이징 처리를 위한 변수
    int count = 0;
    int offset = 0;
    int limit = 30;

    private boolean isloading = false;

    Spinner spinner1 = null;

    ArrayAdapter<CharSequence> adapterPercent = null;

    Spinner spinner2;
    ArrayAdapter<CharSequence> adapterOrder = null;

    // api 호출할 때 정렬 조건에 들어가는 키워드 값 변수
    String OrderKeyword = "percent";
    String search;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogam);

        editSearch = findViewById(R.id.editSearch);
        imgSearch = findViewById(R.id.imgSearch);
        btnRequest = findViewById(R.id.btnRequest);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DogamActivity.this));

        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

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

        // 스피너 처리 관련 코드
        adapterPercent = ArrayAdapter.createFromResource(this, R.array.percent, android.R.layout.simple_spinner_dropdown_item);
        spinner1 = findViewById(R.id.spinner1);
        spinner1.setAdapter(adapterPercent);

        spinner1.setOnItemSelectedListener(DogamActivity.this);

        adapterOrder = ArrayAdapter.createFromResource(this, R.array.order, android.R.layout.simple_spinner_dropdown_item);
        spinner2 = findViewById(R.id.spinner2);
        spinner2.setAdapter(adapterOrder);

        spinner2.setOnItemSelectedListener(DogamActivity.this);


        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DogamActivity.this, OwnerRequestActivity.class);
                startActivity(intent);
            }
        });

//
//        search = editSearch.getText().toString().trim();
//
//        if (search.isEmpty()){
//            return;
//        }


        // TODO: 2023-03-20 검색 기능 개발 미완료 
        // TODO: 2023-03-20 스피너 기능 미처리

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search = editSearch.getText().toString().trim();

                if (search.isEmpty()){
                    return;
                }
            }
        });



        // 네트워크 처리하는 함수
        getNetworkData();



    }

    void getNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(DogamActivity.this);

        DogamApi api = retrofit.create(DogamApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;
        count = 0;

        Call<DogamList> call = api.getDogamlList(accessToken, OrderKeyword, offset, limit);
        call.enqueue(new Callback<DogamList>() {
            @Override
            public void onResponse(Call<DogamList> call, Response<DogamList> response) {

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.
                dogamList.clear();


                if (response.isSuccessful()) {

                    count = response.body().getCount();

                    offset = offset + count;

                    dogamList.addAll(response.body().getItems());

                    dogamAdapter = new DogamAdapter(DogamActivity.this, dogamList);
                    recyclerView.setAdapter(dogamAdapter);


                } else {

                }

            }

            @Override
            public void onFailure(Call<DogamList> call, Throwable t) {

            }
        });

    }

    private void addNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(DogamActivity.this);

        DogamApi api = retrofit.create(DogamApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        Call<DogamList> call = api.getDogamlList(accessToken, "percent", offset, limit);
        call.enqueue(new Callback<DogamList>() {
            @Override
            public void onResponse(Call<DogamList> call, Response<DogamList> response) {

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.
//                alcoholList.clear();


                if (response.isSuccessful()) {
                    offset = offset + count;
//                    alcoholList.clear();

//                    count = response.body().getCount();
//
//                    offset = offset + count;

                    dogamList.addAll(response.body().getItems());

//                    alcoholAdapter = new AlcoholAdapter(MyRecipeWriteSecondActivity.this, alcoholList);
//                    alcoholRecyclerView.setAdapter(alcoholAdapter);
                    dogamAdapter.notifyDataSetChanged();
                    isloading = false;


                }
            }

            @Override
            public void onFailure(Call<DogamList> call, Throwable t) {

            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
