package com.example.mayasfood.recycleView.rv_adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mayasfood.R;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecycleView_Adapter_C extends RecyclerView.Adapter<RecycleView_Adapter_C.MyViewHolder> {

    int click = 1;
    Context context;
    ArrayList<RecycleView_Model> foodModels;

    public RecycleView_Adapter_C(Context context, ArrayList<RecycleView_Model> foodModels){
        this.context = context;
        this.foodModels = foodModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout (Giving look)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.food_categories_rv, parent, false);
        return new RecycleView_Adapter_C.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Assigning values to the views we created
        final RecycleView_Model temp = foodModels.get(position);

        holder.name.setText(foodModels.get(position).getFoodName());
        holder.imageView.setImageResource(foodModels.get(position).getFoodImg());

        ColorStateList color = holder.cardView.getCardBackgroundColor();

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (click == 1){

                    holder.cardView.setCardBackgroundColor(Color.RED);
                    holder.name.setTextColor(Color.WHITE);
                    click = 2;
                }
                else if (click == 2){

                    holder.cardView.setCardBackgroundColor(color);
                    holder.name.setTextColor(Color.BLACK);
                    click = 1;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        //Number of Items you want to display
        return foodModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //grabbing the views from rv_column.xml

        CardView cardView;
        ImageView imageView;
        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.foodimage);
            name = itemView.findViewById(R.id.foodname);
            cardView = itemView.findViewById(R.id.cat_card);
        }
    }
}
