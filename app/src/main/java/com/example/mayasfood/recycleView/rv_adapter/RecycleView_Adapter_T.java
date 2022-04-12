package com.example.mayasfood.recycleView.rv_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mayasfood.R;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;

import java.util.ArrayList;

public class RecycleView_Adapter_T extends RecyclerView.Adapter<RecycleView_Adapter_T.MyViewHolder> {

    Context context;
    ArrayList<RecycleView_Model> foodModels;

    public RecycleView_Adapter_T(Context context, ArrayList<RecycleView_Model> foodModels){
        this.context = context;
        this.foodModels = foodModels;
    }

    @NonNull
    @Override
    public RecycleView_Adapter_T.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout (Giving look)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.topping_rv, parent, false);
        return new RecycleView_Adapter_T.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleView_Adapter_T.MyViewHolder holder, int position) {
        //Assigning values to the views we created
        final RecycleView_Model temp = foodModels.get(position);

        holder.imageView.setImageResource(foodModels.get(position).getFoodImg());
    }

    @Override
    public int getItemCount() {
        //Number of Items you want to display
        return foodModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //grabbing the views from rv_column.xml

        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.topping_image);
        }
    }
}

