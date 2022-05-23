package com.example.mayasfood.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.compose.ui.unit.Constraints
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mayasfood.R
import com.example.mayasfood.Retrofite.request.Request_OrderDetails
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.recycleView.recycleViewModel.RecycleView_Model
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_CO
import com.example.mayasfood.recycleView.rv_adapter.RecycleView_Adapter_MC

class CheckOut_frag : Fragment() {

    lateinit var checkOut_TotalItems : TextView
    lateinit var clear_cart : TextView
    lateinit var recyclerView : RecyclerView
    lateinit var loading : ProgressBar
    var recycleView_models = ArrayList<RecycleView_Model>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_check_out_frag, container, false)

        setHasOptionsMenu(true)

        checkOut_TotalItems = view.findViewById(R.id.checkout_totallItems)
        clear_cart = view.findViewById(R.id.checkout_clearAll)


        recyclerView = view.findViewById(R.id.checkOut_rv)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        setUpModelRv()

       // setUpCheckoutView()

        clear_cart.setOnClickListener {

            recycleView_models.clear()
            Constants.foodName.clear()
            Constants.foodQuantity.clear()
            Constants.foodPrice.clear()
            Constants.foodImg.clear()

            setUpModelRv()
        }

        return view
    }

    private fun setUpModelRv() {

        for (i in Constants.foodName.indices) {
            recycleView_models.add(RecycleView_Model(Constants.foodName[i], Constants.foodImg[i], Constants.foodPrice[i], Constants.foodQuantity[i]))

            Log.d("indiimage1", Constants.foodName[i] + Constants.foodImg[i] + Constants.foodPrice[i] + Constants.foodQuantity[i])

        }

        Constants.cart_totalItems = Constants.foodName.size
        checkOut_TotalItems.setText(Constants.cart_totalItems.toString() + " Items")

        val recycleView_adapter = RecycleView_Adapter_CO(activity, recycleView_models)

        recyclerView.adapter = recycleView_adapter
        recycleView_adapter.notifyDataSetChanged()

    }

    private fun setUpCheckoutView() {

        val request_OrderDetails : Request_OrderDetails = Request_OrderDetails()

        request_OrderDetails.branchId = "1"
        request_OrderDetails.amount = ""
        request_OrderDetails.pickupAt = "11:30 AM"
        request_OrderDetails.paymentMethod = "Cash"
        request_OrderDetails.toalQuantity = Constants.cart_totalItems.toString()
        request_OrderDetails.orderItems[0]



    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        menu.getItem(0).setVisible(false)
        menu.getItem(1).setVisible(true)
        menu.getItem(2).setVisible(false)

        super.onCreateOptionsMenu(menu, inflater)


    }
}