package com.example.mayasfood.recycleView.rv_adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mayasfood.R;
import com.example.mayasfood.activity.singleItem;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecycleView_Adapter_RC extends RecyclerView.Adapter<RecycleView_Adapter_RC.MyViewHolder> {

    Context context;
    ArrayList<RecycleView_Model> foodModels3;
    FirebaseAuth auth;

    public RecycleView_Adapter_RC(Context context, ArrayList<RecycleView_Model> foodModels3){
        this.context = context;
        this.foodModels3 = foodModels3;
        auth = FirebaseAuth.getInstance();
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

        if (auth.getCurrentUser() != null){

            holder.addToFav.setVisibility(View.VISIBLE);
        }
        else {

            holder.addToFav.setVisibility(View.GONE);
        }

        holder.name.setText(foodModels3.get(position).getFoodName());
        Picasso.get()
                .load(Constants.UserProduct_Path + foodModels3.get(position).getFoodImg())
                .into(holder.imageView);

        Log.d("RCImg",Constants.UserProduct_Path + foodModels3.get(position).getFoodImg() );

        //holder.imageView.setImageResource(foodModels3.get(position).getFoodImg());
        holder.price.setText("$"+foodModels3.get(position).getFoodPrice());

        if (foodModels3.get(position).getStars().matches("^[0]") || foodModels3.get(position).getStars().matches("^[0][.]") || foodModels3.get(position).getStars().matches("^[1][.][12345]")){

            holder.star1.setVisibility(View.GONE);
            holder.star2.setVisibility(View.GONE);
            holder.star3.setVisibility(View.VISIBLE);
            holder.star4.setVisibility(View.GONE);
            holder.star5.setVisibility(View.GONE);

        }

        else if (foodModels3.get(position).getStars().matches("^[1][.][6789]") ||foodModels3.get(position).getStars().matches("^[2][.][1234]")){

            holder.star1.setVisibility(View.GONE);
            holder.star2.setVisibility(View.VISIBLE);
            holder.star3.setVisibility(View.VISIBLE);
            holder.star4.setVisibility(View.GONE);
            holder.star5.setVisibility(View.GONE);
        }

        else if (foodModels3.get(position).getStars().matches("^[2][.][6789]") || foodModels3.get(position).getStars().matches("^[3][.][1234]")){

            holder.star1.setVisibility(View.GONE);
            holder.star2.setVisibility(View.VISIBLE);
            holder.star3.setVisibility(View.VISIBLE);
            holder.star4.setVisibility(View.VISIBLE);
            holder.star5.setVisibility(View.GONE);
        }

        else if (foodModels3.get(position).getStars().matches("^[3][.][6789]") || foodModels3.get(position).getStars().matches("^[4][.][12345]")){

            holder.star1.setVisibility(View.VISIBLE);
            holder.star2.setVisibility(View.VISIBLE);
            holder.star3.setVisibility(View.VISIBLE);
            holder.star4.setVisibility(View.VISIBLE);
            holder.star5.setVisibility(View.GONE);
        }

        else {

            holder.star1.setVisibility(View.VISIBLE);
            holder.star2.setVisibility(View.VISIBLE);
            holder.star3.setVisibility(View.VISIBLE);
            holder.star4.setVisibility(View.VISIBLE);
            holder.star5.setVisibility(View.VISIBLE);
        }


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

        ImageView imageView, star1, star2, star3, star4, star5;
        TextView name, price;
        ImageButton addToFav;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            star1 = itemView.findViewById(R.id.star_1);
            star2 = itemView.findViewById(R.id.star_2);
            star3 = itemView.findViewById(R.id.star_3);
            star4 = itemView.findViewById(R.id.star_4);
            star5 = itemView.findViewById(R.id.star_5);
            imageView = itemView.findViewById(R.id.foodimage1);
            name = itemView.findViewById(R.id.name_food1);
            price = itemView.findViewById(R.id.food_price1);
            addToFav = itemView.findViewById(R.id.addToFav);
        }
    }
}
