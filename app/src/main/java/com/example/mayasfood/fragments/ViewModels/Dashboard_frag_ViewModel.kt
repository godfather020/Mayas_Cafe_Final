package com.example.mayasfood.fragments.ViewModels

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mayasfood.R
import com.example.mayasfood.Retrofite.request.Request_Branch
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.development.retrofit.RetrofitInstance
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class Dashboard_frag_ViewModel : ViewModel() {

    lateinit var activity: Fragment
    lateinit var auth : FirebaseAuth
    lateinit var loading: ProgressBar

    var commonResponse = MutableLiveData<Response_Common>()
    var commonResponse1 = MutableLiveData<Response_Common>()

    fun getDashboardData(activity: Fragment, branchId: String, loading: ProgressBar): MutableLiveData<Response_Common> {

        this.activity = activity
        this.loading = loading.findViewById(R.id.progress_bar)
        val requestBranch: Request_Branch = Request_Branch()
        requestBranch.branchId = branchId
        auth = FirebaseAuth.getInstance()

        getDashboardDataApi(requestBranch)

        return commonResponse
    }

    private fun getDashboardDataApi(param: Request_Branch) {

        val retrofitInstance = RetrofitInstance()
        val retrofitData : Call<Response_Common>

        if (auth.currentUser != null){

            retrofitData = retrofitInstance.retrofit.getDashboardItems(Constants.USER_TOKEN, param)
        }else {

            retrofitData = retrofitInstance.retrofit.getDashboardItems("x-token",param)
        }
       // retrofitData = retrofitInstance.retrofit.getDashboardItems(Constants.USER_TOKEN,param)

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

    fun getNotificationData(activity: Fragment): MutableLiveData<Response_Common> {

        this.activity = activity

        //this.loading = loading.findViewById(R.id.loading_notify)

        getNotificationDataApi()

        return commonResponse1
    }

    private fun getNotificationDataApi() {

        val retrofitInstance = RetrofitInstance()
        val retrofitData = retrofitInstance.retrofit.getNotificaiton(Constants.USER_TOKEN)

        //val retrofitData =
        //    retrofitInstance.retrofit.getNotificaiton("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InVzZXJJZCI6NTQsInBob25lTnVtYmVyIjoiKzkxNzAwMDEwNzg3NiIsImNyZWF0ZWRBdCI6IjIwMjItMDUtMjlUMDc6MTU6MjAuNjYyWiJ9LCJpYXQiOjE2NTM4MDg1MjB9.ut7rbdY-NlDANUJomlbRO5JFwCouktCcyoy7Rk9d6Hg")

        retrofitData.enqueue(object : Callback<Response_Common?> {
            override fun onResponse(
                call: Call<Response_Common?>,
                response: Response<Response_Common?>
            ) {
                if (response.isSuccessful) {

                    commonResponse1.value = response.body()

                    Log.d("Dashboard", "success")
                    //Toast.makeText(activity.context, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    //loading.visibility = View.GONE
                    Log.d("Dashboard", "failed")
                }
            }

            override fun onFailure(call: Call<Response_Common?>, t: Throwable) {
                Toast.makeText(activity.context, t.toString(), Toast.LENGTH_SHORT).show()
                //loading.visibility = View.GONE
            }
        })
    }
}