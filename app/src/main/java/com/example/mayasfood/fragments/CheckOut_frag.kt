package com.example.mayasfood.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.TranslateAnimation
import android.widget.*
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
import com.example.mayasfood.constants.CustomTimePicker
import com.example.mayasfood.fragments.ViewModels.CheckOut_frag_ViewModel
import com.example.mayasfood.functions.Functions
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_CO
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.lang.reflect.Field
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

            //loading.visibility = View.VISIBLE
            //sendOrder()
            showBarCode()
            //dashBoard.toolbar_const.visibility = View.GONE
            //view.slideUp(1000)
            // Prepare the View for the animation
            //view1.setAlpha(0.0f);

            /*val animate = TranslateAnimation(
                0F,  // fromXDelta
                0F,  // toXDelta
                view.height.toFloat(),  // fromYDelta
                0F
            ) // toYDelta

            animate.duration = 500
            animate.fillAfter = true
            view1.startAnimation(animate)*/

        }

        cart_empty_btn.setOnClickListener {

            Functions.loadFragment(fragmentManager, Dashboard_frag(), R.id.frag_cont, true, "Dashboard", null)
        }

        return view
    }

    private fun showBarCode() {

        val dialog = Dialog(requireContext())
        dialog.setCancelable(false)

        val activity = context as AppCompatActivity

        val view = activity.layoutInflater.inflate(R.layout.checkout_time_payment, null)

        dialog.setContentView(view)

        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation;
        /*if (dialog.window != null) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }*/
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val close_btn = view.findViewById<Button>(R.id.close_btn)
        pickUp = view.findViewById(R.id.pickup_time)


        close_btn.setOnClickListener {

            dialog.cancel()
        }

        pickUp.setOnClickListener {

            //CustomTimePickerDialog(requireContext(), this@CheckOut_frag, 12, 60, false)

            val customTimePicker : CustomTimePicker = CustomTimePicker(requireContext(), this@CheckOut_frag, 12, 60, false)

            customTimePicker.show()
            //customTimePicker.onAttachedToWindow()

            /*val now: Calendar = Calendar.getInstance()
            val tpd: TimePickerDialog = TimePickerDialog.newInstance(
                this@CheckOut_frag,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
            )
            tpd.show((activity as DashBoard).fragmentManager, "TimePicker")*/
        }

        /*barCodeImg = view.findViewById(R.id.order_barcode1)
        val closeQr = view.findViewById<Button>(R.id.close_qr)

        val writer = QRCodeWriter()
        try {
            val bitMatrix = writer.encode(
                "OrderId:" + "#345456535",
                BarcodeFormat.QR_CODE,
                512,
                512
            )
            val width = 512
            val height = 512
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    if (bitMatrix[x, y].toInt() == 0) bmp.setPixel(
                        x,
                        y,
                        resources.getColor(R.color.Register_Title)
                        //Color.BLACK
                    ) else bmp.setPixel(x, y, Color.WHITE)
                }
            }
            barCodeImg.setImageBitmap(bmp)
        } catch (e: WriterException) {
            //Log.e("QR ERROR", ""+e);
        }

        closeQr.setOnClickListener {

            dialog.cancel()
        }*/

        dialog.show()
    }

    private fun sendOrder() {

       viewModel.sendOrderDetails(this, "1", loading).observe(viewLifecycleOwner, Observer {

           if (it != null){

               if(it.getSuccess()!!){

                   Toast.makeText(activity, "Order Placed Successfully", Toast.LENGTH_SHORT).show()

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

    fun CustomTimePickerDialog(
        context: Context?, callBack: android.app.TimePickerDialog.OnTimeSetListener?,
        hourOfDay: Int, minute: Int, is24HourView: Boolean
    ) {
        //super(context, context, hourOfDay, minute, is24HourView)
        mIs24HourView = is24HourView
    }

    fun onAttachedToWindow() {

        super.onAttach(requireContext())
        //super.onAttachedToWindow()
        try {
            val classForid = Class.forName("com.android.internal.R\$id")
            val timePickerField: Field = classForid.getField("timePicker")
            this.timePicker = requireView().findViewById(
                timePickerField
                    .getInt(null)
            ) as TimePicker
            val field: Field = classForid.getField("hour")
            val mHourSpinner = timePicker
                .findViewById(field.getInt(null)) as NumberPicker
            if (mIs24HourView) {
                mHourSpinner.minValue = 2
                mHourSpinner.maxValue = 20
            } else {
                val amPm1: Field = classForid.getField("amPm")
                mHourSpinner.minValue = 2
                val amPm = timePicker
                    .findViewById(amPm1.getInt(null)) as NumberPicker
                amPm.setOnValueChangedListener { np1, oldVal, newVal ->
                    if (newVal == 0) { // case AM
                        mHourSpinner.minValue = 2
                        mHourSpinner.maxValue = 12
                    } else { // case PM
                        mHourSpinner.minValue = 1
                        mHourSpinner.maxValue = 8
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

