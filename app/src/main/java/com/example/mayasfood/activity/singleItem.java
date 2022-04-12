package com.example.mayasfood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mayasfood.R;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_C;
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_T;

import java.util.ArrayList;

public class singleItem extends AppCompatActivity {

    ImageButton back_img_s;
    ImageView foodImg;
    ImageView favoriteItem;
    int itemNumbers = 0;
    RecyclerView toppingsRv;
    private String numberOfItem = Integer.toString(itemNumbers);
    TextView foodName, foodPrice, foodDes, addItem, minusItem, noOfItem;
    Button add_to_cart;
    ArrayList<RecycleView_Model> topping_recycleView_models = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item);

        back_img_s = findViewById(R.id.back_img_s);
        foodImg = findViewById(R.id.single_foodimg);
        foodName = findViewById(R.id.single_foodname);
        foodPrice = findViewById(R.id.single_foodprice);
        foodDes = findViewById(R.id.food_des);
        add_to_cart = findViewById(R.id.add_to_cart);
        favoriteItem = findViewById(R.id.favorite_item);
        noOfItem = findViewById(R.id.no_of_item);
        addItem = findViewById(R.id.add_item);
        minusItem = findViewById(R.id.minus_item);
        toppingsRv = findViewById(R.id.toppings_rv);

        setUpToppingRv();

        foodImg.setImageResource(getIntent().getIntExtra("imagefood", 0));
        foodName.setText(getIntent().getStringExtra("foodname"));
        foodPrice.setText(getIntent().getStringExtra("foodprice"));
        foodDes.setText(getIntent().getStringExtra("fooddes"));
        noOfItem.setText(numberOfItem);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                itemNumbers = itemNumbers+1;
                numberOfItem = Integer.toString(itemNumbers);
                noOfItem.setText(numberOfItem);
            }
        });

        minusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (!noOfItem.getText().toString().equals("0")) {

                        itemNumbers = itemNumbers - 1;
                        numberOfItem = Integer.toString(itemNumbers);

                    }
                    noOfItem.setText(numberOfItem);
                }
        });

        favoriteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                favoriteItem.setImageResource(R.drawable.ic_baseline_favorite_24);

            }
        });

        back_img_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(singleItem.this, "Item Added to Cart", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void setUpToppingRv(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        toppingsRv.setLayoutManager(layoutManager);

        for(int i = 0; i<Constants.toppingImage.length; i++) {
            topping_recycleView_models.add(new RecycleView_Model(Constants.toppingImage[i]));
        }
        RecycleView_Adapter_T recycleView_adapter = new RecycleView_Adapter_T(getApplicationContext() ,topping_recycleView_models);

        toppingsRv.setAdapter(recycleView_adapter);
        recycleView_adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}