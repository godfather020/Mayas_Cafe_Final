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
import com.example.mayasfood.Retrofite.request.Request_Branch
import com.example.mayasfood.Retrofite.request.Request_OrderDetails
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.development.retrofit.RetrofitInstance
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckOut_frag_ViewModel : ViewModel() {

    lateinit var activity: Fragment
    lateinit var auth : FirebaseAuth
    lateinit var loading: ProgressBar

    var commonResponse = MutableLiveData<Response_Common>()

    fun sendOrderDetails(activity: Fragment, branchId: String, loading: ProgressBar): MutableLiveData<Response_Common> {

        this.activity = activity
        this.loading = loading.findViewById(R.id.progress_bar)
        val requestOrderdetails: Request_OrderDetails = Request_OrderDetails()

        requestOrderdetails.branchId = branchId
        requestOrderdetails.toalQuantity = Constants.cart_totalItems.toString()
        requestOrderdetails.amount = Constants.total.toString()
        requestOrderdetails.paymentMethod = "Cash"
        requestOrderdetails.pickupAt = "24-05-2022 08:45 PM"

        for (i in Constants.foodName.indices) {
            requestOrderdetails.orderItems[i].totalAmount = Constants.foodPrice.get(i).toString()
            requestOrderdetails.orderItems[i].noItems = Constants.foodQuantity.get(i).toString()
            requestOrderdetails.orderItems[i].totalAmount = Constants.foodPrice.get(i).toString()
            requestOrderdetails.orderItems[i].productId = Constants.foodId.get(i).toString()
            requestOrderdetails.orderItems[i].productpriceId = "1"
            requestOrderdetails.orderItems[i].quantity = "M"
        }

        auth = FirebaseAuth.getInstance()

        sendOrderDetailsAPI(requestOrderdetails)

        return commonResponse
    }

    private fun sendOrderDetailsAPI(param: Request_OrderDetails) {


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
                    Toast.makeText(activity.requireContext(), commonResponse.value!!.message, Toast.LENGTH_SHORT).show();
                }
                else{
                    loading.visibility = View.GONE
                    Log.d("Dashboard", "Failed")
                    Toast.makeText(activity.requireContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            override fun onFailure(call: Call<Response_Common?>, t: Throwable) {
                Toast.makeText(activity.requireContext(), t.toString(), Toast.LENGTH_SHORT).show()
                loading.visibility = View.GONE
            }
        })
    }
}