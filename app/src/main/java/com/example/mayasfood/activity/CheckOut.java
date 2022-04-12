package com.example.mayasfood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mayasfood.R;
import com.example.mayasfood.fragments.Dashboard_frag;

public class CheckOut extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}