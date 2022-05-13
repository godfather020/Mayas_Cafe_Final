package com.example.mayasfood.fragments.ViewModels

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mayasfood.Retrofite.request.Request_myCoupons
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.development.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Offers_frag_viewModel : ViewModel() {

    lateinit var activity: Fragment

    val commonResponse = MutableLiveData<Response_Common>()

    fun getAllCoupons(activity : Fragment, branchId : String): MutableLiveData<Response_Common> {

        this.activity = activity

        val requestMycoupons : Request_myCoupons = Request_myCoupons()

        requestMycoupons.branchId = branchId
        requestMycoupons.name = ""
        requestMycoupons.code = ""
        requestMycoupons.title = ""

        getCouponsAPI(requestMycoupons)

        return commonResponse
    }

    private fun getCouponsAPI(param: Request_myCoupons) {

        val retrofitInstance = RetrofitInstance()
        val retrofitData = retrofitInstance.retrofit.myCoupons(param)

        retrofitData.enqueue(object : Callback<Response_Common?> {
            override fun onResponse(
                call: Call<Response_Common?>,
                response: Response<Response_Common?>
            ) {
                if (response.isSuccessful) {

                    commonResponse.value = response.body()

                    Log.d("Offers", "success")
                    //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                }
                else{

                    Log.d("Offers", "failed")
                }
            }

            override fun onFailure(call: Call<Response_Common?>, t: Throwable) {
                Toast.makeText(activity.requireContext(), t.toString(), Toast.LENGTH_SHORT).show()
            }
        })

    }
}