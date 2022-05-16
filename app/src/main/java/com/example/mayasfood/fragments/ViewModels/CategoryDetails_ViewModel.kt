package com.example.mayasfood.fragments.ViewModels

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mayasfood.Retrofite.request.Request_CategoryDetails
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.development.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryDetails_ViewModel : ViewModel() {

    lateinit var activity: Fragment

    var commonResponse = MutableLiveData<Response_Common>()

    fun getCategoryDetails(activity: Fragment, categoryId: String, branchId : String): MutableLiveData<Response_Common> {

        this.activity = activity
        val request_category: Request_CategoryDetails = Request_CategoryDetails()
        request_category.categoryId = categoryId
        request_category.branchId = branchId

        getCategoryDetailsAPI(request_category)

        return commonResponse
    }

    private fun getCategoryDetailsAPI(param: Request_CategoryDetails) {

        val retrofitInstance = RetrofitInstance()
        val retrofitData = retrofitInstance.retrofit.getFoodCategory(param)

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

                    Log.d("Dashboard", "failed")
                }
            }

            override fun onFailure(call: Call<Response_Common?>, t: Throwable) {
                Toast.makeText(activity.requireContext(), t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}