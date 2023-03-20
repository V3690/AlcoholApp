package com.leopard4.alcoholrecipe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leopard4.alcoholrecipe.config.Config;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView navigationView;

    // 각 플래그먼트를 멤버변수로 만든다.
    Fragment firstFragment;
    Fragment secondFragment;
    Fragment thirdFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 억세스 토큰이 있는지 확인
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = sp.getString(Config.ACCESS_TOKEN, "");
        if(accessToken.isEmpty()){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

//        getSupportActionBar().setTitle("홈");

        navigationView = findViewById(R.id.bottomNavigationView);

        firstFragment = new FirstFragment();
        secondFragment = new SecondFragment();
        thirdFragment = new ThirdFragment();

        // 탭바를 누르면 동작하는 코드
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                Fragment fragment = null;

                if(itemId == R.id.firstFragment) {
                    fragment = firstFragment;
//                    getSupportActionBar().setTitle("홈");
                } else if(itemId == R.id.secondFragment) {
                    fragment = secondFragment;
//                    getSupportActionBar().setTitle("즐겨찾기");
                } else {
                    fragment = thirdFragment;
//                    getSupportActionBar().setTitle("마이 페이지");
                }
                return loadFragment(fragment);
            }
        });

    }
    // 탭바를 누르면 동작하는 코드
    private boolean loadFragment(Fragment fragment) {
        if(fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();
            return true;
        }else {
            return false;
        }
    }
}
