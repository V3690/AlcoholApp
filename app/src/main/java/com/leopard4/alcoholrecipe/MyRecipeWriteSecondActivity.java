package com.leopard4.alcoholrecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.leopard4.alcoholrecipe.adapter.AlcoholAdapter;
import com.leopard4.alcoholrecipe.adapter.IngredientAdapter;
import com.leopard4.alcoholrecipe.api.CreatingApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.alcohol.Alcohol;
import com.leopard4.alcoholrecipe.model.alcohol.AlcoholList;
import com.leopard4.alcoholrecipe.model.ingredient.Ingredient;
import com.leopard4.alcoholrecipe.model.ingredient.IngredientList;
import com.leopard4.alcoholrecipe.model.recipeIngreAlcol.RecipeIngreAlcol;
import com.leopard4.alcoholrecipe.model.recipeOne.RecipeOne;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyRecipeWriteSecondActivity extends AppCompatActivity {

    // 알콜 부분 //
    // xml 파일에서는 현재 textview 로 되어있기 때문에
    // 코드 작성할 때 xml 을 recyclerView 로 수정하고 작성해야함
    RecyclerView alcoholListRecyclerView;
    TextView txtAlcohol;
    TextView txtAlcoholCount;
    EditText editSearchAlcohol;
    ImageButton imgSearchAlcohol;

    // 리사이클러뷰 관련
    RecyclerView alcoholRecyclerView;
    AlcoholAdapter alcoholAdapter;
    ArrayList<Alcohol> alcoholList = new ArrayList<>();



    // 부재료 부분 //
    TextView txtIngre;
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
    ImageView imgBack;
    ImageView imgbtnReturnRecipe;

    // 페이징 처리를 위한 변수
    int count = 0;
    int offset = 0;
    int limit = 5;

    // 선택된 알콜과 부재료를 저장하기 위한 변수
    private ArrayList<String> selectedAlcoholList = new ArrayList<String>();
    private ArrayList<String> selectedIngreList = new ArrayList<String>();

    String alcoholKeyword = "%%"; // 기본적으로 재료리스트를 화면에 보여주기위해 %%로 설정
    String ingreKeyword = "%%";
    private Handler handler = new Handler();

    // 비동기적 네트워크를 동기적으로 바꾸기위한 플래그변수
    private boolean isloading = false;
    // 선택한 재료는 한번에 하나씩만 삭제가 가능하다.
    public int isisloading = 0;
    private ProgressDialog dialog;

    // save버튼을 눌렀을때 api로 보낼 데이터
    String recipeId;
    ArrayList<Integer> selectedAlcoholid = new ArrayList<>();
    ArrayList<Integer> selectedIngreid = new ArrayList<>();

    // 완료 버튼을 수정버튼으로 바꾸기 위한 변수
    private RecipeOne recipeOne = new RecipeOne();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe_write_second);

        // 알콜 부분
        txtAlcohol = findViewById(R.id.txtAlcohol);
        txtAlcoholCount = findViewById(R.id.txtAlcoholCount);
        editSearchAlcohol = findViewById(R.id.editSearchAlcohol);
        imgSearchAlcohol = findViewById(R.id.imgSearchAlcohol);

        alcoholRecyclerView = findViewById(R.id.alcoholRecyclerView);
        alcoholRecyclerView.setHasFixedSize(true);
        alcoholRecyclerView.setLayoutManager(new LinearLayoutManager(MyRecipeWriteSecondActivity.this));


        btnSave = findViewById(R.id.btnSave);

        // recipeId 받아오기
        Intent intent = getIntent();
        recipeId = intent.getStringExtra("recipeId");
        // recipeOne 받아오기
        recipeOne = (RecipeOne) intent.getSerializableExtra("recipeOne");
        Log.d("recipeId", recipeId);
        // 완료 버튼을 수정하기로 변경
        if(recipeOne != null) {
            btnSave.setText("수정하기");
        }



        // 뒤로가기
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 주인장의 연구실로 이동
        imgbtnReturnRecipe = findViewById(R.id.btnReturnRecipe);
        imgbtnReturnRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyRecipeWriteSecondActivity.this, RecipeActivity.class);
                startActivity(intent);
                finish();
            }
        });

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
        txtIngre = findViewById(R.id.txtIngre);
        txtIngreCount = findViewById(R.id.txtIngreCount);
        editSearchIngre = findViewById(R.id.editSearchIngre);
        imgSearchIngre = findViewById(R.id.imgSearchIngre);

        ingreRecyclerView = findViewById(R.id.ingreRecyclerView);
        ingreRecyclerView.setHasFixedSize(true);
        ingreRecyclerView.setLayoutManager(new LinearLayoutManager(MyRecipeWriteSecondActivity.this));

        ingreRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                if(lastPosition + 1 == totalCount && !isloading ) {
                    // 네트워크 통해서 데이터를 받아오고, 화면에 표시!
                    isloading = true;
                    addIngreNetworkData();
                }
                }
        });

        // 체크박스
        checkBox = findViewById(R.id.checkBox1);
        // todo: 체크박스 구현

        // 저장
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recipeOne == null) { // 저장하기
                    PostRecipeIngreAlcolNetworkData();
                }else if (recipeOne != null) { // 수정하기
                    EditRecipeIngreAlcolNetworkData();
                }

            }
        });

        imgSearchAlcohol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alcoholKeyword = editSearchAlcohol.getText().toString().trim();
                getAlcoholNetworkData();

            }
        });
        // 실시간으로 검색결과를 가져온다
        editSearchAlcohol.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 업데이트된 텍스트를 가져와 alcoholKeyword에 할당합니다.
                alcoholKeyword = charSequence.toString().trim();
                // 여러 요청을 방지하기 위해 처리기 대기열의 기존 메시지를 취소합니다.
                handler.removeCallbacksAndMessages(null);
                // 1000밀리초 후에 getAlcoholNetworkData()를 호출하도록 지연된 메시지 예약
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getAlcoholNetworkData();
                    }
                }, 800);

            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        editSearchIngre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 업데이트된 텍스트를 가져와 alcoholKeyword에 할당합니다.
                ingreKeyword = charSequence.toString().trim();
                // 여러 요청을 방지하기 위해 처리기 대기열의 기존 메시지를 취소합니다.
                handler.removeCallbacksAndMessages(null);
                // 1000밀리초 후에 getAlcoholNetworkData()를 호출하도록 지연된 메시지 예약
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getIngreNetworkData();
                    }
                }, 800);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        imgSearchIngre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingreKeyword = editSearchIngre.getText().toString().trim();
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

                if (response.isSuccessful()) {
                    offset = offset + count;
                    alcoholList.addAll(response.body().getResult());
                    alcoholAdapter.setOnItemClickListener(new AlcoholAdapter.onItemClickListener() {
                        @Override
                        public void onItemClick(int index) {
                            if (selectedAlcoholList.size() == 10) {
                                Snackbar.make(findViewById(R.id.alcoholRecyclerView), "재료는 10개까지만 추가할 수 있습니다.", Snackbar.LENGTH_SHORT).show();
                            }else {
                                if (txtAlcohol.getText().toString().contains(alcoholList.get(index).getName()+",")) {
                                    Snackbar.make(findViewById(R.id.alcoholRecyclerView), "이미 추가된 재료입니다.", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    selectedAlcoholList.add(alcoholList.get(index).getName());
                                    selectedAlcoholid.add(alcoholList.get(index).getId());
                                    txtAlcohol.setText(txtAlcohol.getText().toString() + alcoholList.get(index).getName() + ", ");

                                    SpannableStringBuilder builder = new SpannableStringBuilder(txtAlcohol.getText());
                                    for (String word : selectedAlcoholList) {
                                        int startIndex = 0;
                                        while ((startIndex = builder.toString().indexOf(word, startIndex)) != -1) {
                                            // Check if the found word is an exact match
                                            if (builder.toString().substring(startIndex, startIndex + word.length()).equals(word)) {
                                                int endIndex = startIndex + word.length();
                                                builder.setSpan(new WordClickableSpan(word, txtAlcohol), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            }
                                            startIndex += word.length();
                                        }
                                    }

                                    txtAlcohol.setText(builder);
                                    txtAlcohol.setMovementMethod(LinkMovementMethod.getInstance());
                                    updateTxtAlcoholCount();
                                }
                            }
                        }


                    });
                    alcoholAdapter.notifyDataSetChanged();
                    isloading = false;

                }
            }

            @Override
            public void onFailure(Call<AlcoholList> call, Throwable t) {

            }
        });


    }

    private void updateTxtAlcoholCount() {
        txtAlcoholCount.setText(selectedAlcoholList.size() + "개");
    }
    private void updateTxtIngreCount() {
        txtIngreCount.setText(selectedIngreList.size() + "개");
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

                if (response.isSuccessful()) {
                    offset = offset + count;
                    ingredientList.addAll(response.body().getResult());
                    ingredientAdapter.setOnItemClickListener(new IngredientAdapter.onItemClickListener() {
                        @Override
                        public void onItemClick(int index) {
                            if (selectedIngreList.size() == 10) {
                                Snackbar.make(findViewById(R.id.ingreRecyclerView), "재료는 10개까지만 추가할 수 있습니다.", Snackbar.LENGTH_SHORT).show();
                            } else {
                                if (txtIngre.getText().toString().contains(ingredientList.get(index).getName()+",")) {
                                    Snackbar.make(findViewById(R.id.ingreRecyclerView), "이미 추가된 재료입니다.", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    selectedIngreList.add(ingredientList.get(index).getName());
                                    selectedIngreid.add(ingredientList.get(index).getId());
                                    txtIngre.setText(txtIngre.getText().toString() + ingredientList.get(index).getName() + ", ");

                                    SpannableStringBuilder builder = new SpannableStringBuilder(txtIngre.getText());
                                    for (String word : selectedIngreList) {
                                        int startIndex = 0;
                                        while ((startIndex = builder.toString().indexOf(word, startIndex)) != -1) {
                                            // 찾은 단어가 정확히 일치하는지 확인
                                            if (builder.toString().substring(startIndex, startIndex + word.length()).equals(word)) {
                                                int endIndex = startIndex + word.length();
                                                builder.setSpan(new WordClickableSpan(word, txtIngre), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            }
                                            startIndex += word.length();
                                        }
                                    }
                                    txtIngre.setText(builder);
                                    txtIngre.setMovementMethod(LinkMovementMethod.getInstance());
                                    updateTxtIngreCount();
                                }
                            }
                        }

                    });
                    ingredientAdapter.notifyDataSetChanged();
                    isloading =false;
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
        if(recipeOne != null) {
            btnSave.setText("수정하기");
        }
        getAlcoholNetworkData();
        getIngreNetworkData();
    }
    // 백버튼을
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

                alcoholList.clear();

                if (response.isSuccessful()) {
                    count = response.body().getCount();
                    offset = offset + count;
                    alcoholList.addAll(response.body().getResult());
                    alcoholAdapter = new AlcoholAdapter(MyRecipeWriteSecondActivity.this, alcoholList);
                    alcoholAdapter.setOnItemClickListener(new AlcoholAdapter.onItemClickListener() {
                        @Override
                        public void onItemClick(int index) {
                            if (selectedAlcoholList.size() == 10) {
                                Snackbar.make(findViewById(R.id.alcoholRecyclerView), "재료는 10개까지만 추가할 수 있습니다.", Snackbar.LENGTH_SHORT).show();
                            }else {
                                if (txtAlcohol.getText().toString().contains(alcoholList.get(index).getName()+",")) {
                                    Snackbar.make(findViewById(R.id.alcoholRecyclerView), "이미 추가된 재료입니다.", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    selectedAlcoholList.add(alcoholList.get(index).getName());
                                    selectedAlcoholid.add(alcoholList.get(index).getId());
                                    txtAlcohol.setText(txtAlcohol.getText().toString() + alcoholList.get(index).getName() + ", ");
                                    SpannableStringBuilder builder = new SpannableStringBuilder(txtAlcohol.getText());
                                    for (String word : selectedAlcoholList) {
                                        int startIndex = 0;
                                        while ((startIndex = builder.toString().indexOf(word, startIndex)) != -1) {
                                            // 찾은 단어가 정확히 일치하는지 확인
                                            if (builder.toString().substring(startIndex, startIndex + word.length()).equals(word)) {
                                                int endIndex = startIndex + word.length();
                                                builder.setSpan(new WordClickableSpan(word, txtAlcohol), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            }
                                            startIndex += word.length();
                                        }
                                    }
//                                    for (String word : selectedAlcoholList) {
//                                        int startIndex = builder.toString().indexOf(word);
//                                        if (startIndex != 1){
//                                            int endIndex = startIndex + word.length();
//                                            builder.setSpan(new WordClickableSpan(word, txtAlcohol), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                        }
//                                    }
                                    txtAlcohol.setText(builder);
                                    txtAlcohol.setMovementMethod(LinkMovementMethod.getInstance()); // Make the text clickable
                                    updateTxtAlcoholCount();
                                }
                            }
                        }

                    });
                    alcoholRecyclerView.setAdapter(alcoholAdapter);
                }
            }
            @Override
            public void onFailure(Call<AlcoholList> call, Throwable t) {
            }
        });

    }
    private class WordClickableSpan extends ClickableSpan {
        private String word;
        private TextView textView;

        public WordClickableSpan(String word, TextView textView) {
            this.word = word;
            this.textView = textView;
        }

        @Override
        public void onClick(View widget) {
            // 버튼을 비활성화하여 다중 클릭 방지
            widget.setEnabled(false);
            showProgress("삭제중입니다.");
            // TextView의 현재 텍스트 가져오기
            SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText());

            // 클릭한 단어의 시작 및 끝 인덱스 찾기
            int startIndex = builder.toString().indexOf(word);
            int endIndex = startIndex + word.length();

            // 클릭한 단어를 텍스트에서 제거
            builder.delete(startIndex, endIndex + 2);
            // 클릭한 단어를 리스트에서 제거
            if (this.textView == txtAlcohol) {

                // 정확히 일치하는 단어로 id값을 찾아서 삭제
                for (int i = 0; i < selectedAlcoholList.size(); i++) {
                    if (selectedAlcoholList.get(i).equals(word.replace(", ", ""))) {
                        selectedAlcoholid.remove(i);
                    }
                }
                selectedAlcoholList.removeIf(selectedAlcoholList -> selectedAlcoholList.equals(word.replace(", ", "")));
                updateTxtAlcoholCount();
            } else {

                // 정확히 일치하는 단어로 id값을 찾아서 삭제
                for (int i = 0; i < selectedIngreList.size(); i++) {
                    if (selectedIngreList.get(i).equals(word.replace(", ", ""))) {
                        selectedIngreid.remove(i);
                    }
                }
                selectedIngreList.removeIf(selectedIngreList -> selectedIngreList.equals(word.replace(", ", "")));
                updateTxtIngreCount();
            }
            Log.i("selectedAlcoholid", selectedAlcoholid.toString());
            Log.i("selectedAlcoholList", selectedAlcoholList.toString());
            Log.i("selectedIngreid", selectedIngreid.toString());
            Log.i("selectedIngreList", selectedIngreList.toString());
            // 수정된 텍스트로 TextView 업데이트
            textView.setText(builder);
            dismissProgress();
            // 버튼을 다시 활성화
            widget.setEnabled(true);

        }
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

                ingredientList.clear();

                if (response.isSuccessful()){
                    count = response.body().getCount();
                    offset = offset + count;
                    ingredientList.addAll(response.body().getResult());
                    ingredientAdapter = new IngredientAdapter(MyRecipeWriteSecondActivity.this, ingredientList);
                    ingredientAdapter.setOnItemClickListener(new IngredientAdapter.onItemClickListener() {
                        @Override
                        public void onItemClick(int index) {
                            if (selectedIngreList.size() == 10) {
                                Snackbar.make(findViewById(R.id.ingreRecyclerView), "재료는 10개까지만 추가할 수 있습니다.", Snackbar.LENGTH_SHORT).show();
                            }else {
                                if (txtIngre.getText().toString().contains(ingredientList.get(index).getName())) {
                                    Snackbar.make(findViewById(R.id.ingreRecyclerView), "이미 추가된 재료입니다.", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    selectedIngreList.add(ingredientList.get(index).getName());
                                    selectedIngreid.add(ingredientList.get(index).getId());
                                    txtIngre.setText(txtIngre.getText().toString() + ingredientList.get(index).getName() + ", ");

                                    SpannableStringBuilder builder = new SpannableStringBuilder(txtIngre.getText());
                                    for (String word : selectedIngreList) {
                                        int startIndex = 0;
                                        while ((startIndex = builder.toString().indexOf(word, startIndex)) != -1) {
                                            // 찾은 단어가 정확히 일치하는지 확인
                                            if (builder.toString().substring(startIndex, startIndex + word.length()).equals(word)) {
                                                int endIndex = startIndex + word.length();
                                                builder.setSpan(new WordClickableSpan(word, txtIngre), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            }
                                            startIndex += word.length();
                                        }
                                    }
                                    txtIngre.setText(builder);
                                    txtIngre.setMovementMethod(LinkMovementMethod.getInstance());
                                    updateTxtIngreCount();
                                }
                            }
                        }
                    });
                    ingreRecyclerView.setAdapter(ingredientAdapter);
                }
            }
            @Override
            public void onFailure(Call<IngredientList> call, Throwable t) {

            }

        });

    }
    // save버튼을눌렀을때 호출되는 함수
    void PostRecipeIngreAlcolNetworkData() {
        showProgress("저장중입니다.");
        Retrofit retrofit = NetworkClient.getRetrofitClient(MyRecipeWriteSecondActivity.this);
        CreatingApi api = retrofit.create(CreatingApi.class);
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        String alcoholIdString = Arrays.toString(new ArrayList[]{selectedAlcoholid}).replace("[", "").replace("]", "").replace(" ", "");
        String ingredientIdString = Arrays.toString(new ArrayList[]{selectedIngreid}).replace("[", "").replace("]", "").replace(" ", "");

        RecipeIngreAlcol recipeIngreAlcol = new RecipeIngreAlcol(Integer.parseInt(recipeId), alcoholIdString, ingredientIdString);
        Call<Void> call = api.addAlcoholIngre(accessToken, recipeIngreAlcol);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dismissProgress();
                if (response.isSuccessful()) {
                    Log.i("성공", "성공");
                    Intent intent = new Intent(MyRecipeWriteSecondActivity.this, RecipeInfoActivity.class);
                    intent.putExtra("recipeId", Integer.parseInt(recipeId));
                    startActivity(intent);
                    finish();

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                dismissProgress();
                Log.i("실패", "실패");
            }
        });
    }
    // 재료수정api
    void EditRecipeIngreAlcolNetworkData() {
        showProgress("수정중입니다.");
        Retrofit retrofit = NetworkClient.getRetrofitClient(MyRecipeWriteSecondActivity.this);
        CreatingApi api = retrofit.create(CreatingApi.class);
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        String alcoholIdString = Arrays.toString(new ArrayList[]{selectedAlcoholid}).replace("[", "").replace("]", "").replace(" ", "");
        String ingredientIdString = Arrays.toString(new ArrayList[]{selectedIngreid}).replace("[", "").replace("]", "").replace(" ", "");
        Map map = new HashMap();
        map.put("alcoholId", alcoholIdString);
        map.put("ingredientId", ingredientIdString);

        Call<Void> call = api.editAlcoholIngre(accessToken, Integer.parseInt(recipeId), map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                dismissProgress();
                if (response.isSuccessful()) {
                    Log.i("성공", "성공");
                    Intent intent = new Intent(MyRecipeWriteSecondActivity.this, RecipeInfoActivity.class);
                    intent.putExtra("recipeId", Integer.parseInt(recipeId));
                    startActivity(intent);
                    finish();

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                dismissProgress();
                Log.i("실패", "실패");
            }
        });
    }

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