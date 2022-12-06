package com.example.mayasfood.recycleView.rv_adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mayasfood.Retrofite.request.Request_Branch;
import com.example.lottry.data.remote.retrofit.request.Request_addOrRemoveToFav;
import com.example.mayasfood.R;
import com.example.mayasfood.Retrofite.response.Response_Common;
import com.example.mayasfood.activity.DashBoard;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.development.retrofit.RetrofitInstance;
import com.example.mayasfood.fragments.Dashboard_frag;
import com.example.mayasfood.fragments.SingleItem_frag;
import com.example.mayasfood.functions.Functions;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecycleView_Adapter_PF extends RecyclerView.Adapter<RecycleView_Adapter_PF.MyViewHolder> implements Filterable {

    Context context;
    ArrayList<RecycleView_Model> foodModels2;
    FirebaseAuth auth;
    MutableLiveData<Response_Common> response_commons = new MutableLiveData<>();
    ArrayList<String> productId = new ArrayList<String>();
    String foodName = "";
    List<RecycleView_Model> foodModelAll;
    //int q = 1;
    //int i = 0;

    public RecycleView_Adapter_PF(Context context, ArrayList<RecycleView_Model> foodModels2){
        this.context = context;
        this.foodModels2 = foodModels2;
        this.foodModelAll = new ArrayList<>(foodModels2);
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public RecycleView_Adapter_PF.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout (Giving look)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popular_food_rv, parent, false);
        return new RecycleView_Adapter_PF.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleView_Adapter_PF.MyViewHolder holder, int position) {
        //Assigning values to the views we created

        final RecycleView_Model temp = foodModels2.get(position);

        ArrayList<RecycleView_Model> tempFood = new ArrayList<>();

        holder.addToFav.setEnabled(true);
        holder.loading.setVisibility(View.GONE);

        tempFood.clear();
        /*ArrayList<Integer> isFav = new ArrayList<>();

        isFav.clear();*/

        for (int i = 0; i < foodModels2.size(); i++) {

            tempFood.add(foodModels2.get(position));
        }

        Log.d("rcPN", foodModels2.get(position).getFoodName());

        boolean isLogin = context.getSharedPreferences("LogIn", Context.MODE_PRIVATE).getBoolean("LogIn", false);

        if (auth.getCurrentUser() == null && isLogin == false){

            holder.addToFav.setVisibility(View.GONE);
        }
        else {

            holder.addToFav.setVisibility(View.VISIBLE);
        }

        if (auth.getCurrentUser() != null || isLogin != false) {

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

                //Toast.makeText(context, "Item added to cart", Toast.LENGTH_SHORT).show();

                if (Constants.foodName.contains(foodModels2.get(holder.getAdapterPosition()).getFoodName())) {

                    for (int j =0 ; j < Constants.foodName.size(); j++){

                        if (Constants.foodName.get(j).matches(foodModels2.get(holder.getAdapterPosition()).getFoodName()) && Constants.foodSize.get(j).matches(foodModels2.get(holder.getAdapterPosition()).getFoodSize())){
                            int q = Constants.foodQuantity.get(j);
                            Log.d("foodQ", String.valueOf(q + 1));
                            //Constants.foodQuantity.add(j, 1 + Constants.q);
                            Constants.foodQuantity.set(j, q + 1);
                            Constants.cart_totalItems = Constants.foodId.size();
                        }
                    }
                }
                else {
                        Constants.foodSize.add(foodModels2.get(holder.getAdapterPosition()).getFoodSize());
                        Constants.foodId.add(Integer.valueOf(foodModels2.get(holder.getLayoutPosition()).getProductId()));
                        Constants.foodQuantity.add(1);
                        Constants.foodImg.add(foodModels2.get(holder.getAdapterPosition()).getFoodImg());
                        Constants.foodName.add(foodModels2.get(holder.getAdapterPosition()).getFoodName());
                        if (foodModels2.get(holder.getAdapterPosition()).getOfferAmt() != "0") {
                            Constants.foodPrice.add(Integer.valueOf(Integer.valueOf(foodModels2.get(holder.getAdapterPosition()).getOfferAmt())));
                        }
                        else {
                            Constants.foodPrice.add(Integer.valueOf(Integer.valueOf(foodModels2.get(holder.getAdapterPosition()).getFoodPrice())));
                        }
                        //i++;
                    Constants.cart_totalItems = Constants.foodId.size();

                }
                DashBoard activity = (DashBoard) view.getContext();
                activity.card_count.setVisibility(View.VISIBLE);
                activity.setCartCounter();
            }
        });


        holder.addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Request_addOrRemoveToFav request_addOrRemoveToFav = new Request_addOrRemoveToFav();

                request_addOrRemoveToFav.setBranchId("1");
                request_addOrRemoveToFav.setProductId(foodModels2.get(holder.getLayoutPosition()).getProductId());

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

                                holder.isFav.setChecked(true);

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

        if (foodModels2.get(position).getFoodName().length() > 18){

            foodName = foodModels2.get(position).getFoodName().substring(0, 18) + "...";
        }
        else {

            foodName = foodModels2.get(position).getFoodName();
        }

        holder.name.setText(foodName);

        Picasso.get()
                .load(Constants.UserProduct_Path + foodModels2.get(position).getFoodImg())
                .into(holder.imageView);

        //holder.imageView.setImageResource(foodModels2.get(position).getFoodImg());

        if (foodModels2.get(position).getOfferAmt() != "0") {
            holder.orgPrice.setVisibility(View.VISIBLE);
            holder.orgPrice.setText(context.getResources().getString(R.string.Rupee) + foodModels2.get(position).getFoodPrice());
            holder.orgPrice.setPaintFlags(holder.orgPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.price.setText(context.getResources().getString(R.string.Rupee)+foodModels2.get(position).getOfferAmt());
        }
        else {
            holder.orgPrice.setVisibility(View.GONE);
            holder.price.setText(context.getResources().getString(R.string.Rupee)+foodModels2.get(position).getFoodPrice());
        }


        if (foodModels2.get(position).getStars().matches("^[0]") || foodModels2.get(position).getStars().matches("^[0][.]") || foodModels2.get(position).getStars().matches("^[1][.][12345]")){

            holder.star1.setImageResource(R.drawable.clarity_favorite_solid);
            holder.star2.setImageResource(R.drawable.vector__6_);
            holder.star3.setImageResource(R.drawable.vector__6_);
            holder.star4.setImageResource(R.drawable.vector__6_);
            holder.star5.setImageResource(R.drawable.vector__6_);
            /*holder.star2.setVisibility(View.GONE);
            holder.star3.setVisibility(View.VISIBLE);
            holder.star4.setVisibility(View.GONE);
            holder.star5.setVisibility(View.GONE);*/

        }

        else if (foodModels2.get(position).getStars().matches("^[1][.][6789]") ||foodModels2.get(position).getStars().matches("^[2][.][1234]")){

            //holder.star1.setVisibility(View.VISIBLE);
            //holder.star2.setVisibility(View.VISIBLE);
            /*holder.star3.setVisibility(View.VISIBLE);
            holder.star4.setVisibility(View.GONE);
            holder.star5.setVisibility(View.GONE);*/
            holder.star1.setImageResource(R.drawable.clarity_favorite_solid);
            holder.star2.setImageResource(R.drawable.clarity_favorite_solid);
            holder.star3.setImageResource(R.drawable.vector__6_);
            holder.star4.setImageResource(R.drawable.vector__6_);
            holder.star5.setImageResource(R.drawable.vector__6_);
        }

        else if (foodModels2.get(position).getStars().matches("^[2][.][6789]") || foodModels2.get(position).getStars().matches("^[3][.][1234]")){

            //holder.star1.setVisibility(View.VISIBLE);
           // holder.star2.setVisibility(View.VISIBLE);
            //holder.star3.setVisibility(View.VISIBLE);
            /*holder.star4.setVisibility(View.GONE);
            holder.star5.setVisibility(View.GONE);*/
            holder.star1.setImageResource(R.drawable.clarity_favorite_solid);
            holder.star2.setImageResource(R.drawable.clarity_favorite_solid);
            holder.star3.setImageResource(R.drawable.clarity_favorite_solid);
            holder.star4.setImageResource(R.drawable.vector__6_);
            holder.star5.setImageResource(R.drawable.vector__6_);
        }

        else if (foodModels2.get(position).getStars().matches("^[3][.][6789]") || foodModels2.get(position).getStars().matches("^[4][.][12345]")){

            //holder.star1.setVisibility(View.VISIBLE);
           // holder.star2.setVisibility(View.VISIBLE);
            //holder.star3.setVisibility(View.VISIBLE);
           // holder.star4.setVisibility(View.VISIBLE);
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
            //holder.star1.setVisibility(View.VISIBLE);
           // holder.star2.setVisibility(View.VISIBLE);
           // holder.star3.setVisibility(View.VISIBLE);
           // holder.star4.setVisibility(View.VISIBLE);
           // holder.star5.setVisibility(View.VISIBLE);
        }

        holder.pfCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Constants.productID = foodModels2.get(holder.getAdapterPosition()).getProductId();

                Constants.singleFoodName = foodModels2.get(holder.getAdapterPosition()).getFoodName();

                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                Functions.loadFragment(activity.getSupportFragmentManager(), new SingleItem_frag(), R.id.frag_cont, false, "Single Item", null);
            }
        });
    }

    @Override
    public int getItemCount() {
        //Number of Items you want to display
        return foodModels2.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<RecycleView_Model> filteredList = new ArrayList<>();

            if (charSequence.toString().isEmpty()){

                filteredList.addAll(foodModelAll);
            }
            else {

                    for (RecycleView_Model filterData : foodModelAll) {

                        if (filterData.getFoodName().toLowerCase().contains(charSequence.toString().toLowerCase())) {

                            filteredList.add(filterData);
                        }
                    }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;


            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            foodModels2.clear();
            foodModels2.addAll((Collection<? extends RecycleView_Model>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //grabbing the views from rv_column.xml

        ImageView  star1, star2, star3, star4, star5;
        CircleImageView imageView;
        TextView name, price, orgPrice;
        ImageView addToFav;
        CheckBox isFav;
        ProgressBar loading;
        ImageButton addToCart;
        CardView pfCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //this.setIsRecyclable(false);


            star1 = itemView.findViewById(R.id.star_1);
            star2 = itemView.findViewById(R.id.star_2);
            star3 = itemView.findViewById(R.id.star_3);
            star4 = itemView.findViewById(R.id.star_4);
            star5 = itemView.findViewById(R.id.star_5);
            imageView = itemView.findViewById(R.id.imgfood);
            name = itemView.findViewById(R.id.name_food);
            price = itemView.findViewById(R.id.food_price);
            addToFav = itemView.findViewById(R.id.addToFav);
            isFav = itemView.findViewById(R.id.isFav);
            loading = itemView.findViewById(R.id.loading_pop_rv);
            addToCart = itemView.findViewById(R.id.addToCart);
            pfCard = itemView.findViewById(R.id.pfCard);
            orgPrice = itemView.findViewById(R.id.orgPrice);

            //getFavList();

            /*for (int i =0; i < productId.size(); i++){

                for (int j = 0; j< foodModels2.size(); j++) {

                    if (foodModels2.get(j).getProductId().equals(productId.get(i))) {

                        addToFav.setImageResource(R.drawable.red_heart);

                    }
                }
            }*/

        }
    }

    /*private void addOrRemoveToFav(String productId){

        Request_addOrRemoveToFav request_addOrRemoveToFav = new Request_addOrRemoveToFav();

        request_addOrRemoveToFav.setBranchId("1");
        request_addOrRemoveToFav.setProductId(productId);

        RetrofitInstance retrofitInstance = new RetrofitInstance();

        Call<Response_Common> retrofitData = retrofitInstance.getRetrofit().addOrRemoveToFav(Constants.USER_TOKEN, request_addOrRemoveToFav);

        retrofitData.enqueue(new Callback<Response_Common>() {
            @Override
            public void onResponse(@NonNull Call<Response_Common> call, @NonNull Response<Response_Common> response) {

                if (response.isSuccessful()){

                    if (response.body().getData().getProductId() != null){

                        Log.d("ProductR", response.body().getData().getProductId().toString());

                        Toast.makeText(context, "Added to Favorite", Toast.LENGTH_SHORT).show();

                        Constants.add = 1;



                    }
                    else {
                        Constants.add = 0;
                        Toast.makeText(context, "Removed From Favorite", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Constants.add = 0;
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response_Common> call, Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Log.d("add", String.valueOf(Constants.add));

    }*/

    private void getFavList(){

        Request_Branch request_branch = new Request_Branch();

        request_branch.setBranchId("1");

        RetrofitInstance retrofitInstance = new RetrofitInstance();

        Call<Response_Common> retrofitData = retrofitInstance.getRetrofit().getFavList(Constants.USER_TOKEN, request_branch);

        retrofitData.enqueue(new Callback<Response_Common>() {
            @Override
            public void onResponse(@NonNull Call<Response_Common> call, @NonNull Response<Response_Common> response) {

                if (response.isSuccessful()){

                    productId.clear();

                    response_commons.setValue(response.body());

                    for (int i = 0; i < response_commons.getValue().getData().getFavoriteListResponce().size(); i++){

                        productId.add(i , String.valueOf(response_commons.getValue().getData().getFavoriteListResponce().get(i).getProductId()));

                        Log.d("productId", productId.get(i));
                    }

                       // Toast.makeText(context, "All Ids success", Toast.LENGTH_SHORT).show();

                }
                else {

                    Toast.makeText(context, "ID Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response_Common> call, Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
