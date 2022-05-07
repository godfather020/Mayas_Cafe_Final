package com.example.mayasfood.fragments

import com.example.mayasfood.activity.DashBoard
import com.example.mayasfood.R
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.ViewPagerAdapter.SliderData
import com.smarteist.autoimageslider.SliderView
import com.example.mayasfood.ViewPagerAdapter.SliderAdapter
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.functions.Functions
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_C
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_PF
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_RC
import java.util.ArrayList

class Dashboard_frag : Fragment() {

    lateinit var dashBoard: DashBoard
    lateinit var see_offers: TextView

    var url1 = "https://i.postimg.cc/2Sq6C4V8/002-1.png"
    var url2 = "https://i.postimg.cc/FFMd1CXk/001-1-1.jpg"
    var url3 = "https://i.postimg.cc/VNk523np/image-2.png"


    var recycleView_models = ArrayList<RecycleView_Model>()
    var recycleView_models1 = ArrayList<RecycleView_Model>()
    var recycleView_models2 = ArrayList<RecycleView_Model>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_dashboard_frag, container, false)

        dashBoard = (activity as DashBoard)
        dashBoard.toolbar_const.title = ""
        see_offers = v.findViewById(R.id.see_offers)
        // Initializing the ViewPager Object
        val sliderDataArrayList = ArrayList<SliderData>()

        // initializing the slider view.
        val sliderView: SliderView = v.findViewById(R.id.slider)

        // adding the urls inside array list
        //sliderDataArrayList.add(new SliderData(images));
        sliderDataArrayList.add(SliderData(url1))
        sliderDataArrayList.add(SliderData(url2))
        sliderDataArrayList.add(SliderData(url3))

        // passing this array list inside our adapter class.
        val adapter = SliderAdapter(context, sliderDataArrayList)

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter)

        // below method is use to set
        // scroll time in seconds.
        sliderView.scrollTimeInSec = 3

        // to set it scrollable automatically
        // we use below method.
        sliderView.isAutoCycle = true

        // to start autocycle below method is used.
        sliderView.startAutoCycle()


        val recyclerView: RecyclerView = v.findViewById(R.id.rv1)
        val recyclerView2: RecyclerView = v.findViewById(R.id.rv2)
        val recyclerView3: RecyclerView = v.findViewById(R.id.rv3)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val layoutManager2 = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val layoutManager3 = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView2.layoutManager = layoutManager2
        recyclerView3.layoutManager = layoutManager3
        setUpFoodModel()
        val recycleView_adapter = RecycleView_Adapter_C(activity, recycleView_models)
        val recycleView_adapter_pf = RecycleView_Adapter_PF(activity, recycleView_models1)
        val recycleView_adapter_rc = RecycleView_Adapter_RC(activity, recycleView_models2)
        recyclerView.adapter = recycleView_adapter
        recyclerView2.adapter = recycleView_adapter_pf
        recyclerView3.adapter = recycleView_adapter_rc
        recycleView_adapter.notifyDataSetChanged()

        see_offers.setOnClickListener {

            dashBoard.bottomNavigationView.selectedItemId = R.id.bottom_nav_discount

            //Functions.loadFragment(fragmentManager,  Offers_frag(), R.id.frag_cont, true, "All Offers", null)
        }

        return v
    }

    private fun setUpFoodModel() {
        val foodName = resources.getStringArray(R.array.Food_txt)
        val nameFood = resources.getStringArray(R.array.Food_name)
        val foodop = resources.getStringArray(R.array.Food_option)
        val foodprice = resources.getStringArray(R.array.Food_price)
        val NameFood = resources.getStringArray(R.array.Name_Food)
        val Food_op = resources.getStringArray(R.array.Food_op)
        val Food_rate = resources.getStringArray(R.array.Food_rate)
        val starts = arrayOf(1,2,3,4,5)

        for (s in foodName) {
            recycleView_models.add(RecycleView_Model(s))
        }
        for (i in nameFood.indices) {
            recycleView_models1.add(
                RecycleView_Model(
                    nameFood[i],
                    foodprice[i],
                    Constants.imgFood[i],starts[i]
                )
            )
        }
        for (i in NameFood.indices) {
            recycleView_models2.add(
                RecycleView_Model(
                    NameFood[i],
                    Food_rate[i],
                    Constants.foodimg[i],starts[i]
                )
            )
        }
    }
}