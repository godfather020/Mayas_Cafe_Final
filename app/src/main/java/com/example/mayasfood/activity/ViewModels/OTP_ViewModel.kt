package com.example.mayasfood.activity.ViewModels

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lottry.data.remote.retrofit.request.Request_Login
import com.example.mayasfood.FirebaseCloudMsg
import com.example.mayasfood.Modules.NetworkModule
import com.example.mayasfood.Retrofite.request.Request_Verify
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.development.retrofit.RetrofitInstance
import com.google.gson.Gson
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OTP_ViewModel: ViewModel() {

    lateinit var activity: AppCompatActivity

    val commonResponse = MutableLiveData<Response_Common>()
    val commonResponse1 = MutableLiveData<Response_Common>()

    fun verify_otp(activity:AppCompatActivity,phoneNumber:String,deviceId: String, deviceName: String, deviceOS: String):MutableLiveData<Response_Common>{

        this.activity=activity

        val token = FirebaseCloudMsg.getToken(activity)
        Log.d("OTPToken1", token)

        val requestVerify= Request_Verify()

        requestVerify.phoneNumber=phoneNumber
        requestVerify.deviceType = "Android"
        requestVerify.deviceId = deviceId
        requestVerify.deviceName = deviceName
        requestVerify.osVersion = deviceOS
        requestVerify.deviceToken = token
        //requestVerify.deviceToken = token
        //Log.d("deviceToken", Constant.ApiConstant.DEVICE_TOKEN )
        //Log.d("deviceTokensh", Constant.sharedPrefrencesConstant.DEVICE_TOKEN)

        getResposneOfVerifyOtpFromApi(requestVerify)

        return commonResponse
    }

    fun getResposneOfVerifyOtpFromApi(param: Request_Verify){


        val retrofitInstance: RetrofitInstance = RetrofitInstance()
        val retrofitData = retrofitInstance.retrofit.getVerifyOtp(param)

        retrofitData.enqueue(object : Callback<Response_Common> {

            override fun onResponse(
                call: Call<Response_Common>,
                response: Response<Response_Common>
            ) {
//               TODO("Not yet implemented")
                if(response.isSuccessful) {

//                   sharedPreferencesUtil.saveString(Constant.sharedPrefrencesConstant.X_TOKEN,response.body()!!.getData()!!.token)

                    commonResponse.value=response.body()!!

                    Toast.makeText(activity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    /*var userDetail=UserDetail()

                    userDetail.profilePic = response.body()!!.getData()!!.result!!.profilePic

                    sharedPreferences.saveUserData(userDetail)*/

                }
                else{

                    Toast.makeText(activity, "Enter Valid OTP", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Response_Common>, t: Throwable) {
//               TODO("Not yet implemented")

                Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun get_otp(activity:AppCompatActivity,phoneNumber:String):MutableLiveData<Response_Common>{

        this.activity=activity

        val requestLogin= Request_Login()

        requestLogin.phoneNumber=phoneNumber

        getResposneOfLoginFromApi(requestLogin)

        return commonResponse1
    }

    fun getResposneOfLoginFromApi(param: Request_Login){


        val phoneNumber= param.phoneNumber


        val retrofitInstance: RetrofitInstance = RetrofitInstance()
        val retrofitData = retrofitInstance.retrofit.getOtp(param)


        retrofitData.enqueue(object : Callback<Response_Common> {

            override fun onResponse(
                call: Call<Response_Common>,
                response: Response<Response_Common>
            ) {
//               TODO("Not yet implemented")
                if(response.isSuccessful) {

                    commonResponse1.value=response.body()!!
                    Log.d("OTP", commonResponse1.value!!.getData()!!.otp)

                    Toast.makeText(activity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                }else {

                    Toast.makeText(activity, "User not found please register", Toast.LENGTH_SHORT).show()

                }

            }

            override fun onFailure(call: Call<Response_Common>, t: Throwable) {
//               TODO("Not yet implemented")

                Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}