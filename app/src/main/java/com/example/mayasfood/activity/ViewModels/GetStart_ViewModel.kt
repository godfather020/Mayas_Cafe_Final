package com.example.mayasfood.activity.ViewModels

import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lottry.data.remote.retrofit.request.Request_DeviceInfo
import com.example.mayasfood.FirebaseCloudMsg
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.development.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetStart_ViewModel : ViewModel() {

    lateinit var activity: AppCompatActivity

    val commonResponse = MutableLiveData<Response_Common>()

    fun sendDeviceInfo(activity:AppCompatActivity) {

        this.activity=activity

        val deviceName = Build.BRAND + " " + Build.MODEL
        val token = FirebaseCloudMsg.getToken(activity)
        val deviceInfo = Request_DeviceInfo()
        deviceInfo.deviceId = Settings.Secure.ANDROID_ID
        deviceInfo.deviceName = deviceName
        deviceInfo.deviceToken = token.toString()
        deviceInfo.deviceType = "Android"
        deviceInfo.osVersion = Build.VERSION.RELEASE

        val retrofitInstance = RetrofitInstance()
        val retrofitData = retrofitInstance.retrofit.sendDeviceDetail(deviceInfo)

        retrofitData.enqueue(object : Callback<Response_Common?> {
            override fun onResponse(
                call: Call<Response_Common?>,
                response: Response<Response_Common?>
            ) {
                if (response.isSuccessful) {



                    Log.d("deviceInfoSent", "success")
                    //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                }
            }

            override fun onFailure(call: Call<Response_Common?>, t: Throwable) {
                Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}