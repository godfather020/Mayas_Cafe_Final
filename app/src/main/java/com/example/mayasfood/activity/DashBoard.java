package com.example.mayasfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mayasfood.MainActivity;
import com.example.mayasfood.R;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.fragments.Dashboard_frag;
import com.example.mayasfood.fragments.Favorite_frag;
import com.example.mayasfood.fragments.Profile_frag;
import com.example.mayasfood.fragments.Search_frag;
import com.example.mayasfood.functions.Functions;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_C;
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_PF;
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_RC;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem;
import np.com.susanthapa.curved_bottom_navigation.CurvedBottomNavigationView;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private boolean isBackPressed = false;
    Toolbar toolbar_const;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ImageButton close;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        setUpToolbar();

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont, new Dashboard_frag()).commit();

        CurvedBottomNavigationView cbn = findViewById(R.id.chip_nav);
        CbnMenuItem dashboard = new CbnMenuItem(R.drawable.mdi___view_grid_outline, R.drawable.dashboard_anim, 0);
        CbnMenuItem search = new CbnMenuItem(R.drawable.icon_feather_search_r, R.drawable.search_anim, 0);
        CbnMenuItem favorite = new CbnMenuItem(R.drawable.icon_feather_heart_red, R.drawable.avd_anim, 0);
        CbnMenuItem profile = new CbnMenuItem(R.drawable.icon_feather_user_red, R.drawable.profile_anim,0);
        CbnMenuItem[] navigation_items = {dashboard,search,favorite,profile};
        cbn.setMenuItems(navigation_items, 0);

        cbn.setOnMenuItemClickListener(new Function2<CbnMenuItem, Integer, Unit>() {
            @Override
            public Unit invoke(CbnMenuItem cbnMenuItem, Integer integer) {

                switch (cbn.getSelectedIndex()){

                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont, new Dashboard_frag()).commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont, new Search_frag()).commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont, new Favorite_frag()).commit();
                        break;
                    case 3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont, new Profile_frag()).commit();
                        break;
                }
                return null;
            }
        });


       /* cbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbn.getSelectedIndex() == 0){

                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont, new Dashboard_frag()).commit();
                }
                else if (cbn.getSelectedIndex() == 1){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_cont, new Search_frag()).commit();

                }
            }
        });*/


    }

    public void setUpToolbar(){

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        Menu menu = navigationView.getMenu();
        toolbar_const = findViewById(R.id.toolbar_const);
        setSupportActionBar(toolbar_const);
        navigationView.setNavigationItemSelectedListener(this);
        Functions.setArrow(navigationView);
        navigationView.setCheckedItem(R.id.homeNav);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar_const, R.string.app_name, R.string.app_name);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.group_8);

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                actionBarDrawerToggle.syncState();
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.group_8);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                actionBarDrawerToggle.syncState();
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.group_8);

                close = findViewById(R.id.close_frag);

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

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                actionBarDrawerToggle.syncState();
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.group_8);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                actionBarDrawerToggle.syncState();
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.group_8);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.notify){
            Toast.makeText(getApplicationContext(), "Notifications", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.setting){
            Toast.makeText(getApplicationContext(), "Setting", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        if(isBackPressed){
            super.onBackPressed();
            return;
        }

        Toast.makeText(DashBoard.this, "Press again to exit", Toast.LENGTH_SHORT).show();
        isBackPressed = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackPressed = false;
            }
        },2000);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            //go to home
            case R.id.homeNav:
                break;

            case R.id.orderNav:
                startActivity(new Intent(DashBoard.this, CheckOut.class));
                finish();
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
    }

}