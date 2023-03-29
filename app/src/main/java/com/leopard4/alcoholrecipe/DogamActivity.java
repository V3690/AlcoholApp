package com.leopard4.alcoholrecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
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

import com.leopard4.alcoholrecipe.adapter.DogamAdapter;
import com.leopard4.alcoholrecipe.api.DogamApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.dogam.Dogam;
import com.leopard4.alcoholrecipe.model.dogam.DogamList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DogamActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText editSearch;
    ImageButton imgSearch;
    Button btnRequest;

    RecyclerView recyclerView;
    DogamAdapter dogamAdapter;
    ArrayList<Dogam> dogamList = new ArrayList<>();
    private ProgressDialog dialog;



    // 쿼리 스트링
    int percent;
    String order;
    // 페이징 처리를 위한 변수
    int count = 0;
    int offset = 0;
    int limit = 30;

    private boolean isloading = false;

    Spinner spinner1;

    Spinner spinner2;

    String keyword;
    int spinner1Id;
    int spinner2Id;
    Button btnMyLike;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogam);

        editSearch = findViewById(R.id.editSearch);
        imgSearch = findViewById(R.id.imgSearch);
        btnRequest = findViewById(R.id.btnRequest);
        btnMyLike=findViewById(R.id.btnMyLike);
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

        ArrayAdapter<CharSequence> adapterPercent = ArrayAdapter.createFromResource(this,R.array.percent , R.layout.layout_spinner);;
        ArrayAdapter<CharSequence> adapterOrder = ArrayAdapter.createFromResource(this,R.array.order , R.layout.layout_spinner);;

        spinner1.setOnItemSelectedListener(DogamActivity.this);
        spinner2.setOnItemSelectedListener(DogamActivity.this);

        spinner1.setAdapter(adapterPercent);
        spinner2.setAdapter(adapterOrder);


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                percent = spinner1.getSelectedItemPosition();
                Log.i("SPINNERTEST", spinner1Id + "");

                getNetworkData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                percent = spinner2.getSelectedItemPosition();
                Log.i("SPINNERTEST", spinner2Id + "");

                if (spinner2Id == 0){
                    order = "cnt";
                } else if (spinner2Id == 1) {
                    order = "cnt";
                } else if (spinner2Id == 2) {
                    order = "createdAt";
                } else if (spinner2Id == 3) {
                    order = "name";
                }
                getNetworkData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


//        Log.i("ORDERTEST", "order" + order + " percent" + percent);


        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DogamActivity.this, OwnerRequestActivity.class);
                startActivity(intent);
            }
        });


        btnMyLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DogamActivity.this, MyDogamActivity.class);
                startActivity(intent);
            }
        });


        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword = editSearch.getText().toString().trim();

                if (keyword.isEmpty()){
                    return;
                }

                searchKeyword();

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
                                addSearchKeyword();


                            }


                        }
                });
            }
        });


    }

    void getNetworkData() {



        Retrofit retrofit = NetworkClient.getRetrofitClient(DogamActivity.this);

        DogamApi api = retrofit.create(DogamApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;
        count = 0;

        Call<DogamList> call = api.getDogamlList(accessToken, percent ,order , offset, limit);
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
        showProgress("술도감 불러오는 중...");


        Retrofit retrofit = NetworkClient.getRetrofitClient(DogamActivity.this);

        DogamApi api = retrofit.create(DogamApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        Call<DogamList> call = api.getDogamlList(accessToken, percent ,order , offset, limit);
        call.enqueue(new Callback<DogamList>() {
            @Override
            public void onResponse(Call<DogamList> call, Response<DogamList> response) {
                dismissProgress();

                if (response.isSuccessful()) {

                    offset = offset + count;


                    dogamList.addAll(response.body().getItems());

                    dogamAdapter.notifyDataSetChanged();
                    isloading = false;


                }
            }

            @Override
            public void onFailure(Call<DogamList> call, Throwable t) {
                dismissProgress();


            }
        });


    }


    void searchKeyword(){

        showProgress("술도감 불러오는 중...");

        Retrofit retrofit = NetworkClient.getRetrofitClient(DogamActivity.this);

        DogamApi api = retrofit.create(DogamApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;
        count = 0;

        Call<DogamList> call = api.getKeywordDogamList(accessToken, keyword, offset, limit);
        call.enqueue(new Callback<DogamList>() {
            @Override
            public void onResponse(Call<DogamList> call, Response<DogamList> response) {
                dismissProgress();

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
                dismissProgress();


            }
        });

    }

    private void addSearchKeyword(){

        showProgress("술도감 불러오는 중...");


        Retrofit retrofit = NetworkClient.getRetrofitClient(DogamActivity.this);

        DogamApi api = retrofit.create(DogamApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        Call<DogamList> call = api.getKeywordDogamList(accessToken, keyword, offset, limit);
        call.enqueue(new Callback<DogamList>() {
            @Override
            public void onResponse(Call<DogamList> call, Response<DogamList> response) {
                dismissProgress();

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.
//                alcoholList.clear();


                if (response.isSuccessful()) {
                    dismissProgress();
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
                dismissProgress();


            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private int getSpinnerIndex(Spinner spinner, String value) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                index = i;
                break;
            }
        }
        return index;
    }
    // 네트워크 로직 처리시에 화면에 보여주는 함수
    void showProgress(String message){
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);
        dialog.show();
    }

    // 로직처리가 끝나면 화면에서 사라지는 함수
    void dismissProgress(){
        dialog.dismiss();
    }


}
