package com.example.mayasfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mayasfood.MainActivity;
import com.example.mayasfood.R;

public class Splash extends AppCompatActivity {

    private static final long SPLASH_DELAY = 2000;
    private Handler mHandler = new Handler();
    private Launcher mLauncher = new Launcher();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        launch();
    }

    public void launch(){
        mHandler.postDelayed(mLauncher, SPLASH_DELAY);
    }

    private class Launcher implements Runnable {
        @Override
        public void run() {

            intent = new Intent(Splash.this, GetStart.class);
            startActivity(intent);
            finish();
        }
    }
}