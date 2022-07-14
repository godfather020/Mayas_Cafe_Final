package com.example.mayasfood.fragments.ViewModels

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mayasfood.R
import com.example.mayasfood.Retrofite.request.Request_CategoryDetails
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.development.retrofit.RetrofitInstance
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Search_ViewModel : ViewModel(){

    lateinit var activity: Fragment
    lateinit var loading: ProgressBar
    lateinit var auth : FirebaseAuth

    var commonResponse = MutableLiveData<Response_Common>()

    fun getProductList(activity: Fragment, branchId : String, loading : ProgressBar): MutableLiveData<Response_Common> {

        this.activity = activity
        this.loading = loading.findViewById(R.id.loading_all)
        val request_category: Request_CategoryDetails = Request_CategoryDetails()
        request_category.branchId = branchId
        auth = FirebaseAuth.getInstance()

        getProductListAPI(request_category)

        return commonResponse
    }

    private fun getProductListAPI(param: Request_CategoryDetails) {

        val retrofitInstance = RetrofitInstance()
        val retrofitData : Call<Response_Common>

        if (auth.currentUser != null || Constants.isLogin != false){

            retrofitData = retrofitInstance.retrofit.getFoodCategory(Constants.USER_TOKEN, param)
        }else {

            retrofitData = retrofitInstance.retrofit.getFoodCategory("x-token",param)
        }

        retrofitData.enqueue(object : Callback<Response_Common?> {
            override fun onResponse(
                call: Call<Response_Common?>,
                response: Response<Response_Common?>
            ) {
                if (response.isSuccessful) {

                    commonResponse.value = response.body()

                    Log.d("Dashboard", "success")
                    //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                }
                else{
                    loading.visibility = View.GONE
                    Log.d("Dashboard", "failed")
                }
            }

            override fun onFailure(call: Call<Response_Common?>, t: Throwable) {
                loading.visibility = View.GONE
                Toast.makeText(activity.requireContext(), t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}