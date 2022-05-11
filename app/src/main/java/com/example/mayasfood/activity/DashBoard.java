package com.example.mayasfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lottry.utils.shared_prefrence.SharedPreferencesUtil;
import com.example.mayasfood.FirebaseCloudMsg;
import com.example.mayasfood.R;
import com.example.mayasfood.activity.ViewModels.GetStart_ViewModel;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.fragments.Category_frag;
import com.example.mayasfood.fragments.Dashboard_frag;
import com.example.mayasfood.fragments.Favorite_frag;
import com.example.mayasfood.fragments.Notification_frag;
import com.example.mayasfood.fragments.Offers_frag;
import com.example.mayasfood.fragments.Orders_frag;
import com.example.mayasfood.fragments.UserProfile_frag;
import com.example.mayasfood.functions.Functions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private boolean isBackPressed = false;
    public Toolbar toolbar_const;
    public NavigationView navigationView;
    DrawerLayout drawerLayout;
    ImageButton close;
    public CircleImageView user_profile;
    TextView user_name_nav;
    ActionBarDrawerToggle actionBarDrawerToggle;
    public BottomNavigationView bottomNavigationView;
    FirebaseAuth auth;
    String userProfile;
    SharedPreferencesUtil sharedPreferencesUtil;
    GetStart_ViewModel getStart_viewModel;
    ViewModelProvider viewModelProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        String token = FirebaseCloudMsg.getToken(this);
        Log.d("mainToken", token);

        viewModelProvider = new ViewModelProvider(this);
        getStart_viewModel = viewModelProvider.get(GetStart_ViewModel.class);

        sharedPreferencesUtil = new SharedPreferencesUtil(this);

        /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/

        Constants.USER_NAME = getSharedPreferences(Constants.sharedPrefrencesConstant.USER_N, MODE_PRIVATE).getString(Constants.sharedPrefrencesConstant.USER_N, "Ramu Kaka");
        Constants.USER_PHONE = getSharedPreferences(Constants.sharedPrefrencesConstant.USER_P, MODE_PRIVATE).getString(Constants.sharedPrefrencesConstant.USER_P, "");
        Constants.USER_TOKEN = getSharedPreferences(Constants.sharedPrefrencesConstant.X_TOKEN, MODE_PRIVATE).getString(Constants.sharedPrefrencesConstant.X_TOKEN, "");
        //userProfile = getSharedPreferences(Constants.sharedPrefrencesConstant.USER_I, MODE_PRIVATE).getString(Constants.sharedPrefrencesConstant.USER_I, "default.png");
        userProfile = sharedPreferencesUtil.getString(Constants.sharedPrefrencesConstant.USER_I);

        if(userProfile != null) {
            Log.d("userProfile", userProfile);
        }
        Log.d("userToken", Constants.USER_TOKEN);

        drawerLayout = findViewById(R.id.drawer);
        toolbar_const = findViewById(R.id.toolbar_const);

        setUpToolbar();

        Functions.loadFragment(getSupportFragmentManager(), new Dashboard_frag(), R.id.frag_cont, true, "DashBoard", null );

        bottomNavigationView = findViewById(R.id.chip_nav);

        bottomNavigationView.setSelectedItemId(R.id.invisible);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.bottom_nav_category:
                        navigationView.setCheckedItem(R.id.categoriesNav);
                        toolbar_const.getMenu().getItem(0).setVisible(true);
                        toolbar_const.getMenu().getItem(2).setVisible(true);
                        toolbar_const.getMenu().getItem(1).setVisible(true);
                        Functions.loadFragment(getSupportFragmentManager(), new Category_frag(),R.id.frag_cont, true, "Category", null);
                        return true;

                    case R.id.bottom_nav_favorie:
                        navigationView.setCheckedItem(R.id.invisibleNav);
                        toolbar_const.getMenu().getItem(0).setVisible(true);
                        toolbar_const.getMenu().getItem(2).setVisible(true);
                        toolbar_const.getMenu().getItem(1).setVisible(true);
                        Functions.loadFragment(getSupportFragmentManager(), new Favorite_frag(),R.id.frag_cont, true, "Favorites", null);
                        return true;

                    case R.id.bottom_nav_orders:
                        navigationView.setCheckedItem(R.id.orderNav);
                        toolbar_const.getMenu().getItem(0).setVisible(false);
                        toolbar_const.getMenu().getItem(2).setVisible(false);
                        toolbar_const.getMenu().getItem(1).setVisible(true);
                        Functions.loadFragment(getSupportFragmentManager(), new Orders_frag(),R.id.frag_cont, true, "Orders", null);
                        return true;

                    case R.id.bottom_nav_discount:
                        navigationView.setCheckedItem(R.id.offersNav);
                        toolbar_const.getMenu().getItem(0).setVisible(true);
                        toolbar_const.getMenu().getItem(2).setVisible(false);
                        toolbar_const.getMenu().getItem(1).setVisible(false);
                        Functions.loadFragment(getSupportFragmentManager(), new Offers_frag(),R.id.frag_cont, true, "Offers", null);
                        return true;

                    case R.id.invisible:
                        navigationView.setCheckedItem(R.id.homeNav);
                        toolbar_const.setTitle("");
                        toolbar_const.getMenu().getItem(0).setVisible(true);
                        toolbar_const.getMenu().getItem(2).setVisible(true);
                        toolbar_const.getMenu().getItem(1).setVisible(true);
                        Functions.loadFragment(getSupportFragmentManager(), new Dashboard_frag(), R.id.frag_cont, true, "DashBoard", null );
                        return true;

                }

                return false;
            }
        });

        getDashboardItems();

    }

    private void getDashboardItems() {


    }

    public void setUpToolbar() {

        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        Menu menu = navigationView.getMenu();
        setSupportActionBar(toolbar_const);
        navigationView.setNavigationItemSelectedListener(this);
        Functions.setArrow(navigationView);

        navigationView.setCheckedItem(R.id.homeNav);

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar_const,
                R.string.app_name,
                R.string.app_name
        ){

            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);

                close = findViewById(R.id.close_frag);
                user_profile = findViewById(R.id.user_profile);
                user_name_nav = findViewById(R.id.user_name_nav);

                userProfile = sharedPreferencesUtil.getString(Constants.sharedPrefrencesConstant.USER_I);

                if (userProfile != null) {

                    Picasso.get()
                            .load(Constants.UserProfile_Path + userProfile)
                            .into(user_profile);
                }

                //user_profile.setImageBitmap(getBitmapFromURL(Constants.UserProfile_Path+userProfile));


                user_name_nav.setText(Constants.USER_NAME);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        drawerLayout.isDrawerOpen(GravityCompat.START);
                        {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                    }
                });

                user_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        drawerLayout.isDrawerOpen(GravityCompat.START);
                        {
                            Functions.loadFragment(getSupportFragmentManager(), new UserProfile_frag(), R.id.frag_cont, false, "UserProfile", null);
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                    }
                });
            }
        };

        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_const.getNavigationIcon().setTint(getResources().getColor(R.color.black));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);

                    toolbar_const.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getSupportFragmentManager().popBackStack();
                        }
                    });

                } else {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    actionBarDrawerToggle.syncState();
                    toolbar_const.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            drawerLayout.openDrawer(GravityCompat.START);
                        }
                    });
                }
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

        if (id == R.id.search) {
            Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.notification) {

            toolbar_const.getMenu().getItem(1).setVisible(false);
            navigationView.setCheckedItem(R.id.notificationNav);
            Functions.loadFragment(getSupportFragmentManager(), new Notification_frag(), R.id.frag_cont, true, "Notification", null);

            Toast.makeText(getApplicationContext(), "notification", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.cart) {
            Toast.makeText(getApplicationContext(), "Cart", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        if (isBackPressed) {
            super.onBackPressed();
            this.finish();
            return;
        }

        Toast.makeText(DashBoard.this, "Press again to exit", Toast.LENGTH_SHORT).show();
        isBackPressed = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackPressed = false;
            }
        }, 2000);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            //go to home
            case R.id.homeNav:

                bottomNavigationView.setSelectedItemId(R.id.invisible);
                //Functions.loadFragment(getSupportFragmentManager(), new Dashboard_frag(), R.id.frag_cont, true, "DashBoard", null );
                break;

            case R.id.orderNav:
                bottomNavigationView.setSelectedItemId(R.id.bottom_nav_orders);
                break;

            case R.id.offersNav:
                bottomNavigationView.setSelectedItemId(R.id.bottom_nav_discount);
                break;

            case R.id.categoriesNav:
                bottomNavigationView.setSelectedItemId(R.id.bottom_nav_category);
                break;

            case R.id.notificationNav:
                navigationView.setCheckedItem(R.id.notificationNav);
                Functions.loadFragment(getSupportFragmentManager(), new Notification_frag(), R.id.frag_cont, true, "Notification", null);
                break;

            case R.id.logoutNav:

                drawerLayout.closeDrawer(GravityCompat.START);
                AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);
                builder.setCancelable(false);
                builder.setTitle("Do you want to logout");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        auth = FirebaseAuth.getInstance();
                        auth.signOut();

                        getSharedPreferences("LogIn", MODE_PRIVATE).edit().putBoolean("LogIn", false).apply();
                        startActivity(new Intent(DashBoard.this, Login.class));
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                Dialog alertDialog = builder.create();
                alertDialog.show();

                break;

        }

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);

                    toolbar_const.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getSupportFragmentManager().popBackStack();
                        }
                    });

                } else {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    actionBarDrawerToggle.syncState();
                    toolbar_const.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            drawerLayout.openDrawer(GravityCompat.START);
                        }
                    });
                }
            }
        });

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }
}