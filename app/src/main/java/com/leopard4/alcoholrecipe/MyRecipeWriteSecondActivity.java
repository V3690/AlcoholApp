package com.leopard4.alcoholrecipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.leopard4.alcoholrecipe.adapter.AlcoholAdapter;
import com.leopard4.alcoholrecipe.api.CreatingApi;
import com.leopard4.alcoholrecipe.api.NetworkClient;
import com.leopard4.alcoholrecipe.config.Config;
import com.leopard4.alcoholrecipe.model.alcohol.Alcohol;
import com.leopard4.alcoholrecipe.model.alcohol.AlcoholList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyRecipeWriteSecondActivity extends AppCompatActivity {

    // 알콜 부분
    TextView txtAlcoholList;
    TextView txtAlcoholCount;
    EditText editSearchAlcohol;
    ImageButton imgSearchAlcohol;
    RecyclerView alcoholRecyclerView;

    // 부재료 부분
    TextView txtIngreList;
    TextView txtIngreCount;
    EditText editSearchIngre;
    ImageButton imgSearchIngre;

    // 체크박스
    CheckBox checkBox;

    // 저장 버튼
    Button btnSave;

    // 리사이클러뷰 관련
    RecyclerView recyclerView;
    AlcoholAdapter alcoholAdapter;
    ArrayList<Alcohol> alcoholList = new ArrayList<>();

    // 페이징 처리를 위한 변수
    int count = 0;
    int offset = 0;
    int limit = 25;
    private Alcohol selectedAlcohol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe_write_second);

        // 알콜 부분
        txtAlcoholList = findViewById(R.id.txtAlcoholList);
        txtAlcoholCount = findViewById(R.id.txtAlcoholCount);
        editSearchAlcohol = findViewById(R.id.editSearchAlcohol);
        imgSearchAlcohol = findViewById(R.id.imgSearchAlcohol);

        alcoholRecyclerView = findViewById(R.id.alcoholRecyclerView);

        alcoholRecyclerView.setHasFixedSize(true);
        alcoholRecyclerView.setLayoutManager(new LinearLayoutManager(MyRecipeWriteSecondActivity.this));

        // 재료부분
        txtIngreList = findViewById(R.id.txtIngreList);
        txtIngreCount = findViewById(R.id.txtIngreCount);
        editSearchIngre = findViewById(R.id.editSearchIngre);
        imgSearchIngre = findViewById(R.id.imgSearchIngre);

        // 체크박스
        checkBox = findViewById(R.id.checkBox);

        // 저장
        btnSave = findViewById(R.id.btnSave);

        imgSearchAlcohol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String alcoholKeyword = editSearchAlcohol.getText().toString().trim();

                Retrofit retrofit = NetworkClient.getRetrofitClient(MyRecipeWriteSecondActivity.this);

                CreatingApi api = retrofit.create(CreatingApi.class);

                SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

                offset = 0;






            }
        });









        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyRecipeWriteSecondActivity.this, RecipeInfoActivity.class);
                startActivity(intent);
            }
        });


    }
}