package com.example.mayasfood.fragments

import android.R.attr
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
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
import com.example.mayasfood.shared_prefrence.TinyDB
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


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
    lateinit var payCash : RadioButton
    lateinit var payUPI : RadioButton
    var mIs24HourView  = true
    var sDate = ""
    var sPickupTime = ""
    lateinit var dialog : Dialog
    var orderID = ""
    var paymentMethod = "CASH"
    lateinit var tinyDB : TinyDB
    var pickUpDateTimeUPI = ""
    var timeStamp:String? = null
    var paymentStatus = 0
    var auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_check_out_frag, container, false)

        viewModel = ViewModelProvider(this).get(CheckOut_frag_ViewModel::class.java)

        dashBoard = activity as DashBoard

        setHasOptionsMenu(true)

        tinyDB = TinyDB(dashBoard)

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

            if (auth.currentUser != null || Constants.isLogin != false){

                showBarCode()
            }
            else {

                Toast.makeText(context, "Please Login to Continue", Toast.LENGTH_SHORT).show()
            }
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
        val time = view.findViewById<ImageButton>(R.id.timePick_img)
        val checkTotal = view.findViewById<TextView>(R.id.checkout_total)
        val checkSubTotal = view.findViewById<TextView>(R.id.checkout_subtotal)
        val checkTax = view.findViewById<TextView>(R.id.checkout_tax)
        val checkDiscount_txt = view.findViewById<TextView>(R.id.textView35)
        val checkDiscount = view.findViewById<TextView>(R.id.textView37)
        val pickup_radio = view.findViewById<RadioButton>(R.id.pickUp_order)
        val deliver_radio = view.findViewById<RadioButton>(R.id.getItDelivered)
        val timePick_txt = view.findViewById<TextView>(R.id.timePick_txt)
        payUPI = view.findViewById(R.id.payUPI)
        payCash = view.findViewById(R.id.payCash)

        checkDiscount.visibility = View.GONE
        checkDiscount_txt.visibility = View.GONE

        checkSubTotal.setText(checkOut_subTotal.text.toString())
        checkTax.setText(checkout_tax.text.toString())
        checkTotal.setText(checkout_Total.text.toString())

        close_btn.setOnClickListener {

            dialog.cancel()
        }

        if (pickup_radio.isChecked){

            timePick_txt.text = "PickUp Time"
        }
        else{

            timePick_txt.text = "Delivery Time"
        }

        pickup_radio.setOnClickListener {

            timePick_txt.text = "PickUp Time"
        }

        deliver_radio.setOnClickListener {

            timePick_txt.text = "Delivery Time"
        }

        checkOut.setOnClickListener {

            if (pickUp.text.isNotEmpty()){

                if(payUPI.isChecked){

                    timeStamp =
                        TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toString()+"UPI"

                    pickUpDateTimeUPI = sDate + " " + sPickupTime

                    paymentMethod = "UPI"

                    val UPI =
                        "upi://pay?pa=7000107876@hdfcbank&pn=Merchant%20Name&am=1&cu=INR&mode=02&orgid=000000&tn=Maya's%20Cafe%20&%20Restaurant&tr=$timeStamp"

                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.data = Uri.parse(UPI)
                    val chooser = Intent.createChooser(intent, "Pay with...")
                    startActivityForResult(chooser, 1, null)
                }
                else {

                    Log.d("pickUp", sPickupTime)
                    Log.d("pickUp", sDate)
                    Log.d("pickUp", sDate + " " + sPickupTime)

                    val pickUpDateTime = sDate + " " + sPickupTime

                    if (pickup_radio.isChecked) {

                        paymentMethod = "CASH"
                    } else {

                        paymentMethod = "ONLINE"
                    }

                    paymentStatus = 0

                    sendOrder(pickUpDateTime, paymentMethod)
                }
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

        time.setOnClickListener {

            val timePicker = android.app.TimePickerDialog(requireContext(),AlertDialog.THEME_HOLO_LIGHT, object : android.app.TimePickerDialog.OnTimeSetListener{
                @RequiresApi(Build.VERSION_CODES.P)
                override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {

                    sH = p1
                    sM = p2

                    val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val calendar1 : Calendar = Calendar.getInstance()
                    sDate = todayDate
                    val strings : List<String> = sDate.split(Regex("-"))
                    val sDay = strings[0].toInt()

                    calendar1.set(Calendar.DAY_OF_MONTH, sDay)
                    calendar1.set(Calendar.HOUR_OF_DAY, sH)
                    calendar1.set(Calendar.MINUTE, sM)

                    Log.d("currentHou", calendar1.get(Calendar.HOUR_OF_DAY).toString())
                    Log.d("currentHou", calendar1.get(Calendar.MINUTE).toString())

                    if (calendar1.timeInMillis == Calendar.getInstance().timeInMillis || calendar1.timeInMillis < Calendar.getInstance().timeInMillis){

                        Toast.makeText(activity, "Please select a future time", Toast.LENGTH_SHORT).show()

                        //pickUp.setText("Please Select Time")
                    }
                    else if (calendar1.timeInMillis > Calendar.getInstance().timeInMillis+1800000 && calendar1.get(Calendar.HOUR_OF_DAY) < 22 && calendar1.get(Calendar.HOUR_OF_DAY) > 8){

                        sPickupTime = android.text.format.DateFormat.format("HH:mm:ss", calendar1).toString()

                        pickUp.setText(android.text.format.DateFormat.format("hh:mm aa", calendar1))

                    }
                    else if(calendar1.get(Calendar.HOUR_OF_DAY) >= 22 || calendar1.get(Calendar.HOUR_OF_DAY) <= 8){

                        Toast.makeText(activity, "Please select a time between 8:00 Am to 10:00 Pm", Toast.LENGTH_SHORT).show()
                        //pickUp.setText("Please Select Time")
                    }
                }

            }, cH, cM, false)

            timePicker.show()

        }

        dialog.show()
    }

    private fun sendOrder(pickUpDateTime: String, paymentMethod: String) {

       viewModel.sendOrderDetails(this, "1", loading, pickUpDateTime, paymentMethod, timeStamp.toString(), paymentStatus).observe(viewLifecycleOwner, Observer {

           if (it != null){

               if(it.getSuccess()!!){

                   orderID = it.getData()!!.id.toString()
                   //Toast.makeText(activity, "Order Placed Successfully", Toast.LENGTH_SHORT).show()

                   dialog.cancel()
                   showOrderSuccessDialog()
                   loading.visibility = View.GONE
               }
           }
       })
    }

    private fun showOrderSuccessDialog() {

        val orderDialog = Dialog(requireContext())

        orderDialog.setCancelable(false)

        val activity = context as AppCompatActivity

        val view = activity.layoutInflater.inflate(R.layout.order_confirm_dialog, null)

        orderDialog.setContentView(view)

        orderDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation;
        if (orderDialog.window != null) {
            orderDialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        orderDialog.window!!.setGravity(Gravity.CENTER)
        orderDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val goToOrders = view.findViewById<Button>(R.id.go_to_orders)
        val orderId = view.findViewById<TextView>(R.id.orderId)
        val orderAmt = view.findViewById<TextView>(R.id.orderAmt)
        val payMethod = view.findViewById<TextView>(R.id.payMethod)

        orderAmt.text = checkout_Total.text.toString()
        orderId.text = "#"+orderID
        payMethod.text = paymentMethod

        Constants.cart_totalItems = 0
        clearCart()
        setUpModelRv()

        goToOrders.setOnClickListener {


            Functions.loadFragment(fragmentManager, Orders_frag(), R.id.frag_cont, true, "Running Orders", null)
            dashBoard.bottomNavigationView.selectedItemId = R.id.bottom_nav_orders
            orderDialog.cancel()

        }

        orderDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            Log.e("UPI RESULT REQUEST CODE-->", "" + requestCode)
            Log.e("UPI RESULT RESULT CODE-->", "" + resultCode)
            Log.e("UPI RESULT DATA-->", "" + attr.data)
            if (Activity.RESULT_OK == resultCode || resultCode == 11) {
                if (data != null) {
                    val trxt = data.getStringExtra("response")
                    Log.d("UPI", "onActivityResult: $trxt")
                    val dataList = ArrayList<String>()
                    dataList.add(trxt!!)
                    upiPaymentDataOperation(dataList)
                }
            } else {
                // 400 Failed
                Toast.makeText(context, "Payment Failed", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("Error in UPI onActivityResult->", "" + e.message)
        }
    }

    private fun upiPaymentDataOperation(dataList: ArrayList<String>) {
        var str = dataList[0]
        Log.d("UPIPAY", "upiPaymentDataOperation: $str")
        var paymentCancel = ""
        if (str == null) {
            str = "discard"
        }
        var status = ""
        var approvalRefNo = ""
        val response = str.split("&".toRegex()).toTypedArray()
        Arrays.sort(response)
        for (i in response.indices) {
            val equlaStr = response[i].split("=".toRegex()).toTypedArray()
            if (equlaStr.size >= 2) {
                if (equlaStr[0].lowercase(Locale.getDefault()) == "Status".lowercase(Locale.getDefault())) {
                    status = equlaStr[1].lowercase(Locale.getDefault())
                } else if (equlaStr[0].lowercase(Locale.getDefault()) == "ApprovalRefNo".lowercase(
                        Locale.getDefault()
                    ) || equlaStr[0].lowercase(Locale.getDefault()) == "txnRef".lowercase(
                        Locale.getDefault()
                    )
                ) {
                    approvalRefNo = equlaStr[1]
                }
            } else {
                paymentCancel = "Payment cancelled by user."
            }
        }
        if (status == "success") {
            Log.d("UPI", "responseStr: $approvalRefNo")
            Toast.makeText(context, "Payment Successfull", Toast.LENGTH_LONG).show()

            paymentStatus = 1
            sendOrder(pickUpDateTimeUPI, paymentMethod)

        } else if ("Payment cancelled by user." == paymentCancel) {
            //showResponseDialog("Payment cancelled by user.")
            Toast.makeText(context, "Payment cancelled by user.", Toast.LENGTH_SHORT).show()
        } else {
            //showResponseDialog("Transaction failed.Please try again")
            Toast.makeText(context, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show()
        }
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

        tinyDB.putListInt("foodId", Constants.foodId)
        tinyDB.putListString("foodSize", Constants.foodSize)
        tinyDB.putListString("foodName", Constants.foodName)
        tinyDB.putListString("foodImg", Constants.foodImg)
        tinyDB.putListInt("foodPrice", Constants.foodPrice)
        tinyDB.putListInt("foodQuantity", Constants.foodQuantity)

        for (i in Constants.foodName.indices) {
            recycleView_models.add(RecycleView_Model(Constants.foodSize[i], Constants.foodName[i], Constants.foodImg[i], Constants.foodPrice[i].toString(), Constants.foodQuantity[i]))

            Log.d("indiimage1", Constants.foodName[i] + Constants.foodImg[i] + Constants.foodPrice[i].toString() + Constants.foodQuantity[i])

        }

        Constants.cart_totalItems = Constants.foodName.size

        tinyDB.putInt("cartCount", Constants.cart_totalItems)

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
        menu.getItem(1).setVisible(false)
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

        tinyDB.clear()

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

