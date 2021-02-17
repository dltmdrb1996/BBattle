package com.example.BBATTLE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class
SplashActivity extends AppCompatActivity {
    ImageView splash_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        splash_logo=(ImageView)findViewById(R.id.splash_logo3);
        startLoading();
    }

    //잠깐 딜레이후 로그인페이지로 이동
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run(){
                Intent intent = new Intent(getBaseContext(), GoogleLoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}