package com.example.mayasfood.activity.ViewModels

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lottry.data.remote.retrofit.request.Request_Login
import com.example.mayasfood.Retrofite.request.Request_Registration
import com.example.mayasfood.Retrofite.response.Response_Common
import com.example.mayasfood.activity.Registration
import com.example.mayasfood.development.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registration_ViewModel : ViewModel() {


    lateinit var activity: AppCompatActivity

    val commonResponse = MutableLiveData<Response_Common>()
    val commonResponse1 = MutableLiveData<Response_Common>()

    fun registerUser(activity:AppCompatActivity,phoneNumber:String,name:String):MutableLiveData<Response_Common>{

        this.activity=activity

        val requestRegistration= Request_Registration()

        requestRegistration.phoneNumber=phoneNumber
        requestRegistration.userName=name

        getResposneOfRegistrationFromApi(requestRegistration)

        return commonResponse
    }

    fun getResposneOfRegistrationFromApi(param: Request_Registration){

       val retrofitInstance: RetrofitInstance = RetrofitInstance()
        val retrofitData = retrofitInstance.retrofit.getRegistraion(param)

        retrofitData.enqueue(object : Callback<Response_Common> {

            override fun onResponse(
                call: Call<Response_Common>,
                response: Response<Response_Common>
            ) {
//               TODO("Not yet implemented")
                if(response.isSuccessful) {

                    commonResponse.value=response.body()!!
                    Toast.makeText(activity, "Verify your number", Toast.LENGTH_SHORT).show()

                }else {
                    Log.d("error", response.message().toString())
                    Toast.makeText(activity, "User Name or User Number is already registered", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Response_Common>, t: Throwable) {
//               TODO("Not yet implemented")
                Log.d("error", t.toString())
                Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();
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