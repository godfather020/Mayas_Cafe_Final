package com.example.mayasfood.fragments

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mayasfood.R
import com.example.mayasfood.activity.DashBoard
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.fragments.ViewModels.SingleItem_viewModel
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class SingleItem_frag : Fragment() {

    lateinit var dashBoard: DashBoard
    lateinit var singleItem_img : CircleImageView
    lateinit var singleItem_name : TextView
    lateinit var singleItem_price : TextView
    lateinit var singleItem_total : TextView
    lateinit var singleItem_addToCart : Button
    lateinit var singleItem_rating : TextView
    lateinit var singleItem_comment : TextView
    lateinit var singleItem_des : TextView
    lateinit var singleItem_star1 : ImageView
    lateinit var singleItem_star2 : ImageView
    lateinit var singleItem_star3 : ImageView
    lateinit var singleItem_star4 : ImageView
    lateinit var singleItem_star5 : ImageView
    lateinit var singleItem_radio_r : RadioButton
    lateinit var singleItem_radio_s : RadioButton
    lateinit var singleItem_radio_l : RadioButton
    lateinit var singleItem_plus : ImageView
    lateinit var singleItem_minus : ImageView
    lateinit var singleItem_num : TextView
    lateinit var singleItem_addToFav : ImageView
    lateinit var loading : ProgressBar
    lateinit var viewModel : SingleItem_viewModel
    var itemAmount = ArrayList<String>()
    var offerAmount = ArrayList<String>()
    var itemOfferAmt = ArrayList<String>()
    var itemSize = ArrayList<String>()
    lateinit var auth: FirebaseAuth
    var productImg = ""
    lateinit var orgPrice : TextView

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
        val view : View =  inflater.inflate(R.layout.fragment_single_item_frag, container, false)

        viewModel = ViewModelProvider(this).get(SingleItem_viewModel::class.java)

        dashBoard = activity as DashBoard

        dashBoard.toolbar_const.setTitle("Product Details")
        dashBoard.toolbar_const.setTitleTextColor(Color.BLACK)

        dashBoard.bottomNavigationView.visibility = View.GONE

        auth = FirebaseAuth.getInstance()

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
        orgPrice = view.findViewById(R.id.orgPrice)
        loading = view.findViewById(R.id.loading_singleItem)
        loading.visibility = View.VISIBLE

        Log.d("productId", Constants.productID)

        getItemData()

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

            val productPrice = singleItem_price.text.substring(1,singleItem_price.text.length)
            val itemCount = singleItem_num.text.toString()

            singleItem_total.text = "$"+(productPrice.toInt() * itemCount.toInt()).toString()
        }

        singleItem_minus.setOnClickListener {

            val q = singleItem_num.text.toString()

            if (q.toInt() == 1) {

                singleItem_num.text = (q.toInt()).toString()

                val productPrice = singleItem_price.text.substring(1,singleItem_price.text.length)
                val itemCount = singleItem_num.text.toString()

                singleItem_total.text = "$"+(productPrice.toInt() * itemCount.toInt()).toString()

            } else {

                singleItem_num.text = (q.toInt() - 1).toString()

                val productPrice = singleItem_price.text.substring(1,singleItem_price.text.length)
                val itemCount = singleItem_num.text.toString()

                singleItem_total.text = "$"+(productPrice.toInt() * itemCount.toInt()).toString()
            }
        }

        singleItem_addToCart.setOnClickListener {

            //Toast.makeText(context, "Item added to cart", Toast.LENGTH_SHORT).show()

            if (Constants.foodName.contains(
                    singleItem_name.text
                )
            ) {
                for (j in Constants.foodName.indices) {
                    if (Constants.foodName[j].matches(
                            Regex(singleItem_name.text.toString())
                        )
                    ) {
                        val q = Constants.foodQuantity[j]
                        Log.d("foodQ", (q + 1).toString())
                        //Constants.foodQuantity.add(j, 1 + Constants.q);
                        Constants.foodQuantity[j] = q + 1
                        dashBoard.setCartCounter()
                    }
                }
            } else {
                Constants.foodId.add(
                    Integer.valueOf(Constants.productID)
                )
                val itemCount = singleItem_num.text.toString()

                Constants.foodQuantity.add(itemCount.toInt())
                Constants.foodImg.add(productImg)
                Constants.foodName.add(singleItem_name.text.toString())
                Constants.foodPrice.add(Integer.valueOf(singleItem_price.text.toString().substring(1,singleItem_price.text.length)))
                Constants.cart_totalItems = Constants.foodId.size
            }
            dashBoard.card_count.visibility = View.VISIBLE
            dashBoard.setCartCounter()
        }

        singleItem_addToFav.setOnClickListener {

            addOrRemoveFav()
        }

        return view
    }

    private fun addOrRemoveFav() {

        viewModel.addOrRemoveFav(this, Constants.productID, "1", singleItem_addToFav).observe(viewLifecycleOwner, Observer {

            if (it != null){

                if (it.getSuccess()!!){

                    Toast.makeText(activity, "Added to Faverite", Toast.LENGTH_SHORT).show()

                }
            }
        })

    }

    private fun getProductSize(size: String) {

        for (i in itemSize.indices){

            if (size.equals("S")){

                if (itemSize[i].equals(size)){

                    singleItem_radio_s.isEnabled = true
                    singleItem_radio_s.isChecked = true

                    orgPrice.text = "$"+itemAmount[i]
                    singleItem_price.text = "$"+offerAmount[i]
                    orgPrice.paintFlags = orgPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    var productPrice = "0"

                    if (offerAmount.size != 0){
                        productPrice = offerAmount[i]
                    }
                    else {
                        productPrice = itemAmount[i]
                    }

                    val itemCount = singleItem_num.text.toString()

                    singleItem_total.text = "$"+(productPrice.toInt() * itemCount.toInt()).toString()
                }
                else{

                    //Toast.makeText(activity, "Size not available", Toast.LENGTH_SHORT).show()
                }
            }
            else if (size.equals("M")){

                if (itemSize[i].equals(size)){

                    singleItem_radio_r.isEnabled = true
                    singleItem_radio_r.isChecked = true

                    orgPrice.text = "$"+itemAmount[i]
                    singleItem_price.text = "$"+offerAmount[i]
                    orgPrice.paintFlags = orgPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    var productPrice = "0"

                    if (offerAmount.size != 0){
                        productPrice = offerAmount[i]
                    }
                    else {
                        productPrice = itemAmount[i]
                    }
                    val itemCount = singleItem_num.text.toString()

                    singleItem_total.text = "$"+(productPrice.toInt() * itemCount.toInt()).toString()
                }
                else{

                    //Toast.makeText(activity, "Size not available", Toast.LENGTH_SHORT).show()
                }
            }
            else if (size.equals("L")){

                if (itemSize[i].equals(size)){

                    singleItem_radio_l.isEnabled = true
                    singleItem_radio_l.isChecked = true

                    orgPrice.text = "$"+itemAmount[i]
                    singleItem_price.text = "$"+offerAmount[i]
                    orgPrice.paintFlags = orgPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                    var productPrice = "0"

                    if (offerAmount.size != 0){
                        productPrice = offerAmount[i]
                    }
                    else {
                        productPrice = itemAmount[i]
                    }
                    val itemCount = singleItem_num.text.toString()

                    singleItem_total.text = "$"+(productPrice.toInt() * itemCount.toInt()).toString()
                }
                else{

                    //Toast.makeText(activity, "Size not available", Toast.LENGTH_SHORT).show()
                }
            }
            else{

                Toast.makeText(activity, "Size not available", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getItemData() {

        viewModel.getItemDetails(this, Constants.productID, loading).observe(viewLifecycleOwner, Observer {

            if (it != null){

                if (it.getSuccess()!!) {

                    itemAmount.clear()
                    itemSize.clear()
                    itemOfferAmt.clear()
                    offerAmount.clear()

                    singleItem_name.text = it.getData()!!.ProductResponce!!.productName.toString()
                    singleItem_des.text = it.getData()!!.ProductResponce!!.productDesc.toString()
                    productImg = it.getData()!!.ProductResponce!!.productPic.toString()

                    Picasso.get().load(Constants.UserProduct_Path+it.getData()!!.ProductResponce!!.productPic.toString())
                        .into(singleItem_img)

                    singleItem_rating.text = it.getData()!!.ProductResponce!!.customerrating.toString()

                    if (auth.currentUser != null){

                        if (it.getData()!!.ProductResponce!!.favorite == 1){

                            singleItem_addToFav.setImageResource(R.drawable.red_heart)
                        }
                        else{

                            singleItem_addToFav.setImageResource(R.drawable.bi_heart)
                        }
                    }
                    else{

                        singleItem_addToFav.visibility = View.GONE
                    }

                    setRatings()

                    for (i in it.getData()!!.ProductResponce!!.Productprices!!.indices){

                        if (it.getData()!!.ProductResponce!!.Productprices!![i].offerAmount != null) {

                            offerAmount.add(it.getData()!!.ProductResponce!!.Productprices!![i].offerAmount.toString())
                            orgPrice.visibility = View.VISIBLE
                        }
                        else{

                            orgPrice.visibility = View.GONE
                        }
                        itemAmount.add(it.getData()!!.ProductResponce!!.Productprices!![i].amount.toString())
                        itemOfferAmt.add(it.getData()!!.ProductResponce!!.Productprices!![i].offerAmount.toString())
                        itemSize.add(it.getData()!!.ProductResponce!!.Productprices!![i].productsize.toString())

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
                Regex("^[2][.][1234]"))
        ) {

            singleItem_star1.setImageResource(R.drawable.clarity_favorite_solid)
            singleItem_star2.setImageResource(R.drawable.clarity_favorite_solid)
            singleItem_star3.setImageResource(R.drawable.vector__6_)
            singleItem_star4.setImageResource(R.drawable.vector__6_)
            singleItem_star5.setImageResource(R.drawable.vector__6_)

        } else if (singleItem_rating.text.matches(Regex("^[2][.][6789]")) || singleItem_rating.text.matches(
                Regex("^[3][.][1234]"))
        ) {

            singleItem_star1.setImageResource(R.drawable.clarity_favorite_solid)
            singleItem_star2.setImageResource(R.drawable.clarity_favorite_solid)
            singleItem_star3.setImageResource(R.drawable.clarity_favorite_solid)
            singleItem_star4.setImageResource(R.drawable.vector__6_)
            singleItem_star5.setImageResource(R.drawable.vector__6_)

        } else if (singleItem_rating.text.matches(Regex("^[3][.][6789]")) || singleItem_rating.text.matches(
                Regex("^[4][.][12345]"))
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

            if (size.equals("S")) {

                singleItem_radio_s.isChecked = true
                singleItem_radio_r.isChecked = false
                singleItem_radio_l.isChecked = false
                singleItem_radio_r.isEnabled = false
                singleItem_radio_l.isEnabled = false

                orgPrice.text = "$"+itemAmount[0]
                singleItem_price.text = "$"+offerAmount[0]
                orgPrice.paintFlags = orgPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                var productPrice = "0"

                if (offerAmount.size != 0){
                    productPrice = offerAmount[0]
                }
                else {
                    productPrice = itemAmount[0]
                }
                val itemCount = singleItem_num.text.toString()

                singleItem_total.text = "$"+(productPrice.toInt() * itemCount.toInt()).toString()

            } else if (size.equals("M")) {

                singleItem_radio_s.isChecked = false
                singleItem_radio_r.isChecked = true
                singleItem_radio_l.isChecked = false
                singleItem_radio_s.isEnabled = false
                singleItem_radio_l.isEnabled = false

                orgPrice.text = "$"+itemAmount[0]
                singleItem_price.text = "$"+offerAmount[0]
                orgPrice.paintFlags = orgPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                var productPrice = "0"

                if (offerAmount.size != 0){
                    productPrice = offerAmount[0]
                }
                else {
                    productPrice = itemAmount[0]
                }
                val itemCount = singleItem_num.text.toString()

                singleItem_total.text = "$"+(productPrice.toInt() * itemCount.toInt()).toString()

            } else {

                singleItem_radio_s.isChecked = false
                singleItem_radio_r.isChecked = false
                singleItem_radio_l.isChecked = true
                singleItem_radio_s.isEnabled = false
                singleItem_radio_r.isEnabled = false

                orgPrice.text = "$"+itemAmount[0]
                singleItem_price.text = "$"+offerAmount[0]
                orgPrice.paintFlags = orgPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                var productPrice = "0"

                if (offerAmount.size != 0){
                    productPrice = offerAmount[0]
                }
                else {
                    productPrice = itemAmount[0]
                }
                val itemCount = singleItem_num.text.toString()

                singleItem_total.text = "$"+(productPrice.toInt() * itemCount.toInt()).toString()

            }

        if (itemSize.size > 1){

            singleItem_radio_s.isEnabled = true
            singleItem_radio_r.isEnabled = true
            singleItem_radio_l.isEnabled = true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.getItem(0).setVisible(false)
        menu.getItem(1).setVisible(false)
        menu.getItem(2).setVisible(true)
    }

}