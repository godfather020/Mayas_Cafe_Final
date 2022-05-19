package com.example.mayasfood.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.R
import com.example.mayasfood.Retrofite.request.Request_Branch
import com.example.mayasfood.Retrofite.response.ListcouponResponce
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.ViewPagerAdapter.SliderAdapter
import com.example.mayasfood.ViewPagerAdapter.SliderData
import com.example.mayasfood.activity.DashBoard
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.development.retrofit.RetrofitInstance
import com.example.mayasfood.fragments.ViewModels.Dashboard_frag_ViewModel
import com.example.mayasfood.functions.Functions
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_C
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_PF
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_RC
import com.smarteist.autoimageslider.SliderView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Dashboard_frag : Fragment() {

    lateinit var dashBoard: DashBoard
    lateinit var see_offers: TextView
    lateinit var see_popular: TextView
    lateinit var see_restchoice: TextView
    lateinit var userName : TextView
    val commonResponse = MutableLiveData<Response_Common>()
    lateinit var viewModel: Dashboard_frag_ViewModel
    lateinit var sliderView: SliderView
    var categoryName = ArrayList<String>()
    var categoryId = ArrayList<String>()
    var popularFoodName = ArrayList<String>()
    var popularFoodId = ArrayList<String>()
    var popularFoodPrice = ArrayList<String>()
    var popularFoodImg = ArrayList<String>()
    var popularFoodIsFav = ArrayList<Int>()
    var restaurantFoodName = ArrayList<String>()
    var restaurantFoodPrice = ArrayList<String>()
    var restaurantFoodImg = ArrayList<String>()
    var restaurantFoodId = ArrayList<String>()
    var popularFoodRating = ArrayList<String>()
    var restaurantFoodRating = ArrayList<String>()
    var restaurantFoodIsFav = ArrayList<Int>()
    lateinit var recyclerView : RecyclerView
    lateinit var recyclerView3 : RecyclerView
    lateinit var recyclerView2 : RecyclerView
    lateinit var homeResList: ArrayList<ListcouponResponce>
    lateinit var loading : ProgressBar
    var notResumed = false
    var favProductId = ArrayList<String>()
    //var commonResponse = ArrayList<Response_Common>()

    var url1 = "https://i.postimg.cc/2Sq6C4V8/002-1.png"
    var url2 = "https://i.postimg.cc/FFMd1CXk/001-1-1.jpg"
    var url3 = "https://i.postimg.cc/VNk523np/image-2.png"
    var couponImg = ArrayList<String>()
    val sliderDataArrayList = ArrayList<SliderData>()

    var recycleView_models = ArrayList<RecycleView_Model>()
    var recycleView_models1 = ArrayList<RecycleView_Model>()
    var recycleView_models2 = ArrayList<RecycleView_Model>()

    /*private val dataObserver = Observer<Response_Common> { it ->

        //setDashboardView(it)
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_dashboard_frag, container, false)

        //viewModel.getDashboardData(this, "1").observe(viewLifecycleOwner, dataObserver)
        viewModel = ViewModelProvider(this).get(Dashboard_frag_ViewModel::class.java)

        homeResList =ArrayList<ListcouponResponce>()
        dashBoard = (activity as DashBoard)
        dashBoard.toolbar_const.title = ""
        dashBoard.bottomNavigationView.visibility = View.VISIBLE

        see_offers = v.findViewById(R.id.see_offers)
        userName = v.findViewById(R.id.user_name)
        see_popular = v.findViewById(R.id.see_popular)
        see_restchoice = v.findViewById(R.id.see_restChoice)

        if (Constants.USER_NAME != null) {

            userName.setText(Constants.USER_NAME)
        }

        loading = v.findViewById(R.id.progress_bar)
        loading.visibility = View.VISIBLE

        //setDashboardView()

        // Initializing the ViewPager Object

        setHasOptionsMenu(true)


        // initializing the slider view.
        sliderView = v.findViewById(R.id.slider)

        recyclerView = v.findViewById(R.id.rv1)
        recyclerView2 = v.findViewById(R.id.rv2)
        recyclerView3 = v.findViewById(R.id.rv3)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val layoutManager2 = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val layoutManager3 = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView2.layoutManager = layoutManager2
        recyclerView3.layoutManager = layoutManager3


        see_popular.setOnClickListener {

            dashBoard.bottomNavigationView.visibility = View.GONE
            Functions.loadFragment(fragmentManager, popular_frag(), R.id.frag_cont, false, "Popular Food", null)
        }

        see_restchoice.setOnClickListener {

            dashBoard.bottomNavigationView.visibility = View.GONE
            Functions.loadFragment(fragmentManager, Restarant_choice_frag(), R.id.frag_cont, false, "Restaurant Choices", null)
        }

        see_offers.setOnClickListener {

            dashBoard.bottomNavigationView.selectedItemId = R.id.bottom_nav_discount

            //Functions.loadFragment(fragmentManager,  Offers_frag(), R.id.frag_cont, true, "All Offers", null)
        }

        //setDashboardView()

        return v
    }

    private fun setDashboardView() {

        viewModel.getDashboardData(this, "1").observe(viewLifecycleOwner, Observer {

            if (it != null) {

                if (it.getSuccess()!!) {

                    recycleView_models.clear()
                    recycleView_models1.clear()
                    recycleView_models2.clear()
                    popularFoodName.clear()
                    popularFoodImg.clear()
                    popularFoodPrice.clear()
                    popularFoodRating.clear()
                    restaurantFoodName.clear()
                    restaurantFoodImg.clear()
                    restaurantFoodPrice.clear()
                    restaurantFoodRating.clear()
                    categoryName.clear()
                    sliderDataArrayList.clear()
                    homeResList.clear()
                    popularFoodId.clear()
                    restaurantFoodId.clear()
                    popularFoodIsFav.clear()
                    restaurantFoodIsFav.clear()

                    Log.d(
                        "indice",
                        it.getData()!!.ListcouponResponce!!.indices.toString()
                    )

                    for (i in it.getData()!!.ListcouponResponce!!.indices) {

                        var ListcouponResponce = ListcouponResponce()

                        Log.d("indice", i.toString())

                        ListcouponResponce = it.getData()!!.ListcouponResponce!![i]

                        //couponImg.add(i ,it.getData()!!.ListcouponResponce!![i].bannerImage.toString())

                        var bannerImage = it.getData()!!.ListcouponResponce!![i].name

                        Log.d(
                            "indiimage",
                            it.getData()!!.ListcouponResponce!![i].bannerImage.toString()
                        )
                        Log.d("id", it.getData()!!.ListcouponResponce!![i].toString())
                        Log.d("indiimage", bannerImage.toString())


                        homeResList.add(ListcouponResponce)

                        //Log.d("url", Constants.UserCoupon_Path+couponImg[i])

                    }
                    for (i in homeResList.indices) {

                        sliderDataArrayList.add(
                            SliderData(
                                Constants.UserCoupon_Path + homeResList.get(
                                    i
                                ).bannerImage
                            )
                        )

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

                    for (i in it.getData()!!.ListpopularproductResponce!!.indices) {

                        popularFoodName.add(
                            i,
                            it.getData()!!.ListpopularproductResponce!![i].productName.toString()
                        )
                        popularFoodPrice.add(
                            i,
                            it.getData()!!.ListpopularproductResponce!![i].Productprices!![0].amount.toString()
                        )
                        popularFoodRating.add(
                            i,
                            it.getData()!!.ListpopularproductResponce!![i].customerrating.toString()
                        )
                        popularFoodImg.add(
                            i,
                            it.getData()!!.ListpopularproductResponce!![i].productPic.toString()
                        )
                        popularFoodId.add(
                            i,
                            it.getData()!!.ListpopularproductResponce!![i].id.toString()
                        )
                        popularFoodIsFav.add(i ,
                            it.getData()!!.ListpopularproductResponce!![i].favorite!!
                        )

                        Log.d(
                            "indiimage",
                            it.getData()!!.ListpopularproductResponce!![i].productName.toString()
                        )
                        Log.d(
                            "indiimage",
                            it.getData()!!.ListpopularproductResponce!![i].Productprices!![0].amount.toString()
                        )
                        Log.d(
                            "indiimage",
                            it.getData()!!.ListpopularproductResponce!![i].customerrating.toString()
                        )
                        Log.d(
                            "indiimage",
                            it.getData()!!.ListpopularproductResponce!![i].productPic.toString()
                        )
                    }

                    for (i in it.getData()!!.ListrestaurantproductResponce!!.indices) {

                        restaurantFoodName.add(
                            i,
                            it.getData()!!.ListrestaurantproductResponce!![i].productName.toString()
                        )
                        restaurantFoodPrice.add(
                            i,
                            it.getData()!!.ListrestaurantproductResponce!![i].Productprices!![0].amount.toString()
                        )
                        restaurantFoodImg.add(
                            i,
                            it.getData()!!.ListrestaurantproductResponce!![i].productPic.toString()
                        )
                        restaurantFoodRating.add(
                            i,
                            it.getData()!!.ListrestaurantproductResponce!![i].systemrating.toString()
                        )
                        restaurantFoodId.add(
                            i,
                            it.getData()!!.ListrestaurantproductResponce!![i].id.toString()
                        )
                        restaurantFoodIsFav.add(i , it.getData()!!.ListrestaurantproductResponce!![i].favorite!!)

                        Log.d(
                            "indiimage",
                            it.getData()!!.ListrestaurantproductResponce!![i].productName.toString()
                        )
                        Log.d(
                            "indiimage",
                            it.getData()!!.ListrestaurantproductResponce!![i].Productprices!![0].amount.toString()
                        )
                        Log.d(
                            "indiimage",
                            it.getData()!!.ListrestaurantproductResponce!![i].customerrating.toString()
                        )
                        Log.d(
                            "indiimage",
                            it.getData()!!.ListrestaurantproductResponce!![i].productPic.toString()
                        )
                    }

                    for (i in it.getData()!!.ListcategoryResponce!!.indices) {

                        categoryName.add(
                            i,
                            it.getData()!!.ListcategoryResponce!![i].categoryName.toString()
                        )
                        categoryId.add(i, it.getData()!!.ListcategoryResponce!![i].id.toString())

                        Log.d(
                            "indiimage",
                            it.getData()!!.ListcategoryResponce!![i].categoryName.toString()
                        )
                    }

                    loading.visibility = View.GONE
                } else {

                    Log.d("response", "Failed")
                }
                setUpFoodModel()

            }

        })

        /*val requestBranch: Request_Branch = Request_Branch()
        requestBranch.branchId = "1"

        val retrofitInstance = RetrofitInstance()

        val retrofitData : Call<Response_Common> =
            retrofitInstance.retrofit.getFavList(Constants.USER_TOKEN, requestBranch)

        retrofitData.enqueue(object : Callback<Response_Common?> {
            override fun onResponse(
                call: Call<Response_Common?>,
                response: Response<Response_Common?>
            ) {
                if (response.isSuccessful) {

                    commonResponse.value = response.body()

                    for (i in commonResponse.value.getData()!!.FavoriteListResponce!!.indices) {

                        favProductId.add(response.body()!!.getData()!!.productId.toString())
                    }

                    Log.d("Dashboard", "success")
                    //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                }
                else{

                    Log.d("Dashboard", "failed")
                }
            }

            override fun onFailure(call: Call<Response_Common?>, t: Throwable) {
                Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show()
            }
        })*/
    }

    private fun setUpFoodModel() {


        for (s in categoryName.indices) {
            recycleView_models.add(RecycleView_Model(categoryName[s], categoryId[s]))

            Log.d("indiimage1", categoryId[s])
        }
        for (i in popularFoodName.indices) {
            recycleView_models1.add(
                RecycleView_Model(
                    popularFoodName[i],
                    popularFoodPrice[i],
                    popularFoodId[i],
                    popularFoodImg[i],
                    popularFoodRating[i],
                    popularFoodIsFav[i]

                )
            )

            Log.d("indiimage1", popularFoodName[i])
            Log.d("indiimage1", popularFoodPrice[i])
            Log.d("indiimage1", popularFoodImg[i])
            Log.d("indiimage1", popularFoodRating[i])

        }
        for (i in restaurantFoodName.indices) {
            recycleView_models2.add(
                RecycleView_Model(
                    restaurantFoodName[i],
                    restaurantFoodPrice[i],
                    restaurantFoodId[i],
                    restaurantFoodImg[i],
                    restaurantFoodRating[i],
                    restaurantFoodIsFav[i]
                )
            )

            Log.d("indiimage1", restaurantFoodName[i])
            Log.d("indiimage1", restaurantFoodName[i])
            Log.d("indiimage1", restaurantFoodName[i])
            Log.d("indiimage1", restaurantFoodName[i])
        }

        val recycleView_adapter = RecycleView_Adapter_C(activity, recycleView_models)
        val recycleView_adapter_pf = RecycleView_Adapter_PF(activity, recycleView_models1)
        val recycleView_adapter_rc = RecycleView_Adapter_RC(activity, recycleView_models2)
        recyclerView.adapter = recycleView_adapter
        recyclerView2.adapter = recycleView_adapter_pf
        recyclerView3.adapter = recycleView_adapter_rc
        recycleView_adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        notResumed = false
        Log.d("life", "resume")
        Log.d("lifer", Constants.onetTime.toString())
        //viewModel = ViewModelProvider(this).get(Dashboard_frag_ViewModel::class.java)
        setDashboardView()
        loading.visibility = View.VISIBLE
        //setHasOptionsMenu(true)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("life", "create")
        Log.d("lifec", Constants.onetTime.toString())
    }

    override fun onPause() {
        super.onPause()
        Log.d("life", "pause")
        Log.d("lifep", Constants.onetTime.toString())
    }

    override fun onStop() {
        Log.d("life", "stop")
        super.onStop()
        this.onDestroy()
        //Constants.onetTime = 0
        Log.d("lifes", Constants.onetTime.toString())

    }

    override fun onDestroy() {
        Log.d("life", "destroy")
        super.onDestroy()
        //Constants.onetTime = 1
        Log.d("lifed", Constants.onetTime.toString())
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Do something that differs the Activity's menu here

        menu.getItem(0).setVisible(true)
        menu.getItem(1).setVisible(true)
        menu.getItem(2).setVisible(true)
        dashBoard.navigationView.setCheckedItem(R.id.homeNav)
        //dashBoard.bottomNavigationView.selectedItemId = R.id.bottom_nav_category
        super.onCreateOptionsMenu(menu, inflater)
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.notification ->
                //item.setVisible(false)

            R.id.search ->
               //item.setVisible(false)
            else -> {}
        }
        return false
    }*/
}