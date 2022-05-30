package com.example.mayasfood.recycleView.rv_adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mayasfood.R;
import com.example.mayasfood.activity.singleItem;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecycleView_Adapter_O extends RecyclerView.Adapter<RecycleView_Adapter_O.MyViewHolder> implements Filterable {

    Context context;
    ArrayList<RecycleView_Model> foodModels4;
    List<RecycleView_Model> foodModelAll;

    public RecycleView_Adapter_O(Context context, ArrayList<RecycleView_Model> foodModels4){
        this.context = context;
        this.foodModels4 = foodModels4;
        this.foodModelAll = new ArrayList<>(foodModels4);
    }

    @NonNull
    @Override
    public RecycleView_Adapter_O.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout (Giving look)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.offers_rv, parent, false);
        return new RecycleView_Adapter_O.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleView_Adapter_O.MyViewHolder holder, int position) {
        //Assigning values to the views we created

        final RecycleView_Model temp = foodModels4.get(position);

        holder.offer_txt.setText(foodModels4.get(position).getFoodName());
        //holder.offer_img.setBackgroundResource(foodModels4.get(position).getFoodImg());

        Picasso.get()
                .load(Constants.UserCoupon_Path + foodModels4.get(position).getFoodHeading())
                .fit()
                .into(holder.offer_img);


        holder.copy_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context.getApplicationContext(), "Coupon Code Copied", Toast.LENGTH_SHORT).show();

                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Coupon_Code", foodModels4.get(holder.getAdapterPosition()).getFoodPrice());
                clipboard.setPrimaryClip(clip);

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
        return foodModels4.size();
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

            foodModels4.clear();
            foodModels4.addAll((Collection<? extends RecycleView_Model>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //grabbing the views from rv_column.xml

        ImageView offer_img;
        ImageButton copy_offer;
        TextView offer_txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            offer_img = itemView.findViewById(R.id.offer_img);
            offer_txt = itemView.findViewById(R.id.offer_txt);
            copy_offer = itemView.findViewById(R.id.copy_offer);
        }
    }
}
