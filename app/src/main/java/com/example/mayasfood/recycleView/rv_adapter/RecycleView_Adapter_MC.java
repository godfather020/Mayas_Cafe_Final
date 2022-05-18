package com.example.mayasfood.recycleView.rv_adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mayasfood.R;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.fragments.CategoryDetails_frag;
import com.example.mayasfood.fragments.Dashboard_Category_frag;
import com.example.mayasfood.functions.Functions;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;

import java.util.ArrayList;

public class RecycleView_Adapter_MC extends RecyclerView.Adapter<RecycleView_Adapter_MC.MyViewHolder>{

    Context context;
    ArrayList<RecycleView_Model> foodModels;

    public RecycleView_Adapter_MC(Context context, ArrayList<RecycleView_Model> foodModels){
        this.context = context;
        this.foodModels = foodModels;
    }

    @NonNull
    @Override
    public RecycleView_Adapter_MC.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout (Giving look)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.nav_category_rv, parent, false);
        return new RecycleView_Adapter_MC.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleView_Adapter_MC.MyViewHolder holder, int position) {
        //Assigning values to the views we created
        final RecycleView_Model temp = foodModels.get(position);

        holder.name.setText(foodModels.get(position).getFoodName());

        String categoryId = foodModels.get(position).getFoodImg();

        ColorStateList color = holder.cardView.getCardBackgroundColor();

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                Constants.categoryId = foodModels.get(holder.getAdapterPosition()).getFoodImg();

                //Constants.poptToMainCat = 1;

                Constants.categoryName = foodModels.get(holder.getAdapterPosition()).getFoodName();

                Functions.loadFragment(activity.getSupportFragmentManager(), new CategoryDetails_frag(),R.id.frag_cont, false, "Category", null);
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
        TextView name;
        ImageView catImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.main_cat_txt);
            catImg = itemView.findViewById(R.id.main_cat_img);
            cardView = itemView.findViewById(R.id.main_cat_card);
        }
    }
}
