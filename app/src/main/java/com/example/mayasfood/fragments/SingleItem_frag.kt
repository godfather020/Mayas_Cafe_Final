package com.example.mayasfood.fragments

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
    var itemOfferAmt = ArrayList<String>()
    var itemSize = ArrayList<String>()

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

        dashBoard.bottomNavigationView.visibility = View.GONE

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
        loading = view.findViewById(R.id.loading_singleItem)

        Log.d("productId", Constants.productID)

        getItemData()

        setHasOptionsMenu(true)

        return view
    }

    private fun getItemData() {

        viewModel.getItemDetails(this, Constants.productID, loading).observe(viewLifecycleOwner, Observer {

            if (it != null){

                if (it.getSuccess()!!) {

                    itemAmount.clear()
                    itemSize.clear()
                    itemOfferAmt.clear()

                    singleItem_name.text = it.getData()!!.ProductResponce!!.productName.toString()
                    singleItem_des.text = it.getData()!!.ProductResponce!!.productDesc.toString()

                    Picasso.get().load(Constants.UserProduct_Path+it.getData()!!.ProductResponce!!.productPic.toString())
                        .into(singleItem_img)

                    singleItem_rating.text = it.getData()!!.ProductResponce!!.customerrating.toString()

                    for (i in it.getData()!!.ProductResponce!!.Productprices!!.indices){

                        itemAmount.add(it.getData()!!.ProductResponce!!.Productprices!![i].amount.toString())
                        itemOfferAmt.add(it.getData()!!.ProductResponce!!.Productprices!![i].offerAmount.toString())
                        itemSize.add(it.getData()!!.ProductResponce!!.Productprices!![i].productsize.toString())

                    }

                    Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
                }

                loading.visibility = View.GONE
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.getItem(0).setVisible(false)
        menu.getItem(1).setVisible(false)
        menu.getItem(2).setVisible(true)
    }

}