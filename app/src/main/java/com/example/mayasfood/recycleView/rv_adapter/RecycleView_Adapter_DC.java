package com.example.mayasfood.recycleView.rv_adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mayasfood.R;
import com.example.mayasfood.Retrofite.request.Request_CategoryDetails;
import com.example.mayasfood.Retrofite.response.Response_Common;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.development.retrofit.RetrofitInstance;
import com.example.mayasfood.fragments.CategoryDetails_frag;
import com.example.mayasfood.fragments.Dashboard_Category_frag;
import com.example.mayasfood.functions.Functions;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecycleView_Adapter_DC extends RecyclerView.Adapter<RecycleView_Adapter_DC.MyViewHolder> {

    Context context;
    ArrayList<RecycleView_Model> foodModels;
    List<CardView> cardViewList = new ArrayList<>();
    int click = 0;
    int row_index = -1;

    public RecycleView_Adapter_DC(Context context, ArrayList<RecycleView_Model> foodModels) {
        this.context = context;
        this.foodModels = foodModels;

    }

    @NonNull
    @Override
    public RecycleView_Adapter_DC.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout (Giving look)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.food_categories_rv, parent, false);
        RecycleView_Adapter_DC.MyViewHolder myViewHolder = new RecycleView_Adapter_DC.MyViewHolder(view);

        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(View tempItemView : itemViewList) {

                    if(itemViewList.get(holder.getAdapterPosition()) == tempItemView) {
                        holder.cardView.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                        holder.name.setTextColor(Color.WHITE);
                    }
                    else{
                        holder.cardView.setBackgroundTintList(context.getColorStateList(R.color.Food_category_bg));
                        holder.name.setTextColor(Color.BLACK);
                    }
                }
            }
        });*/

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleView_Adapter_DC.MyViewHolder holder, int position) {
        //Assigning values to the views we created
        final RecycleView_Model temp = foodModels.get(position);

        if (!cardViewList.contains(holder.cardView)) {
            cardViewList.add(holder.cardView);
        }

        holder.name.setText(foodModels.get(position).getFoodName());

        String categoryId = foodModels.get(position).getFoodImg();

        ColorStateList color = holder.cardView.getCardBackgroundColor();

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                row_index = holder.getAdapterPosition();

                Constants.categoryId = categoryId;

                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                Constants.categoryName = foodModels.get(position).getFoodName();

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont_cat, new CategoryDetails_frag()).commit();
                //Functions.loadFragment(activity.getSupportFragmentManager(), new CategoryDetails_frag(), R.id.frag_cont_cat, false, "Category", null);
                Constants.onetTime = 1;

                notifyDataSetChanged();
            }
        });
        if (row_index == position) {
            holder.cardView.setBackgroundTintList(context.getColorStateList(R.color.Card_Back));
            holder.name.setTextColor(Color.WHITE);
        } else {
            holder.cardView.setBackgroundTintList(context.getColorStateList(R.color.Food_category_bg));
            holder.name.setTextColor(context.getColorStateList(R.color.Food_category_txt));
        }

        if (!Constants.categoryId.equals("") && Constants.onetTime == 1) {

            Log.d("DC", "iN");

            if (categoryId.equals(Constants.categoryId)) {

                Log.d("DC1", "iN");

                //holder.cardView.setCardBackgroundColor(Color.BLACK);
                holder.name.setTextColor(Color.WHITE);

                holder.cardView.setBackgroundTintList(context.getColorStateList(R.color.Card_Back));
                Constants.onetTime = 0;
            }

        }
        else {

            Constants.categoryId = categoryId;

            Log.d("ID", Constants.categoryId);
        }




               /* if (holder.cardView.getBackgroundTintList() == ColorStateList.valueOf(Color.BLACK)){

                    for(CardView cardView : cardViewList) {

                            Log.d("DC3", "iN");

                            holder.cardView.setBackgroundTintList(context.getColorStateList(R.color.Food_category_bg));
                            holder.name.setTextColor(Color.BLACK);

                           // notifyDataSetChanged();
                    }

                    Log.d("DC2", "iN");

                    holder.name.setTextColor(Color.BLACK);

                    holder.cardView.setBackgroundTintList(context.getColorStateList(R.color.Food_category_bg));

                }
                else {

                    holder.cardView.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                    holder.name.setTextColor(Color.WHITE);

                }

                *//*for(CardView cardView : cardViewList) {

                    if (cardView.getBackgroundTintList() == ColorStateList.valueOf(Color.BLACK)) {

                        Log.d("DC3", "iN");

                        holder.cardView.setBackgroundTintList(context.getColorStateList(R.color.Food_category_bg));
                        holder.name.setTextColor(Color.BLACK);

                        notifyDataSetChanged();
                    }
                }*//*
                   // }
                 //   else{
                        //holder.cardView.setBackgroundTintList(context.getColorStateList(R.color.Food_category_bg));

                  //  }

                *//*holder.cardView.setCardBackgroundColor(Color.BLACK);
                holder.name.setTextColor(Color.WHITE);*//*


            }
        });*/

        /*holder.name.setOnClickListener(new View.OnClickListener() {
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
        });*/
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.foodname);
            cardView = itemView.findViewById(R.id.cat_card);
        }
    }
}
