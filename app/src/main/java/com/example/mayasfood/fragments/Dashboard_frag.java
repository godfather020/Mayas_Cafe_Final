package com.example.mayasfood.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mayasfood.R;
import com.example.mayasfood.ViewPagerAdapter.SliderAdapter;
import com.example.mayasfood.ViewPagerAdapter.SliderData;
import com.example.mayasfood.constants.Constants;
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model;
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_C;
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_PF;
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_RC;
import com.smarteist.autoimageslider.SliderView;

import java.net.URL;
import java.util.ArrayList;

public class Dashboard_frag extends Fragment {

    private boolean isBackPressed = false;

    // images array
    int[] images = {R.drawable._01_1__1_, R.drawable._02_1, R.drawable.image_2};
    String url1 = "https://i.postimg.cc/2Sq6C4V8/002-1.png";
    String url2 = "https://i.postimg.cc/FFMd1CXk/001-1-1.jpg";
    String url3 = "https://i.postimg.cc/VNk523np/image-2.png";
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

        // Initializing the ViewPager Object
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.
        SliderView sliderView = v.findViewById(R.id.slider);

        // adding the urls inside array list
        //sliderDataArrayList.add(new SliderData(images));

        sliderDataArrayList.add(new SliderData(url1));
        sliderDataArrayList.add(new SliderData(url2));
        sliderDataArrayList.add(new SliderData(url3));

        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(getContext(), sliderDataArrayList);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();

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

        for (String s : foodName) {
            recycleView_models.add(new RecycleView_Model(s));
        }
        for(int i = 0; i<nameFood.length; i++){
            recycleView_models1.add(new RecycleView_Model(nameFood[i], foodprice[i], Constants.imgFood[i]));
        }
        for (int i =0; i<NameFood.length; i++){
            recycleView_models2.add(new RecycleView_Model(NameFood[i], Food_rate[i], Constants.foodimg[i]));
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