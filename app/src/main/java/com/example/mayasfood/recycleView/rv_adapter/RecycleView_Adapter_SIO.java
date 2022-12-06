package com.example.mayasfood.recycleView.rv_adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mayasfood.R;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.fragments.Dashboard_Category_frag;
import com.example.mayasfood.fragments.SingleItem_frag;
import com.example.mayasfood.functions.Functions;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleView_Adapter_SIO extends RecyclerView.Adapter<RecycleView_Adapter_SIO.MyViewHolder>{

    int click = 1;
    Context context;
    ArrayList<RecycleView_Model> foodModels;

    public RecycleView_Adapter_SIO(Context context, ArrayList<RecycleView_Model> foodModels){
        this.context = context;
        this.foodModels = foodModels;
    }

    @NonNull
    @Override
    public RecycleView_Adapter_SIO.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout (Giving look)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_order_details_rv, parent, false);
        return new RecycleView_Adapter_SIO.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleView_Adapter_SIO.MyViewHolder holder, int position) {
        //Assigning values to the views we created
        final RecycleView_Model temp = foodModels.get(position);

        holder.itemName.setText(foodModels.get(position).getFoodName());
        holder.itemQty.setText("Qty : "+foodModels.get(position).getStars());
        holder.itemTotal.setText(context.getResources().getString(R.string.Rupee)+foodModels.get(position).getFoodImg());
        //String categoryId = foodModels.get(position).getFoodImg();

        Picasso.get().load(Constants.UserProduct_Path+foodModels.get(position).getFoodPrice())
                .into(holder.item_img);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Constants.productID = foodModels.get(position).getProductId();
                Constants.singleFoodName = foodModels.get(position).getFoodName();

                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                Functions.loadFragment(activity.getSupportFragmentManager(), new SingleItem_frag(), R.id.frag_cont, false, "Single Item", null);
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
        TextView itemName, itemQty, itemTotal;
        CircleImageView item_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.singleOrder_name);
            itemQty = itemView.findViewById(R.id.singleOrder_qty);
            itemTotal = itemView.findViewById(R.id.singleOrder_total);
            item_img = itemView.findViewById(R.id.singleOrder_img);
            cardView = itemView.findViewById(R.id.singleOrderCard);

        }
    }
}
