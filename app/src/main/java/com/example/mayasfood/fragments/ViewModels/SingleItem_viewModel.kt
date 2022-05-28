package com.example.mayasfood.fragments.ViewModels

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lottry.data.remote.retrofit.request.Request_ProductDetails
import com.example.mayasfood.FirebaseCloudMsg
import com.example.mayasfood.R
import com.example.mayasfood.Retrofite.request.Request_Branch
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.development.retrofit.RetrofitInstance
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SingleItem_viewModel : ViewModel() {

    lateinit var activity: Fragment

    val commonResponse = MutableLiveData<Response_Common>()

    lateinit var loading : ProgressBar
    lateinit var auth : FirebaseAuth

    fun getItemDetails(activity : Fragment, productId : String, loading: ProgressBar): MutableLiveData<Response_Common> {

        this.activity = activity

        this.loading = loading.findViewById(R.id.loading_singleItem)

        auth = FirebaseAuth.getInstance()

        val requestProductdetails : Request_ProductDetails = Request_ProductDetails()

        requestProductdetails.productId = productId

        getAllOrdersAPI(requestProductdetails)

        return commonResponse
    }

    private fun getAllOrdersAPI(param: Request_ProductDetails) {

        val retrofitInstance = RetrofitInstance()
        val retrofitData : Call<Response_Common>

        if (auth.currentUser != null){

            retrofitData = retrofitInstance.retrofit.getProductDetail(Constants.USER_TOKEN, param)
        }else {

            retrofitData = retrofitInstance.retrofit.getProductDetail("x-token",param)
        }

        retrofitData.enqueue(object : Callback<Response_Common?> {
            override fun onResponse(
                call: Call<Response_Common?>,
                response: Response<Response_Common?>
            ) {
                if (response.isSuccessful) {

                    commonResponse.value = response.body()

                    Log.d("Orders", "success")
                    //Toast.makeText(activity.requireContext(), "Success", Toast.LENGTH_SHORT).show();
                }
                else{
                    loading.visibility = View.GONE
                    Toast.makeText(activity.requireContext(), response.message(), Toast.LENGTH_SHORT).show()
                    Log.d("Orders", "failed")
                }
            }

            override fun onFailure(call: Call<Response_Common?>, t: Throwable) {
                loading.visibility = View.GONE
                Toast.makeText(activity.requireContext(), t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}