package com.example.mayasfood.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.R
import com.example.mayasfood.Retrofite.response.ListcouponResponce
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.ViewPagerAdapter.SliderAdapter
import com.example.mayasfood.ViewPagerAdapter.SliderData
import com.example.mayasfood.activity.DashBoard
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.fragments.ViewModels.Dashboard_frag_ViewModel
import com.example.mayasfood.functions.Functions
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_C
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_PF
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_RC
import com.example.mayasfood.shared_prefrence.TinyDB
import com.google.firebase.auth.FirebaseAuth
import com.smarteist.autoimageslider.SliderView
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Dashboard_frag : Fragment() {

    lateinit var dashBoard: DashBoard
    lateinit var see_offers: TextView
    lateinit var see_popular: TextView
    lateinit var see_restchoice: TextView
    lateinit var userName: TextView
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
    var popularOfferAmt = ArrayList<String>()
    var popularFoodSize = ArrayList<String>()
    var restaurantFoodName = ArrayList<String>()
    var restaurantFoodPrice = ArrayList<String>()
    var restaurantFoodImg = ArrayList<String>()
    var restaurantFoodId = ArrayList<String>()
    var popularFoodRating = ArrayList<String>()
    var restaurantFoodRating = ArrayList<String>()
    var restaurantFoodIsFav = ArrayList<Int>()
    var restaurantOfferAmt = ArrayList<String>()
    var restaurantFoodSize = ArrayList<String>()
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerView3: RecyclerView
    lateinit var recyclerView2: RecyclerView
    lateinit var homeResList: ArrayList<ListcouponResponce>
    lateinit var loading: ProgressBar
    var notResumed = false
    var favProductId = ArrayList<String>()

    //var commonResponse = ArrayList<Response_Common>()
    lateinit var auth: FirebaseAuth
    lateinit var notify_count: TextView
    lateinit var cart_count: TextView
    lateinit var card_count: CardView

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

        homeResList = ArrayList<ListcouponResponce>()
        dashBoard = (activity as DashBoard)
        dashBoard.toolbar_const.title = ""
        dashBoard.toolbar_const.setNavigationIcon(R.drawable.menubar)
        dashBoard.bottomNavigationView.visibility = View.VISIBLE

        Log.d("isLogin", Constants.isLogin.toString())

        val tinyDB = TinyDB(dashBoard)

        val foodName: ArrayList<String> = tinyDB.getListString("foodName")
        val foodSize: ArrayList<String> = tinyDB.getListString("foodSize")
        val foodImg: ArrayList<String> = tinyDB.getListString("foodImg")
        val foodPrice: ArrayList<Int> = tinyDB.getListInt("foodPrice")
        val foodQuantity: ArrayList<Int> = tinyDB.getListInt("foodQuantity")
        val foodId: ArrayList<Int> = tinyDB.getListInt("foodId")
        val cartCount = tinyDB.getInt("cartCount")

        if (foodName != null) {

            Constants.foodId.clear()
            Constants.foodSize.clear()
            Constants.foodImg.clear()
            Constants.foodPrice.clear()
            Constants.foodQuantity.clear()
            Constants.foodName.clear()
            Constants.cart_totalItems = cartCount

            for (i in foodName.indices) {

                Constants.foodName.add(foodName[i])
                Constants.foodSize.add(foodSize[i])
                Constants.foodImg.add(foodImg[i])
                Constants.foodPrice.add(foodPrice[i])
                Constants.foodQuantity.add(foodQuantity[i])
                Constants.foodId.add(foodId[i])

                Log.d(
                    "food",
                    foodName[i] + " " + foodId[i] + " " + foodImg[i] + " " + foodSize[i] + " " + foodPrice[i] + " " + foodQuantity[i]
                )
            }
        }

        dashBoard.toolbar_const.setOnMenuItemClickListener(object :
            androidx.appcompat.widget.Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {

                if (item!!.itemId == R.id.search) {

                    Functions.loadFragment(
                        fragmentManager,
                        Search_frag(),
                        R.id.frag_cont,
                        false,
                        "Search",
                        null
                    );
                }

                return true
            }


        })

        auth = FirebaseAuth.getInstance()

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
            Functions.loadFragment(
                fragmentManager,
                popular_frag(),
                R.id.frag_cont,
                false,
                "Popular Food",
                null
            )
        }

        see_restchoice.setOnClickListener {

            dashBoard.bottomNavigationView.visibility = View.GONE
            Functions.loadFragment(
                fragmentManager,
                Restarant_choice_frag(),
                R.id.frag_cont,
                false,
                "Restaurant Choices",
                null
            )
        }

        see_offers.setOnClickListener {

            dashBoard.bottomNavigationView.selectedItemId = R.id.bottom_nav_discount

            //Functions.loadFragment(fragmentManager,  Offers_frag(), R.id.frag_cont, true, "All Offers", null)
        }

        getNotificationCount()

        setDashboardView()

        return v
    }

    private fun getNotificationCount() {

        viewModel.getNotificationData(this).observe(viewLifecycleOwner, Observer {

            if (it != null) {

                if (it.getSuccess()!!) {

                    Constants.notifyCount = it.getData()!!.notifications!!.count!!

                    notify_count.text = Constants.notifyCount.toString()
                    Log.d("notifyC", Constants.notifyCount.toString())
                    cart_count.text = Constants.cart_totalItems.toString()
                    dashBoard.notify_count.text = Constants.notifyCount.toString()
                    if (!notify_count.text.equals("0")) {

                        dashBoard.notify_card.visibility = View.VISIBLE
                    }
                }
            }

        })
    }

    private fun setDashboardView() {

        viewModel.getDashboardData(this, "1", loading).observe(viewLifecycleOwner, Observer {

            if (it != null) {

                if (it.getSuccess()!!) {

                    clearArrayList()

                    for (i in it.getData()!!.ListcouponResponce!!.indices) {

                        var ListcouponResponce = ListcouponResponce()

                        Log.d("indice", i.toString())

                        val dateFormat: DateFormat =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                SimpleDateFormat(
                                    "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                                    Locale.getDefault()
                                )
                            } else {
                                TODO("VERSION.SDK_INT < N")
                            }
                        val date =
                            dateFormat.parse(it.getData()!!.ListcouponResponce!![i].stopAt.toString())

                        val formatter: DateFormat =
                            SimpleDateFormat("dd-MM-yyyy") //If you need time just put specific format for time like 'HH:mm:ss'

                        val dateStr = formatter.format(date)

                        val valid_until = dateStr
                        val sdf = SimpleDateFormat("dd-MM-yyyy")
                        var strDate: Date? = null
                        try {
                            strDate = sdf.parse(valid_until)
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }

                        if (it.getData()!!.ListcouponResponce!![i].status != false && !Date().after(
                                strDate
                            )
                        ) {

                            ListcouponResponce = it.getData()!!.ListcouponResponce!![i]


                            var bannerImage = it.getData()!!.ListcouponResponce!![i].name

                            homeResList.add(ListcouponResponce)
                        }

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

                    sliderView.isAutoCycle = true
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
                        if (auth.currentUser != null || Constants.isLogin != false) {

                            popularFoodIsFav.add(
                                i,
                                it.getData()!!.ListpopularproductResponce!![i].favorite!!
                            )
                        } else {

                            popularFoodIsFav.add(
                                i,
                                0
                            )
                        }
                        if (it.getData()!!.ListpopularproductResponce!![i].Productprices!![0].offerAmount != null) {

                            popularOfferAmt.add(
                                i,
                                it.getData()!!.ListpopularproductResponce!![i].Productprices!![0].offerAmount.toString()
                            )
                        } else {

                            popularOfferAmt.add(i, "0")
                        }
                        if (it.getData()!!.ListpopularproductResponce!![i].Productprices!!.size == 1) {

                            popularFoodSize.add(it.getData()!!.ListpopularproductResponce!![i].Productprices!![0].productsize.toString())
                        } else {

                            for (j in it.getData()!!.ListpopularproductResponce!![i].Productprices!!.indices) {

                                if (it.getData()!!.ListpopularproductResponce!![i].Productprices!![j].productsize.equals(
                                        "M"
                                    )
                                ) {
                                    popularFoodSize.add(it.getData()!!.ListpopularproductResponce!![i].Productprices!![j].productsize.toString())
                                }
                            }

                            if (popularFoodSize.isEmpty()) {

                                popularFoodSize.add(it.getData()!!.ListpopularproductResponce!![i].Productprices!![0].productsize.toString())
                            }

                        }
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

                        if (auth.currentUser != null || Constants.isLogin != false) {
                            restaurantFoodIsFav.add(
                                i,
                                it.getData()!!.ListrestaurantproductResponce!![i].favorite!!
                            )
                        } else {
                            restaurantFoodIsFav.add(
                                i,
                                0
                            )

                        }

                        if (it.getData()!!.ListrestaurantproductResponce!![i].Productprices!![0].offerAmount != null) {

                            restaurantOfferAmt.add(
                                i,
                                it.getData()!!.ListrestaurantproductResponce!![i].Productprices!![0].offerAmount.toString()
                            )
                        } else {

                            restaurantOfferAmt.add(i, "0")
                        }

                        if (it.getData()!!.ListrestaurantproductResponce!![i].Productprices!!.size == 1) {

                            restaurantFoodSize.add(it.getData()!!.ListrestaurantproductResponce!![i].Productprices!![0].productsize.toString())
                        } else {

                            for (j in it.getData()!!.ListrestaurantproductResponce!![i].Productprices!!.indices) {

                                if (it.getData()!!.ListrestaurantproductResponce!![i].Productprices!![j].productsize.equals(
                                        "M"
                                    )
                                ) {
                                    restaurantFoodSize.add(it.getData()!!.ListrestaurantproductResponce!![i].Productprices!![j].productsize.toString())
                                }
                            }

                            if (restaurantFoodSize.isEmpty()) {

                                restaurantFoodSize.add(it.getData()!!.ListrestaurantproductResponce!![i].Productprices!![0].productsize.toString())
                            }

                        }
                    }

                    for (i in it.getData()!!.ListcategoryResponce!!.indices) {

                        categoryName.add(
                            i,
                            it.getData()!!.ListcategoryResponce!![i].categoryName.toString()
                        )
                        categoryId.add(i, it.getData()!!.ListcategoryResponce!![i].id.toString())

                    }

                    loading.visibility = View.GONE
                } else {

                    Log.d("response", "Failed")
                }
                setUpFoodModel()

            }

        })
    }

    private fun setUpFoodModel() {


        for (s in categoryName.indices) {
            recycleView_models.add(RecycleView_Model(categoryName[s], categoryId[s]))

            Log.d("indiimage1", categoryId[s])
        }
        for (i in popularFoodName.indices) {
            recycleView_models1.add(
                RecycleView_Model(
                    popularFoodSize[i],
                    popularOfferAmt[i],
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
                    restaurantFoodSize[i],
                    restaurantOfferAmt[i],
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
        //setDashboardView()
        //loading.visibility = View.VISIBLE
        dashBoard.toolbar_const.setNavigationIcon(R.drawable.menubar)
        //setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Do something that differs the Activity's menu here

        menu.getItem(0).setVisible(true)
        menu.getItem(1).setVisible(true)
        menu.getItem(3).setVisible(true)
        dashBoard.navigationView.setCheckedItem(R.id.homeNav)

        val notifyMenuItem = menu.findItem(R.id.notification)
        val cartMenuItem = menu.findItem(R.id.cart)

        val actionView2: View = cartMenuItem.actionView

        val actionView: View = notifyMenuItem.getActionView()



        if (actionView != null) {
            //bell = actionView.findViewById<ImageView>(R.id.bell)
            notify_count = actionView.findViewById(R.id.notify_count)
            Log.d("notifyC", Constants.notifyCount.toString())
            //notify_count.setText(Constants.notifyCount.toString())
        }

        if (actionView2 != null) {

            cart_count = actionView2.findViewById(R.id.cart_count)
            card_count = actionView2.findViewById(R.id.card_count)
        }

        if (cart_count.text.equals("0")) {

            card_count.visibility = View.GONE
        } else {

            card_count.visibility = View.VISIBLE
        }

        super.onCreateOptionsMenu(menu, inflater)
    }


    fun clearArrayList() {

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
        popularOfferAmt.clear()
        restaurantOfferAmt.clear()

    }


}
