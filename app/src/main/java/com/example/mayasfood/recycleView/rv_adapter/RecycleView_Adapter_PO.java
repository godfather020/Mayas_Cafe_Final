package com.example.mayasfood.recycleView.rv_adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mayasfood.R;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleView_Adapter_PO extends RecyclerView.Adapter<RecycleView_Adapter_PO.MyViewHolder>{

    Context context;
    ArrayList<RecycleView_Model> foodModels4;

    public RecycleView_Adapter_PO(Context context, ArrayList<RecycleView_Model> foodModels4){
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

        holder.runOrder_num.setText(foodModels4.get(position).getRunOrder_num());
        holder.runOrder_total.setText("$"+foodModels4.get(position).getRunOrder_total());
        holder.runOrder_Quantity.setText("Number of Items "+foodModels4.get(position).getRunOrder_Quantity());
        //holder.runOrder_status.setText(foodModels4.get(position).getRunOrder_status());
//        holder.runOrder_pickup.setText(foodModels4.get(position).getRunOrder_pickup());
        holder.runOrder_date.setText(foodModels4.get(position).getRunOrder_date());
        holder.runOrder_create.setText(foodModels4.get(position).getRunOrder_create());

        Picasso.get().load(Constants.UserProduct_Path+foodModels4.get(position).getRunOrder_img())
                .into(holder.runOrder_img);

        if (foodModels4.get(position).getRunOrder_status().equals("5")){

            holder.runOrder_status.setText("Delivered");
            holder.runOrder_status.setTextColor(context.getColorStateList(R.color.accepted));
        }
        else {

            holder.runOrder_status.setText("Canceled");
            holder.runOrder_status.setTextColor(context.getColorStateList(R.color.accepted));
        }

        if (foodModels4.get(position).getOrderComment().equals("null")){

            holder.runOrder_btn.setVisibility(View.VISIBLE);
            holder.orderReview.setText("You Haven't Review Yet");
        }
        else {

            holder.orderReview.setText(foodModels4.get(position).getOrderComment());
            holder.runOrder_btn.setVisibility(View.GONE);
        }

        if (!foodModels4.get(position).getOrderRating().equals("null")) {

            if (foodModels4.get(position).getOrderRating().matches("^[0]") || foodModels4.get(position).getOrderRating().matches("^[0][.]") || foodModels4.get(position).getOrderRating().matches("^[1][.][12345]")) {

                holder.star1.setImageResource(R.drawable.clarity_favorite_solid);
                holder.star2.setImageResource(R.drawable.vector__6_);
                holder.star3.setImageResource(R.drawable.vector__6_);
                holder.star4.setImageResource(R.drawable.vector__6_);
                holder.star5.setImageResource(R.drawable.vector__6_);
            /*holder.star2.setVisibility(View.GONE);
            holder.star3.setVisibility(View.VISIBLE);
            holder.star4.setVisibility(View.GONE);
            holder.star5.setVisibility(View.GONE);*/

            } else if (foodModels4.get(position).getOrderRating().matches("^[1][.][6789]") || foodModels4.get(position).getOrderRating().matches("^[2][.][1234]")) {

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
            } else if (foodModels4.get(position).getOrderRating().matches("^[2][.][6789]") || foodModels4.get(position).getOrderRating().matches("^[3][.][1234]")) {

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
            } else if (foodModels4.get(position).getOrderRating().matches("^[3][.][6789]") || foodModels4.get(position).getOrderRating().matches("^[4][.][12345]")) {

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
        }
        else {

            holder.star1.setImageResource(R.drawable.vector__6_);
            holder.star2.setImageResource(R.drawable.vector__6_);
            holder.star3.setImageResource(R.drawable.vector__6_);
            holder.star4.setImageResource(R.drawable.vector__6_);
            holder.star5.setImageResource(R.drawable.vector__6_);
        }

        holder.runOrder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showRatingDialog();

            }
        });

    }

    private void showRatingDialog() {

        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);

        AppCompatActivity activity = (AppCompatActivity) context;

        View view =activity.getLayoutInflater().inflate(R.layout.rating_comment_dialog, null);

        dialog.setContentView(view);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
        }

        TextView notNow = view.findViewById(R.id.rating_notNow);
        Button update = view.findViewById(R.id.rating_submit);
        //EditText userNameE = view.findViewById(R.id.userEdit_name);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.cancel();
            }
        });

        dialog.show();
    }

    @Override
    public int getItemCount() {
        //Number of Items you want to display
        return foodModels4.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //grabbing the views from rv_column.xml

        TextView runOrder_num , runOrder_total, runOrder_pickup, runOrder_create, runOrder_Quantity, runOrder_status,
                runOrder_date, orderReview;
        CircleImageView runOrder_img;
        AppCompatButton runOrder_btn;
        ImageView star1, star2, star3, star4, star5;

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

        }
    }
}
