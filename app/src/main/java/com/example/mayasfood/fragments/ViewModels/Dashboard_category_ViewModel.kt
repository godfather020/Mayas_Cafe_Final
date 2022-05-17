package com.example.mayasfood.fragments.ViewModels

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lottry.data.remote.retrofit.request.Request_Branch
import com.example.mayasfood.R
import com.example.mayasfood.Retrofite.request.Request_CategoryDetails
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.development.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Dashboard_category_ViewModel: ViewModel() {

    lateinit var activity: Fragment
    lateinit var loading : ProgressBar

    var commonResponse = MutableLiveData<Response_Common>()

    fun getDashboardData(activity: Fragment, branchId: String, loading : ProgressBar): MutableLiveData<Response_Common> {

        this.activity = activity
        val requestBranch: Request_Branch = Request_Branch()
        requestBranch.branchId = branchId

        this.loading = loading.findViewById(R.id.loading_catD)

        getDashboardDataApi(requestBranch)

        return commonResponse
    }

    private fun getDashboardDataApi(param: Request_Branch) {

        val retrofitInstance = RetrofitInstance()
        val retrofitData = retrofitInstance.retrofit.getDashboardItems(param)

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
                Toast.makeText(activity.requireContext(), t.toString(), Toast.LENGTH_SHORT).show()
                loading.visibility = View.GONE
            }
        })

    }
}