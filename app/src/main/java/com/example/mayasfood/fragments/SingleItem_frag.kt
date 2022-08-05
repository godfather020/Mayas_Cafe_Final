package com.example.mayasfood.fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lottry.data.remote.retrofit.request.Request_ProductDetails
import com.example.mayasfood.R
import com.example.mayasfood.Retrofite.request.RequestProductRating
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.activity.DashBoard
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.development.retrofit.RetrofitInstance
import com.example.mayasfood.fragments.ViewModels.SingleItem_viewModel
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_CC
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class SingleItem_frag : Fragment() {

    var recycleView_models = ArrayList<RecycleView_Model>()
    lateinit var dashBoard: DashBoard
    lateinit var singleItem_img: CircleImageView
    lateinit var singleItem_name: TextView
    lateinit var singleItem_price: TextView
    lateinit var singleItem_total: TextView
    lateinit var dialog : Dialog
    lateinit var singleItem_addToCart: Button
    lateinit var singleItem_rating: TextView
    lateinit var singleItem_comment: TextView
    lateinit var singleItem_des: TextView
    lateinit var singleItem_star1: ImageView
    lateinit var singleItem_star2: ImageView
    lateinit var singleItem_star3: ImageView
    lateinit var singleItem_star4: ImageView
    lateinit var singleItem_star5: ImageView
    lateinit var singleItem_radio_r: RadioButton
    lateinit var singleItem_radio_s: RadioButton
    lateinit var singleItem_radio_l: RadioButton
    lateinit var singleItem_plus: ImageView
    lateinit var singleItem_minus: ImageView
    lateinit var singleItem_num: TextView
    lateinit var singleOComments : TextView
    lateinit var singleItem_addToFav: ImageView
    lateinit var loading: ProgressBar
    lateinit var viewModel: SingleItem_viewModel
    lateinit var customerCommentsRv: RecyclerView
    lateinit var seek5Bar: SeekBar
    lateinit var seek4Bar: SeekBar
    lateinit var seek3Bar: SeekBar
    lateinit var seek2Bar: SeekBar
    lateinit var seek1Bar: SeekBar
    lateinit var seek5Count: TextView
    lateinit var seek4Count: TextView
    lateinit var seek3Count: TextView
    lateinit var seek2Count: TextView
    lateinit var seek1Count: TextView
    lateinit var productRating: TextView
    lateinit var totalReviews: TextView
 //   lateinit var showMoreReview : TextView
    lateinit var nestedScroll : NestedScrollView
    lateinit var loadingRv : ProgressBar
    var itemAmount = ArrayList<String>()
    var offerAmount = ArrayList<String>()
    var itemOfferAmt = ArrayList<String>()
    var itemSize = ArrayList<String>()
    var custName = ArrayList<String>()
    var custComment = ArrayList<String>()
    var custImg = ArrayList<String>()
    var custRating = ArrayList<String>()
    var rateDate = ArrayList<String>()
    var custNameN = ArrayList<String>()
    var custCommentN = ArrayList<String>()
    var custImgN = ArrayList<String>()
    var custRatingN = ArrayList<String>()
    var rateDateN = ArrayList<String>()
    lateinit var auth: FirebaseAuth
    var productImg = ""
    lateinit var orgPrice: TextView
    var foodSize = ""
    var sameFood = 0
    lateinit var layout: View
    var isLogin = false
    var page = 1
    var itemPic = "default.png"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_single_item_frag, container, false)


        /* val inflater = layoutInflater

          layout = inflater.inflate(
             R.layout.custom_item_add_toast,
             view.findViewById(R.id.custom_toast) as ViewGroup?
         )*/
        Constants.page = 1

        viewModel = ViewModelProvider(this).get(SingleItem_viewModel::class.java)

        dashBoard = activity as DashBoard

        dashBoard.toolbar_const.setTitle("Product Details")
        dashBoard.toolbar_const.setTitleTextColor(Color.BLACK)

        dashBoard.bottomNavigationView.visibility = View.GONE

        auth = FirebaseAuth.getInstance()

        isLogin =
            requireContext().getSharedPreferences("LogIn", Context.MODE_PRIVATE)
                .getBoolean("LogIn", false)

        singleItem_img = view.findViewById(R.id.singleO_foodImg)
        singleItem_comment = view.findViewById(R.id.singleO_comments)
        singleItem_des = view.findViewById(R.id.singleO_des)
        singleItem_minus = view.findViewById(R.id.singleO_minus)
        singleItem_plus = view.findViewById(R.id.singleO_plus)
        singleItem_addToCart = view.findViewById(R.id.singleO_addToCart)
        singleItem_name = view.findViewById(R.id.singleO_foodName)
        singleItem_num = view.findViewById(R.id.singleO_Foodnum)
        singleItem_price = view.findViewById(R.id.singleO_foodPrice)
        singleItem_radio_s = view.findViewById(R.id.singleO_checkboxS)
        singleItem_radio_r = view.findViewById(R.id.singleO_checkboxR)
        singleItem_radio_l = view.findViewById(R.id.singleO_checkboxL)
        singleItem_star1 = view.findViewById(R.id.singleO_start1)
        singleItem_star2 = view.findViewById(R.id.singleO_start2)
        singleItem_star3 = view.findViewById(R.id.singleO_start3)
        singleItem_star4 = view.findViewById(R.id.singleO_start4)
        singleItem_star5 = view.findViewById(R.id.singleO_start5)
        singleItem_rating = view.findViewById(R.id.singleO_rating)
        singleItem_total = view.findViewById(R.id.singleO_totalPrice)
        singleItem_addToFav = view.findViewById(R.id.singleO_addToFav)
        singleOComments = view.findViewById(R.id.singleO_comments)
        orgPrice = view.findViewById(R.id.orgPrice)
        loading = view.findViewById(R.id.loading_singleItem)
        customerCommentsRv = view.findViewById(R.id.custCommentsRv)
        productRating = view.findViewById(R.id.productRating)
        totalReviews = view.findViewById(R.id.totalReviews)
        seek1Bar = view.findViewById(R.id.star1_bar)
        seek2Bar = view.findViewById(R.id.star2_bar)
        seek3Bar = view.findViewById(R.id.star3_bar)
        seek4Bar = view.findViewById(R.id.star4_bar)
        seek5Bar = view.findViewById(R.id.star5_bar)
        seek1Count = view.findViewById(R.id.star1Count)
        seek2Count = view.findViewById(R.id.star2Count)
        seek3Count = view.findViewById(R.id.star3Count)
        seek4Count = view.findViewById(R.id.star4Count)
        seek5Count = view.findViewById(R.id.star5Count)
        //showMoreReview = view.findViewById(R.id.showMoreReview)
        loadingRv = view.findViewById(R.id.loadingRv)
        nestedScroll = view.findViewById(R.id.nestedScroll)

        loading.visibility = View.VISIBLE

        /*val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        customerCommentsRv.layoutManager = layoutManager*/

        Log.d("productId", Constants.productID)

        getItemData()

        getProductRatingComments(page)

        nestedScroll.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                // in this method we are incrementing page number,
                // making progress bar visible and calling get data method.
                Log.d("scroll", "scroll")
                page++
                loadingRv.setVisibility(View.VISIBLE)
                getProductRatingComments(page)
            }
        })

        singleItem_radio_s.visibility = View.GONE
        singleItem_radio_r.visibility = View.GONE
        singleItem_radio_l.visibility = View.GONE

        setHasOptionsMenu(true)

        singleItem_radio_s.setOnClickListener {

            getProductSize("S")
        }

        singleItem_radio_r.setOnClickListener {

            getProductSize("M")
        }

        singleItem_radio_l.setOnClickListener {

            getProductSize("L")
        }

        singleItem_plus.setOnClickListener {

            val q = singleItem_num.text.toString()

            singleItem_num.text = (q.toInt() + 1).toString()

            val productPrice = singleItem_price.text.substring(1, singleItem_price.text.length)
            val itemCount = singleItem_num.text.toString()

            singleItem_total.text = "$" + (productPrice.toInt() * itemCount.toInt()).toString()
        }

        singleItem_minus.setOnClickListener {

            val q = singleItem_num.text.toString()

            if (q.toInt() == 1) {

                singleItem_num.text = (q.toInt()).toString()

                val productPrice = singleItem_price.text.substring(1, singleItem_price.text.length)
                val itemCount = singleItem_num.text.toString()

                singleItem_total.text = "$" + (productPrice.toInt() * itemCount.toInt()).toString()

            } else {

                singleItem_num.text = (q.toInt() - 1).toString()

                val productPrice = singleItem_price.text.substring(1, singleItem_price.text.length)
                val itemCount = singleItem_num.text.toString()

                singleItem_total.text = "$" + (productPrice.toInt() * itemCount.toInt()).toString()
            }
        }

        /*showMoreReview.setOnClickListener {

            Constants.page = Constants.page+1

            getProductRatingComments(Constants.page)
        }*/

        singleItem_addToCart.setOnClickListener {

            //Toast.makeText(context, "Item added to cart", Toast.LENGTH_SHORT).show()
            val size = Constants.foodName.size

            Log.d("size", foodSize)

            var dub = 0

            Log.d("name", singleItem_name.text.toString())


            for (i in Constants.foodName){

                for (j in Constants.foodSize){

                    for (k in  Constants.foodId)

                if (i.equals(singleItem_name.text.toString()) && j.equals(foodSize) && k.toString().equals(Constants.productID)) {

                    Log.d("mtches", "matchse")
                }

                }
            }

            if (Constants.foodName.contains(
                    singleItem_name.text
                ) && Constants.foodSize.contains(foodSize)
            ) {

                Log.d("size", foodSize)

                for (j in Constants.foodName.indices) {
                    if (Constants.foodName[j].matches(
                            Regex(singleItem_name.text.toString())
                        ) && Constants.foodSize[j].matches(Regex(foodSize))
                    ) {

                        Log.d("size1", foodSize)
                        val q = Constants.foodQuantity[j]
                        Log.d("foodQ", (q + 1).toString())
                        //Constants.foodQuantity.add(j, 1 + Constants.q);
                        Constants.foodQuantity[j] = q + 1
                        dashBoard.setCartCounter()
                        //sameFood = 1
                    }
                }
                /*if (size == Constants.foodName.size){

                    Constants.foodId.add(
                        Integer.valueOf(Constants.productID)
                    )
                    val itemCount = singleItem_num.text.toString()
                    Constants.foodSize.add(foodSize)
                    Constants.foodQuantity.add(itemCount.toInt())
                    Constants.foodImg.add(productImg)
                    Constants.foodName.add(singleItem_name.text.toString())
                    Constants.foodPrice.add(Integer.valueOf(singleItem_price.text.toString().substring(1,singleItem_price.text.length)))
                    Constants.cart_totalItems = Constants.foodId.size
                }*/

            } else {
                Constants.foodId.add(
                    Integer.valueOf(Constants.productID)
                )
                Log.d("id", Constants.productID)
                val itemCount = singleItem_num.text.toString()
                Constants.foodSize.add(foodSize)
                Constants.foodQuantity.add(itemCount.toInt())
                Constants.foodImg.add(productImg)
                Constants.foodName.add(singleItem_name.text.toString())
                Constants.foodPrice.add(
                    Integer.valueOf(
                        singleItem_price.text.toString().substring(1, singleItem_price.text.length)
                    )
                )
                Constants.cart_totalItems = Constants.foodId.size
            }
            dashBoard.card_count.visibility = View.VISIBLE
            dashBoard.setCartCounter()
        }

        singleItem_addToFav.setOnClickListener {

            addOrRemoveFav()
        }

        seek5Bar.setOnTouchListener(OnTouchListener { v, event -> true })
        seek4Bar.setOnTouchListener(OnTouchListener { v, event -> true })
        seek3Bar.setOnTouchListener(OnTouchListener { v, event -> true })
        seek2Bar.setOnTouchListener(OnTouchListener { v, event -> true })
        seek1Bar.setOnTouchListener(OnTouchListener { v, event -> true })

        singleOComments.setOnClickListener {

            if (auth.currentUser != null || Constants.isLogin != false){

                showRatingCommentDialog()
            }
            else {

                Toast.makeText(context, "Please Login to give rating", Toast.LENGTH_SHORT).show()
            }


        }

        return view
    }

    private fun showRatingCommentDialog() {

        dialog = Dialog(requireContext())
        dialog.setCancelable(true)

        val activity = context as AppCompatActivity

        val view = activity.layoutInflater.inflate(R.layout.rating_comment_dialog, null)

        dialog.setContentView(view)
        /*if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }*/

        /*if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }*/dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val ratingStar1 = view.findViewById<ImageView>(R.id.rating_star1)
        val ratingStar2 = view.findViewById<ImageView>(R.id.rating_star2)
        val ratingStar3 = view.findViewById<ImageView>(R.id.rating_star3)
        val ratingStar4 = view.findViewById<ImageView>(R.id.rating_star4)
        val ratingStar5 = view.findViewById<ImageView>(R.id.rating_star5)
        //TextView notNow = view.findViewById(R.id.rating_notNow);
        //TextView notNow = view.findViewById(R.id.rating_notNow);
        val update = view.findViewById<Button>(R.id.rating_submit)
        val orderComment = view.findViewById<EditText>(R.id.orderComment)
        val ratingImg = view.findViewById<CircleImageView>(R.id.rating_img)
        //EditText userNameE = view.findViewById(R.id.userEdit_name);

        //EditText userNameE = view.findViewById(R.id.userEdit_name);
        Picasso.get()
            .load(Constants.UserProduct_Path + itemPic)
            .into(ratingImg)

        ratingStar1.tag = ""
        ratingStar2.tag = ""
        ratingStar3.tag = ""
        ratingStar4.tag = ""
        ratingStar5.tag = ""
        var rating = "0"

        ratingStar1.setOnClickListener {
            if (ratingStar1.tag == "rated") {
                ratingStar1.setImageResource(R.drawable.vector__6_)
                ratingStar2.setImageResource(R.drawable.vector__6_)
                ratingStar3.setImageResource(R.drawable.vector__6_)
                ratingStar4.setImageResource(R.drawable.vector__6_)
                ratingStar5.setImageResource(R.drawable.vector__6_)
                rating = "0"
                ratingStar1.tag = "unrated"
                ratingStar2.tag = "unrated"
                ratingStar3.tag = "unrated"
                ratingStar4.tag = "unrated"
                ratingStar5.tag = "unrated"
            } else {
                ratingStar1.setImageResource(R.drawable.clarity_favorite_solid)
                rating = "1"
                ratingStar1.tag = "rated"
            }
        }

        ratingStar2.setOnClickListener {
            if (ratingStar2.tag == "rated") {
                ratingStar2.setImageResource(R.drawable.vector__6_)
                ratingStar3.setImageResource(R.drawable.vector__6_)
                ratingStar4.setImageResource(R.drawable.vector__6_)
                ratingStar5.setImageResource(R.drawable.vector__6_)
                rating = "1"
                ratingStar2.tag = "unrated"
                ratingStar3.tag = "unrated"
                ratingStar4.tag = "unrated"
                ratingStar5.tag = "unrated"
            } else {
                ratingStar1.setImageResource(R.drawable.clarity_favorite_solid)
                ratingStar2.setImageResource(R.drawable.clarity_favorite_solid)
                rating = "2"
                ratingStar1.tag = "rated"
                ratingStar2.tag = "rated"
            }
        }

        ratingStar3.setOnClickListener {
            if (ratingStar3.tag == "rated") {
                ratingStar3.setImageResource(R.drawable.vector__6_)
                ratingStar4.setImageResource(R.drawable.vector__6_)
                ratingStar5.setImageResource(R.drawable.vector__6_)
                rating = "2"
                ratingStar3.tag = "unrated"
                ratingStar4.tag = "unrated"
                ratingStar5.tag = "unrated"
            } else {
                ratingStar1.setImageResource(R.drawable.clarity_favorite_solid)
                ratingStar2.setImageResource(R.drawable.clarity_favorite_solid)
                ratingStar3.setImageResource(R.drawable.clarity_favorite_solid)
                rating = "3"
                ratingStar1.tag = "rated"
                ratingStar2.tag = "rated"
                ratingStar3.tag = "rated"
            }
        }

        ratingStar4.setOnClickListener {
            if (ratingStar4.tag == "rated") {
                ratingStar4.setImageResource(R.drawable.vector__6_)
                ratingStar5.setImageResource(R.drawable.vector__6_)
                rating = "3"
                ratingStar4.tag = "unrated"
                ratingStar5.tag = "unrated"
            } else {
                ratingStar1.setImageResource(R.drawable.clarity_favorite_solid)
                ratingStar2.setImageResource(R.drawable.clarity_favorite_solid)
                ratingStar3.setImageResource(R.drawable.clarity_favorite_solid)
                ratingStar4.setImageResource(R.drawable.clarity_favorite_solid)
                rating = "4"
                ratingStar1.tag = "rated"
                ratingStar2.tag = "rated"
                ratingStar3.tag = "rated"
                ratingStar4.tag = "rated"
            }
        }

        ratingStar5.setOnClickListener {
            if (ratingStar5.tag == "rated") {
                ratingStar5.setImageResource(R.drawable.vector__6_)
                rating = "4"
                ratingStar5.tag = "unrated"
            } else {
                ratingStar1.setImageResource(R.drawable.clarity_favorite_solid)
                ratingStar2.setImageResource(R.drawable.clarity_favorite_solid)
                ratingStar3.setImageResource(R.drawable.clarity_favorite_solid)
                ratingStar4.setImageResource(R.drawable.clarity_favorite_solid)
                ratingStar5.setImageResource(R.drawable.clarity_favorite_solid)
                rating = "5"
                ratingStar1.tag = "rated"
                ratingStar2.tag = "rated"
                ratingStar3.tag = "rated"
                ratingStar4.tag = "rated"
                ratingStar5.tag = "rated"
            }
        }

        Log.d("rating", rating)

        update.setOnClickListener {

            if (orderComment.text.toString().isNotEmpty() && rating != "0"){

                rateAndCommentAPI(
                    rating,
                    orderComment.text.toString(),
                    Constants.productID
                )
            }
            else {

                Toast.makeText(context, "Please rate and comment", Toast.LENGTH_SHORT).show()
            }

            //dialog.cancel();
            Log.d("rating", rating)
        }

        dialog.show()
    }

    private fun rateAndCommentAPI(rating: String, comment: String, productId: String) {


        val requestSetProductRating = RequestProductRating()

        requestSetProductRating.branchId = "1"
        requestSetProductRating.productId = productId
        requestSetProductRating.ratingScore = rating
        requestSetProductRating.ratingcomment = comment

        val retrofitInstance = RetrofitInstance()

        val retrofitData : Call<Response_Common>

        retrofitData = retrofitInstance.retrofit.setProductRatingComment(Constants.USER_TOKEN, requestSetProductRating)

        retrofitData.enqueue(object : Callback<Response_Common>{
            override fun onResponse(
                call: Call<Response_Common>,
                response: Response<Response_Common>
            ) {

                if (response.isSuccessful){

                    Toast.makeText(context, "Thanks for rating this item", Toast.LENGTH_SHORT).show()

                    dialog.cancel()
                }
                else {

                    Toast.makeText(context,"You already rated this item", Toast.LENGTH_SHORT).show()
                    dialog.cancel()
                }
            }

            override fun onFailure(call: Call<Response_Common>, t: Throwable) {

                Log.d("error", t.toString())

            }
        })

    }

    private fun getProductRatingComments(page: Int) {

        if(page == 1){

            recycleView_models.clear()
            custCommentN.clear()
            custNameN.clear()
            custRatingN.clear()
            custImgN.clear()
            rateDateN.clear()
        }

        val requestProductDetails = Request_ProductDetails()
        requestProductDetails.productId = Constants.productID
        requestProductDetails.limit = 10
        requestProductDetails.page = page

        val retrofitInstance = RetrofitInstance()

        val retrofitData : Call<Response_Common>

        if (auth.currentUser != null || Constants.isLogin != false){

            retrofitData = retrofitInstance.retrofit.getProductRatingComment(Constants.USER_TOKEN, requestProductDetails)
        }else {

            retrofitData = retrofitInstance.retrofit.getProductRatingComment("x-token",requestProductDetails)
        }

        retrofitData.enqueue(object : Callback<Response_Common> {
            override fun onResponse(
                call: Call<Response_Common>,
                response: Response<Response_Common>
            ) {

                if (response.isSuccessful) {

                    Log.d("pagework", "work")

                    custComment.clear()
                    custName.clear()
                    custRating.clear()
                    custImg.clear()
                    rateDate.clear()

                    val sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())

                    Log.d("indice", response.body()!!.getData()!!.RatingResponce!!.rows.indices.toString())

                    if (response.body()!!.getData()!!.RatingResponce!!.rows.isEmpty()){

                        loadingRv.visibility = View.GONE

                    }

                    if (response.body()!!.getData()!!.RatingResponce!!.rows.isNotEmpty()) {

                        for (i in response.body()!!.getData()!!.RatingResponce!!.rows.indices) {

                            val createdDateTime =
                                response.body()!!.getData()!!.RatingResponce!!.rows[i].createdAt!!
                            val date1: Date = sdf.parse(createdDateTime) as Date
                            val createdDate =
                                SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date1)

                            Log.d("date122222", createdDate)

                            Log.d("pagework11", "work")

                            recycleView_models.add(
                                RecycleView_Model(
                                    response.body()!!.getData()!!.RatingResponce!!.rows[i].Customer!!.userName.toString(),
                                    createdDate,
                                    response.body()!!.getData()!!.RatingResponce!!.rows[i].comment.toString(),
                                    response.body()!!.getData()!!.RatingResponce!!.rows[i].Customer!!.profilePic.toString(),
                                    response.body()!!.getData()!!.RatingResponce!!.rows[i].rating.toString()
                                )
                            )

                            val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

                            customerCommentsRv.layoutManager = layoutManager
                            val recycleView_adapter_CC = RecycleView_Adapter_CC(activity, recycleView_models)
                            customerCommentsRv.adapter = recycleView_adapter_CC

                        }
                    }

                    productRating.text = response.body()!!.getData()!!.productaveraeratingresponce!!.customerrating.toString()
                    seek1Count.text = response.body()!!.getData()!!.productaveraeratingresponce!!.starone.toString()
                    seek2Count.text = response.body()!!.getData()!!.productaveraeratingresponce!!.startwo.toString()
                    seek3Count.text = response.body()!!.getData()!!.productaveraeratingresponce!!.startthree.toString()
                    seek4Count.text = response.body()!!.getData()!!.productaveraeratingresponce!!.startfour.toString()
                    seek5Count.text = response.body()!!.getData()!!.productaveraeratingresponce!!.starfive.toString()

                    val totalCount = Integer.parseInt(seek1Count.text.toString())+Integer.parseInt(seek2Count.text.toString())+
                            Integer.parseInt(seek3Count.text.toString())+Integer.parseInt(seek4Count.text.toString())+
                            Integer.parseInt(seek5Count.text.toString())

                    val onePercentValue : Float = (100/totalCount.toFloat())

                    Log.d("onePer", onePercentValue.toString())

                    val seekBar1Per = onePercentValue * Integer.parseInt(seek1Count.text.toString())
                    val seekBar2Per = onePercentValue * Integer.parseInt(seek2Count.text.toString())
                    val seekBar3Per = onePercentValue * Integer.parseInt(seek3Count.text.toString())
                    val seekBar4Per = onePercentValue * Integer.parseInt(seek4Count.text.toString())
                    val seekBar5Per = onePercentValue * Integer.parseInt(seek5Count.text.toString())

                    totalReviews.text = response.body()!!.getData()!!.RatingResponce!!.count.toString()+" reviews"

                    Log.d("percent", seekBar1Per.toInt().toString() + "--" + seekBar2Per.toInt().toString()+ "--" + seekBar3Per.toInt().toString() + "--" +
                            seekBar4Per.toInt().toString()+ "--" + seekBar5Per.toInt().toString())

                    if (seekBar1Per.toInt() != 0){

                        seek1Bar.secondaryProgress = seekBar1Per.toInt()
                    }
                    if(seekBar2Per.toInt() != 0){

                        seek2Bar.secondaryProgress = seekBar2Per.toInt()
                    }
                    if (seekBar3Per.toInt() != 0){

                        seek3Bar.secondaryProgress = seekBar3Per.toInt()
                    }
                    if (seekBar4Per.toInt() != 0){

                        seek4Bar.secondaryProgress = seekBar4Per.toInt()
                    }
                    if (seekBar5Per.toInt() != 0){

                        seek5Bar.secondaryProgress = seekBar5Per.toInt()
                    }
                }
            }

            override fun onFailure(call: Call<Response_Common>, t: Throwable) {
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setCommentsRv() {

        recycleView_models.clear()

            for (i in custName.indices) {

                custCommentN.add(custComment[i])
                custNameN.add(custName[i])
                custImgN.add(custImg[i])
                custRatingN.add(custRating[i])
                rateDateN.add(rateDate[i])
            }

        Log.d("pagesizeN", custNameN.size.toString())

            for (i in custNameN.indices) {

                recycleView_models.add(
                    RecycleView_Model(
                        custNameN[i],
                        rateDateN[i],
                        custCommentN[i],
                        custImgN[i],
                        custRatingN[i]
                    )
                )

                val recycleView_adapter_CC = RecycleView_Adapter_CC(activity, recycleView_models)
                customerCommentsRv.adapter = recycleView_adapter_CC
                recycleView_adapter_CC.notifyItemInserted(i)
            }
    }

    private fun addOrRemoveFav() {

        viewModel.addOrRemoveFav(this, Constants.productID, "1", singleItem_addToFav)
            .observe(viewLifecycleOwner, Observer {

                if (it != null) {

                    if (it.getSuccess()!!) {


                        Toast.makeText(activity, "Added to Faverite", Toast.LENGTH_SHORT).show()

                    }
                }
            })

    }

    private fun getProductSize(size: String) {

        for (i in itemSize.indices) {

            if (size.equals("S")) {

                if (itemSize[i].equals(size)) {

                    singleItem_radio_s.isEnabled = true
                    singleItem_radio_s.isChecked = true

                    orgPrice.text = "$" + itemAmount[i]
                    singleItem_price.text = "$" + offerAmount[i]
                    orgPrice.paintFlags = orgPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    var productPrice = "0"

                    if (offerAmount.size != 0) {
                        productPrice = offerAmount[i]
                    } else {
                        productPrice = itemAmount[i]
                    }

                    val itemCount = singleItem_num.text.toString()
                    foodSize = "S"
                    singleItem_total.text =
                        "$" + (productPrice.toInt() * itemCount.toInt()).toString()
                } else {

                    //Toast.makeText(activity, "Size not available", Toast.LENGTH_SHORT).show()
                }
            } else if (size.equals("M")) {

                if (itemSize[i].equals(size)) {

                    singleItem_radio_r.isEnabled = true
                    singleItem_radio_r.isChecked = true

                    orgPrice.text = "$" + itemAmount[i]
                    singleItem_price.text = "$" + offerAmount[i]
                    orgPrice.paintFlags = orgPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    var productPrice = "0"

                    if (offerAmount.size != 0) {
                        productPrice = offerAmount[i]
                    } else {
                        productPrice = itemAmount[i]
                    }
                    val itemCount = singleItem_num.text.toString()
                    foodSize = "M"
                    singleItem_total.text =
                        "$" + (productPrice.toInt() * itemCount.toInt()).toString()
                } else {

                    //Toast.makeText(activity, "Size not available", Toast.LENGTH_SHORT).show()
                }
            } else if (size.equals("L")) {

                if (itemSize[i].equals(size)) {

                    singleItem_radio_l.isEnabled = true
                    singleItem_radio_l.isChecked = true

                    orgPrice.text = "$" + itemAmount[i]
                    singleItem_price.text = "$" + offerAmount[i]
                    orgPrice.paintFlags = orgPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    var productPrice = "0"

                    if (offerAmount.size != 0) {
                        productPrice = offerAmount[i]
                    } else {
                        productPrice = itemAmount[i]
                    }
                    val itemCount = singleItem_num.text.toString()
                    foodSize = "L"
                    singleItem_total.text =
                        "$" + (productPrice.toInt() * itemCount.toInt()).toString()
                } else {

                    //Toast.makeText(activity, "Size not available", Toast.LENGTH_SHORT).show()
                }
            } else {

                Toast.makeText(activity, "Size not available", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getItemData() {

        viewModel.getItemDetails(this, Constants.productID, loading)
            .observe(viewLifecycleOwner, Observer {

                if (it != null) {

                    if (it.getSuccess()!!) {

                        itemAmount.clear()
                        itemSize.clear()
                        itemOfferAmt.clear()
                        offerAmount.clear()

                        singleItem_name.text =
                            it.getData()!!.ProductResponce!!.productName.toString()
                        singleItem_des.text =
                            it.getData()!!.ProductResponce!!.productDesc.toString()
                        productImg = it.getData()!!.ProductResponce!!.productPic.toString()

                        itemPic = it.getData()!!.ProductResponce!!.productPic.toString()

                        Picasso.get()
                            .load(Constants.UserProduct_Path + it.getData()!!.ProductResponce!!.productPic.toString())
                            .into(singleItem_img)

                        singleItem_rating.text =
                            it.getData()!!.ProductResponce!!.customerrating.toString()

                        if (auth.currentUser != null || isLogin != false) {

                            if (it.getData()!!.ProductResponce!!.favorite == 1) {

                                singleItem_addToFav.setImageResource(R.drawable.red_heart)
                            } else {

                                singleItem_addToFav.setImageResource(R.drawable.bi_heart)
                            }
                        } else {

                            singleItem_addToFav.visibility = View.GONE
                        }

                        setRatings()

                        productRating.text = singleItem_rating.text

                        for (i in it.getData()!!.ProductResponce!!.Productprices!!.indices) {

                            if (it.getData()!!.ProductResponce!!.Productprices!![i].offerAmount != null) {

                                offerAmount.add(it.getData()!!.ProductResponce!!.Productprices!![i].offerAmount.toString())
                                orgPrice.visibility = View.VISIBLE
                            } else {

                                orgPrice.visibility = View.GONE
                            }
                            itemAmount.add(it.getData()!!.ProductResponce!!.Productprices!![i].amount.toString())
                            itemOfferAmt.add(it.getData()!!.ProductResponce!!.Productprices!![i].offerAmount.toString())
                            itemSize.add(it.getData()!!.ProductResponce!!.Productprices!![i].productsize.toString())

                        }

                        for (i in itemSize.indices){

                            Log.d("item", itemSize[i])

                            if (itemSize[i] == "S"){

                                singleItem_radio_s.visibility = View.VISIBLE
                            }
                            else if (itemSize[i] == "M"){

                                singleItem_radio_r.visibility = View.VISIBLE
                            }
                            else if (itemSize[i] == "L"){

                                singleItem_radio_l.visibility = View.VISIBLE
                            }
                        }

                        //Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
                    }

                    loading.visibility = View.GONE
                    setUpView()
                }

            })
    }

    private fun setRatings() {

        if (singleItem_rating.text.matches(Regex("^[0]")) || singleItem_rating.text.matches(Regex("^[0][.]")) ||
            singleItem_rating.text.matches(Regex("^[1][.][12345]"))
        ) {

            singleItem_rating.text = "1.0"
            singleItem_star1.setImageResource(R.drawable.clarity_favorite_solid)
            singleItem_star2.setImageResource(R.drawable.vector__6_)
            singleItem_star3.setImageResource(R.drawable.vector__6_)
            singleItem_star4.setImageResource(R.drawable.vector__6_)
            singleItem_star5.setImageResource(R.drawable.vector__6_)

        } else if (singleItem_rating.text.matches(Regex("^[1][.][6789]")) || singleItem_rating.text.matches(
                Regex("^[2][.][1234]")
            )
        ) {

            singleItem_star1.setImageResource(R.drawable.clarity_favorite_solid)
            singleItem_star2.setImageResource(R.drawable.clarity_favorite_solid)
            singleItem_star3.setImageResource(R.drawable.vector__6_)
            singleItem_star4.setImageResource(R.drawable.vector__6_)
            singleItem_star5.setImageResource(R.drawable.vector__6_)

        } else if (singleItem_rating.text.matches(Regex("^[2][.][6789]")) || singleItem_rating.text.matches(
                Regex("^[3][.][1234]")
            )
        ) {

            singleItem_star1.setImageResource(R.drawable.clarity_favorite_solid)
            singleItem_star2.setImageResource(R.drawable.clarity_favorite_solid)
            singleItem_star3.setImageResource(R.drawable.clarity_favorite_solid)
            singleItem_star4.setImageResource(R.drawable.vector__6_)
            singleItem_star5.setImageResource(R.drawable.vector__6_)

        } else if (singleItem_rating.text.matches(Regex("^[3][.][6789]")) || singleItem_rating.text.matches(
                Regex("^[4][.][12345]")
            )
        ) {

            singleItem_star1.setImageResource(R.drawable.clarity_favorite_solid)
            singleItem_star2.setImageResource(R.drawable.clarity_favorite_solid)
            singleItem_star3.setImageResource(R.drawable.clarity_favorite_solid)
            singleItem_star4.setImageResource(R.drawable.clarity_favorite_solid)
            singleItem_star5.setImageResource(R.drawable.vector__6_)

        } else {

            singleItem_star1.setImageResource(R.drawable.clarity_favorite_solid)
            singleItem_star2.setImageResource(R.drawable.clarity_favorite_solid)
            singleItem_star3.setImageResource(R.drawable.clarity_favorite_solid)
            singleItem_star4.setImageResource(R.drawable.clarity_favorite_solid)
            singleItem_star5.setImageResource(R.drawable.clarity_favorite_solid)

        }

    }

    private fun setUpView() {

        val size = itemSize.get(0)

        Log.d("size", size)

        if (size.equals("S")) {

            singleItem_radio_s.isChecked = true
            singleItem_radio_r.isChecked = false
            singleItem_radio_l.isChecked = false
            singleItem_radio_r.isEnabled = false
            singleItem_radio_l.isEnabled = false

            orgPrice.text = "$" + itemAmount[0]
            singleItem_price.text = "$" + offerAmount[0]
            orgPrice.paintFlags = orgPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            var productPrice = "0"

            if (offerAmount.size != 0) {
                productPrice = offerAmount[0]
            } else {
                productPrice = itemAmount[0]
            }
            val itemCount = singleItem_num.text.toString()
            foodSize = "S"
            singleItem_total.text = "$" + (productPrice.toInt() * itemCount.toInt()).toString()

        } else if (size.equals("M")) {

            singleItem_radio_s.isChecked = false
            singleItem_radio_r.isChecked = true
            singleItem_radio_l.isChecked = false
            singleItem_radio_s.isEnabled = false
            singleItem_radio_l.isEnabled = false

            orgPrice.text = "$" + itemAmount[0]
            singleItem_price.text = "$" + offerAmount[0]
            orgPrice.paintFlags = orgPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            var productPrice = "0"

            if (offerAmount.size != 0) {
                productPrice = offerAmount[0]
            } else {
                productPrice = itemAmount[0]
            }
            val itemCount = singleItem_num.text.toString()
            foodSize = "M"
            singleItem_total.text = "$" + (productPrice.toInt() * itemCount.toInt()).toString()

        } else {

            singleItem_radio_s.isChecked = false
            singleItem_radio_r.isChecked = false
            singleItem_radio_l.isChecked = true
            singleItem_radio_s.isEnabled = false
            singleItem_radio_r.isEnabled = false

            orgPrice.text = "$" + itemAmount[0]
            singleItem_price.text = "$" + offerAmount[0]
            orgPrice.paintFlags = orgPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            var productPrice = "0"

            if (offerAmount.size != 0) {
                productPrice = offerAmount[0]
            } else {
                productPrice = itemAmount[0]
            }
            val itemCount = singleItem_num.text.toString()
            foodSize = "L"
            singleItem_total.text = "$" + (productPrice.toInt() * itemCount.toInt()).toString()

        }

        if (itemSize.size > 1) {

            if (itemSize.size == 2) {
                val size = itemSize.get(1)
                if (size.equals("M")) {

                    singleItem_radio_s.isChecked = false
                    singleItem_radio_r.isChecked = true
                    singleItem_radio_l.isChecked = false
                    singleItem_radio_s.isEnabled = false
                    singleItem_radio_l.isEnabled = false

                    orgPrice.text = "$" + itemAmount[1]
                    singleItem_price.text = "$" + offerAmount[1]
                    orgPrice.paintFlags = orgPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    var productPrice = "0"

                    if (offerAmount.size != 0) {
                        productPrice = offerAmount[1]
                    } else {
                        productPrice = itemAmount[1]
                    }
                    val itemCount = singleItem_num.text.toString()
                    foodSize = "M"
                    singleItem_total.text =
                        "$" + (productPrice.toInt() * itemCount.toInt()).toString()

                }
            } else {

                val size = itemSize.get(2)
                if (size.equals("M")) {
                    Log.d("size", size)
                    singleItem_radio_s.isChecked = false
                    singleItem_radio_r.isChecked = true
                    singleItem_radio_l.isChecked = false
                    singleItem_radio_s.isEnabled = false
                    singleItem_radio_l.isEnabled = false

                    orgPrice.text = "$" + itemAmount[2]
                    singleItem_price.text = "$" + offerAmount[2]
                    orgPrice.paintFlags = orgPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    var productPrice = "0"

                    if (offerAmount.size != 0) {
                        productPrice = offerAmount[2]
                    } else {
                        productPrice = itemAmount[2]
                    }
                    val itemCount = singleItem_num.text.toString()
                    foodSize = "M"
                    singleItem_total.text =
                        "$" + (productPrice.toInt() * itemCount.toInt()).toString()

                }
            }

            singleItem_radio_s.isEnabled = true
            singleItem_radio_r.isEnabled = true
            singleItem_radio_l.isEnabled = true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.getItem(0).setVisible(false)
        menu.getItem(1).setVisible(false)
        menu.getItem(2).setVisible(false)
        menu.getItem(3).setVisible(true)
    }

}