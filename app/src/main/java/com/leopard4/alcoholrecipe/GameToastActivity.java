package com.leopard4.alcoholrecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameToastActivity extends AppCompatActivity {

    Button btnToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_toast);

        btnToast = findViewById(R.id.btnMyToast);

        btnToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameToastActivity.this, GameToastMakerActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}