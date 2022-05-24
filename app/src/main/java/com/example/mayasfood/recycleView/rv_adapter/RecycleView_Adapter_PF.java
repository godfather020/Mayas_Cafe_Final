package com.example.mayasfood.recycleView.rv_adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mayasfood.Retrofite.request.Request_Branch;
import com.example.lottry.data.remote.retrofit.request.Request_addOrRemoveToFav;
import com.example.mayasfood.R;
import com.example.mayasfood.Retrofite.response.Response_Common;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.development.retrofit.RetrofitInstance;
import com.example.mayasfood.fragments.Dashboard_frag;
import com.example.mayasfood.functions.Functions;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecycleView_Adapter_PF extends RecyclerView.Adapter<RecycleView_Adapter_PF.MyViewHolder> {

    Context context;
    ArrayList<RecycleView_Model> foodModels2;
    FirebaseAuth auth;
    MutableLiveData<Response_Common> response_commons = new MutableLiveData<>();
    ArrayList<String> productId = new ArrayList<String>();
    String foodName = "";
    //int q = 1;
    int i = 0;

    public RecycleView_Adapter_PF(Context context, ArrayList<RecycleView_Model> foodModels2){
        this.context = context;
        this.foodModels2 = foodModels2;
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

        if (auth.getCurrentUser() == null){

            holder.addToFav.setVisibility(View.GONE);
        }
        else {

            holder.addToFav.setVisibility(View.VISIBLE);
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

                if (Constants.foodName.contains(foodModels2.get(holder.getAdapterPosition()).getFoodName())) {

                    for (int j =0 ; j < Constants.foodName.size(); j++){

                        if (Constants.foodName.get(j).matches(foodModels2.get(holder.getAdapterPosition()).getFoodName())){
                            int q = Constants.foodQuantity.get(j);
                            Log.d("foodQ", String.valueOf(q + 1));
                            //Constants.foodQuantity.add(j, 1 + Constants.q);
                            Constants.foodQuantity.set(j, q + 1);
                        }
                    }
                }
                else {

                        Constants.foodId.add(i, Integer.valueOf(foodModels2.get(holder.getLayoutPosition()).getProductId()));
                        Constants.foodQuantity.add(i,1);
                        Constants.foodImg.add(i,foodModels2.get(holder.getAdapterPosition()).getFoodImg());
                        Constants.foodName.add(i,foodModels2.get(holder.getAdapterPosition()).getFoodName());
                        Constants.foodPrice.add(i, Integer.valueOf(Integer.valueOf(foodModels2.get(holder.getAdapterPosition()).getFoodPrice())));
                        i++;
                    }
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

        if (foodModels2.get(position).getFoodName().length() > 13){

            foodName = foodModels2.get(position).getFoodName().substring(0, 13) + "...";
        }
        else {

            foodName = foodModels2.get(position).getFoodName();
        }

        holder.name.setText(foodName);

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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //grabbing the views from rv_column.xml

        ImageView  star1, star2, star3, star4, star5;
        CircleImageView imageView;
        TextView name, price;
        ImageView addToFav;
        CheckBox isFav;
        ProgressBar loading;
        ImageButton addToCart;

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
