package com.example.mayasfood.fragments.ViewModels

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.compose.ui.unit.Constraints
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mayasfood.R
import com.example.mayasfood.Retrofite.request.OrderItems
import com.example.mayasfood.Retrofite.request.Request_Branch
import com.example.mayasfood.Retrofite.request.Request_OrderDetails
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.development.retrofit.RetrofitInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckOut_frag_ViewModel : ViewModel() {

    lateinit var activity: Fragment
    lateinit var loading: ProgressBar

    var commonResponse = MutableLiveData<Response_Common>()

    fun sendOrderDetails(
        activity: Fragment,
        branchId: String,
        loading: ProgressBar,
        pickUpDateTime: String,
        paymentMethod: String
    ): MutableLiveData<Response_Common> {

        this.activity = activity
        this.loading = loading

        val requestOrderdetails = Request_OrderDetails()

        Log.d("orderQty", Constants.cart_totalItems.toString())
        Log.d("orderAmt", Constants.total.toString())
        Log.d("orderPay", paymentMethod)
        Log.d("orderTime", pickUpDateTime)

        requestOrderdetails.branchId = "1"
        requestOrderdetails.toalQuantity = Constants.cart_totalItems.toString()
        requestOrderdetails.amount = Constants.total.toString()
        requestOrderdetails.paymentMethod = paymentMethod
        requestOrderdetails.pickupAt = pickUpDateTime

        //Log.d("orderItem", requestOrderdetails.toString())
        //Log.d("orderItem", requestOrderdetails.amount.toString())

        for (i in Constants.foodId.indices) {

            Log.d("orderId", Constants.foodId[i].toString())
            Log.d("orderPrice", Constants.foodPrice[i].toString())
            Log.d("orderSize", Constants.foodSize[i])
            Log.d("orderQty", Constants.foodQuantity[i].toString())

            val orderItems1 = OrderItems(
                Constants.foodId[i].toString(),
                Constants.foodPrice[i].toString(),
                Constants.foodSize[i],
                Constants.foodQuantity[i].toString(),
                "1"
            )


            requestOrderdetails.orderItems.add(orderItems1)

        }


        Log.d("orderItem12", requestOrderdetails.toString())
        // auth = FirebaseAuth.getInstance()

        sendOrderDetailsAPI(requestOrderdetails)

        return commonResponse
    }

    private fun sendOrderDetailsAPI(param: Request_OrderDetails) {

        Log.d("token", Constants.USER_TOKEN)

        val retrofitInstance = RetrofitInstance()
        val retrofitData = retrofitInstance.retrofit.sendOrderDetails(Constants.USER_TOKEN, param)

        retrofitData.enqueue(object : Callback<Response_Common?> {
            override fun onResponse(
                call: Call<Response_Common?>,
                response: Response<Response_Common?>
            ) {
                if (response.isSuccessful) {

                    commonResponse.value = response.body()

                    Log.d("Dashboard", "success")
                    // Toast.makeText(activity.requireContext(), commonResponse.value!!.message, Toast.LENGTH_SHORT).show();
                } else {
                    loading.visibility = View.GONE
                    Log.d("Dashboard", "Failed")
                    Toast.makeText(
                        activity.requireContext(),
                        response.message(),
                        Toast.LENGTH_SHORT
                    ).show();
                }
            }

            override fun onFailure(call: Call<Response_Common?>, t: Throwable) {
                Toast.makeText(activity.requireContext(), t.toString(), Toast.LENGTH_SHORT).show()
                loading.visibility = View.GONE
            }
        })
    }
}