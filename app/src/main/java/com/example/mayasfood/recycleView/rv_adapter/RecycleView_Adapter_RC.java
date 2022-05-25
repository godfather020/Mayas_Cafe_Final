package com.example.mayasfood.recycleView.rv_adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lottry.data.remote.retrofit.request.Request_addOrRemoveToFav;
import com.example.mayasfood.R;
import com.example.mayasfood.Retrofite.response.Response_Common;
import com.example.mayasfood.activity.singleItem;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.development.retrofit.RetrofitInstance;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecycleView_Adapter_RC extends RecyclerView.Adapter<RecycleView_Adapter_RC.MyViewHolder> {

    Context context;
    ArrayList<RecycleView_Model> foodModels3;
    FirebaseAuth auth;
    String foodName;
    int i = 0;

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

        ArrayList<RecycleView_Model> tempFood = new ArrayList<>();

        holder.addToFav.setEnabled(true);
        holder.loading.setVisibility(View.GONE);

        tempFood.clear();
        /*ArrayList<Integer> isFav = new ArrayList<>();

        isFav.clear();*/

        for (int i = 0; i < foodModels3.size(); i++) {

            tempFood.add(foodModels3.get(position));
        }

        if (auth.getCurrentUser() != null){

            holder.addToFav.setVisibility(View.VISIBLE);
        }
        else {

            holder.addToFav.setVisibility(View.GONE);
        }

        if (auth.getCurrentUser() != null) {

            if (tempFood.get(holder.getAdapterPosition()).getIsFav() == 1) {

                holder.addToFav.setImageResource(R.drawable.red_heart);
            }
            else {

                holder.addToFav.setImageResource(R.drawable.bi_heart);
            }
        }

        holder.addToFav.setOnClickListener(null);

        holder.addToCart.setOnClickListener(null);

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "Item added to cart", Toast.LENGTH_SHORT).show();

                if (Constants.foodName.contains(foodModels3.get(holder.getAdapterPosition()).getFoodName())) {

                    for (int j =0 ; j < Constants.foodName.size(); j++){

                        if (Constants.foodName.get(j).matches(foodModels3.get(holder.getAdapterPosition()).getFoodName())){
                            int q = Constants.foodQuantity.get(j);
                            Log.d("foodQ", String.valueOf(q + 1));
                            //Constants.foodQuantity.add(j, 1 + Constants.q);
                            Constants.foodQuantity.set(j, q + 1);
                        }
                    }
                }
                else {

                    //Constants.foodId.add(i, )
                    Constants.foodQuantity.add(i,1);
                    Constants.foodImg.add(i,foodModels3.get(holder.getAdapterPosition()).getFoodImg());
                    Constants.foodName.add(i,foodModels3.get(holder.getAdapterPosition()).getFoodName());
                    Constants.foodPrice.add(i, Integer.valueOf(Integer.valueOf(foodModels3.get(holder.getAdapterPosition()).getFoodPrice())));
                    i++;
                }
            }
        });

        holder.addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Request_addOrRemoveToFav request_addOrRemoveToFav = new Request_addOrRemoveToFav();

                request_addOrRemoveToFav.setBranchId("1");
                request_addOrRemoveToFav.setProductId(foodModels3.get(holder.getLayoutPosition()).getProductId());

                RetrofitInstance retrofitInstance = new RetrofitInstance();

                Log.d("click", String.valueOf(holder.getBindingAdapterPosition()));

                Call<Response_Common> retrofitData = retrofitInstance.getRetrofit().addOrRemoveToFav(Constants.USER_TOKEN, request_addOrRemoveToFav);

                retrofitData.enqueue(new Callback<Response_Common>() {
                    @Override
                    public void onResponse(@NonNull Call<Response_Common> call, @NonNull Response<Response_Common> response) {

                        if (response.isSuccessful()){

                            if (response.body().getData().getProductId() != null){

                                Log.d("ProductR", response.body().getData().getProductId().toString());

                                //Toast.makeText(context, "Added to Favorite", Toast.LENGTH_SHORT).show();

                                Constants.add = 1;

                                Log.d("adapter", String.valueOf(holder.getAdapterPosition()));

                                //holder.isFav.setChecked(true);

                                holder.addToFav.setImageResource(R.drawable.red_heart);

                                Log.d("clickF", holder.addToFav.toString());

                                tempFood.get(holder.getAdapterPosition()).setIsFav(1);

                                holder.addToFav.setEnabled(false);

                                holder.loading.setVisibility(View.VISIBLE);

                                notifyDataSetChanged();
                                //notifyItemChanged(holder.getAdapterPosition());

                                //AppCompatActivity activity = (AppCompatActivity) view.getContext();


                                //Functions.loadFragment(activity.getSupportFragmentManager(), new Dashboard_frag(), R.id.frag_cont, true, "Dashboard", null);

                            }
                            else {
                                Constants.add = 0;
                                holder.addToFav.setImageResource(R.drawable.bi_heart);
                                //Toast.makeText(context, "Removed From Favorite", Toast.LENGTH_SHORT).show();
                                tempFood.get(holder.getAdapterPosition()).setIsFav(0);
                                holder.addToFav.setEnabled(false);
                                holder.loading.setVisibility(View.VISIBLE);
                                //notifyItemChanged(holder.getAdapterPosition());
                                notifyDataSetChanged();
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

                Log.d("add", String.valueOf(Constants.add));
            }
        });

        if (foodModels3.get(position).getFoodName().length() > 18){

            foodName = foodModels3.get(position).getFoodName().substring(0, 18) + "...";
        }
        else {

            foodName = foodModels3.get(position).getFoodName();
        }

        holder.name.setText(foodName);

        //holder.name.setText(foodModels3.get(position).getFoodName());
        Picasso.get()
                .load(Constants.UserProduct_Path + foodModels3.get(position).getFoodImg())
                .into(holder.imageView);

        Log.d("RCImg",Constants.UserProduct_Path + foodModels3.get(position).getFoodImg() );

        //holder.imageView.setImageResource(foodModels3.get(position).getFoodImg());
        holder.price.setText("$"+foodModels3.get(position).getFoodPrice());

        if (foodModels3.get(position).getStars().matches("^[0]") || foodModels3.get(position).getStars().matches("^[0][.]") || foodModels3.get(position).getStars().matches("^[1][.][12345]")){

            holder.star1.setImageResource(R.drawable.clarity_favorite_solid);
            /*holder.star2.setVisibility(View.GONE);
            holder.star3.setVisibility(View.VISIBLE);
            holder.star4.setVisibility(View.GONE);
            holder.star5.setVisibility(View.GONE);*/
            holder.star2.setImageResource(R.drawable.vector__6_);
            holder.star3.setImageResource(R.drawable.vector__6_);
            holder.star4.setImageResource(R.drawable.vector__6_);
            holder.star5.setImageResource(R.drawable.vector__6_);

        }

        else if (foodModels3.get(position).getStars().matches("^[1][.][6789]") ||foodModels3.get(position).getStars().matches("^[2][.][1234]")){

            holder.star1.setImageResource(R.drawable.clarity_favorite_solid);
            holder.star2.setImageResource(R.drawable.clarity_favorite_solid);
            /*holder.star3.setVisibility(View.GONE);
            holder.star4.setVisibility(View.GONE);
            holder.star5.setVisibility(View.GONE);*/
            holder.star3.setImageResource(R.drawable.vector__6_);
            holder.star4.setImageResource(R.drawable.vector__6_);
            holder.star5.setImageResource(R.drawable.vector__6_);
        }

        else if (foodModels3.get(position).getStars().matches("^[2][.][6789]") || foodModels3.get(position).getStars().matches("^[3][.][1234]")){

            holder.star1.setImageResource(R.drawable.clarity_favorite_solid);
            holder.star2.setImageResource(R.drawable.clarity_favorite_solid);
            holder.star3.setImageResource(R.drawable.clarity_favorite_solid);
            /*holder.star4.setVisibility(View.GONE);
            holder.star5.setVisibility(View.GONE);*/
            holder.star4.setImageResource(R.drawable.vector__6_);
            holder.star5.setImageResource(R.drawable.vector__6_);
        }

        else if (foodModels3.get(position).getStars().matches("^[3][.][6789]") || foodModels3.get(position).getStars().matches("^[4][.][12345]")){

            holder.star1.setImageResource(R.drawable.clarity_favorite_solid);
            holder.star2.setImageResource(R.drawable.clarity_favorite_solid);
            holder.star3.setImageResource(R.drawable.clarity_favorite_solid);
            holder.star4.setImageResource(R.drawable.clarity_favorite_solid);
            holder.star5.setImageResource(R.drawable.vector__6_);
        }

        else {

            holder.star1.setImageResource(R.drawable.clarity_favorite_solid);
            holder.star2.setImageResource(R.drawable.clarity_favorite_solid);
            holder.star3.setImageResource(R.drawable.clarity_favorite_solid);
            holder.star4.setImageResource(R.drawable.clarity_favorite_solid);
            holder.star5.setImageResource(R.drawable.clarity_favorite_solid);
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

        ImageView star1, star2, star3, star4, star5;
        CircleImageView imageView;
        TextView name, price;
        ImageView addToFav;
        ProgressBar loading;
        ImageButton addToCart;

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
            loading = itemView.findViewById(R.id.loading_rest_rv);
            addToCart = itemView.findViewById(R.id.addToCart);
        }
    }
}
