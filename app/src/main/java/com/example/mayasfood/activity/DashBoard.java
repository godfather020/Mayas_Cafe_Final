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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lottry.utils.shared_prefrence.SharedPreferencesUtil;
import com.example.mayasfood.FirebaseCloudMsg;
import com.example.mayasfood.R;
import com.example.mayasfood.activity.ViewModels.GetStart_ViewModel;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.fragments.Category_frag;
import com.example.mayasfood.fragments.CheckOut_frag;
import com.example.mayasfood.fragments.Dashboard_frag;
import com.example.mayasfood.fragments.Favorite_frag;
import com.example.mayasfood.fragments.NotLogIn_frag;
import com.example.mayasfood.fragments.Notification_frag;
import com.example.mayasfood.fragments.Offers_frag;
import com.example.mayasfood.fragments.Orders_frag;
import com.example.mayasfood.fragments.Search_frag;
import com.example.mayasfood.fragments.UserProfile_frag;
import com.example.mayasfood.functions.Functions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    TextView user_num_nav;
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

        auth = FirebaseAuth.getInstance();

        viewModelProvider = new ViewModelProvider(this);
        getStart_viewModel = viewModelProvider.get(GetStart_ViewModel.class);

        sharedPreferencesUtil = new SharedPreferencesUtil(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Constants.USER_NAME = getSharedPreferences(Constants.sharedPrefrencesConstant.USER_N, MODE_PRIVATE).getString(Constants.sharedPrefrencesConstant.USER_N, "Ramu Kaka");
        Constants.USER_PHONE = getSharedPreferences(Constants.sharedPrefrencesConstant.USER_P, MODE_PRIVATE).getString(Constants.sharedPrefrencesConstant.USER_P, "");
        Constants.USER_TOKEN = getSharedPreferences(Constants.sharedPrefrencesConstant.X_TOKEN, MODE_PRIVATE).getString(Constants.sharedPrefrencesConstant.X_TOKEN, "");
        //userProfile = getSharedPreferences(Constants.sharedPrefrencesConstant.USER_I, MODE_PRIVATE).getString(Constants.sharedPrefrencesConstant.USER_I, "default.png");
        userProfile = sharedPreferencesUtil.getString(Constants.sharedPrefrencesConstant.USER_I);

        Constants.USER_NAME = sharedPreferencesUtil.getString(Constants.sharedPrefrencesConstant.USER_N);


        if(userProfile != null) {
            Log.d("userProfile", userProfile);
        }
        Log.d("userToken", Constants.USER_TOKEN);

        drawerLayout = findViewById(R.id.drawer);
        toolbar_const = findViewById(R.id.toolbar_const);

        setUpToolbar();

        Functions.loadFragment(getSupportFragmentManager(), new Dashboard_frag(), R.id.frag_cont, true, "DashBoard", null );

        bottomNavigationView = findViewById(R.id.chip_nav);

        bottomNavigationView.setVisibility(View.VISIBLE);

        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_category);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.bottom_nav_category:
                        navigationView.setCheckedItem(R.id.homeNav);
                        toolbar_const.setTitle("");
                        item.setChecked(true);
                        item.setEnabled(true);
                        item.setCheckable(true);
                        toolbar_const.getMenu().getItem(0).setVisible(true);
                        toolbar_const.getMenu().getItem(2).setVisible(true);
                        toolbar_const.getMenu().getItem(1).setVisible(true);
                        Functions.loadFragment(getSupportFragmentManager(), new Dashboard_frag(),R.id.frag_cont, true, "Category", null);
                        return true;

                    case R.id.bottom_nav_favorie:
                        navigationView.setCheckedItem(R.id.invisibleNav);
                        toolbar_const.getMenu().getItem(0).setVisible(true);
                        toolbar_const.getMenu().getItem(2).setVisible(true);
                        toolbar_const.getMenu().getItem(1).setVisible(true);

                        if (auth.getCurrentUser()!=null) {

                            Functions.loadFragment(getSupportFragmentManager(), new Favorite_frag(), R.id.frag_cont, true, "Favorites", null);
                        }
                        else {

                            Functions.loadFragment(getSupportFragmentManager(), new NotLogIn_frag(), R.id.frag_cont, true, "Faveroties", null);
                        }
                        return true;

                    case R.id.bottom_nav_orders:
                        navigationView.setCheckedItem(R.id.orderNav);
                        toolbar_const.getMenu().getItem(0).setVisible(false);
                        toolbar_const.getMenu().getItem(2).setVisible(false);
                        toolbar_const.getMenu().getItem(1).setVisible(true);
                        if (auth.getCurrentUser()!=null) {

                            Functions.loadFragment(getSupportFragmentManager(), new Orders_frag(), R.id.frag_cont, true, "Orders", null);
                        }
                        else{

                            Functions.loadFragment(getSupportFragmentManager(), new NotLogIn_frag(), R.id.frag_cont, true, "Faveroties", null);
                        }
                        return true;

                    case R.id.bottom_nav_discount:
                        navigationView.setCheckedItem(R.id.offersNav);
                        toolbar_const.getMenu().getItem(0).setVisible(true);
                        toolbar_const.getMenu().getItem(2).setVisible(false);
                        toolbar_const.getMenu().getItem(1).setVisible(false);
                        Functions.loadFragment(getSupportFragmentManager(), new Offers_frag(),R.id.frag_cont, true, "Offers", null);
                        return true;

                    case R.id.invisible:
                        navigationView.setCheckedItem(R.id.categoriesNav);
                        //toolbar_const.setTitle("");
                        toolbar_const.getMenu().getItem(0).setVisible(true);
                        toolbar_const.getMenu().getItem(2).setVisible(true);
                        toolbar_const.getMenu().getItem(1).setVisible(true);
                        Functions.loadFragment(getSupportFragmentManager(), new Category_frag(), R.id.frag_cont, false, "DashBoard", null );
                        return true;

                }

                return false;
            }
        });

    }

    public void setUpToolbar() {

        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        Menu menu = navigationView.getMenu();
        setSupportActionBar(toolbar_const);
        navigationView.setNavigationItemSelectedListener(this);
        //Functions.setArrow(navigationView);

        navigationView.setCheckedItem(R.id.homeNav);

        if (auth.getCurrentUser() == null) {

            navigationView.getMenu().getItem(6).setTitle("Login");

        }

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
                user_num_nav = findViewById(R.id.user_num_nav);

                if (Constants.USER_NAME != null) {
                    user_name_nav.setText(Constants.USER_NAME);

                }

                if (auth.getCurrentUser() != null){

                    user_num_nav.setText(Constants.USER_PHONE);
                }
                else {

                    user_num_nav.setVisibility(View.GONE);
                }

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        drawerLayout.isDrawerOpen(GravityCompat.START);
                        {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                    }
                });

                userProfile = sharedPreferencesUtil.getString(Constants.sharedPrefrencesConstant.USER_I);

                if (auth.getCurrentUser() != null) {

                    if (userProfile != null) {

                    /*BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(Constants.UserProfile_Path + userProfile,bmOptions);
                    bitmap = Bitmap.createScaledBitmap(bitmap,150,150,true);*/

                        //Bitmap bitmap = rotateImageIfRequired(Constants.UserProfile_Path + userProfile);

                        //loadBitmapByPicasso(getApplicationContext(), bitmap, user_profile);

                        Picasso.get()
                                .load(Constants.UserProfile_Path + userProfile)
                                .into(user_profile);

                        //user_profile.setImageBitmap(bitmap);
                    }
                }
                else {

                    Picasso.get()
                            .load(R.drawable.mask_group_1)
                            .into(user_profile);
                }

                if (auth.getCurrentUser() != null) {

                    user_profile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            drawerLayout.isDrawerOpen(GravityCompat.START);
                            {
                                bottomNavigationView.setVisibility(View.GONE);
                                Functions.loadFragment(getSupportFragmentManager(), new UserProfile_frag(), R.id.frag_cont, false, "UserProfile", null);
                                drawerLayout.closeDrawer(GravityCompat.START);
                            }
                        }
                    });
                }
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
                            Log.d("backClick", "back");

                            Log.d("backpop", String.valueOf(Constants.popBack));

                           /* if (Constants.poptToMainCat == 1){

                               // bottomNavigationView.setSelectedItemId(R.id.invisible);
                                Functions.loadFragment(getSupportFragmentManager(), new Category_frag(), R.id.frag_cont, false, "Dashboard", null);
                                bottomNavigationView.setVisibility(View.GONE);
                                //getSupportFragmentManager().popBackStack();
                                Constants.poptToMainCat = 0;
                            }

                            else if (Constants.popBack == 1){

                                Functions.loadFragment(getSupportFragmentManager(), new Dashboard_frag(), R.id.frag_cont, true, "Dashboard", null);
                                getSupportFragmentManager().popBackStackImmediate();
                                getSupportFragmentManager().popBackStack();
                                //bottomNavigationView.setSelectedItemId(R.id.bottom_nav_category);
                                //Functions.loadFragment(getSupportFragmentManager(), new Dashboard_frag(), R.id.frag_cont, true, "Dashboard", null);
                                //getSupportFragmentManager().popBackStackImmediate();
                            }
                            else {
                                navigationView.setCheckedItem(R.id.homeNav);
                                bottomNavigationView.setSelectedItemId(R.id.bottom_nav_category);
                                getSupportFragmentManager().popBackStackImmediate();
                                getSupportFragmentManager().popBackStack();
                            }*/

                            //bottomNavigationView.setVisibility(View.VISIBLE);
                            //bottomNavigationView.setSelectedItemId(R.id.bottom_nav_category);

                            Constants.onetTime = 1;
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

        if (Constants.currentFrag.equals("N")){

            menu.getItem(1).setVisible(true);
        }
        else if (Constants.currentFrag.equals("S")){

            menu.getItem(0).setVisible(true);
        }
        else {

            menu.getItem(2).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search) {
            Constants.currentFrag = "S";
            bottomNavigationView.setVisibility(View.GONE);
            toolbar_const.getMenu().getItem(0).setVisible(false);
            //Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_SHORT).show();
            Functions.loadFragment(getSupportFragmentManager(), new Search_frag(), R.id.frag_cont, false, "Search", null);

        } else if (id == R.id.notification) {

            if (auth.getCurrentUser() != null) {
                Constants.currentFrag = "N";
                toolbar_const.getMenu().getItem(1).setVisible(false);
                navigationView.setCheckedItem(R.id.notificationNav);
                bottomNavigationView.setVisibility(View.GONE);
                Functions.loadFragment(getSupportFragmentManager(), new Notification_frag(), R.id.frag_cont, false, "Notification", null);
            }
            else {
                
                dialog("Please Login/Register to see notifications.");
            }

            //Toast.makeText(getApplicationContext(), "notification", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.cart) {
            Constants.currentFrag = "C";
            bottomNavigationView.setVisibility(View.GONE);
            toolbar_const.getMenu().getItem(2).setVisible(false);
            Functions.loadFragment(getSupportFragmentManager(), new CheckOut_frag(), R.id.frag_cont, false, "CheckOut", null);
            //Toast.makeText(getApplicationContext(), "Cart", Toast.LENGTH_SHORT).show();
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

                bottomNavigationView.setSelectedItemId(R.id.bottom_nav_category);
                //Functions.loadFragment(getSupportFragmentManager(), new Dashboard_frag(), R.id.frag_cont, true, "DashBoard", null );
                break;

            case R.id.orderNav:

                bottomNavigationView.setSelectedItemId(R.id.bottom_nav_orders);
                break;

            case R.id.offersNav:
                bottomNavigationView.setSelectedItemId(R.id.bottom_nav_discount);
                break;

            case R.id.categoriesNav:
                navigationView.setCheckedItem(R.id.categoriesNav);
                bottomNavigationView.setVisibility(View.GONE);
                Functions.loadFragment(getSupportFragmentManager(), new Category_frag(), R.id.frag_cont, false, "Notification", null);
                break;

            case R.id.notificationNav:

                if (auth.getCurrentUser() != null) {

                    navigationView.setCheckedItem(R.id.notificationNav);
                    bottomNavigationView.setVisibility(View.GONE);
                    Functions.loadFragment(getSupportFragmentManager(), new Notification_frag(), R.id.frag_cont, false, "Notification", null);
                }
                else {

                    dialog("Please Login/Register to see notifications.");
                }
                break;

            case R.id.invisibleNav:
                navigationView.setCheckedItem(R.id.invisibleNav);
                break;

            case R.id.logoutNav:

                if (auth.getCurrentUser() != null) {

                    dialog("Do you want to logout?");

                }
                else {

                    dialog("Do you want to login?");

                }
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
                            //bottomNavigationView.setVisibility(View.VISIBLE);

                            Log.d("backpop", String.valueOf(Constants.popBack));

                            /*if (Constants.poptToMainCat == 1){

                                //bottomNavigationView.setSelectedItemId(R.id.invisible);
                                Functions.loadFragment(getSupportFragmentManager(), new Category_frag(), R.id.frag_cont, false, "Dashboard", null);
                                bottomNavigationView.setVisibility(View.GONE);
                               // getSupportFragmentManager().popBackStack();
                                Constants.poptToMainCat = 0;
                            }

                            else if (Constants.popBack == 1){

                                Functions.loadFragment(getSupportFragmentManager(), new Dashboard_frag(), R.id.frag_cont, true, "Dashboard", null);
                                getSupportFragmentManager().popBackStackImmediate();
                                getSupportFragmentManager().popBackStack();
                            }
                            else {
                                Functions.loadFragment(getSupportFragmentManager(), new Dashboard_frag(), R.id.frag_cont, true, "Dashboard", null);
                                navigationView.setCheckedItem(R.id.homeNav);
                                bottomNavigationView.setSelectedItemId(R.id.bottom_nav_category);
                                getSupportFragmentManager().popBackStackImmediate();
                                getSupportFragmentManager().popBackStack();
                            }*/

                            //bottomNavigationView.setSelectedItemId(R.id.bottom_nav_category);
                            //Functions.loadFragment(getSupportFragmentManager(), new Dashboard_frag(), R.id.frag_cont, true, "Dashboard", null);
                            Constants.onetTime = 1;
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

    private void loadBitmapByPicasso(Context pContext, Bitmap pBitmap, ImageView pImageView) {
        try {
            Uri uri = Uri.fromFile(File.createTempFile("temp_file_name", ".jpg", pContext.getCacheDir()));
            OutputStream outputStream = pContext.getContentResolver().openOutputStream(uri);
            pBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            Picasso.get().load(uri).into(pImageView);
        } catch (Exception e) {
            Log.e("LoadBitmapByPicasso", e.getMessage());
        }
    }

    public  Bitmap rotateImageIfRequired(String imagePath) {
        int degrees = 0;

        try {
            ExifInterface exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degrees = 90;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    degrees = 180;
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    degrees = 270;
                    break;
            }
        } catch (IOException e) {
            Log.e("ImageError", "Error in reading Exif data of " + imagePath, e);
        }

        BitmapFactory.Options decodeBounds = new BitmapFactory.Options();
        decodeBounds.inJustDecodeBounds = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, decodeBounds);
        int numPixels = decodeBounds.outWidth * decodeBounds.outHeight;
        int maxPixels = 2048 * 1536; // requires 12 MB heap

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = (numPixels > maxPixels) ? 2 : 1;

        bitmap = BitmapFactory.decodeFile(imagePath, options);

        if (bitmap == null) {
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);

        return bitmap;
    }

    private void dialog(String msg){

        drawerLayout.closeDrawer(GravityCompat.START);
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);
        builder.setCancelable(false);
        builder.setTitle(msg);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //auth = FirebaseAuth.getInstance();
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


    }


}