package com.example.mayasfood.functions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mayasfood.R;
import com.example.mayasfood.constants.Constants;
import com.google.android.material.navigation.NavigationView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Functions {

    public static boolean checkData(String user_name, String user_phone, EditText userName, EditText phoneNum) {

        if (user_name.isEmpty()){
            userName.requestFocus();
            userName.setError(Constants.emptyFieldError);
            return false;
        }
        else if (!user_name.matches("[a-z A-z]{3,15}+")){
            userName.requestFocus();
            userName.setError(Constants.userNameError);
            return false;
        }
        else if (user_phone.isEmpty()){
            phoneNum.requestFocus();
            phoneNum.setError(Constants.emptyFieldError);
            return false;
        }
        else if (!user_phone.matches("^[0-9 +]{9,13}$")){
            phoneNum.requestFocus();
            phoneNum.setError(Constants.phoneNumError);
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean checkData(String user_phone, EditText phoneNum){

        if (user_phone.isEmpty()){
            phoneNum.requestFocus();
            phoneNum.setError(Constants.emptyFieldError);
            return false;
        }
        else if (!user_phone.matches("^[0-9 +]{9,13}$")){
            phoneNum.requestFocus();
            phoneNum.setError(Constants.phoneNumError);
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean checkOtp(String otp_1, String otp_2, String otp_3, String otp_4, EditText otp1, EditText otp2, EditText otp3, EditText otp4){

        if (otp_1.isEmpty()){
            otp1.requestFocus();
            otp1.setError("Filed can't be Empty");
            return false;
        }
        else if (otp_2.isEmpty()){
            otp2.requestFocus();
            otp2.setError("Filed can't be Empty");
            return false;
        }
        else if (otp_3.isEmpty()){
            otp3.requestFocus();
            otp3.setError("Filed can't be Empty");
            return false;
        }
        else if (otp_4.isEmpty()){
            otp4.requestFocus();
            otp4.setError("Filed can't be Empty");
            return false;
        }
        else {
            return true;
        }
    }

    public static void otpTextChange(EditText otp1, EditText otp2){

        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (otp1.getText().toString().isEmpty()){
                    otp1.setBackgroundResource(R.drawable.custom_otp_edittext);
                }
                else {
                    otp1.setBackgroundResource(R.drawable.black_back);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!otp1.getText().toString().isEmpty()) {
                    otp2.requestFocus();
                }
            }
        });
    }

    public static void setArrow(NavigationView navigationView){

        for (int i = 0; i < navigationView.getMenu().size(); i++){

            int size = navigationView.getMenu().size();

            Log.d("size" , String.valueOf(size));

            if (!(navigationView.getMenu().getItem(i).getTitle() == null)){
                navigationView.getMenu().getItem(i).setActionView(R.layout.arrow);
            }
            else {
                return;
            }
        }
    }

}



