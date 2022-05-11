package com.example.mayasfood.activity.ViewModels

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lottry.data.remote.retrofit.request.Request_Branch
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.development.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Dashboard_ViewModel : ViewModel() {

    lateinit var activity: AppCompatActivity

    val commonResponse = MutableLiveData<Response_Common>()

    fun getDashboardData(activity:AppCompatActivity, branchId: String){

        this.activity = activity
        val requestBranch: Request_Branch = Request_Branch()
        requestBranch.branchId = branchId

        getDashboardDataApi(requestBranch)
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
            }

            override fun onFailure(call: Call<Response_Common?>, t: Throwable) {
                Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show()
            }
        })

    }
}