package com.example.mayasfood.recycleView.rv_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mayasfood.R;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;

import java.util.ArrayList;

public class RecycleView_Adapter_N extends RecyclerView.Adapter<RecycleView_Adapter_N.MyViewHolder>{

    Context context;
    ArrayList<RecycleView_Model> foodModels4;

    public RecycleView_Adapter_N(Context context, ArrayList<RecycleView_Model> foodModels4){
        this.context = context;
        this.foodModels4 = foodModels4;
    }

    @NonNull
    @Override
    public RecycleView_Adapter_N.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout (Giving look)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.today_rv, parent, false);
        return new RecycleView_Adapter_N.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleView_Adapter_N.MyViewHolder holder, int position) {
        //Assigning values to the views we created

        final RecycleView_Model temp = foodModels4.get(position);

        holder.noti_time.setText(foodModels4.get(position).getFoodImg());
        holder.noti_body.setText(foodModels4.get(position).getFoodName());


        /*holder.copy_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context.getApplicationContext(), "Code Copied", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, singleItem.class);
                intent.putExtra("imagefood", temp.getFoodImg());
                intent.putExtra("foodname", temp.getFoodName());
                intent.putExtra("foodprice", temp.getFoodPrice());
                intent.putExtra("fooddes", temp.getFoodHeading());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        //Number of Items you want to display
        return foodModels4.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //grabbing the views from rv_column.xml


        TextView noti_body, noti_time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            noti_body = itemView.findViewById(R.id.noti_body);
            noti_time = itemView.findViewById(R.id.noti_time);
        }
    }
}
