package com.example.mayasfood.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.R
import com.example.mayasfood.Retrofite.request.Request_OrderDetails
import com.example.mayasfood.activity.Login
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.fragments.ViewModels.CheckOut_frag_ViewModel
import com.example.mayasfood.functions.Functions
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_CO

class CheckOut_frag : Fragment() {

    lateinit var checkOut_TotalItems : TextView
    lateinit var checkOut_subTotal : TextView
    lateinit var checkout_tax: TextView
    lateinit var checkout_Total : TextView
    lateinit var clear_cart : TextView
    lateinit var recyclerView : RecyclerView
    lateinit var loading : ProgressBar
    var recycleView_models = ArrayList<RecycleView_Model>()
    lateinit var checkOut : Button
    lateinit var cart_discount_txt : TextView
    lateinit var cart_discount_img : ImageView
    lateinit var cart_discount_clickHere : TextView
    lateinit var cart_card : CardView
    lateinit var cart_promo : EditText
    lateinit var cart_promo_btn : Button
    lateinit var cart_empty_img : ImageView
    lateinit var cart_empty_txt : TextView
    lateinit var cart_empty_btn : Button
    lateinit var viewModel : CheckOut_frag_ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_check_out_frag, container, false)

        viewModel = ViewModelProvider(this).get(CheckOut_frag_ViewModel::class.java)

        setHasOptionsMenu(true)

        checkOut_TotalItems = view.findViewById(R.id.checkout_totallItems)
        clear_cart = view.findViewById(R.id.checkout_clearAll)
        checkOut_subTotal = view.findViewById(R.id.checkout_subtotal)
        checkout_tax = view.findViewById(R.id.checkout_tax)
        checkout_Total = view.findViewById(R.id.checkout_total)
        checkOut = view.findViewById(R.id.checkout_btn)
        cart_discount_txt = view.findViewById(R.id.cart_dicount_txt)
        cart_discount_img = view.findViewById(R.id.cart_discount_img)
        cart_discount_clickHere = view.findViewById(R.id.cart_clickHere)
        cart_card = view.findViewById(R.id.cart_card)
        cart_promo = view.findViewById(R.id.cart_promo)
        cart_promo_btn = view.findViewById(R.id.cart_promo_btn)
        cart_empty_img = view.findViewById(R.id.cart_empty_img)
        cart_empty_txt = view.findViewById(R.id.cart_empty_txt)
        cart_empty_btn = view.findViewById(R.id.cart_empty_btn)


        recyclerView = view.findViewById(R.id.checkOut_rv)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        setUpModelRv()

       // setUpCheckoutView()

        clear_cart.setOnClickListener {

            if (Constants.foodName.size >= 1){

                dialog("Do you want to clear cart?")
            }
            else{

                Toast.makeText(context, "Your cart is empty", Toast.LENGTH_SHORT).show()
            }
        }

        cart_discount_clickHere.setOnClickListener {

            cart_promo.visibility = View.VISIBLE
            cart_promo_btn.visibility = View.VISIBLE
        }

        checkOut.setOnClickListener {

            sendOrder()
        }

        cart_empty_btn.setOnClickListener {

            Functions.loadFragment(fragmentManager, Dashboard_frag(), R.id.frag_cont, true, "Dashboard", null)
        }

        return view
    }

    private fun sendOrder() {

       viewModel.sendOrderDetails(this, "1", loading).observe(viewLifecycleOwner, Observer {



       })
    }

    public fun setUpModelRv() {

        recycleView_models.clear()

        if (Constants.foodName.size == 0){

            recyclerView.visibility = View.GONE
            checkOut_TotalItems.visibility = View.GONE
            cart_discount_txt.visibility = View.GONE
            cart_discount_img.visibility = View.GONE
            cart_discount_clickHere.visibility = View.GONE
            cart_card.visibility = View.GONE
            cart_promo_btn.visibility = View.GONE
            cart_promo.visibility = View.GONE
            cart_empty_img.visibility = View.VISIBLE
            cart_empty_txt.visibility = View.VISIBLE
            cart_empty_btn.visibility = View.VISIBLE

        }
        else{

            cart_discount_txt.visibility = View.VISIBLE
            cart_discount_img.visibility = View.VISIBLE
            cart_discount_clickHere.visibility = View.VISIBLE
            cart_card.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
            checkOut_TotalItems.visibility = View.VISIBLE
            cart_empty_img.visibility = View.GONE
            cart_empty_txt.visibility = View.GONE
            cart_empty_btn.visibility = View.GONE
        }

        for (i in Constants.foodName.indices) {
            recycleView_models.add(RecycleView_Model(Constants.foodName[i], Constants.foodImg[i], Constants.foodPrice[i].toString(), Constants.foodQuantity[i]))

            Log.d("indiimage1", Constants.foodName[i] + Constants.foodImg[i] + Constants.foodPrice[i].toString() + Constants.foodQuantity[i])

        }

        Constants.cart_totalItems = Constants.foodName.size
        checkOut_TotalItems.setText(Constants.cart_totalItems.toString() + " Items")

            for (i in Constants.foodPrice.indices){

                    Constants.subTotal += Constants.foodPrice[i] * Constants.foodQuantity[i]

                    Log.d("subtotal", Constants.subTotal.toString())

            }

        Constants.tax = Constants.subTotal * 5/100

        Constants.total = Constants.subTotal + Constants.tax

        checkOut_subTotal.setText("$"+Constants.subTotal.toString()+".00")

        checkout_tax.setText("$"+Constants.tax.toString()+".00")

        checkout_Total.setText("$"+Constants.total.toString()+".00")

        Constants.subTotal = 0

        val recycleView_adapter = RecycleView_Adapter_CO(activity, recycleView_models, this)

        recyclerView.adapter = recycleView_adapter
        recycleView_adapter.notifyDataSetChanged()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        menu.getItem(0).setVisible(false)
        menu.getItem(1).setVisible(true)
        menu.getItem(2).setVisible(false)

        super.onCreateOptionsMenu(menu, inflater)


    }

    private fun dialog(msg: String) {

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setTitle(msg)
        builder.setPositiveButton(
            "Yes"
        ) { dialogInterface, i -> //auth = FirebaseAuth.getInstance();

            clearCart()
        }
        builder.setNegativeButton(
            "No"
        ) { dialogInterface, i -> dialogInterface.dismiss() }
        val alertDialog: Dialog = builder.create()
        alertDialog.show()
    }

    private fun clearCart() {

        recycleView_models.clear()
        Constants.foodName.clear()
        Constants.foodQuantity.clear()
        Constants.foodPrice.clear()
        Constants.foodImg.clear()

        Constants.subTotal = 0

        setUpModelRv()
    }
}