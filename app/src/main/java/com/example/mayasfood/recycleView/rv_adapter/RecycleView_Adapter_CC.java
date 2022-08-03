package com.example.mayasfood.recycleView.rv_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mayasfood.R;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleView_Adapter_CC extends RecyclerView.Adapter<RecycleView_Adapter_CC.MyViewHolder>{

    Context context;
    ArrayList<RecycleView_Model> foodModels4;

    public RecycleView_Adapter_CC(Context context, ArrayList<RecycleView_Model> foodModels4){
        this.context = context;
        this.foodModels4 = foodModels4;
    }

    @NonNull
    @Override
    public RecycleView_Adapter_CC.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout (Giving look)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.customer_comments_rv, parent, false);
        return new RecycleView_Adapter_CC.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleView_Adapter_CC.MyViewHolder holder, int position) {
        //Assigning values to the views we created

        final RecycleView_Model temp = foodModels4.get(position);

        holder.custName.setText(foodModels4.get(position).getFoodName());
        holder.custRating.setText(foodModels4.get(position).getStars());
        holder.custComment.setText(foodModels4.get(position).getProductId());
        holder.commentDate.setText(foodModels4.get(position).getFoodPrice());

        Picasso.get()
                .load(Constants.UserProfile_Path+foodModels4.get(position).getFoodImg())
                .into(holder.custImg);

    }

    @Override
    public int getItemCount() {
        //Number of Items you want to display
        return foodModels4.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //grabbing the views from rv_column.xml


        TextView custName, custRating, commentDate, custComment;
        CircleImageView custImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            custName = itemView.findViewById(R.id.custNameR);
            custComment = itemView.findViewById(R.id.cust_comment);
            custRating = itemView.findViewById(R.id.cust_rating);
            commentDate = itemView.findViewById(R.id.commentDate);
            custImg = itemView.findViewById(R.id.custImg);
        }
    }
}
