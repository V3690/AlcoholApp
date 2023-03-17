package com.leopard4.alcoholrecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class RegisterInfoActivity extends AppCompatActivity {

    TextView txtPercent;
    SeekBar seekBar;
    CheckBox checkBox1_1, checkBox1_2, checkBox1_3, checkBox1_4, checkBox1_5, checkBox1_6;
    CheckBox checkBox2_1, checkBox2_2, checkBox2_3, checkBox2_4, checkBox2_5, checkBox2_6;

    CheckBox[] checkBoxes_Q1;
    CheckBox[] checkBoxes_Q3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);

//        // 1.맥주 2.소주 3.막걸리 4.와인 5.양주 6.기타
//        checkBox1_1 = findViewById(R.id.checkBox1_1);
//        checkBox1_2 = findViewById(R.id.checkBox1_2);
//        checkBox1_3 = findViewById(R.id.checkBox1_3);
//        checkBox1_4 = findViewById(R.id.checkBox1_4);
//        checkBox1_5 = findViewById(R.id.checkBox1_5);
//        checkBox1_6 = findViewById(R.id.checkBox1_6);
//
//        // 1.가족 2.친구 3.혼자 4.직장 5.지인 6.기타
//        checkBox2_1 = findViewById(R.id.checkBox3_1);
//        checkBox2_2 = findViewById(R.id.checkBox3_2);
//        checkBox2_3 = findViewById(R.id.checkBox3_3);
//        checkBox2_4 = findViewById(R.id.checkBox3_4);
//        checkBox2_5 = findViewById(R.id.checkBox3_5);
//        checkBox2_6 = findViewById(R.id.checkBox3_6);
//
//        checkBoxes_Q1 = new CheckBox[6];
//        checkBoxes_Q1[0] = checkBox1_1;
//        checkBoxes_Q1[1] = checkBox1_2;
//        checkBoxes_Q1[2] = checkBox1_3;
//        checkBoxes_Q1[3] = checkBox1_4;
//        checkBoxes_Q1[4] = checkBox1_5;
//        checkBoxes_Q1[5] = checkBox1_6;
//        for (int i = 0; i < checkBoxes_Q1.length; i++) {
//            checkBoxes_Q1[i].setTag(i);
//            checkBoxes_Q1[i].setOnClickListener(clickListener);
//        }
//
//
//        // 정보 받기
//        if(checkBox1_1.isChecked()){
//
//        }




        txtPercent = findViewById(R.id.txtPercent);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtPercent.setText(String.format("%d도", seekBar.getProgress()));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}