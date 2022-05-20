package com.example.mayasfood.recycleView.rv_adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lottry.data.remote.retrofit.request.Request_addOrRemoveToFav;
import com.example.mayasfood.R;
import com.example.mayasfood.Retrofite.response.Response_Common;
import com.example.mayasfood.activity.DashBoard;
import com.example.mayasfood.activity.Login;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.development.retrofit.RetrofitInstance;
import com.example.mayasfood.fragments.Dashboard_frag;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecycleView_Adapter_F  extends RecyclerView.Adapter<RecycleView_Adapter_F.MyViewHolder>{

    Context context;
    ArrayList<RecycleView_Model> foodModels2;
    FirebaseAuth auth;

    public RecycleView_Adapter_F(Context context, ArrayList<RecycleView_Model> foodModels2){
        this.context = context;
        this.foodModels2 = foodModels2;
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public RecycleView_Adapter_F.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout (Giving look)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popular_food_rv, parent, false);
        return new RecycleView_Adapter_F.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleView_Adapter_F.MyViewHolder holder, int position) {
        //Assigning values to the views we created

        final RecycleView_Model temp = foodModels2.get(position);

        Log.d("rcPN", foodModels2.get(position).getFoodName());

        if (auth.getCurrentUser() == null){

            holder.addToFav.setVisibility(View.GONE);
        }
        else {

            holder.addToFav.setVisibility(View.VISIBLE);
        }

        holder.addToFav.setImageResource(R.drawable.red_heart);

        holder.addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Request_addOrRemoveToFav request_addOrRemoveToFav = new Request_addOrRemoveToFav();

                request_addOrRemoveToFav.setBranchId("1");
                request_addOrRemoveToFav.setProductId(foodModels2.get(holder.getAdapterPosition()).getProductId());

                RetrofitInstance retrofitInstance = new RetrofitInstance();

                Call<Response_Common> retrofitData = retrofitInstance.getRetrofit().addOrRemoveToFav(Constants.USER_TOKEN, request_addOrRemoveToFav);


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setTitle("Do you want to remove this from Favorites?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        retrofitData.enqueue(new Callback<Response_Common>() {
                            @Override
                            public void onResponse(@NonNull Call<Response_Common> call, @NonNull Response<Response_Common> response) {

                                if (response.isSuccessful()){

                                    if (response.body().getData().getProductId() != null){

                                        Log.d("ProductR", response.body().getData().getProductId().toString());

                                        Toast.makeText(context, "Added to Favorite", Toast.LENGTH_SHORT).show();

                                        Constants.add = 1;

                                        holder.addToFav.setImageResource(R.drawable.red_heart);

                                    }
                                    else {

                                        Constants.add = 0;
                                        holder.addToFav.setImageResource(R.drawable.bi_heart);
                                        Toast.makeText(context, "Removed From Favorite", Toast.LENGTH_SHORT).show();
                                       /* notifyDataSetChanged();
                                        notifyItemChanged(holder.getAdapterPosition());
                                        notifyItemRemoved(holder.getAdapterPosition());*/

                                            foodModels2.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position, foodModels2.size());

                                    }

                                }
                                else {
                                    Constants.add = 0;
                                    holder.addToFav.setImageResource(R.drawable.bi_heart);
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Response_Common> call, Throwable t) {
                                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                Dialog alertDialog = builder.create();
                alertDialog.show();

                //Log.d("add", String.valueOf(Constants.add));
            }
        });

        holder.name.setText(foodModels2.get(position).getFoodName());

        Picasso.get()
                .load(Constants.UserProduct_Path + foodModels2.get(position).getFoodImg())
                .into(holder.imageView);

        //holder.imageView.setImageResource(foodModels2.get(position).getFoodImg());
        holder.price.setText("$"+foodModels2.get(position).getFoodPrice());

        if (foodModels2.get(position).getStars().matches("^[0]") || foodModels2.get(position).getStars().matches("^[0][.]") || foodModels2.get(position).getStars().matches("^[1][.][12345]")){

            holder.star1.setVisibility(View.GONE);
            holder.star2.setVisibility(View.GONE);
            holder.star3.setVisibility(View.VISIBLE);
            holder.star4.setVisibility(View.GONE);
            holder.star5.setVisibility(View.GONE);

        }

        else if (foodModels2.get(position).getStars().matches("^[1][.][6789]") ||foodModels2.get(position).getStars().matches("^[2][.][1234]")){

            holder.star1.setVisibility(View.GONE);
            holder.star2.setVisibility(View.VISIBLE);
            holder.star3.setVisibility(View.VISIBLE);
            holder.star4.setVisibility(View.GONE);
            holder.star5.setVisibility(View.GONE);
        }

        else if (foodModels2.get(position).getStars().matches("^[2][.][6789]") || foodModels2.get(position).getStars().matches("^[3][.][1234]")){

            holder.star1.setVisibility(View.GONE);
            holder.star2.setVisibility(View.VISIBLE);
            holder.star3.setVisibility(View.VISIBLE);
            holder.star4.setVisibility(View.VISIBLE);
            holder.star5.setVisibility(View.GONE);
        }

        else if (foodModels2.get(position).getStars().matches("^[3][.][6789]") || foodModels2.get(position).getStars().matches("^[4][.][12345]")){

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

                /*Intent intent = new Intent(context, singleItem.class);
                intent.putExtra("imagefood", temp.getFoodImg());
                intent.putExtra("foodname", temp.getFoodName());
                intent.putExtra("foodprice", temp.getFoodPrice());
                intent.putExtra("fooddes", temp.getFoodHeading());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        //Number of Items you want to display
        return foodModels2.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //grabbing the views from rv_column.xml

        ImageView imageView, star1, star2, star3, star4, star5;
        TextView name, price;
        ImageView addToFav;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            star1 = itemView.findViewById(R.id.star_1);
            star2 = itemView.findViewById(R.id.star_2);
            star3 = itemView.findViewById(R.id.star_3);
            star4 = itemView.findViewById(R.id.star_4);
            star5 = itemView.findViewById(R.id.star_5);
            imageView = itemView.findViewById(R.id.imgfood);
            name = itemView.findViewById(R.id.name_food);
            price = itemView.findViewById(R.id.food_price);
            addToFav = itemView.findViewById(R.id.addToFav);
        }
    }

    /*private void dialog(String msg){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(msg);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //auth = FirebaseAuth.getInstance();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        Dialog alertDialog = builder.create();
        alertDialog.show();


    }*/
}