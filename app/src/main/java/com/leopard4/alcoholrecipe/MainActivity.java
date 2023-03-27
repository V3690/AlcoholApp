package com.leopard4.alcoholrecipe;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leopard4.alcoholrecipe.config.Config;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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


        Log.d("getKeyHash", "" + getKeyHash(MainActivity.this));

        // 억세스 토큰이 있는지 확인
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = sp.getString(Config.ACCESS_TOKEN, "");
        Log.i(MotionEffect.TAG,"확인"+accessToken);
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

    public static String getKeyHash(final Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            if (packageInfo == null)
                return null;

            for (Signature signature : packageInfo.signatures) {
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    // 탭바를 누르면 동작하는 코드
    public boolean loadFragment(Fragment fragment) {
        if(fragment != null) {
            getSupportFragmentManager() // 프래그먼트 매니저를 가져온다.
                    .beginTransaction() // 트랜잭션을 시작한다.
                    .replace(R.id.fragment, fragment) // fragment를 가져온다.
                    .commit(); // 트랜잭션을 커밋한다.
            return true;
        }else {
            return false;
        }
    }

}
