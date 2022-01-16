package com.diary.deardiary.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.diary.deardiary.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity  {

    @Bind(R.id.btnlogin)Button _btnlogin;
    @Bind(R.id.btnregis)Button _btnregis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        ButterKnife.bind(this);

        //check already login or not in first activity
        SharedPreferences sp = getSharedPreferences("NICE_LOGIN", Context.MODE_PRIVATE);
        int keyLogin = sp.getInt("KEY_LOGIN", 0);
        if (keyLogin == 1) {
            Intent intent = new Intent(MainActivity.this, HamburgerMenu.class);
            startActivity(intent);
        }

        _btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
            }
        });

        _btnregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisteruActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }


}
