package com.example.mayasfood.recycleView.rv_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mayasfood.R;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleView_Adapter_RO extends RecyclerView.Adapter<RecycleView_Adapter_RO.MyViewHolder>{

    Context context;
    ArrayList<RecycleView_Model> foodModels4;

    public RecycleView_Adapter_RO(Context context, ArrayList<RecycleView_Model> foodModels4){
        this.context = context;
        this.foodModels4 = foodModels4;
    }

    @NonNull
    @Override
    public RecycleView_Adapter_RO.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout (Giving look)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.running_order_rv, parent, false);
        return new RecycleView_Adapter_RO.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleView_Adapter_RO.MyViewHolder holder, int position) {
        //Assigning values to the views we created

        final RecycleView_Model temp = foodModels4.get(position);

        holder.runOrder_num.setText(foodModels4.get(position).getRunOrder_num());
        holder.runOrder_total.setText(foodModels4.get(position).getRunOrder_total());
        holder.runOrder_Quantity.setText("Number of Items "+foodModels4.get(position).getRunOrder_Quantity());
        holder.runOrder_status.setText(foodModels4.get(position).getRunOrder_status());
        holder.runOrder_pickup.setText(foodModels4.get(position).getRunOrder_pickup());
        holder.runOrder_date.setText(foodModels4.get(position).getRunOrder_date());
        holder.runOrder_create.setText(foodModels4.get(position).getRunOrder_create());

        Picasso.get().load(Constants.UserProduct_Path+foodModels4.get(position).getRunOrder_img())
                .into(holder.runOrder_img);

    }

    @Override
    public int getItemCount() {
        //Number of Items you want to display
        return foodModels4.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //grabbing the views from rv_column.xml

        TextView runOrder_num , runOrder_total, runOrder_pickup, runOrder_create, runOrder_Quantity, runOrder_status,
        runOrder_date;
        CircleImageView runOrder_img;
        AppCompatButton runOrder_btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            runOrder_btn = itemView.findViewById(R.id.runOrder_cancel);
            runOrder_create = itemView.findViewById(R.id.runOrder_time);
            runOrder_date = itemView.findViewById(R.id.runOrder_date);
            runOrder_img = itemView.findViewById(R.id.run_order_img);
            runOrder_num = itemView.findViewById(R.id.runOrder_num);
            runOrder_pickup = itemView.findViewById(R.id.runOrder_pickup);
            runOrder_status = itemView.findViewById(R.id.runOrder_status);
            runOrder_Quantity = itemView.findViewById(R.id.runOrder_quantity);
            runOrder_total = itemView.findViewById(R.id.runOrder_total);

        }
    }
}
