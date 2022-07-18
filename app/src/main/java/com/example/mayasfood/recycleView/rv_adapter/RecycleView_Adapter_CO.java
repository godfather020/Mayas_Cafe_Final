package com.example.mayasfood.recycleView.rv_adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mayasfood.R;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.fragments.CategoryDetails_frag;
import com.example.mayasfood.fragments.CheckOut_frag;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;
import com.example.mayasfood.shared_prefrence.TinyDB;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecycleView_Adapter_CO extends RecyclerView.Adapter<RecycleView_Adapter_CO.MyViewHolder>{

    Context context;
    ArrayList<RecycleView_Model> foodModels;
    List<CardView> cardViewList = new ArrayList<>();
    int click = 0;
    int row_index = -1;
    String foodName = "";
    CheckOut_frag fragment;
    TinyDB tinyDB;

    public RecycleView_Adapter_CO(Context context, ArrayList<RecycleView_Model> foodModels, CheckOut_frag fragment) {
        this.context = context;
        this.foodModels = foodModels;
        this.fragment = fragment;
        tinyDB = new TinyDB(context.getApplicationContext());
    }

    @NonNull
    @Override
    public RecycleView_Adapter_CO.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout (Giving look)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cart_details_rv, parent, false);
        RecycleView_Adapter_CO.MyViewHolder myViewHolder = new RecycleView_Adapter_CO.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleView_Adapter_CO.MyViewHolder holder, int position) {
        //Assigning values to the views we created
        tinyDB = new TinyDB(context.getApplicationContext());

        final RecycleView_Model temp = foodModels.get(position);

        if (foodModels.get(position).getFoodName().length() > 25){

            foodName = foodModels.get(position).getFoodName().substring(0, 25) + "...";
        }
        else {

            foodName = foodModels.get(position).getFoodName();
        }

        holder.foodName.setText(foodName);
        //holder.foodName.setText(foodModels.get(position).getFoodName());


        Picasso.get()
                .load(Constants.UserProduct_Path + foodModels.get(position).getFoodHeading())
                .into(holder.food_img);

        holder.foodItems.setText("Size "+String.valueOf(foodModels.get(position).getFoodSize()));
        holder.foodCount.setText(String.valueOf(foodModels.get(position).getFoodImg1()));

        holder.foodPrice.setText("$"+Integer.valueOf(foodModels.get(position).getFoodPrice())*foodModels.get(position).getFoodImg1());

        holder.food_clear.setOnClickListener(null);

        holder.food_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Constants.subTotal = 0;

                Log.d("subtotal", String.valueOf(Constants.subTotal));
                Constants.foodId.remove(holder.getAdapterPosition());
                Constants.foodName.remove(holder.getAdapterPosition());
                Constants.foodImg.remove(holder.getAdapterPosition());
                Constants.foodPrice.remove(holder.getAdapterPosition());
                Constants.foodQuantity.remove(holder.getAdapterPosition());
                Constants.foodSize.remove(holder.getAdapterPosition());

                Constants.cart_totalItems -= 1;

                tinyDB.putListInt("foodId", Constants.foodId);
                tinyDB.putListString("foodSize", Constants.foodSize);
                tinyDB.putListString("foodName", Constants.foodName);
                tinyDB.putListString("foodImg", Constants.foodImg);
                tinyDB.putListInt("foodPrice", Constants.foodPrice);
                tinyDB.putListInt("foodQuantity", Constants.foodQuantity);
                tinyDB.putInt("cartCount", Constants.cart_totalItems);

                foodModels.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), foodModels.size());

                fragment.setUpModelRv();
            }
        });

        holder.foodPlus.setOnClickListener(null);

        holder.foodPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int q = Constants.foodQuantity.get(holder.getAdapterPosition());

                Constants.foodQuantity.set(holder.getAdapterPosition(), q +1);

                tinyDB.putListInt("foodQuantity", Constants.foodQuantity);

                fragment.setUpModelRv();
            }
        });

        holder.foodMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int q = Constants.foodQuantity.get(holder.getAdapterPosition());

                if (q == 1){

                    foodModels.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), foodModels.size());
                }
                else {

                    Constants.foodQuantity.set(holder.getAdapterPosition(), q - 1);

                    tinyDB.putListInt("foodQuantity", Constants.foodQuantity);
                }
                fragment.setUpModelRv();
            }
        });

    }

    @Override
    public int getItemCount() {
        //Number of Items you want to display
        Constants.cart_totalItems = foodModels.size();
        return foodModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //grabbing the views from rv_column.xml

        CardView cardView;
        TextView foodName, foodItems, foodPrice, foodCount;
        ImageView foodMinus, foodPlus, food_clear, food_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.cart_food_name);
            cardView = itemView.findViewById(R.id.cart_cardView);
            foodItems = itemView.findViewById(R.id.cart_items);
            foodPrice = itemView.findViewById(R.id.cart_foodAmt);
            foodCount = itemView.findViewById(R.id.cart_Foodnum);
            foodMinus = itemView.findViewById(R.id.cart_minus);
            foodPlus = itemView.findViewById(R.id.cart_plus);
            food_clear = itemView.findViewById(R.id.cart_remove);
            food_img = itemView.findViewById(R.id.cart_food_img);
        }
    }
}
