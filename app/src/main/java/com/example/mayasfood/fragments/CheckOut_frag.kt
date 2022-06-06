package com.example.mayasfood.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.borax12.materialdaterangepicker.time.RadialPickerLayout
import com.borax12.materialdaterangepicker.time.TimePickerDialog
import com.example.mayasfood.R
import com.example.mayasfood.activity.DashBoard
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.fragments.ViewModels.CheckOut_frag_ViewModel
import com.example.mayasfood.functions.Functions
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_CO
import java.lang.reflect.Field
import java.text.SimpleDateFormat
import java.util.*


class CheckOut_frag : Fragment(), TimePickerDialog.OnTimeSetListener,
    android.app.TimePickerDialog.OnTimeSetListener {

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
    lateinit var dashBoard: DashBoard
    lateinit var barCodeImg : ImageView
    lateinit var pickUp : TextView
    lateinit var timePicker : TimePicker
    var mIs24HourView  = true
    var sDate = ""
    var sPickupTime = ""
    lateinit var dialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_check_out_frag, container, false)

        viewModel = ViewModelProvider(this).get(CheckOut_frag_ViewModel::class.java)

        dashBoard = activity as DashBoard

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
        loading = view.findViewById(R.id.loading_checkOut)


        dashBoard.toolbar_const.setTitle("My Cart")
        dashBoard.toolbar_const.setTitleTextColor(resources.getColor(R.color.black))

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

            showBarCode()

        }

        cart_empty_btn.setOnClickListener {

            Functions.loadFragment(fragmentManager, Dashboard_frag(), R.id.frag_cont, true, "Dashboard", null)
        }

        return view
    }

    private fun showBarCode() {

        dialog = Dialog(requireContext())

        dialog.setCancelable(false)

        val activity = context as AppCompatActivity

        val view = activity.layoutInflater.inflate(R.layout.checkout_time_payment, null)

        dialog.setContentView(view)

        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation;
        if (dialog.window != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val close_btn = view.findViewById<Button>(R.id.close_btn)
        val checkOut = view.findViewById<Button>(R.id.final_checkOut)
        pickUp = view.findViewById(R.id.pickup_time)
        val checkTotal = view.findViewById<TextView>(R.id.checkout_total)
        val checkSubTotal = view.findViewById<TextView>(R.id.checkout_subtotal)
        val checkTax = view.findViewById<TextView>(R.id.checkout_tax)
        val checkDiscount_txt = view.findViewById<TextView>(R.id.textView35)
        val checkDiscount = view.findViewById<TextView>(R.id.textView37)
        val pickup_radio = view.findViewById<RadioButton>(R.id.pickUp_order)
        val deliver_radio = view.findViewById<RadioButton>(R.id.getItDelivered)

        checkDiscount.visibility = View.GONE
        checkDiscount_txt.visibility = View.GONE

        checkSubTotal.setText(checkOut_subTotal.text.toString())
        checkTax.setText(checkout_tax.text.toString())
        checkTotal.setText(checkout_Total.text.toString())

        close_btn.setOnClickListener {

            dialog.cancel()
        }

        checkOut.setOnClickListener {

            if (!pickUp.text.equals("Pick Time")){

                var paymentMethod = "CASH"
                Log.d("pickUp", sPickupTime)
                Log.d("pickUp", sDate)
                Log.d("pickUp", sDate+" "+sPickupTime)

                val pickUpDateTime = sDate+" "+sPickupTime

                if (pickup_radio.isChecked){

                    paymentMethod = "CASH"
                }
                else{

                    paymentMethod = "ONLINE"
                }

                sendOrder(pickUpDateTime, paymentMethod)
            }
            else{

                pickUp.requestFocus()
                pickUp.error = "Please specify a time"
            }
        }

        var sH= 0
        var sM = 0
        val calendar : Calendar = Calendar.getInstance()
        //val todayDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(Date())
        val cH = calendar.get(Calendar.HOUR_OF_DAY)
        val cM = calendar.get(Calendar.MINUTE)

        pickUp.setOnClickListener {

            val timePicker = android.app.TimePickerDialog(requireContext(),AlertDialog.THEME_HOLO_LIGHT, object : android.app.TimePickerDialog.OnTimeSetListener{
                @RequiresApi(Build.VERSION_CODES.P)
                override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {

                    sH = p1
                    sM = p2

                    val todayDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                    val calendar1 : Calendar = Calendar.getInstance()
                    sDate = todayDate
                    val strings : List<String> = sDate.split(Regex("-"))
                    val sDay = strings[0].toInt()

                    calendar1.set(Calendar.DAY_OF_MONTH, sDay)
                    calendar1.set(Calendar.HOUR_OF_DAY, sH)
                    calendar1.set(Calendar.MINUTE, sM)

                    if (calendar1.timeInMillis == Calendar.getInstance().timeInMillis){

                        Toast.makeText(activity, "Please select a future time", Toast.LENGTH_SHORT).show()

                        pickUp.setText("Pick Time")
                    }
                    else if (calendar1.timeInMillis > Calendar.getInstance().timeInMillis+1800000){

                        sPickupTime = android.text.format.DateFormat.format("HH:mm:ss", calendar1).toString()

                        pickUp.setText(android.text.format.DateFormat.format("hh:mm aa", calendar1))

                    }
                    else{

                        Toast.makeText(activity, "Please select a future time", Toast.LENGTH_SHORT).show()
                        pickUp.setText("Pick Time")
                    }
                }

            }, cH, cM, false)

            timePicker.show()

        }

        dialog.show()
    }

    private fun sendOrder(pickUpDateTime: String, paymentMethod: String) {

       viewModel.sendOrderDetails(this, "1", loading, pickUpDateTime, paymentMethod).observe(viewLifecycleOwner, Observer {

           if (it != null){

               if(it.getSuccess()!!){

                   Toast.makeText(activity, "Order Placed Successfully", Toast.LENGTH_SHORT).show()
                   Constants.cart_totalItems = 0
                   clearCart()
                   dialog.cancel()
                   Functions.loadFragment(fragmentManager, Orders_frag(), R.id.frag_cont, true, "Running Orders", null)
                   dashBoard.bottomNavigationView.selectedItemId = R.id.bottom_nav_orders
                   loading.visibility = View.GONE
               }
           }
       })
    }

    public fun setUpModelRv() {

        recycleView_models.clear()

        if (Constants.foodName.size == 0){

            recyclerView.visibility = View.GONE
            //checkOut_TotalItems.visibility = View.GONE
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
            //checkOut_TotalItems.visibility = View.VISIBLE
            cart_empty_img.visibility = View.GONE
            cart_empty_txt.visibility = View.GONE
            cart_empty_btn.visibility = View.GONE
        }

        for (i in Constants.foodName.indices) {
            recycleView_models.add(RecycleView_Model(Constants.foodSize[i], Constants.foodName[i], Constants.foodImg[i], Constants.foodPrice[i].toString(), Constants.foodQuantity[i]))

            Log.d("indiimage1", Constants.foodName[i] + Constants.foodImg[i] + Constants.foodPrice[i].toString() + Constants.foodQuantity[i])

        }

        Constants.cart_totalItems = Constants.foodName.size
        checkOut_TotalItems.setText( "Total Items " + Constants.cart_totalItems.toString())

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
        menu.getItem(3).setVisible(false)

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
        Constants.foodSize.clear()
        Constants.foodId.clear()

        Constants.subTotal = 0

        setUpModelRv()
    }

    override fun onTimeSet(
        view: RadialPickerLayout?,
        hourOfDay: Int,
        minute: Int,
        hourOfDayEnd: Int,
        minuteEnd: Int
    ) {
        val hourString = if (hourOfDay < 10) "0$hourOfDay" else "" + hourOfDay
        val minuteString = if (minute < 10) "0$minute" else "" + minute
        val hourStringEnd = if (hourOfDayEnd < 10) "0$hourOfDayEnd" else "" + hourOfDayEnd
        val minuteStringEnd = if (minuteEnd < 10) "0$minuteEnd" else "" + minuteEnd
        val time =
            "You picked the following time: From - " + hourString + "h" + minuteString + " To - " + hourStringEnd + "h" + minuteStringEnd

        pickUp.setText(time)

    }

    fun View.slideUp(duration: Int = 500) {
        visibility = View.VISIBLE
        val animate = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
        animate.duration = duration.toLong()
        animate.fillAfter = true
        this.startAnimation(animate)
    }

    fun View.slideDown(duration: Int = 500) {
        visibility = View.VISIBLE
        val animate = TranslateAnimation(0f, 0f, 0f, this.height.toFloat())
        animate.duration = duration.toLong()
        animate.fillAfter = true
        this.startAnimation(animate)
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {

        val hourString = p1.toString()
        val minuteString = p2.toString()

        pickUp.setText(hourString + ":" + minuteString)
    }
}

