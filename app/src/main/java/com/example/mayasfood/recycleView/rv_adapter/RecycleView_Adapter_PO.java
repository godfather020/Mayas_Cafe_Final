package com.example.mayasfood.recycleView.rv_adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mayasfood.R;
import com.example.mayasfood.Retrofite.request.Request_Order_Rating;
import com.example.mayasfood.Retrofite.response.Response_Common;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.development.retrofit.RetrofitInstance;
import com.example.mayasfood.fragments.Orders_Single_item_frag;
import com.example.mayasfood.functions.Functions;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecycleView_Adapter_PO extends RecyclerView.Adapter<RecycleView_Adapter_PO.MyViewHolder> {

    Context context;
    ArrayList<RecycleView_Model> foodModels4;
    String rating = "";
    Dialog dialog;

    public RecycleView_Adapter_PO(Context context, ArrayList<RecycleView_Model> foodModels4) {
        this.context = context;
        this.foodModels4 = foodModels4;
    }

    @NonNull
    @Override
    public RecycleView_Adapter_PO.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout (Giving look)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.past_order_rv, parent, false);
        return new RecycleView_Adapter_PO.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleView_Adapter_PO.MyViewHolder holder, int position) {
        //Assigning values to the views we created

        final RecycleView_Model temp = foodModels4.get(position);

        holder.runOrder_num.setText("Order Id - #" + foodModels4.get(position).getRunOrder_num());
        holder.runOrder_total.setText(context.getResources().getString(R.string.Rupee) + foodModels4.get(position).getRunOrder_total());
        holder.runOrder_Quantity.setText("Number of Items " + foodModels4.get(position).getRunOrder_Quantity());
        //holder.runOrder_status.setText(foodModels4.get(position).getRunOrder_status());
//        holder.runOrder_pickup.setText(foodModels4.get(position).getRunOrder_pickup());
        holder.runOrder_date.setText(foodModels4.get(position).getRunOrder_date());
        holder.runOrder_create.setText(foodModels4.get(position).getRunOrder_create());

        Picasso.get().load(Constants.UserProduct_Path + foodModels4.get(position).getRunOrder_img())
                .into(holder.runOrder_img);

        if (foodModels4.get(position).getRunOrder_status().equals("5")) {

            holder.runOrder_status.setText("Delivered");
            holder.runOrder_status.setTextColor(context.getColorStateList(R.color.accepted));
            holder.star1.setVisibility(View.VISIBLE);
            holder.star2.setVisibility(View.VISIBLE);
            holder.star3.setVisibility(View.VISIBLE);
            holder.star4.setVisibility(View.VISIBLE);
            holder.star5.setVisibility(View.VISIBLE);
            holder.runOrder_btn.setVisibility(View.VISIBLE);
            holder.orderReview.setVisibility(View.VISIBLE);
        } else {

            holder.runOrder_status.setText("Canceled");
            holder.runOrder_status.setTextColor(context.getColorStateList(R.color.Login_signUp));
            holder.star1.setVisibility(View.GONE);
            holder.star2.setVisibility(View.GONE);
            holder.star3.setVisibility(View.GONE);
            holder.star4.setVisibility(View.GONE);
            holder.star5.setVisibility(View.GONE);
            holder.runOrder_btn.setVisibility(View.GONE);
            holder.orderReview.setVisibility(View.GONE);
        }

        if (foodModels4.get(position).getOrderComment().equals("null") && foodModels4.get(position).getRunOrder_status().equals("5")) {

            holder.runOrder_btn.setVisibility(View.VISIBLE);
            holder.orderReview.setText("You Haven't Review Yet");
        } else {

            holder.orderReview.setText(foodModels4.get(position).getOrderComment());
            holder.runOrder_btn.setVisibility(View.GONE);
        }

        if (!foodModels4.get(position).getOrderRating().equals("null")) {

            if (foodModels4.get(position).getOrderRating().matches("^[1]") || foodModels4.get(position).getOrderRating().matches("^[0]") || foodModels4.get(position).getOrderRating().matches("^[0][.]") || foodModels4.get(position).getOrderRating().matches("^[1][.][12345]")) {

                holder.star1.setImageResource(R.drawable.clarity_favorite_solid);
                holder.star2.setImageResource(R.drawable.vector__6_);
                holder.star3.setImageResource(R.drawable.vector__6_);
                holder.star4.setImageResource(R.drawable.vector__6_);
                holder.star5.setImageResource(R.drawable.vector__6_);
            /*holder.star2.setVisibility(View.GONE);
            holder.star3.setVisibility(View.VISIBLE);
            holder.star4.setVisibility(View.GONE);
            holder.star5.setVisibility(View.GONE);*/

            } else if (foodModels4.get(position).getOrderRating().matches("^[2]") || foodModels4.get(position).getOrderRating().matches("^[1][.][6789]") || foodModels4.get(position).getOrderRating().matches("^[2][.][1234]")) {

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
            } else if (foodModels4.get(position).getOrderRating().matches("^[3]") || foodModels4.get(position).getOrderRating().matches("^[2][.][6789]") || foodModels4.get(position).getOrderRating().matches("^[3][.][1234]")) {

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
            } else if (foodModels4.get(position).getOrderRating().matches("^[4]") || foodModels4.get(position).getOrderRating().matches("^[3][.][6789]") || foodModels4.get(position).getOrderRating().matches("^[4][.][12345]")) {

                //holder.star1.setVisibility(View.VISIBLE);
                // holder.star2.setVisibility(View.VISIBLE);
                //holder.star3.setVisibility(View.VISIBLE);
                // holder.star4.setVisibility(View.VISIBLE);
                holder.star1.setImageResource(R.drawable.clarity_favorite_solid);
                holder.star2.setImageResource(R.drawable.clarity_favorite_solid);
                holder.star3.setImageResource(R.drawable.clarity_favorite_solid);
                holder.star4.setImageResource(R.drawable.clarity_favorite_solid);
                holder.star5.setImageResource(R.drawable.vector__6_);
            } else {

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
        } else {

            holder.star1.setImageResource(R.drawable.vector__6_);
            holder.star2.setImageResource(R.drawable.vector__6_);
            holder.star3.setImageResource(R.drawable.vector__6_);
            holder.star4.setImageResource(R.drawable.vector__6_);
            holder.star5.setImageResource(R.drawable.vector__6_);
        }

        holder.runOrder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showRatingDialog(holder.getAbsoluteAdapterPosition());

            }
        });

        holder.pastOrderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                Constants.singleID = foodModels4.get(holder.getAdapterPosition()).getRunOrder_num();

                Log.d("SingleId", Constants.singleID);

                Functions.loadFragment(activity.getSupportFragmentManager(), new Orders_Single_item_frag(), R.id.frag_cont, false, "Single Order", null);
            }
        });

    }

    private void showRatingDialog(int position) {

        dialog = new Dialog(context);
        dialog.setCancelable(true);

        AppCompatActivity activity = (AppCompatActivity) context;

        View view = activity.getLayoutInflater().inflate(R.layout.rating_comment_dialog, null);

        dialog.setContentView(view);
        /*if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }*/

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView ratingStar1 = view.findViewById(R.id.rating_star1);
        ImageView ratingStar2 = view.findViewById(R.id.rating_star2);
        ImageView ratingStar3 = view.findViewById(R.id.rating_star3);
        ImageView ratingStar4 = view.findViewById(R.id.rating_star4);
        ImageView ratingStar5 = view.findViewById(R.id.rating_star5);
        //TextView notNow = view.findViewById(R.id.rating_notNow);
        Button update = view.findViewById(R.id.rating_submit);
        EditText orderComment = view.findViewById(R.id.orderComment);
        CircleImageView ratingImg = view.findViewById(R.id.rating_img);
        //EditText userNameE = view.findViewById(R.id.userEdit_name);

        Picasso.get()
                .load(Constants.UserProduct_Path + foodModels4.get(position).getRunOrder_img())
                .into(ratingImg);

        ratingStar1.setTag("");
        ratingStar2.setTag("");
        ratingStar3.setTag("");
        ratingStar4.setTag("");
        ratingStar5.setTag("");
        rating = "0";

        ratingStar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ratingStar1.getTag().equals("rated")) {

                    ratingStar1.setImageResource(R.drawable.vector__6_);
                    ratingStar2.setImageResource(R.drawable.vector__6_);
                    ratingStar3.setImageResource(R.drawable.vector__6_);
                    ratingStar4.setImageResource(R.drawable.vector__6_);
                    ratingStar5.setImageResource(R.drawable.vector__6_);
                    rating = "0";
                    ratingStar1.setTag("unrated");
                    ratingStar2.setTag("unrated");
                    ratingStar3.setTag("unrated");
                    ratingStar4.setTag("unrated");
                    ratingStar5.setTag("unrated");
                } else {
                    ratingStar1.setImageResource(R.drawable.clarity_favorite_solid);
                    rating = "1";
                    ratingStar1.setTag("rated");
                }

            }
        });

        ratingStar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ratingStar2.getTag().equals("rated")) {

                    ratingStar2.setImageResource(R.drawable.vector__6_);
                    ratingStar3.setImageResource(R.drawable.vector__6_);
                    ratingStar4.setImageResource(R.drawable.vector__6_);
                    ratingStar5.setImageResource(R.drawable.vector__6_);
                    rating = "1";
                    ratingStar2.setTag("unrated");
                    ratingStar3.setTag("unrated");
                    ratingStar4.setTag("unrated");
                    ratingStar5.setTag("unrated");
                } else {

                    ratingStar1.setImageResource(R.drawable.clarity_favorite_solid);
                    ratingStar2.setImageResource(R.drawable.clarity_favorite_solid);
                    rating = "2";
                    ratingStar1.setTag("rated");
                    ratingStar2.setTag("rated");
                }
            }
        });

        ratingStar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ratingStar3.getTag().equals("rated")) {

                    ratingStar3.setImageResource(R.drawable.vector__6_);
                    ratingStar4.setImageResource(R.drawable.vector__6_);
                    ratingStar5.setImageResource(R.drawable.vector__6_);
                    rating = "2";
                    ratingStar3.setTag("unrated");
                    ratingStar4.setTag("unrated");
                    ratingStar5.setTag("unrated");
                } else {

                    ratingStar1.setImageResource(R.drawable.clarity_favorite_solid);
                    ratingStar2.setImageResource(R.drawable.clarity_favorite_solid);
                    ratingStar3.setImageResource(R.drawable.clarity_favorite_solid);
                    rating = "3";
                    ratingStar1.setTag("rated");
                    ratingStar2.setTag("rated");
                    ratingStar3.setTag("rated");
                }
            }
        });

        ratingStar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ratingStar4.getTag().equals("rated")) {

                    ratingStar4.setImageResource(R.drawable.vector__6_);
                    ratingStar5.setImageResource(R.drawable.vector__6_);
                    rating = "3";
                    ratingStar4.setTag("unrated");
                    ratingStar5.setTag("unrated");
                } else {

                    ratingStar1.setImageResource(R.drawable.clarity_favorite_solid);
                    ratingStar2.setImageResource(R.drawable.clarity_favorite_solid);
                    ratingStar3.setImageResource(R.drawable.clarity_favorite_solid);
                    ratingStar4.setImageResource(R.drawable.clarity_favorite_solid);
                    rating = "4";
                    ratingStar1.setTag("rated");
                    ratingStar2.setTag("rated");
                    ratingStar3.setTag("rated");
                    ratingStar4.setTag("rated");
                }
            }
        });

        ratingStar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ratingStar5.getTag().equals("rated")) {

                    ratingStar5.setImageResource(R.drawable.vector__6_);
                    rating = "4";

                    ratingStar5.setTag("unrated");
                } else {

                    ratingStar1.setImageResource(R.drawable.clarity_favorite_solid);
                    ratingStar2.setImageResource(R.drawable.clarity_favorite_solid);
                    ratingStar3.setImageResource(R.drawable.clarity_favorite_solid);
                    ratingStar4.setImageResource(R.drawable.clarity_favorite_solid);
                    ratingStar5.setImageResource(R.drawable.clarity_favorite_solid);
                    rating = "5";
                    ratingStar1.setTag("rated");
                    ratingStar2.setTag("rated");
                    ratingStar3.setTag("rated");
                    ratingStar4.setTag("rated");
                    ratingStar5.setTag("rated");
                }
            }
        });

        Log.d("rating", rating);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!rating.equals("0") && !orderComment.getText().toString().isEmpty()){

                    rateAndCommentAPI(rating, orderComment.getText().toString(), foodModels4.get(position).getRunOrder_num());
                }
                else {
                    Toast.makeText(context, "Please give rating and comment", Toast.LENGTH_SHORT).show();
                }
                //dialog.cancel();
                Log.d("rating", rating);
            }
        });

       /* notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.cancel();
            }
        });*/

        dialog.show();
    }

    private void rateAndCommentAPI(String rating, String  orderComment, String orderId) {

        Log.d("orderId", orderId);
        Log.d("orderId", rating);
        Log.d("orderId", orderComment);

        Request_Order_Rating request_order_rating = new Request_Order_Rating();
        request_order_rating.setBranchId("1");
        request_order_rating.setOrderId(orderId);
        request_order_rating.setOrderRating(rating);
        request_order_rating.setOrderComment(orderComment);

        RetrofitInstance retrofitInstance = new RetrofitInstance();

        Call<Response_Common> retrofitData = retrofitInstance.getRetrofit().setOrderRatingComment(Constants.USER_TOKEN, request_order_rating);

        retrofitData.enqueue(new Callback<Response_Common>() {
            @Override
            public void onResponse(Call<Response_Common> call, Response<Response_Common> response) {

                if (response.isSuccessful()){

                    Log.d("success", response.message());

                    dialog.cancel();

                    Toast.makeText(context, "Thank you for reviewing this order.", Toast.LENGTH_SHORT).show();
                }
                else {

                    Log.d("success", response.message() + response.code());
                }
            }

            @Override
            public void onFailure(Call<Response_Common> call, Throwable t) {
                Log.d("failed", t.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        //Number of Items you want to display
        return foodModels4.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //grabbing the views from rv_column.xml

        TextView runOrder_num, runOrder_total, runOrder_pickup, runOrder_create, runOrder_Quantity, runOrder_status,
                runOrder_date, orderReview;
        CircleImageView runOrder_img;
        AppCompatButton runOrder_btn;
        ImageView star1, star2, star3, star4, star5;
        CardView pastOrderCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            runOrder_btn = itemView.findViewById(R.id.pastOrder_review_btn);
            runOrder_create = itemView.findViewById(R.id.pastOrder_pick_txt);
            runOrder_date = itemView.findViewById(R.id.pastOrder_date);
            runOrder_img = itemView.findViewById(R.id.past_order_img);
            runOrder_num = itemView.findViewById(R.id.pastOrder_num);
            //runOrder_pickup = itemView.findViewById(R.id.runOrder_pickup);
            runOrder_status = itemView.findViewById(R.id.pastOrder_status);
            runOrder_Quantity = itemView.findViewById(R.id.pastOrder_quantity);
            runOrder_total = itemView.findViewById(R.id.pastOrder_total);
            orderReview = itemView.findViewById(R.id.pastOrder_review_txt);
            star1 = itemView.findViewById(R.id.pastOrder_star1);
            star2 = itemView.findViewById(R.id.pastOrder_star2);
            star3 = itemView.findViewById(R.id.pastOrder_star3);
            star4 = itemView.findViewById(R.id.pastOrder_star4);
            star5 = itemView.findViewById(R.id.pastOrder_star5);
            pastOrderCard = itemView.findViewById(R.id.pastOrder_card);

        }
    }
}
