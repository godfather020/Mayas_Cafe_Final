package com.example.mayasfood.fragments.ViewModels

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.unit.Constraints
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mayasfood.R
import com.example.mayasfood.Retrofite.request.Request_Branch
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.constants.Constants
import com.example.mayasfood.development.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Favorite_frag_ViewModel : ViewModel() {

    lateinit var activity: Fragment
    lateinit var loading: ProgressBar

    val commonResponse = MutableLiveData<Response_Common>()

    fun getFavoriteList(activity:Fragment, branchId: String, loading : ProgressBar): MutableLiveData<Response_Common> {

        this.activity = activity
        this.loading = loading.findViewById(R.id.loading_fav)
        val requestBranch: Request_Branch = Request_Branch()
        requestBranch.branchId = branchId

        getFavListAPI(requestBranch)

        return commonResponse
    }

    private fun getFavListAPI(param: Request_Branch) {

        val retrofitInstance = RetrofitInstance()
        val retrofitData = retrofitInstance.retrofit.getFavList(Constants.USER_TOKEN, param)

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