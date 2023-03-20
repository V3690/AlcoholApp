package com.leopard4.alcoholrecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameToastActivity extends AppCompatActivity {

    Button btnMyToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_toast);

        btnMyToast = findViewById(R.id.btnPassEdit);

        btnMyToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameToastActivity.this, GameToastFactoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}