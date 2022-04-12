package com.example.mayasfood.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mayasfood.R;
import com.example.mayasfood.activity.DashBoard;
import com.example.mayasfood.activity.singleItem;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_C;
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_PF;
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_RC;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class Dashboard_frag extends Fragment {

    private boolean isBackPressed = false;
   // NavigationView navigationView;
    //DrawerLayout drawerLayout;
   // ImageButton toolbar;
  //  ImageButton close;
   // Fragment fragment;
    ArrayList<RecycleView_Model> recycleView_models = new ArrayList<>();
    ArrayList<RecycleView_Model> recycleView_models1 = new ArrayList<>();
    ArrayList<RecycleView_Model> recycleView_models2 = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_dashboard_frag, container, false);


       // drawerLayout = v.findViewById(R.id.drawer_frag);
      //  navigationView = v.findViewById(R.id.nav_view_frag);
      //  navigationView.bringToFront();
      //  Menu menu = navigationView.getMenu();
       // toolbar = v.findViewById(R.id.toolbar_frag);

        RecyclerView recyclerView = v.findViewById(R.id.rv1);
        RecyclerView recyclerView2 = v.findViewById(R.id.rv2);
        RecyclerView recyclerView3 = v.findViewById(R.id.rv3);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView3.setLayoutManager(layoutManager3);

        setUpFoodModel();

        RecycleView_Adapter_C recycleView_adapter = new RecycleView_Adapter_C(getActivity() ,recycleView_models);
        RecycleView_Adapter_PF recycleView_adapter_pf = new RecycleView_Adapter_PF(getActivity(), recycleView_models1);
        RecycleView_Adapter_RC recycleView_adapter_rc = new RecycleView_Adapter_RC(getActivity(), recycleView_models2);

        recyclerView.setAdapter(recycleView_adapter);
        recyclerView2.setAdapter(recycleView_adapter_pf);
        recyclerView3.setAdapter(recycleView_adapter_rc);

        recycleView_adapter.notifyDataSetChanged();


      /*  toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);

                close = v.findViewById(R.id.close_frag);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        drawerLayout.isDrawerOpen(GravityCompat.START);
                        {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                    }
                });

            }
        });*/

        //navigationView.setNavigationItemSelectedListener(this);
        //Constants.setArrow(navigationView);
       // navigationView.setCheckedItem(R.id.homeNav);

        return v;
    }

    private void setUpFoodModel(){

        String[] foodName = getResources().getStringArray(R.array.Food_txt);
        String[] nameFood = getResources().getStringArray(R.array.Food_name);
        String[] foodop = getResources().getStringArray(R.array.Food_option);
        String[] foodprice = getResources().getStringArray(R.array.Food_price);
        String[] NameFood = getResources().getStringArray(R.array.Name_Food);
        String[] Food_op = getResources().getStringArray(R.array.Food_op);
        String[] Food_rate = getResources().getStringArray(R.array.Food_rate);

        for(int i = 0; i<foodName.length; i++) {
            recycleView_models.add(new RecycleView_Model(foodName[i], Constants.foodImage[i]));
        }
        for(int i = 0; i<nameFood.length; i++){
            recycleView_models1.add(new RecycleView_Model(nameFood[i], foodop[i], foodprice[i], Constants.imgFood[i]));
        }
        for (int i =0; i<NameFood.length; i++){
            recycleView_models2.add(new RecycleView_Model(NameFood[i], Food_op[i], Food_rate[i], Constants.foodimg[i]));
        }
    }

    /*@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            //go to home
            case R.id.homeNav:
                break;

            case R.id.orderNav:

                break;

            case R.id.profileNav:

                break;

            case R.id.categoriesNav:
                break;

            case R.id.notificationNav:
                break;

            case R.id.logoutNav:
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }*/


}