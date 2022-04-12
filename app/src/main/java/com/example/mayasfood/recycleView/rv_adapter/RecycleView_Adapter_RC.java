package com.example.mayasfood.recycleView.rv_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mayasfood.R;
import com.example.mayasfood.activity.singleItem;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;

import java.util.ArrayList;

public class RecycleView_Adapter_RC extends RecyclerView.Adapter<RecycleView_Adapter_RC.MyViewHolder> {

    Context context;
    ArrayList<RecycleView_Model> foodModels3;

    public RecycleView_Adapter_RC(Context context, ArrayList<RecycleView_Model> foodModels3){
        this.context = context;
        this.foodModels3 = foodModels3;
    }

    @NonNull
    @Override
    public RecycleView_Adapter_RC.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout (Giving look)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.restaurant_choice_rv, parent, false);
        return new RecycleView_Adapter_RC.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleView_Adapter_RC.MyViewHolder holder, int position) {
        //Assigning values to the views we created

        final RecycleView_Model temp = foodModels3.get(position);

        holder.name.setText(foodModels3.get(position).getFoodName());
        holder.imageView.setImageResource(foodModels3.get(position).getFoodImg());
        holder.option.setText(foodModels3.get(position).getFoodHeading());
        holder.price.setText(foodModels3.get(position).getFoodPrice());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, singleItem.class);
                intent.putExtra("imagefood", temp.getFoodImg());
                intent.putExtra("foodname", temp.getFoodName());
                intent.putExtra("foodprice", temp.getFoodPrice());
                intent.putExtra("fooddes", temp.getFoodHeading());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        //Number of Items you want to display
        return foodModels3.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //grabbing the views from rv_column.xml

        ImageView imageView;
        TextView name, option, price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.foodimage1);
            name = itemView.findViewById(R.id.name_food1);
            option = itemView.findViewById(R.id.food_op1);
            price = itemView.findViewById(R.id.food_price1);
        }
    }
}
