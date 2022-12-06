package com.example.mayasfood.recycleView.rv_adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.example.lottry.data.remote.retrofit.request.Request_cancelOrder;
import com.example.mayasfood.R;
import com.example.mayasfood.Retrofite.response.Response_cancelOrder;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.development.retrofit.RetrofitInstance;
import com.example.mayasfood.fragments.Orders_Single_item_frag;
import com.example.mayasfood.fragments.RunningOrders_frag;
import com.example.mayasfood.functions.Functions;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.ByteMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecycleView_Adapter_RO extends RecyclerView.Adapter<RecycleView_Adapter_RO.MyViewHolder>{

    Context context;
    ArrayList<RecycleView_Model> foodModels4;
    Boolean isCancelled = false;
    Dialog dialog;
    RunningOrders_frag runningOrders_frag;

    public RecycleView_Adapter_RO(Context context, ArrayList<RecycleView_Model> foodModels4, RunningOrders_frag runningOrders_frag){
        this.context = context;
        this.foodModels4 = foodModels4;
        this.runningOrders_frag = runningOrders_frag;
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

        holder.runOrder_num.setText("Order Id - #"+foodModels4.get(position).getRunOrder_num());
        holder.runOrder_total.setText(context.getResources().getString(R.string.Rupee)+foodModels4.get(position).getRunOrder_total());
        holder.runOrder_Quantity.setText("Number of Items "+foodModels4.get(position).getRunOrder_Quantity());
        //holder.runOrder_status.setText(foodModels4.get(position).getRunOrder_status());
        holder.runOrder_pickup.setText(foodModels4.get(position).getRunOrder_pickup());
        holder.runOrder_date.setText(foodModels4.get(position).getRunOrder_date());
        holder.runOrder_create.setText(foodModels4.get(position).getRunOrder_create());

        Picasso.get().load(Constants.UserProduct_Path+foodModels4.get(position).getRunOrder_img())
                .into(holder.runOrder_img);

        if (foodModels4.get(position).getRunOrder_status().equals("0")){

            holder.runOrder_status.setText("Pending");
            holder.runOrder_btn.setVisibility(View.VISIBLE);
            holder.runOrder_btn.setText("Cancel");
            holder.runOrder_status.setTextColor(context.getColorStateList(R.color.pending));
        }
        else if (foodModels4.get(position).getRunOrder_status().equals("1")){

            holder.runOrder_status.setText("Accepted");
            holder.runOrder_btn.setVisibility(View.GONE);
            holder.runOrder_status.setTextColor(context.getColorStateList(R.color.accepted));
        }
        else  if (foodModels4.get(position).getRunOrder_status().equals("2")){

            holder.runOrder_status.setText("Being Prepared");
            holder.runOrder_btn.setVisibility(View.GONE);
            holder.runOrder_status.setTextColor(context.getColorStateList(R.color.prepared));
        }
        else if (foodModels4.get(position).getRunOrder_status().equals("3")){

            /*holder.runOrder_status.setText("Packed");
            holder.runOrder_btn.setVisibility(View.GONE);
            holder.runOrder_status.setTextColor(context.getColorStateList(R.color.prepared));*/

            holder.runOrder_status.setText("Ready To Pickup");
            holder.runOrder_btn.setVisibility(View.VISIBLE);
            holder.runOrder_btn.setText("PickUp Order");
            holder.runOrder_status.setTextColor(context.getColorStateList(R.color.pickup));
        }
        else {

            holder.runOrder_status.setText("Ready To Pickup");
            holder.runOrder_btn.setVisibility(View.VISIBLE);
            holder.runOrder_btn.setText("PickUp Order");
            holder.runOrder_status.setTextColor(context.getColorStateList(R.color.pickup));
        }

        holder.runOrder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int status = Integer.parseInt(foodModels4.get(holder.getAbsoluteAdapterPosition()).getRunOrder_status());

                if (status >= 3){

                    showPickUpDialog(holder.getAbsoluteAdapterPosition());
                }
                else {

                    showCancelDialog(holder.getAdapterPosition());
                }

            }
        });

        holder.runOrder_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                Constants.singleID = foodModels4.get(holder.getAdapterPosition()).getOrder_id();

                Log.d("SingleId", Constants.singleID);

                Functions.loadFragment(activity.getSupportFragmentManager(), new Orders_Single_item_frag(), R.id.frag_cont, false, "Single Order", null);
            }
        });


    }

    private void showCancelDialog(int adapterPosition) {

        dialog = new Dialog(context);
        dialog.setCancelable(false);

        AppCompatActivity activity = (AppCompatActivity) context;

        View view =activity.getLayoutInflater().inflate(R.layout.order_cancel_dialog, null);

        dialog.setContentView(view);
       /* if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }*/

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView close = view.findViewById(R.id.close_dialog);
                Button update = view.findViewById(R.id.userEdit_update);
                EditText userNameE = view.findViewById(R.id.userEdit_name);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cancelOrder(foodModels4.get(adapterPosition).getOrder_id(), userNameE.getText().toString());

            }
        });

        dialog.show();
    }

    private void cancelOrder(String order_id, String reason) {

        Request_cancelOrder request_cancelOrder = new Request_cancelOrder();

        request_cancelOrder.setBranchId("1");
        request_cancelOrder.setCancelReason(reason);
        request_cancelOrder.setOrderId(order_id);

        RetrofitInstance retrofitInstance = new RetrofitInstance();

        Call<Response_cancelOrder> retrofitData = retrofitInstance.getRetrofit().cancelOrder(Constants.USER_TOKEN, request_cancelOrder);

        retrofitData.enqueue(new Callback<Response_cancelOrder>() {
            @Override
            public void onResponse(@NonNull Call<Response_cancelOrder> call, @NonNull Response<Response_cancelOrder> response) {

                if (response.isSuccessful()){

                    isCancelled = true;

                     Toast.makeText(context, "Order Cancelled", Toast.LENGTH_SHORT).show();
                     dialog.cancel();
                     runningOrders_frag.getOrderDetails();

                }
                else {

                    isCancelled = false;

                    Toast.makeText(context, "Order Not Cancelled", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response_cancelOrder> call, Throwable t) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPickUpDialog(int position) {

        dialog = new Dialog(context);
        dialog.setCancelable(true);

        AppCompatActivity activity = (AppCompatActivity) context;

        View view =activity.getLayoutInflater().inflate(R.layout.barcode_popup, null);

        dialog.setContentView(view);
       /* if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }*/

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView orderBarcode = view.findViewById(R.id.order_barcode1);
        Button closeQr = view.findViewById(R.id.close_qr);

        QRCodeWriter writer = new QRCodeWriter();
        try {
            ByteMatrix bitMatrix = writer.encode(foodModels4.get(position).getOrder_id()+" "+foodModels4.get(position).getRunOrder_date()+" "+foodModels4.get(position).getRunOrder_pickup(), BarcodeFormat.QR_CODE, 512, 512);
            int width = 512;
            int height = 512;
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (bitMatrix.get(x, y)==0)
                        bmp.setPixel(x, y, Color.BLACK);
                    else
                        bmp.setPixel(x, y, Color.WHITE);
                }
            }
            orderBarcode.setImageBitmap(bmp);
        } catch (WriterException e) {
            //Log.e("QR ERROR", ""+e);

        }

        closeQr.setOnClickListener(new View.OnClickListener() {
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
        runOrder_date;
        CircleImageView runOrder_img;
        AppCompatButton runOrder_btn;
        CardView runOrder_card;

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
            runOrder_card = itemView.findViewById(R.id.runOrder_card);


        }
    }


}
