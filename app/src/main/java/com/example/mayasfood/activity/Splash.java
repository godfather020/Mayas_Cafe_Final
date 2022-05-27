package com.example.mayasfood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.mayasfood.FirebaseCloudMsg;
import com.example.mayasfood.MainActivity;
import com.example.mayasfood.R;
import com.example.mayasfood.activity.ViewModels.GetStart_ViewModel;
import com.example.mayasfood.constants.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Splash extends AppCompatActivity {

    private static final long SPLASH_DELAY = 4000;
    private Handler mHandler = new Handler();
    private Launcher mLauncher = new Launcher();
    private Intent intent;
    FirebaseAuth auth;
    String token;
    GetStart_ViewModel getStart_viewModel;
    ViewModelProvider viewModelProvider;
    FirebaseUser currentUser;
    String userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        viewModelProvider = new ViewModelProvider(this);
        getStart_viewModel = viewModelProvider.get(GetStart_ViewModel.class);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        userPhone = getSharedPreferences(Constants.sharedPrefrencesConstant.USER_P, MODE_PRIVATE).getString(Constants.sharedPrefrencesConstant.USER_P, "empty");

        token = FirebaseCloudMsg.getToken(this);

        Log.d("tokenGS", token);

        if(currentUser != null) {

            Log.d("userPhoneC", Objects.requireNonNull(currentUser).getPhoneNumber());
        }

        getStart_viewModel.sendDeviceInfo(this);

        launch();
    }

    public void launch(){
        mHandler.postDelayed(mLauncher, SPLASH_DELAY);
    }

    private class Launcher implements Runnable {
        @Override
        public void run() {

            if (getSharedPreferences("LogIn", MODE_PRIVATE).getBoolean("LogIn", false)) {

                Log.d("userPhone", userPhone);

                if (currentUser != null) {

                    if (currentUser.getPhoneNumber().equals(userPhone)) {

                        Intent intent = new Intent(Splash.this, DashBoard.class);
                        startActivity(intent);
                        finish();
                    } else {

                        Intent intent = new Intent(Splash.this, GetStart.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    Intent intent = new Intent(Splash.this, GetStart.class);
                    startActivity(intent);
                    finish();
                }
            }
            else {

                intent = new Intent(Splash.this, GetStart.class);
                startActivity(intent);
                finish();
            }
        }
    }
}