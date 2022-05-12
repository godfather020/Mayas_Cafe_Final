package com.example.mayasfood.fragments

import android.content.SharedPreferences
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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lottry.data.remote.retrofit.response.HomeTempResponse
import com.example.mayasfood.Retrofite.response.ListcouponResponce
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.ViewPagerAdapter.SliderData
import com.smarteist.autoimageslider.SliderView
import com.example.mayasfood.ViewPagerAdapter.SliderAdapter
import com.example.mayasfood.activity.ViewModels.Dashboard_ViewModel
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.fragments.ViewModels.Dashboard_frag_ViewModel
import com.example.mayasfood.fragments.ViewModels.UserProfile_ViewModel
import com.example.mayasfood.functions.Functions
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_C
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_PF
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_RC
import com.google.android.gms.common.util.SharedPreferencesUtils
import java.util.ArrayList

class Dashboard_frag : Fragment() {

    lateinit var dashBoard: DashBoard
    lateinit var see_offers: TextView
    lateinit var userName : TextView
    //val commonResponse = MutableLiveData<Response_Common>()
    lateinit var viewModel: Dashboard_frag_ViewModel
    lateinit var sliderView: SliderView

    var url1 = "https://i.postimg.cc/2Sq6C4V8/002-1.png"
    var url2 = "https://i.postimg.cc/FFMd1CXk/001-1-1.jpg"
    var url3 = "https://i.postimg.cc/VNk523np/image-2.png"
    var couponImg = ArrayList<String>()
    val sliderDataArrayList = ArrayList<SliderData>()

    var recycleView_models = ArrayList<RecycleView_Model>()
    var recycleView_models1 = ArrayList<RecycleView_Model>()
    var recycleView_models2 = ArrayList<RecycleView_Model>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_dashboard_frag, container, false)

        viewModel = ViewModelProvider(this).get(Dashboard_frag_ViewModel::class.java)

        dashBoard = (activity as DashBoard)
        dashBoard.toolbar_const.title = ""
        see_offers = v.findViewById(R.id.see_offers)
        userName = v.findViewById(R.id.user_name)

        userName.setText(Constants.USER_NAME)

        setDashboardView()

        // Initializing the ViewPager Object


        // initializing the slider view.
        sliderView = v.findViewById(R.id.slider)

        // adding the urls inside array list
        //sliderDataArrayList.add(new SliderData(images));
        /*sliderDataArrayList.add(SliderData(url1))
        sliderDataArrayList.add(SliderData(url2))
        sliderDataArrayList.add(SliderData(url3))*/

        // passing this array list inside our adapter class.
        /*val adapter = SliderAdapter(context, sliderDataArrayList)

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
        sliderView.startAutoCycle()*/


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

    private fun setDashboardView() {

        viewModel.getDashboardData(this, "1").observe(viewLifecycleOwner, Observer {

            if (it != null){

                if (it.getSuccess()!!){

                    Log.d("indice", it.getData()!!.ListcouponResponce!!.indices.toString())

                    val homeResList: ArrayList<ListcouponResponce> =ArrayList()

                    for (i in  it.getData()!!.ListcouponResponce!!.indices){

                        var ListcouponResponce =ListcouponResponce()

                        Log.d("indice", i.toString())

                        ListcouponResponce = it.getData()!!.ListcouponResponce!![i]

                        //couponImg.add(i ,it.getData()!!.ListcouponResponce!![i].bannerImage.toString())

                        val bannerImage = it.getData()!!.ListcouponResponce!![i].name

                        Log.d("indiimage", it.getData()!!.ListcouponResponce!![i].bannerImage.toString())
                        Log.d("id", it.getData()!!.ListcouponResponce!![i].toString())
                        Log.d("indiimage", bannerImage.toString())


                        homeResList.add(ListcouponResponce)

                        //Log.d("url", Constants.UserCoupon_Path+couponImg[i])

                    }
                    for (i in homeResList.indices){

                    sliderDataArrayList.add(SliderData(Constants.UserCoupon_Path+ homeResList.get(i).bannerImage))
                    }

                    val adapter = SliderAdapter(context, sliderDataArrayList)
                    sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
                    sliderView.setSliderAdapter(adapter)
                    sliderView.scrollTimeInSec = 3

                    // to set it scrollable automatically
                    // we use below method.
                    sliderView.isAutoCycle = true

                    // to start autocycle below method is used.
                    sliderView.startAutoCycle()

                }
                else{


                }


            }
        })
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