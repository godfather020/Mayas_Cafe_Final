package com.example.mayasfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mayasfood.R;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.functions.Functions;
import com.hbb20.CountryCodePicker;

public class Registration extends AppCompatActivity {

    private boolean isBackPressed = false;
    EditText userName, phoneNum;
    Button signUp;
    TextView skip;
    ImageButton back_img_r;
    private boolean dataCheck = false;
    private String user_name, user_phone;
    CountryCodePicker cc_r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userName = findViewById(R.id.userName_r);
        phoneNum = findViewById(R.id.phoneNum_r);
        signUp = findViewById(R.id.signUp);
        skip = findViewById(R.id.skip);
        back_img_r = findViewById(R.id.back_img_r);
        cc_r = findViewById(R.id.cc_r);

        back_img_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registration.this, Login.class));
                finish();
            }
        });

        cc_r.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Constants.cc = cc_r.getSelectedCountryCode();
                Log.d("CountryCode", Constants.cc);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                user_name = userName.getText().toString();
                user_phone = Constants.cc + phoneNum.getText().toString();
                Log.d("Phone", user_phone);

                dataCheck = Functions.checkData(user_name, user_phone, userName, phoneNum);

                if (dataCheck){

                    Log.d("Phone", user_phone);
                    startActivity(new Intent(Registration.this, DashBoard.class));
                    finish();
                }
                else {
                    Toast.makeText(Registration.this, "Check Information", Toast.LENGTH_SHORT).show();
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Registration.this, DashBoard.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(isBackPressed){
            super.onBackPressed();
            return;
        }

        Toast.makeText(Registration.this, "Press again to exit", Toast.LENGTH_SHORT).show();
        isBackPressed = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackPressed = false;
            }
        },2000);
    }
}